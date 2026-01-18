package net.thejadeproject.ascension.sects;

public enum SectPermission {
    INVITE(SectRank.ELDER),
    PROMOTE(SectRank.ELDER),
    DEMOTE(SectRank.ELDER),
    SET_TITLE(SectRank.ELDER),
    SET_OPEN(SectRank.ELDER),
    ALLY(SectRank.ELDER),
    KICK(SectRank.ELDER);

    private final SectRank requiredLevel;

    SectPermission(SectRank requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public SectRank getRequiredLevel() {
        return requiredLevel;
    }
}
