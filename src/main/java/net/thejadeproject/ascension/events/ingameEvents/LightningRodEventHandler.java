package net.thejadeproject.ascension.events.ingameEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.data_components.ModDataComponents;
import net.thejadeproject.ascension.items.ModItems;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class LightningRodEventHandler {
    private static final Map<ServerLevel, Map<BlockPos, FormationData>> activeFormations = new HashMap<>();

    @SubscribeEvent
    public static void onLightningStrike(BlockEvent.NeighborNotifyEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        if (level.getBlockState(pos).is(Blocks.LIGHTNING_ROD)) {
            if (level.getBlockState(pos).hasProperty(LightningRodBlock.POWERED) && level.getBlockState(pos).getValue(LightningRodBlock.POWERED)) {
                if (level instanceof ServerLevel serverLevel) {
                    handleLightningRodStrike(serverLevel, pos);
                }
            }
        }
    }

    public static void handleLightningRodStrike(ServerLevel level, BlockPos rodPos) {
        if (level.random.nextFloat() <= 0.25f) {
            startFormationEffect(level, rodPos);
        }
    }

    public static void startFormationEffect(ServerLevel level, BlockPos rodPos) {
        FormationData formation = new FormationData(rodPos);

        activeFormations.computeIfAbsent(level, k -> new HashMap<>()).put(rodPos, formation);
        spawnFormationParticles(level, rodPos, 0);

    }

    @SubscribeEvent
    public static void onPlayerStruckByLightning(EntityStruckByLightningEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        boolean hasArmor = !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.FEET).isEmpty();

        if (hasArmor) {
            return;
        }

        if (player.getRandom().nextFloat() <= 0.25f) {
            if (player.level() instanceof ServerLevel serverLevel) {
                dropLightningPhysique(serverLevel, player.blockPosition(), "heavenly_lightning_physique");
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel)) return;

        ServerLevel level = (ServerLevel) event.getLevel();
        Map<BlockPos, FormationData> levelFormations = activeFormations.get(level);

        if (levelFormations == null || levelFormations.isEmpty()) return;

        Map<BlockPos, FormationData> copy = new HashMap<>(levelFormations);

        for (Map.Entry<BlockPos, FormationData> entry : copy.entrySet()) {
            BlockPos pos = entry.getKey();
            FormationData formation = entry.getValue();

            formation.tick++;

            spawnFormationParticles(level, pos, formation.tick);

            if (formation.tick >= 40) {
                dropLightningPhysique(level, pos, "thunderclap_physique");

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

        if (tick < 10) {
            for (int i = 0; i < 5; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 0.3;
                double offsetY = level.random.nextDouble() * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.3;

                level.sendParticles(ParticleTypes.ELECTRIC_SPARK, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 1, 0, 0, 0, 0);
            }
        } else if (tick < 20) {
            double formationHeight = 0.5 + (tick - 20) * 0.1;

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
            level.sendParticles(ParticleTypes.GLOW,
                    centerX, centerY + formationHeight, centerZ,
                    3, 0.2, 0.2, 0.2, 0);
        } else if (tick < 40) {
            double convergenceProgress = (tick - 30) / 10.0;
            double height = 1.5 * (1 - convergenceProgress);

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


    private static void dropLightningPhysique(ServerLevel level, BlockPos pos, String physiqueId) {
        ItemStack physiqueItem = createPhysiqueItemWithRandomPurity(level, physiqueId);

        double dropX = pos.getX() + 0.5;
        double dropY = pos.getY() + 1.5;
        double dropZ = pos.getZ() + 0.5;

        ItemEntity itemEntity = new ItemEntity(level, dropX, dropY, dropZ, physiqueItem);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);

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

    private static ItemStack createPhysiqueItemWithRandomPurity(ServerLevel level, String physiqueId) {
        ItemStack stack = new ItemStack(ModItems.PHYSIQUE_ESSENCE.get());
        stack.set(ModDataComponents.PHYSIQUE_ID.get(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, physiqueId).toString());

        int purity = 1 + level.random.nextInt(33);
        stack.set(ModDataComponents.PURITY.get(), purity);

        return stack;
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