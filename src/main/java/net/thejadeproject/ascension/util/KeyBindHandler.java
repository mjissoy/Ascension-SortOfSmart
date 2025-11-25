package net.thejadeproject.ascension.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.player.Player;
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
import net.thejadeproject.ascension.guis.easygui.screens.SelectSkillMenu;
import net.thejadeproject.ascension.guis.easygui.screens.SkillMenuScreen;
import net.thejadeproject.ascension.network.serverBound.ServerCastSkillPayload;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;

@OnlyIn(Dist.CLIENT)
public class KeyBindHandler {

    public static final String CULTIVATION_CATEGORY = "category.ascension.cultivation";
    public static final String MENU_CATEGORY = "category.ascension.menu";
    public static final String SKILL_CATEGORY = "category.ascension.skills";
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


    public static final KeyMapping CULTIVATE_KEY = new KeyMapping("key.ascension.cultivate", 67, CULTIVATION_CATEGORY);

    public static final KeyMapping INTROSPECTION_KEY = new KeyMapping("key.ascension.introspection", 73, MENU_CATEGORY);

    public static final KeyMapping SKILL_MENU_KEY = new KeyMapping("key.ascension.skill_menu", 74, MENU_CATEGORY);

    public static final KeyMapping CAST_SKILL_KEY = new KeyMapping("key.ascension.cast_skill",KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_V, SKILL_CATEGORY);


    public static final KeyMapping SKILL_WHEEL_KEY = new KeyMapping("key.ascension.skill_wheel",KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_R, SKILL_CATEGORY);

    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
        eventBus.addListener(EventPriority.HIGHEST,KeyBindHandler::keyInputEvents);
    }

    public static void keyInputEvents(InputEvent.Key event){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null && minecraft.getConnection() == null) return;

        if(CAST_SKILL_KEY.consumeClick()){
            PacketDistributor.sendToServer(new ServerCastSkillPayload());
        }
        //a bit hacky
        if(event.getKey() == SKILL_WHEEL_KEY.getKey().getValue() && event.getAction() == 1){
            //Open menu
            if(minecraft.screen == null){
                try {
                    SelectSkillMenu.open(Component.literal("SKill Wheel"));
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    System.out.println("failed to open SKill Wheel");
                }
            }
        }else if(event.getKey() == SKILL_WHEEL_KEY.getKey().getValue() && event.getAction() == 0){
            //Close
            System.out.println("TRYING TO CLOSE SKILL WHEEL");
            if(minecraft.screen != null && SelectSkillMenu.hasInstance()){
                SelectSkillMenu.close();
            }
        }

    }

    public static void handleKeyInputEvent(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.level == null && minecraft.getConnection() == null) return;



        if(INTROSPECTION_KEY.consumeClick()){
            try {
                minecraft.setScreen(new MainScreen(Component.literal("Introspection")));
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("failed to open introspection menu");
            }
        }
        if(SKILL_MENU_KEY.consumeClick()){
            System.out.println("J KEY PRESSED");
            try {
                minecraft.setScreen(new SkillMenuScreen(Component.literal("Skill Menu")));
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("failed to open skill menu");
            }
        }

        CultivationData.PathData data =  Minecraft.getInstance().player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence");
        boolean cultivating = data.isCultivating();

        data.setCultivating(CULTIVATE_KEY.isDown());

        if(cultivating != data.isCultivating()){
            System.out.println("sending sync packer");
            PacketDistributor.sendToServer(new SyncCultivationPayload("ascension:essence",data.isCultivating()));
        }
    }
}
