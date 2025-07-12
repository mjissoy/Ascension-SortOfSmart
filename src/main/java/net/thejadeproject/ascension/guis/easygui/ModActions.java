package net.thejadeproject.ascension.guis.easygui;

import com.google.gson.JsonPrimitive;
import net.lucent.easygui.EasyGui;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.templating.IRenderableDeserializer;
import net.lucent.easygui.templating.actions.IAction;
import net.lucent.easygui.templating.registry.EasyGuiRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.CultivationData;

public class ModActions {
    public static final DeferredRegister<IAction> ACTIONS =DeferredRegister.create(EasyGuiRegistries.Actions.ACTION_REGISTRY,AscensionCraft.MOD_ID);

    public static final DeferredHolder<IAction, Clickable.IClickAction> CHANGE_MENU_ACTIVE_PANEL = ACTIONS.register("change_active_panel",
    ()->
        new Clickable.IClickAction(){

            @Override
            public void run(ContainerRenderable renderable, double mouseX, double mouseY, int button, boolean clicked, Object[] args) {
                if(!clicked) return;
                if(renderable.getID().equals("stats_button")){
                    renderable.getScreen().getElementByID("stats_panel").setActive(true);
                    renderable.getScreen().getElementByID("other_panel").setActive(false);
                } else if (renderable.getID().equals("other_button")) {
                    renderable.getScreen().getElementByID("other_panel").setActive(true);
                    renderable.getScreen().getElementByID("stats_panel").setActive(false);
                }

            }
        }
    );
    public static final DeferredHolder<IAction,IAction> DISPLAY_ATTRIBUTE_VALUE = ACTIONS.register("display_attribute",
            ()->new IAction(){
                @Override
                public void run(ContainerRenderable renderable, Object[] customArgs) {
                    if(customArgs.length != 1) return;
                    String attribute = customArgs[0] instanceof JsonPrimitive ? ((JsonPrimitive) customArgs[0]).getAsString() : (String) customArgs[0];
                    Player player = Minecraft.getInstance().player;

                    Label label = ((Label) renderable);
                    if(player == null )return;

                    if(attribute.equals("Progress")){
                        label.text = CultivationData.ClientCultivationData.getProgressUI();
                    }else if(attribute.equals("Major Realm")){
                        label.text = CultivationData.ClientCultivationData.getMajorRealmUI();
                    }else if(attribute.equals("Minor Realm")){
                        label.text = CultivationData.ClientCultivationData.getMinorRealmUI();
                    }else if(attribute.equals("Max Health")){
                        label.text =  Component.literal(Double.toString(player.getMaxHealth()));
                    }else if(attribute.equals("Health")){
                        label.text = Component.literal(Double.toString(player.getHealth()));
                    }else if(attribute.equals("Armor")){
                        label.text =  Component.literal(Double.toString(player.getArmorValue()));
                    }else if(attribute.equals("Attack")){
                        label.text =  Component.literal(Double.toString(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
                    }else if(attribute.equals("Speed")){
                        label.text =  Component.literal(Double.toString(player.getSpeed()));
                    }else if(attribute.equals("Attack Speed")){
                        label.text =  Component.literal(Double.toString(player.getAttribute(Attributes.ATTACK_SPEED).getBaseValue()));
                    }
                    else if(attribute.equals("Jump Strength")){
                        label.text =  Component.literal(Double.toString(player.getAttribute(Attributes.JUMP_STRENGTH).getBaseValue()));
                    }

                    label.width = label.font.width(label.text);

                }
            });
    public static void register(IEventBus modEventBus){
        ACTIONS.register(modEventBus);
    }
}
