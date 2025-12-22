package net.thejadeproject.ascension.events.karma;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.thejadeproject.ascension.util.ModAttachments;

public class KarmaData {
    private int karmaValue = 0;
    private KarmaRank rank = KarmaRank.NEUTRAL;

    public static final Codec<KarmaData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("karmaValue").forGetter(KarmaData::getKarmaValue),
                    KarmaRank.CODEC.fieldOf("rank").forGetter(KarmaData::getRank)
            ).apply(instance, KarmaData::new)
    );

    public KarmaData() {}

    public KarmaData(int karmaValue, KarmaRank rank) {
        this.karmaValue = karmaValue;
        this.rank = rank;
    }

    // Getters
    public int getKarmaValue() { return karmaValue; }
    public KarmaRank getRank() { return rank; }

    // Setters
    public void setKarmaValue(int value) {
        this.karmaValue = Math.clamp(value, KarmaRank.MIN_KARMA, KarmaRank.MAX_KARMA);
        updateRank();
    }

    public void addKarma(int amount) {
        setKarmaValue(this.karmaValue + amount);
    }

    private void updateRank() {
        this.rank = KarmaRank.fromValue(this.karmaValue);
    }

    // Serialization
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("karmaValue", karmaValue);
        tag.putString("rank", rank.getId());
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.contains("karmaValue")) {
            karmaValue = tag.getInt("karmaValue");
        }
        if (tag.contains("rank")) {
            rank = KarmaRank.fromId(tag.getString("rank"));
        } else {
            updateRank();
        }
    }

    // Helper method to get from player
    public static KarmaData get(net.minecraft.world.entity.player.Player player) {
        return player.getData(ModAttachments.PLAYER_KARMA);
    }
}
