package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.EntityAttributeManager;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.HashSet;


public class FlightPassiveSkill extends AbstractPassiveSkill {


    public FlightPassiveSkill(){
        super(Component.literal("Flight"));
        this.path = "ascension:body";
        
    }

    public void updateSkillData(Player player){

        player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT).addOrReplacePermanentModifier(new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"flight_passive_boost"),
                1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE

        ));
        
    }
    //TODO have it update on realm change and technique change and physique change
    @Override
    public void onSkillAdded(Player player) {
        super.onSkillAdded(player);
        updateSkillData(player);
        EntityAttributeManager.increaseAttribute(
                player,
                1.0,
                NeoForgeMod.CREATIVE_FLIGHT
        );
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        EntityAttributeManager.increaseAttribute(
                player,
                0.0,
                NeoForgeMod.CREATIVE_FLIGHT
        );
        player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT).removeModifier(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"flight_passive_boost"
        ));
    }


}
