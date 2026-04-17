package net.thejadeproject.ascension.runic_path.events;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.runic_path.skills.passive.helpers.HealthRegenHelper;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class RunicPassiveTickHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isDeadOrDying()) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);
        if (entityData == null) return;

        if (entityData.isCultivating()) return;

        double regen = HealthRegenHelper.getRegenPerTick(entityData);
        if (regen <= 0) return;

        if (player.getHealth() < player.getMaxHealth()) {
            player.heal((float) regen);
        }
    }
}