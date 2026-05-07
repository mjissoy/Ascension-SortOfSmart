package net.thejadeproject.ascension.refactor_packages.runic.runes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModRunicRunes {

    private static final Map<ResourceLocation, IRunicRune> BY_ID = new LinkedHashMap<>();

    public static final ResourceLocation FLAME = id("flame");
    public static final ResourceLocation WATER = id("water");
    public static final ResourceLocation WIND = id("wind");
    public static final ResourceLocation EARTH = id("earth");
    public static final ResourceLocation WOOD = id("wood");
    public static final ResourceLocation METAL = id("metal");
    public static final ResourceLocation LIGHTNING = id("lightning");
    public static final ResourceLocation FROST = id("frost");

    public static final ResourceLocation BIND = id("bind");
    public static final ResourceLocation PUSH = id("push");
    public static final ResourceLocation PULL = id("pull");
    public static final ResourceLocation GUARD = id("guard");
    public static final ResourceLocation CUT = id("cut");
    public static final ResourceLocation HEAL = id("heal");

    public static final ResourceLocation BOLT = id("bolt");
    public static final ResourceLocation VEIL = id("veil");
    public static final ResourceLocation CIRCLE = id("circle");
    public static final ResourceLocation MARK = id("mark");

    public static final ResourceLocation QUICKEN = id("quicken");
    public static final ResourceLocation STABILISE = id("stabilise");

    public static final IRunicRune FLAME_RUNE = register(FLAME, RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune WATER_RUNE = register(WATER, RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune WIND_RUNE = register(WIND, RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune EARTH_RUNE = register(EARTH, RunicRuneType.SOURCE, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune WOOD_RUNE = register(WOOD, RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 0, 1);
    public static final IRunicRune METAL_RUNE = register(METAL, RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 0, 1);
    public static final IRunicRune LIGHTNING_RUNE = register(LIGHTNING, RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 2);
    public static final IRunicRune FROST_RUNE = register(FROST, RunicRuneType.SOURCE, RunicRuneDepth.DEEP, 1, 1);

    public static final IRunicRune BIND_RUNE = register(BIND, RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune PUSH_RUNE = register(PUSH, RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune PULL_RUNE = register(PULL, RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune GUARD_RUNE = register(GUARD, RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune CUT_RUNE = register(CUT, RunicRuneType.INTENT, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune HEAL_RUNE = register(HEAL, RunicRuneType.INTENT, RunicRuneDepth.DEEP, 0, 1);

    public static final IRunicRune BOLT_RUNE = register(BOLT, RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune VEIL_RUNE = register(VEIL, RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune CIRCLE_RUNE = register(CIRCLE, RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);
    public static final IRunicRune MARK_RUNE = register(MARK, RunicRuneType.FORM, RunicRuneDepth.SURFACE, 0, 0);

    public static final IRunicRune QUICKEN_RUNE = register(QUICKEN, RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);
    public static final IRunicRune STABILISE_RUNE = register(STABILISE, RunicRuneType.MODIFIER, RunicRuneDepth.DEEP, 1, 1);

    private ModRunicRunes() {
    }

    public static IRunicRune get(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static Collection<IRunicRune> values() {
        return List.copyOf(BY_ID.values());
    }

    public static List<ResourceLocation> allRuneIds() {
        return List.copyOf(BY_ID.keySet());
    }

    private static IRunicRune register(
            ResourceLocation id,
            RunicRuneType type,
            RunicRuneDepth depth,
            int minimumRunicRealmToObserve,
            int minimumRunicRealmToUse
    ) {
        IRunicRune rune = new GenericRunicRune(
                id,
                Component.translatable("ascension.runic.rune." + id.getPath()),
                type,
                depth,
                minimumRunicRealmToObserve,
                minimumRunicRealmToUse
        );

        BY_ID.put(id, rune);
        return rune;
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);
    }
}