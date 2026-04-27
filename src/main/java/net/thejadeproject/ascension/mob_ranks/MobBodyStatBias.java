package net.thejadeproject.ascension.mob_ranks;

public record MobBodyStatBias(
        double vitality,
        double strength,
        double agility
) {
    public static final MobBodyStatBias NONE = new MobBodyStatBias(0, 0, 0);

    public MobRankStatProfile asProfile() {
        return new MobRankStatProfile(vitality, strength, agility);
    }
}