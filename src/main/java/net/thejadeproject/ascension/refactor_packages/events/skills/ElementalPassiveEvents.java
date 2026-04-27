package net.thejadeproject.ascension.refactor_packages.events.skills;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class ElementalPassiveEvents {

    private static final int WOOD_SCAN_RADIUS = 4;
    private static final int WOOD_REQUIRED_PLANTS = 8;

    private ElementalPassiveEvents() {

    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;

        // Runs once per second so nearby plant checks do not become tiny lag goblins.
        if (player.tickCount % 20 != 0) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (entityData.hasSkill(ModSkills.AQUATIC_CIRCULATION.getId())) {
            applyAquaticCirculation(player);
        }

        if (entityData.hasSkill(ModSkills.VERDANT_RECOVERY.getId())) {
            applyVerdantRecovery(player);
        }
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (
                entityData.hasSkill(ModSkills.FLAME_TEMPERED_BODY.getId())
                        && event.getSource().is(DamageTypeTags.IS_FIRE)
        ) {
            event.setAmount(event.getAmount() * 0.5F);
        }
    }

    private static void applyAquaticCirculation(Player player) {
        if (!player.isUnderWater()) return;

        player.addEffect(new MobEffectInstance(
                MobEffects.CONDUIT_POWER,
                60,
                0,
                true,
                false,
                true
        ));
    }

    private static void applyVerdantRecovery(Player player) {
        if (player.getHealth() >= player.getMaxHealth()) return;
        if (!isNearPlantLife(player, WOOD_SCAN_RADIUS)) return;

        player.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                60,
                0,
                true,
                false,
                true
        ));
    }

    private static boolean isNearPlantLife(Player player, int radius) {
        BlockPos center = player.blockPosition();
        int plants = 0;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -radius, -radius),
                center.offset(radius, radius, radius)
        )) {
            BlockState state = player.level().getBlockState(pos);

            if (isPlantLife(state)) {
                plants++;

                if (plants >= WOOD_REQUIRED_PLANTS) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isPlantLife(BlockState state) {
        return state.is(BlockTags.LEAVES)
                || state.is(BlockTags.LOGS)
                || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.CROPS);
    }


}