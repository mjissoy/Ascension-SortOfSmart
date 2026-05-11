package net.thejadeproject.ascension.refactor_packages.runic.runes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModRunicRunes {

    public static final DeferredRegister<IRunicRune> RUNES = DeferredRegister.create(
            AscensionRegistries.RunicRunes.RUNIC_RUNES_REGISTRY,
            AscensionCraft.MOD_ID
    );

    private static final Map<ResourceLocation, DeferredHolder<IRunicRune, ? extends IRunicRune>> BY_ID = new LinkedHashMap<>();


    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> FLAME_RUNE = register("flame", RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> WATER_RUNE = register("water", RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> WIND_RUNE = register("wind", RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> EARTH_RUNE = register("earth", RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> WOOD_RUNE = register("wood", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> METAL_RUNE = register("metal", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> LIGHTNING_RUNE = register("lightning", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 2);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> FROST_RUNE = register("frost", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> LIGHT_RUNE = register("light", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> SHADOW_RUNE = register("shadow", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> LIFE_RUNE = register("life", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> DECAY_RUNE = register("decay", RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);

    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> BIND_RUNE = register("bind", RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> PUSH_RUNE = register("push", RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> PULL_RUNE = register("pull", RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> GUARD_RUNE = register("guard", RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> CUT_RUNE = register("cut", RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> HEAL_RUNE = register("heal", RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> GATHER_RUNE = register("gather", RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> RELEASE_RUNE = register("release", RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> PIERCE_RUNE = register("pierce", RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> COMPRESS_RUNE = register("compress", RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);

    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> BOLT_RUNE = register("bolt", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> VEIL_RUNE = register("veil", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> CIRCLE_RUNE = register("circle", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> MARK_RUNE = register("mark", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> WALL_RUNE = register("wall", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> PULSE_RUNE = register("pulse", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> LINE_RUNE = register("line", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> SPHERE_RUNE = register("sphere", RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);

    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> QUICKEN_RUNE = register("quicken", RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> STABILISE_RUNE = register("stabilise", RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> HEAVY_RUNE = register("heavy", RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> VIOLENT_RUNE = register("violent", RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);
    public static final DeferredHolder<IRunicRune, ? extends GenericRunicRune> HIDDEN_RUNE = register("hidden", RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);

    private ModRunicRunes() {
    }

    public static void register(IEventBus modEventBus) {
        RUNES.register(modEventBus);
    }

    public static IRunicRune get(ResourceLocation id) {
        DeferredHolder<IRunicRune, ? extends IRunicRune> holder = BY_ID.get(id);
        if (holder != null) {
            return holder.get();
        }

        return AscensionRegistries.getRegistryObject(
                id,
                AscensionRegistries.RunicRunes.RUNIC_RUNES_REGISTRY
        );
    }

    public static Collection<IRunicRune> values() {
        return BY_ID.values().stream()
                .map(holder -> (IRunicRune) holder.get())
                .toList();
    }

    public static List<ResourceLocation> allRuneIds() {
        return List.copyOf(BY_ID.keySet());
    }

    private static DeferredHolder<IRunicRune, GenericRunicRune> register(
            String path,
            RunicRuneType type,
            RunicRuneDepth depth,
            int minimumRunicRealmToObserve,
            int minimumRunicRealmToUse
    ) {
        ResourceLocation id = id(path);

        DeferredHolder<IRunicRune, GenericRunicRune> holder = RUNES.register(path, () -> new GenericRunicRune(
                id,
                Component.translatable("ascension.runic.rune." + path),
                type,
                depth,
                minimumRunicRealmToObserve,
                minimumRunicRealmToUse
        ));

        BY_ID.put(id, holder);
        return holder;
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);
    }
}