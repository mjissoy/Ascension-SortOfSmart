package net.thejadeproject.ascension.runic_path;

import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;

public class RunicScalingHelper {

    private RunicScalingHelper() {}

    public static int getMajorRealm(IEntityData entity) {
        if (entity != null && entity.hasPath(ModPaths.RUNIC.getId())) {
            return entity.getPathData(ModPaths.RUNIC.getId()).getMajorRealm();
        }
        return 0;
    }

    public static double scaleFlat(double base, double perRealm, IEntityData entity) {
        return base + (perRealm * getMajorRealm(entity));
    }

}
