package net.thejadeproject.ascension.mob_ranks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MobRankData {
    public static final Codec<MobRankData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("realm_id").forGetter(MobRankData::getRealmId),
            Codec.INT.fieldOf("stage").forGetter(MobRankData::getStage),
            Codec.BOOL.fieldOf("initialized").forGetter(MobRankData::isInitialized)
    ).apply(instance, MobRankData::new));

    private String realmId;
    private int stage;
    private boolean initialized;

    public MobRankData() {
        this("unranked", 0, false);
    }

    public MobRankData(String realmId, int stage, boolean initialized) {
        this.realmId = realmId;
        this.stage = stage;
        this.initialized = initialized;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public boolean isUnranked() {
        return "unranked".equals(realmId);
    }
}