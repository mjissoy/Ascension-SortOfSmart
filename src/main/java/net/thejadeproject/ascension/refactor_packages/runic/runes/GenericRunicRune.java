package net.thejadeproject.ascension.refactor_packages.runic.runes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GenericRunicRune implements IRunicRune {

    private final ResourceLocation id;
    private final Component name;
    private final RunicRuneType type;
    private final RunicRuneDepth depth;
    private final int minimumRunicRealmToObserve;
    private final int minimumRunicRealmToUse;

    public GenericRunicRune(
            ResourceLocation id,
            Component name,
            RunicRuneType type,
            RunicRuneDepth depth,
            int minimumRunicRealmToObserve,
            int minimumRunicRealmToUse
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.depth = depth;
        this.minimumRunicRealmToObserve = minimumRunicRealmToObserve;
        this.minimumRunicRealmToUse = minimumRunicRealmToUse;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Component getName() {
        return name;
    }

    @Override
    public RunicRuneType getType() {
        return type;
    }

    @Override
    public RunicRuneDepth getDepth() {
        return depth;
    }

    @Override
    public int getMinimumRunicRealmToObserve() {
        return minimumRunicRealmToObserve;
    }

    @Override
    public int getMinimumRunicRealmToUse() {
        return minimumRunicRealmToUse;
    }
}