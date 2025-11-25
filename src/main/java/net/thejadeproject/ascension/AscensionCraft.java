package net.thejadeproject.ascension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.entity.ModBlockEntities;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerAttributeManager;
import net.thejadeproject.ascension.network.clientBound.OpenPickPhysiqueScreen;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.progression.dao.ModDao;
import net.thejadeproject.ascension.progression.physiques.ModPhysiques;
import net.thejadeproject.ascension.cultivation.realms.RealmRegistry;
import net.thejadeproject.ascension.effects.ModEffects;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.client.rat.RatRenderer;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.loot.ModLootModifiers;
import net.thejadeproject.ascension.network.ModPayloads;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.particle.ModParticles;
import net.thejadeproject.ascension.particle.particles.CultivationParticles;
import net.thejadeproject.ascension.recipe.ModRecipes;
import net.thejadeproject.ascension.menus.ModMenuTypes;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.ModSkills;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.util.KeyBindHandler;

import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModAttributes;
import net.thejadeproject.ascension.util.ModTags;
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
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AscensionCraft.MOD_ID)
public class AscensionCraft {
    public static float hue;
    public static final String MOD_ID = "ascension";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation prefix(String name){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.

    public void register(IEventBus modEventBus){


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
        //register easy gui actions



    }

    public AscensionCraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onLoadComplete);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

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
        event.register(KeyBindHandler.SKILL_MENU_KEY);
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
    private  void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event){
        System.out.println(event.getEntity().getData(ModAttachments.PHYSIQUE));
    }
    private void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {


        Player player = (Player) event.getEntity();
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(player.getData(ModAttachments.MOVEMENT_SPEED)); //
        if(!event.getEntity().level().isClientSide()){
            PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new SyncAttackDamageAttribute(player.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));

            if(player.getData(ModAttachments.PHYSIQUE).equals("ascension:empty_vessel")){
                //open menu
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new OpenPickPhysiqueScreen(true));

            }else{
                Minecraft.getInstance().tell(()->{
                    PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPlayerPhysique(player.getData(ModAttachments.PHYSIQUE)));
                });
            }

        }

        for(CultivationData.PathData path : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()){

            Minecraft.getInstance().tell(()->{
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(),new SyncPathDataPayload(
                        path.pathId,
                        path.majorRealm,
                        path.minorRealm,
                        path.pathProgress,
                        path.technique,
                        path.stabilityCultivationTicks
                ));
            });
        }
    }


    public void onLoadComplete(FMLLoadCompleteEvent event) {
        PlayerAttributeManager.changeAttributeRange(1,Double.MAX_VALUE,(RangedAttribute) Attributes.MAX_HEALTH.value());

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());


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

    }


    @EventBusSubscriber(modid = AscensionCraft.MOD_ID)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.RAT.get(), RatRenderer::new);

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

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.CULTIVATION_PARTICLES.get(), CultivationParticles.Provider::new);
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event){
            System.out.println("Registering stuff");
            ModPayloads.registerPayloads(event);
        }


    }
}