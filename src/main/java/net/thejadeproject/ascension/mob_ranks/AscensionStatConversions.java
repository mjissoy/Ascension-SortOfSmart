package net.thejadeproject.ascension.mob_ranks;

public final class AscensionStatConversions {
    private AscensionStatConversions() {
    }

    public static double maxHealthBonus(double vitality) {
        return vitality * 2.0;
    }

    public static double attackDamageBonus(double strength) {
        return strength;
    }

    public static double movementSpeedBonus(double strength, double agility) {
        return (strength * 0.0001) + (agility * 0.001);
    }
}