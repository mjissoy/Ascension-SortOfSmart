package net.thejadeproject.ascension.util;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.CultivationData;
import net.thejadeproject.ascension.cultivation.NetworkHandler;
import net.thejadeproject.ascension.cultivation.PlayerData;
import net.thejadeproject.ascension.guis.Introspection.Overlay;
import net.thejadeproject.ascension.guis.easygui.screens.MainScreen;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;

public class KeyBindHandler {
    private static boolean wasCultivating = false;

    private KeyBindHandler() {
    }

    public static boolean isCultivating(Player player) {
        if (player.level().isClientSide) {
            boolean isDown = KeyBindHandler.CULTIVATE_KEY.isDown();
            if (isDown && !wasCultivating) {
                NetworkHandler.sendCultivationStart(player.getUUID());
            }
            wasCultivating = isDown;
            return isDown;
        }
        return player.getPersistentData().getBoolean("isCultivating");
    }


    public static final KeyMapping CULTIVATE_KEY = new KeyMapping("key.ascension.cultivate", 67, "category.ascension.cultivation");

    public static final KeyMapping INTROSPECTION_KEY = new KeyMapping("key.ascension.introspection", 73, "category.ascension.cultivation");


    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
    }

    public static void handleKeyInputEvent(ClientTickEvent.Post event) {
        if(Minecraft.getInstance().level == null && Minecraft.getInstance().getConnection() == null) return;
        if(INTROSPECTION_KEY.consumeClick()){
            try {
                Minecraft.getInstance().setScreen(new MainScreen(Component.literal("Introspection")));
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("failed to open introspection menu");
            }
        }

        PlayerData.PathData data =  Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getPathData("ascension:essence");
        boolean cultivating = data.isCultivating();

        data.setCultivating(CULTIVATE_KEY.isDown());

        if(cultivating != data.isCultivating()){
            System.out.println("sending sync packer");
            PacketDistributor.sendToServer(new SyncCultivationPayload("ascension:essence",data.isCultivating()));
        }
    }
}
