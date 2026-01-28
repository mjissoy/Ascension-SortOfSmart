// StabilityCheckBreakthroughHandler.java
package net.thejadeproject.ascension.progression.breakthrough.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.network.clientBound.*;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Handles breakthroughs with Heavenly Tribulation.
 * Features multiphase trials, cinematic effects, and world-altering impacts.
 */
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

    @Override
    public Supplier<IBreakthroughData> getBreakthroughDataInstance() {
        return null;
    }
}