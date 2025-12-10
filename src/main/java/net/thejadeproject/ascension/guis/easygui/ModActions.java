package net.thejadeproject.ascension.guis.easygui;

import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.InputConstants;
import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.ContainerRenderable;
import net.lucent.easygui.interfaces.complex_events.Sticky;
import net.lucent.easygui.interfaces.events.Clickable;
import net.lucent.easygui.templating.actions.IAction;
import net.lucent.easygui.templating.registry.EasyGuiRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data.PhysiqueDataContainer;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.path_data.DisplayPathDataContainer;
import net.thejadeproject.ascension.guis.easygui.elements.main_menu.draggable_data.TechniqueDataContainer;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@OnlyIn(Dist.CLIENT)
public class ModActions {
    public static final DeferredRegister<IAction> ACTIONS =DeferredRegister.create(EasyGuiRegistries.Actions.ACTION_REGISTRY,AscensionCraft.MOD_ID);

    public static final DeferredHolder<IAction, Clickable.IClickAction> CHANGE_MENU_ACTIVE_PANEL = ACTIONS.register("change_active_panel",
    ()->
        new Clickable.IClickAction(){

            @Override
            public void run(ContainerRenderable renderable, double mouseX, double mouseY, int button, boolean clicked, Object[] args) {
                if(!clicked || button != InputConstants.MOUSE_BUTTON_LEFT) return;
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

    public static final DeferredHolder<IAction, Clickable.IClickAction> CREATE_CONTAINER = ACTIONS.register("create_container",
            ()->
                    new Clickable.IClickAction(){

                        @Override
                        public void run(ContainerRenderable renderable, double mouseX, double mouseY, int button, boolean clicked, Object[] args) {
                            if(!clicked || button != InputConstants.MOUSE_BUTTON_LEFT) return;
                            if(args.length != 1) return;
                            Player player = Minecraft.getInstance().player;
                            if(player == null) return;

                            String id = args[0] instanceof JsonPrimitive ? ((JsonPrimitive) args[0]).getAsString() : (String) args[0];
                            if(id.equals("technique_data")){
                                if( ((DisplayPathDataContainer) renderable.getScreen().getElementByID("path_data_container")).pathData.technique.equals("ascension:none")) return;
                                renderable.getRoot().addChild(new TechniqueDataContainer(renderable.getScreen(),0,0, ((DisplayPathDataContainer) renderable.getScreen().getElementByID("path_data_container")).pathData.technique));
                            } else if (id.equals("physique_data")) {

                                renderable.getRoot().addChild(new PhysiqueDataContainer(renderable.getScreen(),0,0,player.getData(ModAttachments.PHYSIQUE)));
                            }
                            
                            //renderable.getRoot().addChild(new DraggableDataContainer(renderable.getScreen(),0,0));
                            
                        }
                    }
    );

    public static final DeferredHolder<IAction, Clickable.IClickAction> CHANGE_VISIBILITY = ACTIONS.register("change_visibility",
            ()->
                    new Clickable.IClickAction(){

                        @Override
                        public void run(ContainerRenderable renderable, double mouseX, double mouseY, int button, boolean clicked, Object[] args) {
                            if(!clicked || button != InputConstants.MOUSE_BUTTON_LEFT) return;
                            if(args.length != 1) return;
                            String id = args[0] instanceof JsonPrimitive ? ((JsonPrimitive) args[0]).getAsString() : (String) args[0];

                            ContainerRenderable renderable1 = renderable.getScreen().getElementByID(id);
                            renderable1.setVisible(!renderable1.isVisible());
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

                    NumberFormat formatter = new DecimalFormat("#0.00");
                    if(attribute.equals("Max Health")){
                        label.text =  Component.literal(formatter.format(player.getMaxHealth()));
                    }else if(attribute.equals("Health")){
                        label.text = Component.literal(formatter.format(player.getHealth()));
                    }else if(attribute.equals("Armor")){
                        label.text =  Component.literal(formatter.format(player.getArmorValue()));
                    }else if(attribute.equals("Attack")){
                        label.text =  Component.literal(formatter.format(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));
                    }else if(attribute.equals("Speed")){
                        label.text =  Component.literal(formatter.format(player.getSpeed()));
                    }else if(attribute.equals("Attack Speed")){
                        label.text =  Component.literal(formatter.format(player.getAttribute(Attributes.ATTACK_SPEED).getBaseValue()));
                    }else if(attribute.equals("Safe Fall")){
                        label.text =  Component.literal(formatter.format(player.getAttribute(Attributes.SAFE_FALL_DISTANCE).getBaseValue()));

                    }
                    else if(attribute.equals("Jump Strength")){
                        label.text =  Component.literal(formatter.format(player.getAttribute(Attributes.JUMP_STRENGTH).getBaseValue()));
                    }else if(attribute.equals("Physique")){
                        String physique = player.getData(ModAttachments.PHYSIQUE);
                        if(physique == null) label.text = Component.literal("ascension:empty_vessel");
                        else{
                            label.text =
                                    AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                                            physique,':'
                                    )).getDisplayTitle();

                        }

                    }

                    label.width = label.font.width(label.text);

                }
            });
    public static void register(IEventBus modEventBus){
        ACTIONS.register(modEventBus);
    }
}
