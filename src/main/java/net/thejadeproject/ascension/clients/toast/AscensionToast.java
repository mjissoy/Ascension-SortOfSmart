package net.thejadeproject.ascension.clients.toast;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.AscensionCraft;

public class AscensionToast implements Toast {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "toast/ascension");

    private static final long DISPLAY_TIME = 7500L;

    private static final int WIDTH = 180;
    private static final int HEIGHT = 32;

    private static final int TEXT_X = 30;

    private static final float MESSAGE_SCALE = 0.85F;

    private final Component title;
    private final Component message;
    private final ItemStack icon;

    public AscensionToast(Component title, Component message, ItemStack icon) {
        this.title = title;
        this.message = message;
        this.icon = icon;
    }

    @Override
    public Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceVisible) {
        Minecraft minecraft = toastComponent.getMinecraft();
        Font font = minecraft.font;

        guiGraphics.blitSprite(BACKGROUND, 0, 0, width(), height());

        guiGraphics.renderFakeItem(icon, 8, 8);

        guiGraphics.drawString(
                font,
                title.copy().withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD),
                TEXT_X,
                7,
                0xFFFFFF,
                false
        );

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(MESSAGE_SCALE, MESSAGE_SCALE, 1.0F);

        guiGraphics.drawString(
                font,
                message.copy().withStyle(ChatFormatting.YELLOW),
                (int) (TEXT_X / MESSAGE_SCALE),
                (int) (20 / MESSAGE_SCALE),
                0xFFFFFF,
                false
        );

        guiGraphics.pose().popPose();

        double displayTime = DISPLAY_TIME * toastComponent.getNotificationDisplayTimeMultiplier();
        return timeSinceVisible >= displayTime ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public int width() {
        return WIDTH;
    }

    @Override
    public int height() {
        return HEIGHT;
    }
}