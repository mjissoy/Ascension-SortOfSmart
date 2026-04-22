package net.thejadeproject.ascension.events.ingameEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.data_components.ModDataComponents;
import net.thejadeproject.ascension.items.ModItems;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class NaturalPhenomenonPhysiqueEvents {

    // Stone Monkey tracking: Player UUID -> Break Count
    private static final Map<UUID, Integer> stoneBreakCounts = new HashMap<>();
    private static final int STONE_THRESHOLD = 50;

    // ========== VOID WALKER (Keep - Warden Sonic Boom) ==========
    @SubscribeEvent
    public static void onSonicBoomHit(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (event.getSource().is(DamageTypes.SONIC_BOOM)) {
            if (player.getRandom().nextFloat() <= 0.25f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "void_walker_physique", ParticleTypes.PORTAL);
            }
        }
    }

    // ========== STONE MONKEY (Keep - Bare hand stone breaking) ==========
    @SubscribeEvent
    public static void onStoneBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (!event.getState().is(Blocks.STONE) && !event.getState().is(Blocks.COBBLESTONE)) return;
        if (!player.getMainHandItem().isEmpty()) return;

        UUID id = player.getUUID();
        int count = stoneBreakCounts.getOrDefault(id, 0) + 1;
        stoneBreakCounts.put(id, count);

        if (count >= STONE_THRESHOLD) {
            if (player.getRandom().nextFloat() <= 0.25f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "heavenborn_stone_monkey_physique", ParticleTypes.CRIT);
            }
            stoneBreakCounts.remove(id);
        }
    }

    // ========== SOLAR FLARE (NEW METHOD) ==========
    // Ignite Flint and Steel on a Gold Block at noon (daytime)
    // 25% chance
    @SubscribeEvent
    public static void onIgniteGold(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.FLINT_AND_STEEL)) return;

        BlockPos target = event.getPos();
        if (target == null) return;

        ServerLevel level = player.serverLevel();

        if (!level.getBlockState(target).is(Blocks.GOLD_BLOCK)) return;

        long time = level.getDayTime() % 24000;
        if (time >= 5000 && time <= 7000) { // Near noon window
            if (player.getRandom().nextFloat() <= 0.08f) {
                dropPhysiqueWithRandomPurity(level, player.blockPosition(), "solar_flare_physique", ParticleTypes.FLASH);
            }
        }
    }

    // ========== LUNAR SHADOW (Keep - Sleep exposed during Full Moon) ==========
    @SubscribeEvent
    public static void onMoonSleep(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.WHITE_BED)) return;

        BlockPos pos = event.getPos();
        ServerLevel level = player.serverLevel();

        if (level.getMoonPhase() == 0 && level.canSeeSky(pos.above())) {
            if (level.isNight() && player.getRandom().nextFloat() <= 0.04f) {
                dropPhysiqueWithRandomPurity(level, pos, "lunar_shadow_physique", ParticleTypes.GLOW);
            }
        }
    }

    // ========== PURE FIRE BODY (NEW METHOD) ==========
    // Right click a Campfire with Snowball (extinguishing creates steam paradox)
    // 15% chance
    @SubscribeEvent
    public static void onCampfireSnow(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.SNOWBALL)) return;

        BlockPos target = event.getPos();
        if (target == null) return;

        if (player.level().getBlockState(target).is(Blocks.CAMPFIRE) ||
                player.level().getBlockState(target).is(Blocks.SOUL_CAMPFIRE)) {

            if (player.getRandom().nextFloat() <= 0.15f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "pure_fire_body", ParticleTypes.FLAME);
            }
        }
    }

    // ========== PURE WATER BODY (Keep - Drink water while raining in River/Ocean) ==========
    @SubscribeEvent
    public static void onWaterDrink(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.POTION)) return;

        ServerLevel level = player.serverLevel();
        if (level.isRaining() && (level.getBiome(player.blockPosition()).is(Biomes.RIVER) ||
                level.getBiome(player.blockPosition()).is(Biomes.OCEAN))) {

            if (player.getRandom().nextFloat() <= 0.15f) {
                dropPhysiqueWithRandomPurity(level, player.blockPosition(), "pure_water_body", ParticleTypes.BUBBLE);
            }
        }
    }

    // ========== PURE EARTH BODY (Keep - Fall onto dirt with low health) ==========
    @SubscribeEvent
    public static void onHardFall(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getSource().is(DamageTypes.FALL)) return;

        BlockPos landing = player.blockPosition().below();

        if (!player.level().getBlockState(landing).is(Blocks.DIRT) && !player.level().getBlockState(landing).is(Blocks.GRASS_BLOCK)) return;
        if (player.getHealth() > 6.0f) return;

        if (player.getRandom().nextFloat() <= 0.20f) {
            dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "pure_earth_body",
                    new DustParticleOptions(new Vector3f(0.4f, 0.3f, 0.1f), 1.0f));
        }
    }

    // ========== PURE WOOD BODY (Keep - Eat Golden Apple inside tree leaves) ==========
    @SubscribeEvent
    public static void onTreeSnack(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!event.getItemStack().is(Items.GOLDEN_APPLE)) return;

        BlockPos headPos = player.blockPosition().above();
        if (player.level().getBlockState(headPos).is(Blocks.OAK_LEAVES) ||
                player.level().getBlockState(headPos).is(Blocks.SPRUCE_LEAVES) ||
                player.level().getBlockState(headPos).is(Blocks.BIRCH_LEAVES)) {

            if (player.getRandom().nextFloat() <= 0.25f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "pure_wood_body", ParticleTypes.HAPPY_VILLAGER);
            }
        }
    }

    // ========== PURE METAL BODY (NEW METHOD) ==========
    // Take damage from Iron Golem while holding Iron Ingot
    // 20% chance
    @SubscribeEvent
    public static void onGolemHit(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (event.getSource().getEntity() instanceof IronGolem) {
            if (player.getMainHandItem().is(Items.IRON_INGOT) || player.getOffhandItem().is(Items.IRON_INGOT)) {
                if (player.getRandom().nextFloat() <= 0.20f) {
                    dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "pure_metal_body", ParticleTypes.ELECTRIC_SPARK);
                }
            }
        }
    }

    // ========== COSMIC YIN-YANG (NEW METHOD) ==========
    // Right click dragon egg with white dye
    // 10% chance (rarest)
    @SubscribeEvent
    public static void onDyeOpposite(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemStack hand = event.getItemStack();

        BlockPos target = event.getPos();
        if (target == null) return;

        boolean blackToWhite = hand.is(Items.WHITE_DYE) && player.level().getBlockState(target).is(Blocks.DRAGON_EGG);


        if (blackToWhite) {
            if (player.getRandom().nextFloat() <= 0.03f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "cosmic_yinyang_physique", ParticleTypes.SMOKE);
            }
        }
    }

    // ========== SWIFT WIND (NEW METHOD) ==========
    // Kill any mob while holding Feather and sprinting
    // 25% chance
    @SubscribeEvent
    public static void onFeatherKill(LivingDeathEvent event) {
        if (event.getSource() == null || !(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        if (player.getMainHandItem().is(Items.FEATHER) && player.isSprinting()) {
            if (player.getRandom().nextFloat() <= 0.25f) {
                dropPhysiqueWithRandomPurity(player.serverLevel(), player.blockPosition(), "swift_wind_physique", ParticleTypes.CLOUD);
            }
        }
    }

    // ========== HELPER METHODS ==========

    private static void dropPhysiqueWithRandomPurity(ServerLevel level, BlockPos pos, String physiqueId, ParticleOptions particle) {
        int purity = 1 + level.random.nextInt(33);

        ItemStack stack = new ItemStack(ModItems.PHYSIQUE_ESSENCE.get());
        stack.set(ModDataComponents.PHYSIQUE_ID.get(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, physiqueId).toString());
        stack.set(ModDataComponents.PURITY.get(), purity);

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;

        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);

        for (int i = 0; i < 20; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 0.8;
            double oy = level.random.nextDouble() * 0.5;
            double oz = (level.random.nextDouble() - 0.5) * 0.8;
            level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, ox, oy, oz, 0);
        }

        for (int i = 0; i < 15; i++) {
            double ox = (level.random.nextDouble() - 0.5) * 0.5;
            double oy = level.random.nextDouble() * 0.5;
            double oz = (level.random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(particle, x, y, z, 1, ox, oy, oz, 0.1);
        }

        level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        stoneBreakCounts.remove(event.getEntity().getUUID());
    }
}