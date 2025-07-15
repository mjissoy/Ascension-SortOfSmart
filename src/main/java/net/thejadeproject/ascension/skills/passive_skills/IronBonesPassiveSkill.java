package net.thejadeproject.ascension.skills.passive_skills;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.*;
import net.thejadeproject.ascension.physiques.PhysiqueEventListener;
import net.thejadeproject.ascension.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.techniques.TechniquesEventListener;


public class IronBonesPassiveSkill extends AbstractPassiveSkill {

    public boolean updateOnRealmIncrease = true;

    public IronBonesPassiveSkill(){
        this.path = "ascension:body";
    }

    //TODO have it update on realm change and technique change and physique change
    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        GatherEfficiencyModifiersEvent event = new GatherEfficiencyModifiersEvent(player,this.path,"ascension:metal");
        PhysiqueEventListener.gatherEfficiencyMultipliers(event);
        TechniquesEventListener.gatherEfficiencyMultipliers(event);
        player.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"iron_bone_passive_boost"),
                2*event.getTotalEfficiencyMultiplier(),
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"iron_bone_passive_boost"
        ));
    }
}
