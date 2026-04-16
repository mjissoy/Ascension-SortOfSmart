package net.thejadeproject.ascension.runic_path;

public enum RunicRealm {
    FLESH(0),
    SOUL(1),
    SPARK(2),
    VOID(3);

    private int majorRealm;

    RunicRealm(int majorRealm) {
        this.majorRealm = majorRealm;
    }

    public int getMajorRealm() {
        return majorRealm;
    }

    public static RunicRealm fromMajorRealm(int majorRealm) {
        for (RunicRealm realm : values()) {
            if (realm.majorRealm == majorRealm) {
                return realm;
            }
        }
        return null;
    }

}
