package net.thejadeproject.ascension.events.ingameEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.events.ModDataComponents;
import org.joml.Vector3f;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class NaturalPhenomenonPhysiqueEvents {

    // ========== SOLAR FLARE PHYSIQUE ==========
    // When player stands on exposed block at noon in desert during clear weather
    @SubscribeEvent
    public static void onSolarNoon(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && level.isDay()) {
            // Check if it's noon (between 11:30 and 12:30)
            long time = level.getDayTime() % 24000;
            boolean isNoon = time >= 10500 && time <= 11100;

            if (isNoon && level.canSeeSky(player.blockPosition())) {
                // Check if in desert biome
                Holder<Biome> biome = level.getBiome(player.blockPosition());
                boolean isDesert = biome.is(Biomes.DESERT) || biome.is(Biomes.BADLANDS);

                if (isDesert && !level.isRaining() && level.canSeeSky(player.blockPosition())) {
                    // 1% chance per minute at noon in desert
                    if (level.random.nextDouble() < 0.00017) { // ~0.01 per minute
                        spawnPhysiqueItem((ServerLevel) level, player, "solar_flare_physique", 10);

                        // Sun beam particle effect
                        for (int i = 0; i < 20; i++) {
                            ((ServerLevel) level).sendParticles(ParticleTypes.FLASH,
                                    player.getX(), player.getY() + 3, player.getZ(),
                                    1, 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    // ========== LUNAR SHADOW PHYSIQUE ==========
    // When player kills a mob under a full moon at midnight
    @SubscribeEvent
    public static void onFullMoonKill(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (event.getSource().getEntity() instanceof Player player) {
            Level level = player.level();

            // Check for full moon (moonPhase 0 is full moon in Minecraft)
            boolean isFullMoon = level.getMoonPhase() == 0;
            boolean isNight = level.isNight();

            if (isFullMoon && isNight && level.canSeeSky(player.blockPosition())) {
                // 5% chance per kill during full moon night
                if (level.random.nextDouble() < 0.05) {
                    spawnPhysiqueItem((ServerLevel) level, player, "lunar_shadow_physique", 12);

                    // Moonlight particle effect
                    for (int i = 0; i < 15; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.GLOW_SQUID_INK,
                                player.getX(), player.getY() + 1, player.getZ(),
                                1, 0.5, 0.5, 0.5, 0);
                    }
                }
            }
        }
    }

    // ========== PURE FIRE BODY ==========
    // Survive being on fire in Nether for 30 seconds
    @SubscribeEvent
    public static void onFireSurvival(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && level.dimension() == Level.NETHER && player.isOnFire()) {
            // Track fire survival time
            int fireTime = player.getPersistentData().getInt("ascension_fire_survival");
            fireTime++;
            player.getPersistentData().putInt("ascension_fire_survival", fireTime);

            // Every 5 seconds while on fire, show particles
            if (fireTime % 100 == 0) { // 5 seconds
                for (int i = 0; i < 10; i++) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.FLAME,
                            player.getX(), player.getY() + 1, player.getZ(),
                            1, 0.2, 0.2, 0.2, 0);
                }
            }

            // After 30 seconds (600 ticks), chance to drop physique
            if (fireTime >= 600) {
                if (level.random.nextDouble() < 0.01) {
                    spawnPhysiqueItem((ServerLevel) level, player, "pure_fire_body", 15);

                    // Fire explosion effect
                    for (int i = 0; i < 30; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.FLAME,
                                player.getX(), player.getY() + 1, player.getZ(),
                                3, 0.5, 0.5, 0.5, 0.1);
                    }

                    player.getPersistentData().remove("ascension_fire_survival");
                }
            }
        } else {
            // Reset if not on fire in Nether
            player.getPersistentData().remove("ascension_fire_survival");
        }
    }

    // ========== PURE WATER BODY ==========
    // Stay underwater for 2 minutes
    @SubscribeEvent
    public static void onWaterImmersion(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && player.isUnderWater()) {
            // Track underwater time
            int waterTime = player.getPersistentData().getInt("ascension_water_immersion");
            waterTime++;
            player.getPersistentData().putInt("ascension_water_immersion", waterTime);

            // Every 10 seconds underwater, show bubbles
            if (waterTime % 200 == 0) { // 10 seconds
                for (int i = 0; i < 20; i++) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.BUBBLE,
                            player.getX(), player.getY(), player.getZ(),
                            1, 0.3, 0.3, 0.3, 0);
                }
            }

            // After 2 minutes (2400 ticks), chance to drop physique
            if (waterTime >= 2400) {
                if (level.random.nextDouble() < 0.25) {
                    spawnPhysiqueItem((ServerLevel) level, player, "pure_water_body", 15);

                    // Water vortex effect
                    for (int i = 0; i < 360; i += 15) {
                        double angle = Math.toRadians(i);
                        double x = Math.cos(angle) * 2;
                        double z = Math.sin(angle) * 2;

                        ((ServerLevel) level).sendParticles(ParticleTypes.SPLASH,
                                player.getX() + x, player.getY(), player.getZ() + z,
                                1, 0, 0.5, 0, 0);
                    }

                    player.getPersistentData().remove("ascension_water_immersion");
                }
            }
        } else {
            // Reset if not underwater
            player.getPersistentData().remove("ascension_water_immersion");
        }
    }

    // ========== PURE EARTH BODY ==========
    // Stand on solid ground during earthquake (thunderstorm in mountain)
    @SubscribeEvent
    public static void onEarthquake(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && level.isThundering()) {
            // Check if player is in mountain biome
            Holder<Biome> biome = level.getBiome(player.blockPosition());
            boolean isMountain = biome.is(BiomeTags.IS_MOUNTAIN);

            if (isMountain && player.onGround() && !player.isInWater()) {
                // Small chance during thunderstorm in mountains
                if (level.random.nextDouble() < 0.0001) { // Very rare
                    spawnPhysiqueItem((ServerLevel) level, player, "pure_earth_body", 15);

                    // Earthquake particle effect - using dust particles instead of BLOCK_MARKER
                    for (int i = 0; i < 50; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD,
                                player.getX(), player.getY(), player.getZ(),
                                1, 1, 0, 1, 0);

                        // Brown dust particles for earth effect
                        DustParticleOptions earthParticle = new DustParticleOptions(
                                new Vector3f(0.4f, 0.3f, 0.1f), // Brown color (RGB)
                                1.0f
                        );
                        ((ServerLevel) level).sendParticles(earthParticle,
                                player.getX(), player.getY(), player.getZ(),
                                1, 0.5, 0, 0.5, 0);
                    }
                }
            }
        }
    }

    // ========== PURE WOOD BODY ==========
    // Be in ancient forest during rainfall
    @SubscribeEvent
    public static void onForestRain(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && level.isRaining()) {
            // Check if in old growth forest biome
            Holder<Biome> biome = level.getBiome(player.blockPosition());
            boolean isAncientForest = biome.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA) ||
                    biome.is(Biomes.OLD_GROWTH_PINE_TAIGA) ||
                    biome.is(Biomes.OLD_GROWTH_BIRCH_FOREST);

            if (isAncientForest && level.canSeeSky(player.blockPosition())) {
                // Chance during rain in ancient forest
                if (level.random.nextDouble() < 0.0002) {
                    spawnPhysiqueItem((ServerLevel) level, player, "pure_wood_body", 15);

                    // Forest growth particle effect
                    for (int i = 0; i < 30; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                player.getX(), player.getY() + 1, player.getZ(),
                                1, 0.5, 0.5, 0.5, 0);
                    }
                }
            }
        }
    }

    // ========== PURE METAL BODY ==========
    // Be near large metal deposit during thunderstorm
    @SubscribeEvent
    public static void onMetalDeposit(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide && level.isThundering()) {
            // Check for nearby iron/gold/copper blocks
            int metalCount = 0;
            BlockPos playerPos = player.blockPosition();

            for (int x = -5; x <= 5; x++) {
                for (int y = -3; y <= 3; y++) {
                    for (int z = -5; z <= 5; z++) {
                        BlockPos checkPos = playerPos.offset(x, y, z);
                        if (level.getBlockState(checkPos).getBlock().getDescriptionId().contains("iron") ||
                                level.getBlockState(checkPos).getBlock().getDescriptionId().contains("gold") ||
                                level.getBlockState(checkPos).getBlock().getDescriptionId().contains("copper")) {
                            metalCount++;
                        }
                    }
                }
            }

            if (metalCount >= 10) { // At least 10 metal blocks nearby
                if (level.random.nextDouble() < 0.0003) {
                    spawnPhysiqueItem((ServerLevel) level, player, "pure_metal_body", 15);

                    // Metal spark particle effect
                    for (int i = 0; i < 40; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.ELECTRIC_SPARK,
                                player.getX(), player.getY() + 1, player.getZ(),
                                1, 0.4, 0.4, 0.4, 0);
                    }
                }
            }
        }
    }

    // ========== COSMIC YIN-YANG PHYSIQUE ==========
    // When player stands at exact border of hot and cold biome during dawn/dusk
    @SubscribeEvent
    public static void onBiomeBorder(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            // Check if at biome border (check adjacent blocks)
            BlockPos playerPos = player.blockPosition();
            Holder<Biome> currentBiome = level.getBiome(playerPos);

            boolean isHotBiome = currentBiome.is(Biomes.DESERT) ||
                    currentBiome.is(Biomes.BADLANDS) ||
                    currentBiome.is(BiomeTags.IS_NETHER);

            boolean isColdBiome = currentBiome.is(Biomes.ICE_SPIKES) ||
                    currentBiome.is(Biomes.SNOWY_TAIGA) ||
                    currentBiome.is(Biomes.FROZEN_PEAKS);

            // Check adjacent blocks for opposite biome
            boolean hasOppositeNearby = false;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0) continue;

                    Holder<Biome> adjacentBiome = level.getBiome(playerPos.offset(x, 0, z));
                    boolean adjacentIsHot = adjacentBiome.is(Biomes.DESERT) ||
                            adjacentBiome.is(Biomes.BADLANDS) ||
                            adjacentBiome.is(BiomeTags.IS_NETHER);
                    boolean adjacentIsCold = adjacentBiome.is(Biomes.ICE_SPIKES) ||
                            adjacentBiome.is(Biomes.SNOWY_TAIGA) ||
                            adjacentBiome.is(Biomes.FROZEN_PEAKS);

                    if ((isHotBiome && adjacentIsCold) || (isColdBiome && adjacentIsHot)) {
                        hasOppositeNearby = true;
                        break;
                    }
                }
                if (hasOppositeNearby) break;
            }

            long time = level.getDayTime() % 24000;
            boolean isDawn = time >= 23000 || time < 1000; // 23:00 to 1:00
            boolean isDusk = time >= 11000 && time < 13000; // 11:00 to 13:00

            if (hasOppositeNearby && (isDawn || isDusk) && level.canSeeSky(playerPos)) {
                if (level.random.nextDouble() < 0.00005) { // Extremely rare
                    spawnPhysiqueItem((ServerLevel) level, player, "cosmic_yinyang_physique", 3);

                    // Yin-Yang swirling particle effect
                    for (int i = 0; i < 100; i++) {
                        boolean isYin = i % 2 == 0;
                        ((ServerLevel) level).sendParticles(
                                isYin ? ParticleTypes.SNOWFLAKE : ParticleTypes.FLAME,
                                player.getX(), player.getY() + 2, player.getZ(),
                                1, 0.5, 0.5, 0.5, 0);
                    }
                }
            }
        }
    }

    // ========== VOID WALKER PHYSIQUE ==========
    // When player almost falls into void but saves themselves
    @SubscribeEvent
    public static void onVoidEscape(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            // Check if player is very low but not dead
            if (player.getY() < -64 && player.getY() > -80 && player.isAlive()) {
                // Check if they're moving upward (escaping)
                if (player.getDeltaMovement().y > 0.1) {
                    if (level.random.nextDouble() < 0.001) { // Rare chance when escaping void
                        spawnPhysiqueItem((ServerLevel) level, player, "void_walker_physique", 8);

                        // Void portal particle effect
                        for (int i = 0; i < 30; i++) {
                            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL,
                                    player.getX(), player.getY(), player.getZ(),
                                    3, 0.5, 0.5, 0.5, 0);
                        }
                    }
                }
            }
        }
    }

    // ========== STONE MONKEY PHYSIQUE ==========
    // When player breaks stone with bare hands repeatedly
    // (Note: This requires BlockBreakEvent which we'll handle separately)
    @SubscribeEvent
    public static void onStoneBreakTracking(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            // Check if player has been breaking stone with bare hands
            int stoneBreaks = player.getPersistentData().getInt("ascension_stone_breaks");

            // Gradually decay the counter (lose 1 every 10 seconds if not breaking)
            if (level.getGameTime() % 200 == 0 && stoneBreaks > 0) {
                stoneBreaks = Math.max(0, stoneBreaks - 1);
                player.getPersistentData().putInt("ascension_stone_breaks", stoneBreaks);
            }

            // If they have enough breaks, chance to drop physique
            if (stoneBreaks >= 50) {
                if (level.random.nextDouble() < 0.001) {
                    spawnPhysiqueItem((ServerLevel) level, player, "heavenborn_stone_monkey_physique", 10);

                    // Stone cracking particle effect
                    for (int i = 0; i < 40; i++) {
                        ((ServerLevel) level).sendParticles(ParticleTypes.CRIT,
                                player.getX(), player.getY() + 1, player.getZ(),
                                1, 0.4, 0.4, 0.4, 0);
                    }

                    player.getPersistentData().remove("ascension_stone_breaks");
                }
            }
        }
    }

    // ========== SWIFT WIND PHYSIQUE ==========
    // When player reaches extremely high speed
    @SubscribeEvent
    public static void onHighSpeed(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (!level.isClientSide) {
            // Calculate horizontal speed
            double speed = Math.sqrt(
                    player.getDeltaMovement().x * player.getDeltaMovement().x +
                            player.getDeltaMovement().z * player.getDeltaMovement().z);

            if (speed > 4) { // Very fast (sprinting with speed potion)
                int highSpeedTime = player.getPersistentData().getInt("ascension_high_speed");
                highSpeedTime++;
                player.getPersistentData().putInt("ascension_high_speed", highSpeedTime);

                // Show wind trail particles
                for (int i = 0; i < 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.CLOUD,
                            player.xOld, player.yOld, player.zOld,
                            1, -player.getDeltaMovement().x * 0.5,
                            0.1,
                            -player.getDeltaMovement().z * 0.5, 0);
                }

                // After 10 seconds of high speed, chance to drop
                if (highSpeedTime >= 200) { // 10 seconds at 20 ticks/sec
                    if (level.random.nextDouble() < 0.05) {
                        spawnPhysiqueItem((ServerLevel) level, player, "swift_wind_physique", 25);
                        player.getPersistentData().remove("ascension_high_speed");
                    }
                }
            } else {
                // Reset if not at high speed
                player.getPersistentData().remove("ascension_high_speed");
            }
        }
    }

    // ========== HELPER METHOD ==========
    private static void spawnPhysiqueItem(ServerLevel level, Player player, String physiqueId, int purity) {
        ItemStack item = new ItemStack(ModItems.BLOOD_ESSENCE.get());
        item.set(ModDataComponents.PHYSIQUE_ID.get(),
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, physiqueId).toString());
        item.set(ModDataComponents.PURITY.get(), purity);

        // Spawn item above player
        ItemEntity itemEntity = new ItemEntity(level,
                player.getX(), player.getY() + 1, player.getZ(), item);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);

        // Play success sound
        player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 1.0f);
    }
}