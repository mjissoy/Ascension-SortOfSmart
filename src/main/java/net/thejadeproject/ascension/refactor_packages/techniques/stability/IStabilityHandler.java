package net.thejadeproject.ascension.refactor_packages.techniques.stability;

import net.minecraft.network.chat.Component;

public interface IStabilityHandler {
    double getStability(double cultivationTicks);
    double getMaxCultivationTicks();



}
