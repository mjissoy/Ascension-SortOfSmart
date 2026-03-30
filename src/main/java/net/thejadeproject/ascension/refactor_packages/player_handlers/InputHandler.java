package net.thejadeproject.ascension.refactor_packages.player_handlers;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.serverBound.input.ChangePlayerInputState;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

//lets us dynamically add and remove "actions" associated with inputs
//these actions are then synced with the server
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
public class InputHandler {
    public static final KeyMapping CAST_SKILL_KEY = new KeyMapping("key.ascension.cast_skill", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_V, "ascension skills");

    private final static HashSet<KeyMapping> state = new HashSet<>();
    //maps a keyMapping->handler
    private final static HashMap<KeyMapping, ActionHandler> actionHandlerMapping = new HashMap<>(){{
        put(CAST_SKILL_KEY,new ActionHandler("skill_cast").setOnDown((mod)-> {
            System.out.println("pressed skill cast key");
           //TODO send cast packet
        }));
    }};
    public static class ActionHandler {
        public Consumer<Integer> actionDown = (val)->{};
        public Consumer<Integer> actionHeld =  (val)->{};
        public Consumer<Integer> actionReleased =  (val)->{};
        public String actionName;

        public ActionHandler(String name){
            this.actionName = name;
        }
        public ActionHandler setOnDown(Consumer<Integer> inputEvent){
            this.actionDown = inputEvent;
            return this;
        }
        public ActionHandler setOnHeld(Consumer<Integer> inputEvent){
            this.actionHeld = inputEvent;
            return this;
        }
        public ActionHandler setOnRelease(Consumer<Integer> inputEvent){
            this.actionReleased = inputEvent;
            return this;
        }

        public static void sendSatePacket(String name,int modifier,boolean isDown){
            PacketDistributor.sendToServer(new ChangePlayerInputState(name,modifier,isDown));
        }
    }


    public static void sendSatePacket(String name,int modifier,boolean isDown){
        PacketDistributor.sendToServer(new ChangePlayerInputState(name,modifier,isDown));
    }
    public void removeKeyMapping(KeyMapping mapping){
        state.remove(mapping);
        sendSatePacket(actionHandlerMapping.remove(mapping).actionName,0,false);
    }
    public void setActionDown(KeyMapping mapping,String actionName, Consumer<Integer> inputEven){
        actionHandlerMapping.computeIfAbsent(mapping,key->new ActionHandler(actionName));
        actionHandlerMapping.get(mapping).setOnDown(inputEven);
    }
    public void setActionHeld(KeyMapping mapping,String actionName, Consumer<Integer> inputEven){
        actionHandlerMapping.computeIfAbsent(mapping,key->new ActionHandler(actionName));
        actionHandlerMapping.get(mapping).setOnHeld(inputEven);
    }
    public void setActionRelease(KeyMapping mapping,String actionName, Consumer<Integer> inputEven){
        actionHandlerMapping.computeIfAbsent(mapping,key->new ActionHandler(actionName));
        actionHandlerMapping.get(mapping).setOnRelease(inputEven);
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


        for(Map.Entry<KeyMapping,ActionHandler> keyHandler : actionHandlerMapping.entrySet()){
            if(keyHandler.getKey().getKey().getValue() == button && action == GLFW.GLFW_PRESS){

                //mouse down
                state.add(keyHandler.getKey());
                keyHandler.getValue().actionDown.accept(modifiers);
                sendSatePacket(keyHandler.getValue().actionName,modifiers,true);

            }else if (button == keyHandler.getKey().getKey().getValue() && action == GLFW.GLFW_RELEASE){
                //key released
                state.remove(keyHandler.getKey());
                keyHandler.getValue().actionReleased.accept(modifiers);
                sendSatePacket(keyHandler.getValue().actionName,modifiers,false);
            }else if (button == keyHandler.getKey().getKey().getValue() && action == GLFW.GLFW_REPEAT){
                //repeat press
                keyHandler.getValue().actionHeld.accept(modifiers);
            }
        }

    }
}
