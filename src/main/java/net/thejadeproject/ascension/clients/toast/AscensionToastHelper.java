package net.thejadeproject.ascension.clients.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class AscensionToastHelper {

    private AscensionToastHelper() {}

    public static void show(Component title, Component message, ItemStack icon) {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.execute(() -> {
            minecraft.getToasts().addToast(new AscensionToast(title, message, icon));
        });
    }
}