package net.thejadeproject.ascension;

import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.custom.functions.FreezingEffectItems;
import net.thejadeproject.ascension.blocks.entity.ModBlockEntities;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerAttributeManager;
import net.thejadeproject.ascension.menus.spatialrings.SpatialRingUtils;
import net.thejadeproject.ascension.network.clientBound.OpenPickPhysiqueScreen;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.progression.dao.ModDao;
import net.thejadeproject.ascension.progression.physiques.ModPhysiques;
import net.thejadeproject.ascension.cultivation.realms.RealmRegistry;
import net.thejadeproject.ascension.effects.ModEffects;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.loot.ModLootModifiers;
import net.thejadeproject.ascension.network.ModPayloads;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.particle.ModParticles;
import net.thejadeproject.ascension.recipe.ModRecipes;
import net.thejadeproject.ascension.menus.ModMenuTypes;
import net.thejadeproject.ascension.recipe.crafting.CopySpatialringDataRecipeShaped;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.ModSkills;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.sects.SectCommand;
import net.thejadeproject.ascension.sects.SectEventHandler;
import net.thejadeproject.ascension.sects.SectManager;
import net.thejadeproject.ascension.sects.missions.SectMissionEventHandler;
import net.thejadeproject.ascension.util.KeyBindHandler;

import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModAttributes;
import net.thejadeproject.ascension.util.ToolTips.ToolTipManager;
import net.thejadeproject.ascension.worldgen.biome.ModTerrablender;
import net.thejadeproject.ascension.worldgen.biome.surface.ModSurfaceRules;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.SurfaceRuleManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AscensionCraft.MOD_ID)
public class AscensionCraft {
    public static float hue;
    public static final String MOD_ID = "ascension";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<String, SectManager> WORLD_SECT_MANAGERS = new HashMap<>(); // World ID -> SectManager
    public static final Map<String, String> SECT_DATA = new HashMap<>();

    public static ResourceLocation prefix(String name){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.

    private static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COPYRECIPE = RECIPES.register("spatialring_upgrade", CopySpatialringDataRecipeShaped.Serializer::new);
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> SPATIALRING_UUID = COMPONENTS.register("spatialring_uuid", () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());

    public void register(IEventBus modEventBus){
        COMPONENTS.register(modEventBus);
        RECIPES.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        RealmRegistry.register(modEventBus);

        ModTerrablender.registerBiomes();

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModAttachments.register(modEventBus);

        ModParticles.register(modEventBus);
        ModEntities.register(modEventBus);

        ModItems.register(modEventBus);
        ModEffects.register(modEventBus);

        ModAttributes.register(modEventBus);

        ModPhysiques.register(modEventBus);
        ModSkills.register(modEventBus);
        ModTechniques.register(modEventBus);
        ModDao.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    public AscensionCraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onLoadComplete);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.register(new SectEventHandler());
        NeoForge.EVENT_BUS.register(new SectMissionEventHandler());

        register(modEventBus);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC, "ascension/Ascension-Common.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CULTIVATION_SPEC, "ascension/Ascension-Cultivation.toml");

        modEventBus.addListener(this::registerKeyBindings);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLogin);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLogOut);
    }

    private void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindHandler.INTROSPECTION_KEY);
        event.register(KeyBindHandler.CULTIVATE_KEY);
        event.register(KeyBindHandler.OPEN_SPATIAL_RING_KEY);
        event.register(KeyBindHandler.TOGGLE_ARTIFACT_MODE_KEY);
    }

    private void onPlayerTick(PlayerTickEvent.Pre event) {
        // Only process on server side
        if (!event.getEntity().level().isClientSide) {
            if (event.getEntity().getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").isCultivating()
                    && !event.getEntity().getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").technique.equals("ascension:none")){

                String technique = event.getEntity().getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").technique;
                AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(technique,':')).tryCultivate(event.getEntity());
            }
        }
    }

    private void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event){
        System.out.println(event.getEntity().getData(ModAttachments.PHYSIQUE));
    }

    private void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = (Player) event.getEntity();
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(player.getData(ModAttachments.MOVEMENT_SPEED));

        if(!event.getEntity().level().isClientSide()){
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new SyncAttackDamageAttribute(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));

            if(player.getData(ModAttachments.PHYSIQUE).equals("ascension:empty_vessel")){
                //open menu
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new OpenPickPhysiqueScreen(true));
            }else{
                PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPlayerPhysique(player.getData(ModAttachments.PHYSIQUE)));

            }
            for(CultivationData.PathData path : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()){
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new SyncPathDataPayload(
                        path.pathId,
                        path.majorRealm,
                        path.minorRealm,
                        path.pathProgress,
                        path.technique,
                        path.stabilityCultivationTicks
                ));
            }
        }


    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        PlayerAttributeManager.changeAttributeRange(1,Double.MAX_VALUE,(RangedAttribute) Attributes.MAX_HEALTH.value());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
        ToolTipManager.registerAllTooltips();
        FreezingEffectItems.onCommonSetup(event);
        SpatialRingUtils.checkCuriosLoaded();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.RAT_SPAWN_EGG);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Each world gets its own sect manager
        String worldId = event.getServer().getWorldData().getLevelName();
        SectManager manager = SectManager.get(event.getServer(), worldId);
        WORLD_SECT_MANAGERS.put(worldId, manager);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        // Save all world sect managers
        for (SectManager manager : WORLD_SECT_MANAGERS.values()) {
            if (manager != null) {
                manager.save();
            }
        }
        WORLD_SECT_MANAGERS.clear();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        SectCommand.register(event.getDispatcher());
    }

    // Get sect manager for current world
    public static SectManager getSectManager(net.minecraft.server.MinecraftServer server) {
        if (server == null) return null;
        String worldId = server.getWorldData().getLevelName();
        return WORLD_SECT_MANAGERS.get(worldId);
    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID)
    public static class ClientModEvents {



        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event){
            System.out.println("Registering stuff");
            ModPayloads.registerPayloads(event);
        }
    }

}