package net.thejadeproject.ascension.progression.breakthrough;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public interface IBreakthroughHandler {

    IBreakthroughData getBreakthroughData(CompoundTag tag);

    void attemptBreakthrough(Player player,String pathId,ITechnique technique);
    //should be called after a breakthrough is completed
    void failBreakthrough(Player player,String pathId);
    default void completeBreakthrough(Player player,String pathId){
        PlayerData data = player.getData(ModAttachments.PLAYER_DATA);
        CultivationData cultivationData = data.getCultivationData();
        CultivationData.PathData pathData = cultivationData.getPathData(pathId);
        //TODO add verification. aka make sure they are at the correct minor realm. and roll the chance for now don't bother
        if(pathData.majorRealm >= CultivationSystem.getRealmNumber(pathId)) return;
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                pathData.technique,
                ':'
        ));
        MajorRealmChangeEvent event = new MajorRealmChangeEvent(
                player,
                pathId,
                pathData.majorRealm,
                pathData.majorRealm+1,
                technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks),
                pathData.breakthroughData
                );

        pathData.increaseMajorRealm();
        PacketDistributor.sendToPlayer((ServerPlayer) player, SyncPathDataPayload.fromPathData(pathData));
        NeoForge.EVENT_BUS.post(event);
    }
}
