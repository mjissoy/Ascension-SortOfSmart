package net.thejadeproject.ascension.cultivation.realms;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.AscensionCraft;

public record SubRealm(String key, int qiToNext) {
    public Component getName() {
        return Component.translatable("cultivation." + AscensionCraft.MOD_ID + ".sub_realm." + key);
    }
}
