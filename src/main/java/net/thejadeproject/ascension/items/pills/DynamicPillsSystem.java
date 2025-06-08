package net.thejadeproject.ascension.items.pills;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.Config;
import net.thejadeproject.ascension.effects.ModEffects;
import net.thejadeproject.ascension.items.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DynamicPillsSystem {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    public static final Map<ResourceLocation, PillConfig> PILL_CONFIGS = new HashMap<>();
    private static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get().resolve("ascension").resolve("pills");


    public static class PillConfig {
        final String name;
        final int color;
        final ResourceLocation effectId;
        final int amplification;
        final int duration;

        public PillConfig(String name, int color, ResourceLocation effectId, int amplification, int duration) {
            this.name = name;
            this.color = color;
            this.effectId = effectId;
            this.amplification = amplification;
            this.duration = duration;
        }

        public Registry<PillItem> name() {
            return null;
        }
    }

    public static class PillItem extends Item {
        private final PillConfig config;

        public PillItem(PillConfig config) {
            super(new Item.Properties().stacksTo(64));
            this.config = config;
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            ItemStack stack = user.getMainHandItem();
            if (!level.isClientSide) {
                for (DeferredHolder<MobEffect, ? extends MobEffect> effect: ModEffects.MOB_EFFECT.getEntries()) {
                    if (effect != null && effect.getId() == config.effectId) {
                        user.addEffect(new MobEffectInstance(
                                effect,
                                config.duration * 20,
                                config.amplification,
                                false,
                                true
                        ));
                        stack.shrink(1);
                        return InteractionResultHolder.success(stack);
                    } else {
                        LOGGER.warn("Invalid Effect ID: " + config.effectId);
                    }
                }
                return InteractionResultHolder.consume(stack);
            }
            return InteractionResultHolder.success(stack);
        }

        @Override
        public Component getName(ItemStack stack) {
            return Component.literal(config.name);
        }

        public int getColor() {
            return config.color;
        }
    }

    public static void loadPills(){
        PILL_CONFIGS.clear();
        try {
            Files.createDirectories(CONFIG_DIR);
            Files.walk(CONFIG_DIR, 1)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(DynamicPillsSystem::loadPillConfig);
        } catch (IOException e) {
            LOGGER.error("Failed to load pill configurations", e);
        }
    }

    private static void loadPillConfig(Path path) {
        try {
            String json = Files.readString(path);
            JsonObject obj = GSON.fromJson(json, JsonObject.class);
            String name = obj.get("name").getAsString();
            String colorHex = obj.get("color").getAsString().replace("#", "");
            int color = Integer.parseInt(colorHex, 16);
            String effectIdStr = obj.get("effect").getAsString();
            ResourceLocation effectId = ResourceLocation.tryParse(effectIdStr);
            int amplification = obj.get("amplification").getAsInt();
            int duration = obj.get("duration").getAsInt();

            if (effectId == null) {
                throw new JsonParseException("Invalid effect ID: " + effectIdStr);
            }

            ResourceLocation pillId = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, name.toLowerCase().replace(" ", "_"));
            PILL_CONFIGS.put(pillId, new PillConfig(name, color, effectId, amplification, duration));
            LOGGER.info("Loaded pill: {}", name);
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Failed to load pill config: {}", path.getFileName(), e);
        }
    }

    public static void registerPills() {
        for (Map.Entry<ResourceLocation, PillConfig> entry : PILL_CONFIGS.entrySet()) {
            ResourceLocation id = entry.getKey();
            PillConfig config = entry.getValue();
            Registry.register(config.name(), id, new PillItem(config));
        }
    }


}
