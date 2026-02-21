package net.thejadeproject.ascension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.thejadeproject.ascension.clients.FlameGourdClientTooltip;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.client.CushionRenderer;
import net.thejadeproject.ascension.entity.client.DummyRenderer;
import net.thejadeproject.ascension.entity.client.rat.RatRenderer;
import net.thejadeproject.ascension.events.ModDataComponents;
import net.thejadeproject.ascension.events.custom.OpenPhysiqueSelectScreenEvent;
import net.thejadeproject.ascension.events.custom.PhysiqueGeneratedEvent;
import net.thejadeproject.ascension.formations.formation_renderers.ModFormationRenderers;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.ModOverlays;
import net.thejadeproject.ascension.guis.easygui.screens.SpatialRingItemContainerScreen;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.artifacts.FlameGourd;
import net.thejadeproject.ascension.menus.ModMenuTypes;
import net.thejadeproject.ascension.menus.custom.pill_cauldron.PillCauldronLowHumanScreen;
import net.thejadeproject.ascension.menus.spatialrings.SpatialRingStorageScreen;
import net.thejadeproject.ascension.menus.spatialrings.SpatialRingUpgradeScreen;
import net.thejadeproject.ascension.particle.ModParticles;
import net.thejadeproject.ascension.particle.particles.CultivationParticles;
import net.thejadeproject.ascension.shaders.client.ModShaders;
import net.thejadeproject.ascension.shaders.client.RiftRenderer;

import net.thejadeproject.ascension.util.KeyBindHandler;

@Mod(value = AscensionCraft.MOD_ID,dist = Dist.CLIENT)
public class AscensionCraftClient {
    public AscensionCraftClient(IEventBus modEventBus, ModContainer modContainer)
    {
        KeyBindHandler.register();
        ModActions.register(modEventBus);
        ModFormationRenderers.register(modEventBus);
        ModOverlays.register();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
    static class ClientEvents{

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.PILL_CAULDRON_LOW_HUMAN_MENU.get(), PillCauldronLowHumanScreen::new);
            event.register(ModMenuTypes.SPATIAL_RING_STORAGE.get(), SpatialRingItemContainerScreen::new);
            event.register(ModMenuTypes.SPATIAL_RING_UPGRADE.get(), SpatialRingUpgradeScreen::new);
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
        public static void registerShaders(RegisterShadersEvent event) {
            try {
                ModShaders.register(event);
            } catch (Exception e) {
                throw new RuntimeException("Failed to register Ascension shaders", e);
            }
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // REGISTER RENDERERS - MUST BE IN enqueueWork
                EntityRenderers.register(ModEntities.RIFT.get(), RiftRenderer::new);
                EntityRenderers.register(ModEntities.RAT.get(), RatRenderer::new);
                EntityRenderers.register(ModEntities.POISON_PILL.get(), ThrownItemRenderer::new);
                EntityRenderers.register(ModEntities.CUSHION_ENTITY.get(), CushionRenderer::new);
                EntityRenderers.register(ModEntities.DUMMY_ENTITY.get(), DummyRenderer::new);
                // Register item properties
                ItemProperties.register(ModItems.SPIRITUAL_STONE.get(),
                        ResourceLocation.fromNamespaceAndPath("ascension", "stack_size"),
                        (itemStack, clientLevel, livingEntity, seed) -> {
                            int count = itemStack.getCount();
                            if (count >= 32) return 3.0F;
                            if (count >= 16) return 2.0F;
                            if (count >= 2) return 1.0F;
                            return 0.0F;
                        });

                ItemProperties.register(ModItems.PHYSIQUE_ESSENCE.get(),
                        ResourceLocation.fromNamespaceAndPath("ascension", "physique_variant"),
                        (itemStack, clientLevel, livingEntity, seed) -> {
                            String physiqueId = itemStack.get(ModDataComponents.PHYSIQUE_ID.get());
                            if (physiqueId != null && physiqueId.equals("ascension:jade_bone_physique")) {
                                return 1.0F;
                            }
                            return 0.0F;
                        });
            });
        }

    }



}
