package net.thejadeproject.ascension;

import net.lucent.easygui.EasyGui;
import net.lucent.easygui.elements.other.SquareRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.overlays.EasyGuiOverlayManager;
import net.lucent.easygui.templating.registry.EasyGuiRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.thejadeproject.ascension.clients.FlameGourdClientTooltip;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.client.rat.RatRenderer;
import net.thejadeproject.ascension.events.custom.OpenPhysiqueSelectScreenEvent;
import net.thejadeproject.ascension.events.custom.PhysiqueGeneratedEvent;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.ModOverlays;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.artifacts.FlameGourd;
import net.thejadeproject.ascension.menus.ModMenuTypes;
import net.thejadeproject.ascension.menus.custom.pill_cauldron.PillCauldronLowHumanScreen;
import net.thejadeproject.ascension.menus.spatialrings.SRScreen;
import net.thejadeproject.ascension.network.ModPayloads;
import net.thejadeproject.ascension.particle.ModParticles;
import net.thejadeproject.ascension.particle.particles.CultivationParticles;
import net.thejadeproject.ascension.sects.SectDepositMenu;
import net.thejadeproject.ascension.sects.SectDepositScreen;
import net.thejadeproject.ascension.util.KeyBindHandler;

@Mod(value = AscensionCraft.MOD_ID,dist = Dist.CLIENT)
public class AscensionCraftClient {
    public AscensionCraftClient(IEventBus modEventBus, ModContainer modContainer)
    {
        KeyBindHandler.register();
        ModActions.register(modEventBus);
        ModOverlays.register();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
    static class ClientEvents{

        @SubscribeEvent
        public static void physiqueGenerateEvent(OpenPhysiqueSelectScreenEvent event){
            Minecraft.getInstance().setScreen(new GeneratePhysiqueScreen(Component.literal("Select")));
        }
        @SubscribeEvent
        public static void physiqueGenerateEvent(PhysiqueGeneratedEvent event){
            
           if(Minecraft.getInstance().screen instanceof GeneratePhysiqueScreen screen){
               screen.updateGeneratedPhysiques(event.generatedPhysique,event.otherPhysiques);
           }
        }
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.PILL_CAULDRON_LOW_HUMAN_MENU.get(), PillCauldronLowHumanScreen::new);
            event.register(ModMenuTypes.SR_CONTAINER.get(), SRScreen::new);
            event.register(ModMenuTypes.SECT_DEPOSIT_MENU.get(), SectDepositScreen::new);
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.CULTIVATION_PARTICLES.get(), CultivationParticles.Provider::new);
        }

        @SubscribeEvent
        public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(FlameGourd.FlameGourdTooltip.class, FlameGourdClientTooltip::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.RAT.get(), RatRenderer::new);

            EntityRenderers.register(ModEntities.POISON_PILL.get(), ThrownItemRenderer::new);



            event.enqueueWork(() -> {
                ItemProperties.register(ModItems.SPIRITUAL_STONE.get(),
                        ResourceLocation.fromNamespaceAndPath("ascension", "stack_size"),
                        (itemStack, clientLevel, livingEntity, seed) -> {
                            int count = itemStack.getCount();
                            if (count >= 32) return 3.0F;  // Large stack
                            if (count >= 16) return 2.0F;  // Medium stack
                            if (count >= 2) return 1.0F;   // Small stack
                            return 0.0F;                   // Single item
                        });
            });
        }

    }



}
