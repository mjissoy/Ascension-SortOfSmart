package net.thejadeproject.ascension.refactor_packages.runic.runes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IRunicRune {

    ResourceLocation getId();

    Component getName();

    RunicRuneType getType();

    RunicRuneDepth getDepth();

    int getMinimumRunicRealmToObserve();

    int getMinimumRunicRealmToUse();
}