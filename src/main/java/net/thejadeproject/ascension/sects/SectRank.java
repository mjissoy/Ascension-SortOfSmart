package net.thejadeproject.ascension.sects;

public enum SectRank {
    OUTER(0, "Outer Sect"),
    INNER(1, "Inner Sect"),
    ELDER(2, "Elder"),
    SECT_MASTER(3, "Sect Master");

    private final int level;
    private final String displayName;

    SectRank(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean hasPermission(SectPermission permission) {
        return this.level >= permission.getRequiredLevel().getLevel();
    }

    public boolean canPromoteTo(SectRank targetRank) {
        return this.level < targetRank.level && targetRank.level <= ELDER.level;
    }

    public boolean canDemoteFrom(SectRank targetRank) {
        return this.level > targetRank.level && targetRank.level >= ELDER.level;
    }

    public SectRank getNextRank() {
        if (this == OUTER) return INNER;
        if (this == INNER) return ELDER;
        return this; // ELDER and OWNER don't have next ranks
    }

    public SectRank getPreviousRank() {
        if (this == INNER) return OUTER;
        if (this == ELDER) return INNER;
        return this; // SECT_MASTER and OUTER don't have previous ranks
    }

    public boolean canDemoteTo(SectRank targetRank) {
        return targetRank.level < this.level && targetRank.level >= OUTER.level;
    }
}
