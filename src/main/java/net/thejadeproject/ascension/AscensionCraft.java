package net.thejadeproject.ascension;

import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.realms.RealmRegistry;
import net.thejadeproject.ascension.effects.ModEffects;
import net.thejadeproject.ascension.entity.ModBlockEntities;
import net.thejadeproject.ascension.items.ModCreativeModeTabs;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.items.pills.DynamicPillsSystem;
import net.thejadeproject.ascension.items.pills.ItemColorProvider;
import net.thejadeproject.ascension.network.ModPayloads;
import net.thejadeproject.ascension.util.KeyBindHandler;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AscensionCraft.MOD_ID)
public class AscensionCraft {
    public static final String MOD_ID = "ascension";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation prefix(String name){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public AscensionCraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        KeyBindHandler.register();

        ModCreativeModeTabs.register(modEventBus);
        RealmRegistry.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModEffects.register(modEventBus);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);


        modEventBus.addListener(this::registerKeyBindings);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLogin);

    }

    private void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindHandler.INTROSPECTION_KEY);
        event.register(KeyBindHandler.CULTIVATE_KEY);
    }

    private void onPlayerTick(PlayerTickEvent.Pre event) {
        // Only process on server side
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity().getPersistentData().getCompound("Cultivation").getBoolean("CultivationState")) {
                System.out.println("cultivating");
                System.out.println(event.getEntity().getPersistentData().getCompound("Cultivation").getFloat("CultivationProgress"));
                CultivationSystem.cultivate(event.getEntity());
            }
        }
    }

    private void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        CultivationSystem.initPlayerCultivation(event.getEntity());
        CultivationSystem.updatePlayerAttributes(event.getEntity());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DynamicPillsSystem.loadPills();
        DynamicPillsSystem.registerPills();

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    /*public class ClientModInitializer {
        public static void setup(IEventBus modEventBus) {
            modEventBus.addListener(ClientModInitializer::clientSetup);
        }


        private static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                DynamicPillsSystem.PILL_CONFIGS.forEach((id, config) -> {
                    Item item = Registries.ITEM.get(id);
                    if (item instanceof DynamicPillsSystem.PillItem) {
                        ItemColors.register(new ItemColorProvider(), item);
                    }
                });
            });
        }
    }
*/







    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event){
            ModPayloads.registerPayloads(event);
        }
    }
}