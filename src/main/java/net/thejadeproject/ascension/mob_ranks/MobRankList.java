package net.thejadeproject.ascension.mob_ranks;

import java.util.List;

public final class MobRankList {
    private MobRankList() {
    }

    public static final MobRankDefinition MORTAL_1 =
            new MobRankDefinition("mortal", 1, new MobRankStatProfile(0, 0, 0));

    public static final MobRankDefinition MORTAL_2 =
            new MobRankDefinition("mortal", 2, new MobRankStatProfile(3, 2, 1));

    public static final MobRankDefinition MORTAL_3 =
            new MobRankDefinition("mortal", 3, new MobRankStatProfile(6, 4, 2));

    public static final MobRankDefinition QI_GATHERING_1 =
            new MobRankDefinition("qi_gathering", 1, new MobRankStatProfile(10, 6, 3));

    public static final MobRankDefinition QI_GATHERING_2 =
            new MobRankDefinition("qi_gathering", 2, new MobRankStatProfile(16, 10, 5));

    public static final MobRankDefinition QI_GATHERING_3 =
            new MobRankDefinition("qi_gathering", 3, new MobRankStatProfile(24, 15, 7));

    public static final MobRankDefinition FORMATION_ESTABLISHMENT_1 =
            new MobRankDefinition("formation_establishment", 1, new MobRankStatProfile(32, 21, 9));

    public static final MobRankDefinition FORMATION_ESTABLISHMENT_2 =
            new MobRankDefinition("formation_establishment", 2, new MobRankStatProfile(40, 28, 11));

    public static final MobRankDefinition FORMATION_ESTABLISHMENT_3 =
            new MobRankDefinition("formation_establishment", 3, new MobRankStatProfile(50, 36, 13));

    public static final MobRankDefinition GOLDEN_CORE_1 =
            new MobRankDefinition("golden_core", 1, new MobRankStatProfile(70, 50, 15));

    public static final MobRankDefinition GOLDEN_CORE_2 =
            new MobRankDefinition("golden_core", 2, new MobRankStatProfile(90, 65, 20));

    public static final MobRankDefinition GOLDEN_CORE_3 =
            new MobRankDefinition("golden_core", 3, new MobRankStatProfile(110, 80, 25));

    public static final List<MobRankDefinition> ALL = List.of(
            MORTAL_1,
            MORTAL_2,
            MORTAL_3,
            QI_GATHERING_1,
            QI_GATHERING_2,
            QI_GATHERING_3,
            FORMATION_ESTABLISHMENT_1,
            FORMATION_ESTABLISHMENT_2,
            FORMATION_ESTABLISHMENT_3,
            GOLDEN_CORE_1,
            GOLDEN_CORE_2,
            GOLDEN_CORE_3
    );

    public static MobRankDefinition get(String realmId, int stage) {
        for (MobRankDefinition definition : ALL) {
            if (definition.realmId().equals(realmId) && definition.stage() == stage) {
                return definition;
            }
        }
        return MORTAL_1;
    }
}