package net.thejadeproject.ascension.items.pills;

/**
 * Defines the Pill Realm system – separate from cultivation realms.
 *
 * Major Realms 1-9 and Minor Realms within each.
 * Names are plain strings so developers can rename them easily here.
 */
public class PillRealmData {

    // ── Major Realm Names (index 0 = realm 1, index 8 = realm 9) ──
    private static final String[] MAJOR_REALM_NAMES = {
            "Mortal",       // 1
            "Spirit",       // 2
            "Earth",        // 3
            "Sky",          // 4
            "Heaven",       // 5
            "Profound",     // 6
            "Divine",       // 7
            "Immortal",     // 8
            "Transcendent"  // 9
    };

    // ── Minor Realm Names (index 0 = lower, 1 = middle, 2 = peak) ──
    public static final String MINOR_LOWER = "Lower";
    public static final String MINOR_MIDDLE = "Middle";
    public static final String MINOR_PEAK = "Peak";

    private static final String[] MINOR_REALM_NAMES = {
            MINOR_LOWER,
            MINOR_MIDDLE,
            MINOR_PEAK
    };

    /**
     * Returns the display name for a major realm (1-9).
     * Returns "Unknown" if out of range.
     */
    public static String getMajorRealmName(int majorRealm) {
        if (majorRealm < 1 || majorRealm > 9) return "Unknown";
        return MAJOR_REALM_NAMES[majorRealm - 1];
    }

    /**
     * Returns the display name for a minor realm string key.
     * Keys: "lower", "middle", "peak" (case-insensitive).
     */
    public static String getMinorRealmName(String minorRealm) {
        if (minorRealm == null) return MINOR_LOWER;
        return switch (minorRealm.toLowerCase()) {
            case "lower"  -> MINOR_LOWER;
            case "middle" -> MINOR_MIDDLE;
            case "peak"   -> MINOR_PEAK;
            default       -> minorRealm; // passthrough for custom values
        };
    }

    /**
     * Returns a combined display string, e.g. "Mortal - Lower".
     */
    public static String getFullRealmDisplay(int majorRealm, String minorRealm) {
        return getMajorRealmName(majorRealm) + " - " + getMinorRealmName(minorRealm);
    }

    // ── Bonus Effect Display Names ──────────────────────────────
    // Map ResourceLocation strings to human-readable descriptions.
    // Developers: add entries here as you add herb bonus types.
    public static String getBonusEffectDisplay(String bonusEffectId) {
        if (bonusEffectId == null || bonusEffectId.isEmpty()) return "";
        return switch (bonusEffectId) {
            case "ascension:fire_affinity"      -> "Fire Affinity: Boosts fire-element effects";
            case "ascension:qi_nourishing"      -> "Qi Nourishing: Increased Qi regeneration";
            case "ascension:body_strengthening" -> "Body Strengthening: Enhanced physical resilience";
            case "ascension:cold_essence"       -> "Cold Essence: Resistance to frost damage";
            case "ascension:spirit_clarity"     -> "Spirit Clarity: Improved skill cooldown recovery";
            default -> "Unknown Bonus: " + bonusEffectId;
        };
    }
}