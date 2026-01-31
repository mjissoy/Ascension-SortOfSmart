package net.thejadeproject.ascension.progression.skills.data;


import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.screens.SelectSkillMenu;
import net.thejadeproject.ascension.network.serverBound.ChangeSkillSlotSpellPayload;
import net.thejadeproject.ascension.network.serverBound.ServerCastSkillPayload;
import net.thejadeproject.ascension.network.serverBound.input.ChangePlayerInputState;
import net.thejadeproject.ascension.progression.skills.active_skills.lightning.Testing;
import net.thejadeproject.ascension.util.KeyBindHandler;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

//TODO handles everything related to skill key detection
//TODO make a way for them to "register" down,held and release events
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
public class SkillInputHandler {

    public static final KeyMapping CAST_SKILL_KEY = new KeyMapping("key.ascension.cast_skill", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_V, KeyBindHandler.CULTIVATION_CATEGORY);

    public static InputHandlers castInputHandler = (new InputHandlers("cast_skill_input"))
            .setOnButtonDown((mod)-> PacketDistributor.sendToServer(new ServerCastSkillPayload()));
    private final static Set<KeyMapping> state = new HashSet<>();
    private final static HashMap<KeyMapping,InputHandlers> mappingInputHandlers = new HashMap<>();
    public static class InputHandlers{
        public Consumer<Integer> buttonDown = (val)->{};
        public Consumer<Integer> buttonHeld =  (val)->{};
        public Consumer<Integer> buttonReleased =  (val)->{};
        public String inputName;
        public InputHandlers(String inputName){
            this.inputName = inputName;
        }
        public InputHandlers setOnButtonDown(Consumer<Integer> inputEvent){
            this.buttonDown = inputEvent;
            return this;
        }
        public InputHandlers setOnButtonHeld(Consumer<Integer> inputEvent){
            this.buttonHeld = inputEvent;
            return this;
        }
        public InputHandlers setOnButtonReleased(Consumer<Integer> inputEvent){
            this.buttonReleased = inputEvent;
            return this;
        }

    }

    public static void setOnButtonDown(KeyMapping mapping,String inputName, Consumer<Integer> inputEvent){
        mappingInputHandlers.computeIfAbsent(mapping,(key)->new InputHandlers(inputName));
        mappingInputHandlers.get(mapping).buttonDown = inputEvent;
    }
    public static void setOnButtonHeld(KeyMapping mapping,String inputName, Consumer<Integer> inputEvent){
        mappingInputHandlers.computeIfAbsent(mapping,(key)->new InputHandlers(inputName));
        mappingInputHandlers.get(mapping).buttonHeld = inputEvent;
    }
    public static void setOnButtonReleased(KeyMapping mapping,String inputName, Consumer<Integer> inputEvent){
        mappingInputHandlers.computeIfAbsent(mapping,(key)->new InputHandlers(inputName));
        mappingInputHandlers.get(mapping).buttonReleased = inputEvent;
    }

    public static void sendSatePacket(String name,int modifier,boolean isDown){
        PacketDistributor.sendToServer(new ChangePlayerInputState(name,modifier,isDown));
    }

    public static void clearInputHandlers(){
        for(KeyMapping mapping : state){
            sendSatePacket(mappingInputHandlers.get(mapping).inputName,0,false);
        }
        state.clear();
        mappingInputHandlers.clear();
    }
    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseButton.Pre event){
        if (Minecraft.getInstance().player == null) return;
        handleInput(event.getButton(),event.getAction(),event.getModifiers());
    }
    @SubscribeEvent
    public static void keyInputEvent(InputEvent.Key event) {
        if (Minecraft.getInstance().player == null) return;
        handleInput(event.getKey(),event.getAction(),event.getModifiers());
    }

    public static void handleInput(int button, int action,int modifiers){
        Minecraft minecraft = Minecraft.getInstance();

        if(CAST_SKILL_KEY.getKey().getValue() == button && action == GLFW.GLFW_PRESS){

            state.add(CAST_SKILL_KEY);
            castInputHandler.buttonDown.accept(modifiers);
            sendSatePacket(castInputHandler.inputName,modifiers,true);

        }else if (button == CAST_SKILL_KEY.getKey().getValue() && action == GLFW.GLFW_RELEASE){
            //key released
            state.remove(CAST_SKILL_KEY);
            castInputHandler.buttonReleased.accept(modifiers);
            sendSatePacket(castInputHandler.inputName,modifiers,false);
        }else if (button == CAST_SKILL_KEY.getKey().getValue() && action == GLFW.GLFW_REPEAT){
            //repeat press
            castInputHandler.buttonHeld.accept(modifiers);
        }
        for(Map.Entry<KeyMapping,InputHandlers> keyHandler : mappingInputHandlers.entrySet()){
            if(keyHandler.getKey().getKey().getValue() == button && action == GLFW.GLFW_PRESS){

                //mouse down
                state.add(keyHandler.getKey());
                keyHandler.getValue().buttonDown.accept(modifiers);
                    sendSatePacket(keyHandler.getValue().inputName,modifiers,true);

            }else if (button == keyHandler.getKey().getKey().getValue() && action == GLFW.GLFW_RELEASE){
                //key released
                state.remove(keyHandler.getKey());
                keyHandler.getValue().buttonReleased.accept(modifiers);
                sendSatePacket(keyHandler.getValue().inputName,modifiers,false);
            }else if (button == keyHandler.getKey().getKey().getValue() && action == GLFW.GLFW_REPEAT){
                //repeat press
                keyHandler.getValue().buttonHeld.accept(modifiers);
            }
        }

    }
}
