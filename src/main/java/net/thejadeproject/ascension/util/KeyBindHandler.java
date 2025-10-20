package net.thejadeproject.ascension.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.NetworkHandler;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.guis.easygui.screens.MainScreen;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionEarth;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionHeaven;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionHuman;
import net.thejadeproject.ascension.menus.spatialrings.OpenSpatialRingPacket;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;
import net.thejadeproject.ascension.network.serverBound.ToggleTabletDropModePayload;

@OnlyIn(Dist.CLIENT)
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
    public static final KeyMapping OPEN_SPATIAL_RING_KEY = new KeyMapping("key.ascension.open_spatial_ring", 82, "category.ascension.cultivation"); // 'R' key
    public static final KeyMapping TOGGLE_ARTIFACT_MODE_KEY = new KeyMapping("key.ascension.toggle_artifact_mode", 77, "category.ascension.cultivation"); // 'M' key

    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
    }

    public static void handleKeyInputEvent(ClientTickEvent.Post event) {
        if(Minecraft.getInstance().level == null && Minecraft.getInstance().getConnection() == null) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // Handle Introspection key
        if(INTROSPECTION_KEY.consumeClick()){
            try {
                Minecraft.getInstance().setScreen(new MainScreen(Component.literal("Introspection")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("failed to open introspection menu");
            }
        }

        // Handle Spatial Ring key - SEND PACKET TO SERVER
        if(OPEN_SPATIAL_RING_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new OpenSpatialRingPacket());
        }

        // Handle Toggle Tablet Mode key
        if(TOGGLE_ARTIFACT_MODE_KEY.consumeClick()) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offHandItem = player.getOffhandItem();

            // Check if player is holding either tablet in main hand or offhand
            if (mainHandItem.getItem() instanceof TabletOfDestructionHeaven ||
                    mainHandItem.getItem() instanceof TabletOfDestructionEarth ||
                    offHandItem.getItem() instanceof TabletOfDestructionHeaven ||
                    offHandItem.getItem() instanceof TabletOfDestructionEarth) {

                // Send packet to server to toggle drop mode
                PacketDistributor.sendToServer(new ToggleTabletDropModePayload());
            }
        }

        // Handle Cultivation key
        CultivationData.PathData data = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence");
        boolean cultivating = data.isCultivating();

        data.setCultivating(CULTIVATE_KEY.isDown());

        if(cultivating != data.isCultivating()){
            System.out.println("sending sync packer");
            PacketDistributor.sendToServer(new SyncCultivationPayload("ascension:essence", data.isCultivating()));
        }
    }
}