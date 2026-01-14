package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class TechniquesEventListener {
    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        //TODO
        //check if player has technique
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        for(CultivationData.PathData pathData : data.getCultivationData().getPaths()){
            if(pathData.technique.equals("ascension:none")) continue;
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).onGatherEfficiencyModifiers(event);

        }

    }
    //Needs to be run on pre because otherwise it can be run after a technique is removed meaning it will not get called
    @SubscribeEvent
    public static void onRealmChangeEvent(RealmChangeEvent.Post event){

        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getCultivationData().getPathData(event.pathId).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getCultivationData().getPathData(event.pathId).technique,':')).onRealmChangeEvent(event);
        }
    }
    @SubscribeEvent
    public static void onSkillRemove(PlayerSkillRemoveEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        for(CultivationData.PathData pathData : data.getCultivationData().getPaths()){
            if(!pathData.technique.equals("ascension:none")){
                AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).onSkillRemoveEvent(event);
            }
        }
    }
    //let whatever is removing techniques do its thing, then go in and clean up
    @SubscribeEvent
    public static void onTechniqueChange(TechniqueChangeEvent.Post event){
        if(!event.oldTechniqueId.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.oldTechniqueId,':')).onRemoveTechnique(event.player,event.oldTechniqueData);
        }
        if(!event.newTechniqueId.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.newTechniqueId,':')).onTechniqueAcquisition(event.player);
        }
    }

}
