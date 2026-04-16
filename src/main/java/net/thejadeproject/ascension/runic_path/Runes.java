package net.thejadeproject.ascension.runic_path;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.*;

public final class Runes {

    private static final Map<ResourceLocation, Rune> ALL_RUNES = new LinkedHashMap<>();
    private static final Map<RunicRealm, List<Rune>> RUNES_BY_REALM = new EnumMap<>(RunicRealm.class);

    private Runes() {}


    // Flesh
    public static final Rune STRENGTH = register(
            "strength",
            "ascension.rune.strength",
            "ascension.rune.strength.description",
            RunicRealm.FLESH,
            RuneEffectType.STRENGTH,
            0.10
    );

    public static final Rune VITALITY = register(
            "vitality",
            "ascension.rune.vitality",
            "ascension.rune.vitality.description",
            RunicRealm.FLESH,
            RuneEffectType.MAX_HEALTH,
            0.10
    );

    public static final Rune ARMOR = register(
            "armor",
            "ascension.rune.armor",
            "ascension.rune.armor.description",
            RunicRealm.FLESH,
            RuneEffectType.ARMOR,
            0.75
    );

    public static final Rune ENDURANCE = register(
            "endurance",
            "ascension.rune.endurance",
            "ascension.rune.endurance.description",
            RunicRealm.FLESH,
            RuneEffectType.ARMOR_TOUGHNESS,
            0.40
    );


    // Soul
    public static final Rune REGEN = register(
            "regen",
            "ascension.rune.regen",
            "ascension.rune.regen.description",
            RunicRealm.SOUL,
            RuneEffectType.HEALTH_REGEN,
            0.10
    );

    public static final Rune FLOW = register(
            "flow",
            "ascension.rune.flow",
            "ascension.rune.flow.description",
            RunicRealm.SOUL,
            RuneEffectType.QI_REGEN,
            0.10
    );

    public static final Rune ESSENCE = register(
            "essence",
            "ascension.rune.essence",
            "ascension.rune.essence.description",
            RunicRealm.SOUL,
            RuneEffectType.CULTIVATION_SPEED,
            0.10
    );

    public static final Rune STABILITY = register(
            "stability",
            "ascension.rune.stability",
            "ascension.rune.stability.description",
            RunicRealm.SOUL,
            RuneEffectType.STABILITY,
            0.10
    );


    // Spark
    public static final Rune EDGE = register(
            "edge",
            "ascension.rune.edge",
            "ascension.rune.edge.description",
            RunicRealm.SPARK,
            RuneEffectType.ATTACK_DAMAGE,
            0.10
    );

    public static final Rune SPEED = register(
            "speed",
            "ascension.rune.speed",
            "ascension.rune.speed.description",
            RunicRealm.SPARK,
            RuneEffectType.MOVEMENT_SPEED,
            0.05
    );

    public static final Rune PRECISION = register(
            "precision",
            "ascension.rune.precision",
            "ascension.rune.precision.description",
            RunicRealm.SPARK,
            RuneEffectType.ATTACK_SPEED,
            0.05
    );

    public static final Rune BURST = register(
            "burst",
            "ascension.rune.burst",
            "ascension.rune.burst.description",
            RunicRealm.SPARK,
            RuneEffectType.SKILL_DAMAGE,
            0.10
    );


    // Void
    public static final Rune EFFICIENCY = register(
            "efficiency",
            "ascension.rune.efficiency",
            "ascension.rune.efficiency.description",
            RunicRealm.VOID,
            RuneEffectType.VOID_EFFICIENCY,
            0.10
    );

    public static final Rune RESISTANCE = register(
            "resistance",
            "ascension.rune.resistance",
            "ascension.rune.resistance.description",
            RunicRealm.VOID,
            RuneEffectType.VOID_RESISTANCE,
            0.10
    );

    public static final Rune ECHO = register(
            "echo",
            "ascension.rune.echo",
            "ascension.rune.echo.description",
            RunicRealm.VOID,
            RuneEffectType.VOID_ECHO,
            0.05
    );

    public static final Rune CONVERSION = register(
            "conversion",
            "ascension.rune.conversion",
            "ascension.rune.conversion.description",
            RunicRealm.VOID,
            RuneEffectType.VOID_CONVERSION,
            0.10
    );

    private static Rune register(
            String path,
            String nameKey,
            String descriptionKey,
            RunicRealm realm,
            RuneEffectType effectType,
            double baseValue
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);
        Rune rune = new Rune(
                id,
                Component.translatable(nameKey),
                Component.translatable(descriptionKey),
                realm,
                effectType,
                baseValue
        );

        ALL_RUNES.put(id, rune);
        RUNES_BY_REALM.computeIfAbsent(realm, k -> new ArrayList<>()).add(rune);
        return rune;
    }

    public static Rune get(ResourceLocation id) {
        return ALL_RUNES.get(id);
    }

    public static Collection<Rune> all() {
        return Collections.unmodifiableCollection(ALL_RUNES.values());
    }

    public static List<Rune> forRealm(RunicRealm realm) {
        return Collections.unmodifiableList(RUNES_BY_REALM.getOrDefault(realm, List.of()));
    }

    public static List<Rune> forMajorRealm(int majorRealm) {
        RunicRealm realm = RunicRealm.fromMajorRealm(majorRealm);
        return realm == null ? List.of() : forRealm(realm);
    }
}