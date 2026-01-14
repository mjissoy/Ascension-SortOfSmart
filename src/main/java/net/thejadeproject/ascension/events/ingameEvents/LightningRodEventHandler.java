package net.thejadeproject.ascension.events.ingameEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.events.ModDataComponents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class LightningRodEventHandler {

    // Store active formations: <Level, Map<BlockPos, FormationData>>
    private static final Map<ServerLevel, Map<BlockPos, FormationData>> activeFormations = new HashMap<>();

    @SubscribeEvent
    public static void onLightningStrike(BlockEvent.NeighborNotifyEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        // Check if the block is a lightning rod
        if (level.getBlockState(pos).is(Blocks.LIGHTNING_ROD)) {
            // Check if lightning recently struck (rod is powered)
            if (level.getBlockState(pos).hasProperty(LightningRodBlock.POWERED)
                    && level.getBlockState(pos).getValue(LightningRodBlock.POWERED)) {

                if (level instanceof ServerLevel serverLevel) {
                    handleLightningRodStrike(serverLevel, pos);
                }
            }
        }
    }

    private static void handleLightningRodStrike(ServerLevel level, BlockPos rodPos) {
        Random random = new Random();

        // 25% chance to trigger
        if (random.nextFloat() <= 0.25f) {
            // Start formation effect
            startFormationEffect(level, rodPos);
        }
    }

    private static void startFormationEffect(ServerLevel level, BlockPos rodPos) {
        // Create formation data
        FormationData formation = new FormationData(rodPos);

        // Store in active formations
        activeFormations.computeIfAbsent(level, k -> new HashMap<>())
                .put(rodPos, formation);

        // Send initial particles
        spawnFormationParticles(level, rodPos, 0);
    }

    @SubscribeEvent
    public static void onServerTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        Map<BlockPos, FormationData> levelFormations = activeFormations.get(level);
        if (levelFormations == null || levelFormations.isEmpty()) return;

        // Create a copy to avoid ConcurrentModificationException
        Map<BlockPos, FormationData> copy = new HashMap<>(levelFormations);

        for (Map.Entry<BlockPos, FormationData> entry : copy.entrySet()) {
            BlockPos pos = entry.getKey();
            FormationData formation = entry.getValue();

            formation.tick++;

            // Update particles for this tick
            spawnFormationParticles(level, pos, formation.tick);

            if (formation.tick >= 40) {
                // Formation complete - drop item
                dropThunderclapPhysique(level, pos);

                // Remove from active formations
                levelFormations.remove(pos);
            }
        }

        // Clean up empty maps
        if (levelFormations.isEmpty()) {
            activeFormations.remove(level);
        }
    }

    private static void spawnFormationParticles(ServerLevel level, BlockPos rodPos, int tick) {
        double centerX = rodPos.getX() + 0.5;
        double centerY = rodPos.getY() + 1.5;
        double centerZ = rodPos.getZ() + 0.5;

        // Different particle effects as formation progresses
        if (tick < 10) {
            // Initial sparking phase
            for (int i = 0; i < 5; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 0.3;
                double offsetY = level.random.nextDouble() * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.3;

                level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                        1, 0, 0, 0, 0);
            }
        } else if (tick < 20) {
            // Energy gathering phase
            for (int i = 0; i < 8; i++) {
                double angle = (tick * 18 + i * 45) * Math.PI / 180;
                double radius = 0.5 + (tick - 10) * 0.02;

                double particleX = centerX + Math.cos(angle) * radius;
                double particleY = centerY + 0.1 * (tick - 10);
                double particleZ = centerZ + Math.sin(angle) * radius;

                level.sendParticles(ParticleTypes.ENCHANT,
                        particleX, particleY, particleZ,
                        1, 0, 0.1, 0, 0);
            }
        } else if (tick < 30) {
            // Blood essence forming phase
            double formationHeight = 0.5 + (tick - 20) * 0.1;

            // Spiral particles rising
            for (int i = 0; i < 12; i++) {
                double angle = (tick * 15 + i * 30) * Math.PI / 180;
                double radius = 0.3;

                double particleX = centerX + Math.cos(angle) * radius;
                double particleY = rodPos.getY() + 1.0 + formationHeight;
                double particleZ = centerZ + Math.sin(angle) * radius;

                level.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
                        particleX, particleY, particleZ,
                        1, 0, 0.05, 0, 0);
            }

            // Center glow
            level.sendParticles(ParticleTypes.GLOW,
                    centerX, centerY + formationHeight, centerZ,
                    3, 0.2, 0.2, 0.2, 0);
        } else if (tick < 40) {
            // Final convergence phase
            double convergenceProgress = (tick - 30) / 10.0;
            double height = 1.5 * (1 - convergenceProgress);

            // Converging particles
            for (int i = 0; i < 15; i++) {
                double angle = i * 24 * Math.PI / 180;
                double radius = 0.5 * (1 - convergenceProgress);

                double particleX = centerX + Math.cos(angle) * radius;
                double particleY = centerY + height;
                double particleZ = centerZ + Math.sin(angle) * radius;

                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        particleX, particleY, particleZ,
                        1, 0, 0, 0, 0.05);
            }

            // Lightning particle effects
            if (tick % 3 == 0) {
                for (int i = 0; i < 3; i++) {
                    double offsetX = (level.random.nextDouble() - 0.5) * 0.4;
                    double offsetZ = (level.random.nextDouble() - 0.5) * 0.4;

                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            centerX + offsetX, centerY + height, centerZ + offsetZ,
                            2, 0, 0.1, 0, 0.1);
                }
            }
        }
    }

    private static void dropThunderclapPhysique(ServerLevel level, BlockPos rodPos) {
        // Create the ThunderClap physique item with 13% purity
        ItemStack physiqueItem = new ItemStack(ModItems.BLOOD_ESSENCE.get());
        physiqueItem.set(ModDataComponents.PHYSIQUE_ID.get(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "thunderclap_physique").toString());
        physiqueItem.set(ModDataComponents.PURITY.get(), 13);

        // Drop the item above the lightning rod
        double dropX = rodPos.getX() + 0.5;
        double dropY = rodPos.getY() + 1.5;
        double dropZ = rodPos.getZ() + 0.5;

        ItemEntity itemEntity = new ItemEntity(level, dropX, dropY, dropZ, physiqueItem);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);

        // Final explosion of particles
        for (int i = 0; i < 20; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetY = level.random.nextDouble() * 0.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;

            level.sendParticles(ParticleTypes.FLASH,
                    dropX, dropY, dropZ,
                    1, offsetX, offsetY, offsetZ, 0);

            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    dropX, dropY, dropZ,
                    3, offsetX * 0.5, offsetY * 0.5, offsetZ * 0.5, 0.1);
        }
    }

    private static class FormationData {
        public final BlockPos rodPos;
        public int tick;

        public FormationData(BlockPos rodPos) {
            this.rodPos = rodPos;
            this.tick = 0;
        }
    }
}