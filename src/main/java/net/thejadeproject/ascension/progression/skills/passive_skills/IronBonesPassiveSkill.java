package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.*;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.physiques.PhysiqueEventListener;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.techniques.TechniquesEventListener;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.HashSet;


public class IronBonesPassiveSkill extends AbstractPassiveSkill {


    public IronBonesPassiveSkill(){
        super(Component.literal("Iron Bones"));
        this.path = "ascension:body";
    }
    @Override
    public void onPhysiqueChange(PhysiqueChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill( "ascension:iron_bones_passive_skill","Passive")) updateSkillData(event.player);
    }
    @Override
    public void onTechniqueChange(TechniqueChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:iron_bones_passive_skill","Passive")) updateSkillData(event.player);

    }
    @Override
    public void onRealmChange(RealmChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill( "ascension:iron_bones_passive_skill","Passive")) updateSkillData(event.player);

    }

    public void updateSkillData(Player player){
        GatherEfficiencyModifiersEvent event = new GatherEfficiencyModifiersEvent(player,this.path,new HashSet<>(){{
            add("ascension:metal");
        }});
        PhysiqueEventListener.gatherEfficiencyMultipliers(event);
        TechniquesEventListener.gatherEfficiencyMultipliers(event);
        player.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"iron_bone_passive_boost"),
                2*event.getTotalDaoEfficiencyMultiplier(),
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }
    //TODO have it update on realm change and technique change and physique change
    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        updateSkillData(player);
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"iron_bone_passive_boost"
        ));
    }

}
