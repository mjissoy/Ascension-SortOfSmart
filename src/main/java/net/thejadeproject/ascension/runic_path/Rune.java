package net.thejadeproject.ascension.runic_path;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Rune {

    private final ResourceLocation id;
    private final Component displayName;
    private final Component description;
    private final RunicRealm realm;
    private final RuneEffectType effectType;
    private final double baseValue;

    public Rune(
            ResourceLocation id,
            Component displayName,
            Component description,
            RunicRealm realm,
            RuneEffectType effectType,
            double baseValue
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.realm = realm;
        this.effectType = effectType;
        this.baseValue = baseValue;
    }

    public ResourceLocation getId() {
        return id;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDescription() {
        return description;
    }

    public RunicRealm getRealm() {
        return realm;
    }

    public int getMajorRealm() {
        return realm.getMajorRealm();
    }

    public RuneEffectType getEffectType() {
        return effectType;
    }

    public double getBaseValue() {
        return baseValue;
    }
}