package net.thejadeproject.ascension.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.NetworkHandler;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.guis.easygui.screens.MainScreen;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionEarth;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionHeaven;
import net.thejadeproject.ascension.items.artifacts.TabletOfDestructionHuman;
import net.thejadeproject.ascension.menus.spatialrings.OpenSpatialRingPacket;
import net.thejadeproject.ascension.guis.easygui.screens.SelectSkillMenu;
import net.thejadeproject.ascension.guis.easygui.screens.SkillMenuScreen;
import net.thejadeproject.ascension.network.serverBound.ServerCastSkillPayload;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;
import net.thejadeproject.ascension.network.serverBound.ToggleTabletDropModePayload;

@OnlyIn(Dist.CLIENT)
public class KeyBindHandler {

    public static final String CULTIVATION_CATEGORY = "category.ascension.cultivation";
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

    public static final KeyMapping OPEN_SPATIAL_RING_KEY = new KeyMapping("key.ascension.open_spatial_ring", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_B, CULTIVATION_CATEGORY);
    public static final KeyMapping TOGGLE_ARTIFACT_MODE_KEY = new KeyMapping("key.ascension.toggle_artifact_mode", 77, CULTIVATION_CATEGORY);
    public static final KeyMapping INTROSPECTION_KEY = new KeyMapping("key.ascension.introspection", 73, CULTIVATION_CATEGORY);
    public static final KeyMapping SKILL_MENU_KEY = new KeyMapping("key.ascension.skill_menu", 74, CULTIVATION_CATEGORY);
    public static final KeyMapping CULTIVATE_KEY = new KeyMapping("key.ascension.cultivate", 67, CULTIVATION_CATEGORY);
    public static final KeyMapping SKILL_WHEEL_KEY = new KeyMapping("key.ascension.skill_wheel", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_R, CULTIVATION_CATEGORY);
    public static final KeyMapping CAST_SKILL_KEY = new KeyMapping("key.ascension.cast_skill", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_V, CULTIVATION_CATEGORY);

    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
        eventBus.addListener(EventPriority.HIGHEST, KeyBindHandler::keyInputEvents);
    }

    public static void keyInputEvents(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null && minecraft.getConnection() == null) return;

        if (CAST_SKILL_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ServerCastSkillPayload());
        }
        // a bit hacky
        if (event.getKey() == SKILL_WHEEL_KEY.getKey().getValue() && event.getAction() == 1) {
            // Open menu
            if (minecraft.screen == null) {
                try {
                    SelectSkillMenu.open(Component.literal("Skill Wheel"));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("failed to open Skill Wheel");
                }
            }
        } else if (event.getKey() == SKILL_WHEEL_KEY.getKey().getValue() && event.getAction() == 0) {
            // Close
            System.out.println("TRYING TO CLOSE SKILL WHEEL");
            if (minecraft.screen != null && SelectSkillMenu.hasInstance()) {
                SelectSkillMenu.close();
            }
        }
    }

    public static void handleKeyInputEvent(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null && minecraft.getConnection() == null) return;

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (INTROSPECTION_KEY.consumeClick()) {
            try {
                minecraft.setScreen(new MainScreen(Component.literal("Introspection")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("failed to open introspection menu");
            }
        }
        if (SKILL_MENU_KEY.consumeClick()) {
            System.out.println("J KEY PRESSED");
            try {
                minecraft.setScreen(new SkillMenuScreen(Component.literal("Skill Menu")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("failed to open skill menu");
            }
        }

        // Handle Spatial Ring key - SEND PACKET TO SERVER
        if (OPEN_SPATIAL_RING_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new OpenSpatialRingPacket());
        }

        // Handle Toggle Tablet Mode key
        if (TOGGLE_ARTIFACT_MODE_KEY.consumeClick()) {
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
        if(player.getData(ModAttachments.PLAYER_DATA) != null){
            CultivationData.PathData data = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence");
            boolean cultivating = data.isCultivating();

            data.setCultivating(CULTIVATE_KEY.isDown());

            if (cultivating != data.isCultivating()) {
                System.out.println("sending sync packer");
                PacketDistributor.sendToServer(new SyncCultivationPayload("ascension:essence", data.isCultivating()));
            }
        }
    }
}