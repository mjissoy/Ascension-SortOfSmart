package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
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
    @SubscribeEvent
    public static void onRealmChangeEvent(RealmChangeEvent event){
        ;
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

}
