package net.thejadeproject.ascension.progression.skills.skill_lists;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.constants.SkillRemoveSource;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.progression.paths.IPath;
import net.thejadeproject.ascension.progression.paths.ModPaths;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class SingleSkillAcquirableData implements IAcquirableSkill{
    public final ResourceLocation skillId;
    public final int majorRealm;
    public final int minorRealm;
    public final boolean fixed;
    public final boolean permanent;
    public boolean removingSkill;
    public SingleSkillAcquirableData(ResourceLocation skillId,int majorRealm,int minorRealm,boolean fixed,boolean permanent){
        this.skillId =skillId;
        this.majorRealm= majorRealm;
        this.minorRealm = minorRealm;
        this.fixed = fixed;
        this.permanent = permanent;
    }

    //include min
    @Override
    public void onRealmIncrease(RealmChangeEvent event) {
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill(skillId.toString())) return;
        boolean inMajorRealmRange = majorRealm>=event.oldMajorRealm && majorRealm <= event.newMajorRealm;

        boolean inBetweenMajorRealms = inMajorRealmRange && minorRealm != event.oldMinorRealm && event.newMinorRealm != minorRealm;

        boolean inLowerMajorAndGreaterOrEqualToMinor = inMajorRealmRange && majorRealm == event.oldMajorRealm && minorRealm>=event.oldMinorRealm;

        boolean inHigherMajorAndLessOrEqualToMinor = inMajorRealmRange && majorRealm == event.newMajorRealm && minorRealm <= event.newMinorRealm;

        if(inBetweenMajorRealms || inLowerMajorAndGreaterOrEqualToMinor || inHigherMajorAndLessOrEqualToMinor) {
            // try to add a skill
            //TODO add an event for this
            System.out.println("adding skill");
            event.player.getData(ModAttachments.PLAYER_SKILL_DATA).addSkill(skillId,fixed,permanent);
        }
    }
    //exclude min
    @Override
    public void onRealmDecrease(RealmChangeEvent event) {
        //skill cannot be removed due to realm changes
        if(fixed || permanent) return;
        //TODO something is wrong here
        //does not work for major : 0->-1 aka a 0,0 skill is in this range
        //                  minor : 0->-1
        System.out.println("checking out if skill: "+skillId.toString() + " should be removed");
        boolean inMajorRealmRange = majorRealm>=event.newMinorRealm && majorRealm <= event.oldMajorRealm;
        System.out.println(inMajorRealmRange);
        boolean inBetweenMajorRealms = inMajorRealmRange && minorRealm != event.oldMinorRealm && event.newMinorRealm != minorRealm;
        //TODO i think i need to flip these 2 conditions?
        System.out.println(inBetweenMajorRealms);
        boolean inLowerMajorAndGreaterOrEqualToMinor = inMajorRealmRange && majorRealm == event.newMajorRealm && minorRealm > event.newMinorRealm;
        System.out.println(inLowerMajorAndGreaterOrEqualToMinor);
        boolean inHigherMajorAndLessOrEqualToMinor = inMajorRealmRange && majorRealm== event.oldMajorRealm && minorRealm <= event.oldMinorRealm;
        System.out.println(inMajorRealmRange);
        if(inBetweenMajorRealms || inLowerMajorAndGreaterOrEqualToMinor || inHigherMajorAndLessOrEqualToMinor) {
            // try to remove skill
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
            PlayerSkillRemoveEvent removeEvent = new PlayerSkillRemoveEvent(event.player,skillId,SkillRemoveSource.REALM_CHANGE);
            removingSkill = true;
            NeoForge.EVENT_BUS.post(removeEvent);
            if(removeEvent.isCanceled()) return;
            event.player.getData(ModAttachments.PLAYER_SKILL_DATA).removeSkill(skillId.toString());

        }
    }

    @Override
    public boolean hasSkill(ResourceLocation skill) {
        return skill == skillId;
    }

    @Override
    public boolean tryRemoveSkill(PlayerSkillRemoveEvent event,ResourceLocation path) {
        if(!hasSkill(event.skill)) return true;
        if(removingSkill) {
            removingSkill = false;
            return true;
        }
        if(event.skillRemoveType == SkillRemoveSource.FORCE_REMOVE) return true;

        CultivationData.PathData pathData = event.player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(path.toString());
        if(pathData.majorRealm > majorRealm || (pathData.majorRealm == majorRealm && pathData.minorRealm >= minorRealm)){
            event.setCanceled(true);
            return false;
        }
        //permanent takes priority over fixed
        if(permanent) {
            event.setCanceled(true);
            return false;
        }
        if(fixed && event.skillRemoveType != SkillRemoveSource.REBIRTH){
            event.setCanceled(true);
            return false;
        }
        return true;
    }

    @Override
    public Component asComponent(ResourceLocation technique, ResourceLocation path, int majorRealm, int minorRealm) {
        ISkill skillData = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
        IPath pathInstance = ModPaths.getPath(path);
        Component majorRealmName;
        Component minorRealmName;
        if(technique.equals(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"none"))){
            majorRealmName = pathInstance.getMajorRealmName(this.majorRealm);
            minorRealmName = pathInstance.getMinorRealmName(this.majorRealm,this.minorRealm);
        }else {
            ITechnique techniqueInstance = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
            majorRealmName = techniqueInstance.getMajorRealmName(this.majorRealm);
            minorRealmName = techniqueInstance.getMinorRealmName(this.majorRealm,this.minorRealm);
        }

        return Component.empty()
                .append(skillData.getSkillTitle())
                .append(" : ")
                .append(majorRealmName)
                .append("(").append(minorRealmName).append(")");
    }
}
