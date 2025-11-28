package net.thejadeproject.ascension.progression.skills.passive_skills;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.data.internal.NeoForgeAdvancementProvider;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.PlayerAttributeManager;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.physiques.PhysiqueEventListener;
import net.thejadeproject.ascension.progression.skills.AbstractPassiveSkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.progression.techniques.TechniquesEventListener;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModAttributes;

import java.util.HashSet;


public class FlightPassiveSkill extends AbstractPassiveSkill {


    public FlightPassiveSkill(){
        super(Component.literal("Flight"));
        this.path = "ascension:body";
        
    }
    @Override
    public void onPhysiqueChange(PhysiqueChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill( "ascension:flight_passive_skill","Passive")) updateSkillData(event.player);
        
    }
    @Override
    public void onTechniqueChange(TechniqueChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:flight_passive_skill","Passive")) updateSkillData(event.player);
        
    }
    @Override
    public void onRealmChange(RealmChangeEvent event){
        if(event.player.getData(ModAttachments.PLAYER_SKILL_DATA).hasSkill("ascension:flight_passive_skill","Passive")) updateSkillData(event.player);
        
    }

    public void updateSkillData(Player player){
        GatherEfficiencyModifiersEvent event = new GatherEfficiencyModifiersEvent(player,this.path,new HashSet<>(){{
            add("ascension:flight");
            
        }});
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
        PlayerAttributeManager.increaseAttribute(
                player,
                1.0,
                NeoForgeMod.CREATIVE_FLIGHT
        );
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        PlayerAttributeManager.increaseAttribute(
                player,
                0.0,
                NeoForgeMod.CREATIVE_FLIGHT
        );
        player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT).removeModifier(ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,"flight_passive_boost"
        ));
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }
}
