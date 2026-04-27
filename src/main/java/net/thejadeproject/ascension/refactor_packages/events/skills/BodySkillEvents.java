package net.thejadeproject.ascension.refactor_packages.events.skills;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class BodySkillEvents {

    private static final int PURGE_INTERVAL_TICKS = 100;
    private static final int PURGE_AMOUNT_TICKS = 200;

    private BodySkillEvents() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;
        if (player.tickCount % PURGE_INTERVAL_TICKS != 0) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        if (entityData.hasSkill(ModSkills.TURBID_ENERGY_PURGE.getId())) {
            purgeOneHarmfulEffect(player);
        }
    }

    private static void purgeOneHarmfulEffect(Player player) {
        List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());

        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().value().isBeneficial()) continue;

            int newDuration = effect.getDuration() - PURGE_AMOUNT_TICKS;

            player.removeEffect(effect.getEffect());

            if (newDuration > 0) {
                player.addEffect(new MobEffectInstance(
                        effect.getEffect(),
                        newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                ));
            }

            return;
        }
    }

}
