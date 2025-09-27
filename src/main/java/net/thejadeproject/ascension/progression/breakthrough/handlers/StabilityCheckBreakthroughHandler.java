package net.thejadeproject.ascension.progression.breakthrough.handlers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
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
        System.out.println("attempting to break through");
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(pathId);
        double stability = technique.getStabilityHandler().getStability(pathData.stabilityCultivationTicks);
        double random = ThreadLocalRandom.current().nextDouble(0,1);
        System.out.println(random);
        System.out.println(stability);
        if(random <= stability) completeBreakthrough(player, pathId);
        else failBreakthrough(player,pathId);
    }

    @Override
    public void failBreakthrough(Player player, String pathId) {
        System.out.println("Failed to breakthrough");
    }
}
