package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class RunicBacklashHelper {

    private RunicBacklashHelper() {
    }

    public static void applyMinorBacklash(ServerPlayer player, String reason) {
        player.displayClientMessage(
                Component.translatable("ascension.runic.cast.failure"),
                true
        );

        player.hurt(player.damageSources().generic(), 2.0F);
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
    }
}