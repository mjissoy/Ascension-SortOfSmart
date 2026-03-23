package net.thejadeproject.ascension.refactor_packages.paths.custom;

import net.minecraft.network.chat.Component;

public class ComprehensionPath extends GenericPath{
    public ComprehensionPath(Component title) {
        super(title);
    }

    @Override
    public int getMaxMajorRealm() {
        return 4;
    }
}
