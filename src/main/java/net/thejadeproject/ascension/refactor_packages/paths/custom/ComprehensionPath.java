package net.thejadeproject.ascension.refactor_packages.paths.custom;

import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ModTechniques;

public class ComprehensionPath extends GenericPath{
    public ComprehensionPath(Component title) {
        super(title);
    }

    @Override
    public int getMaxMajorRealm() {
        return 4;
    }

    @Override
    public PathData freshPathData(IEntityData heldEntity) {
        PathData pathData = super.freshPathData(heldEntity);

        if (this == ModPaths.SWORD.get()) {
            pathData.setLastUsedTechnique(ModTechniques.SWORD_COMPREHENSION_TECHNIQUE.getId());
        } else if (this == ModPaths.RUNIC.get()) {
            pathData.setLastUsedTechnique(ModTechniques.RUNE_SERVANT_TECHNIQUE.getId());
        }

        return pathData;
    }
}
