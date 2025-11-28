package net.thejadeproject.ascension.progression.breakthrough.handlers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.concurrent.ThreadLocalRandom;

public class StabilityCheckBreakthroughHandler implements IBreakthroughHandler {
    @Override
    public IBreakthroughData getBreakthroughData(CompoundTag tag) {
        return null;
    }

    @Override
    public void attemptBreakthrough(Player player, String pathId, ITechnique technique) {
        
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        double stability = technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks);
        double random = ThreadLocalRandom.current().nextDouble(0,1);
        
        
        if(random <= stability) completeBreakthrough(player, pathId);
        else failBreakthrough(player,pathId);
    }

    @Override
    public void failBreakthrough(Player player, String pathId) {

        
        CultivationData.PathData data =  player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        data.pathProgress = 0;
        data.stabilityCultivationTicks = 0;
        PacketDistributor.sendToPlayer((ServerPlayer) player, SyncPathDataPayload.fromPathData(data));
    }
}
