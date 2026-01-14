package net.thejadeproject.ascension.events.karma;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public enum KarmaRank {
    DEMONIC("demonic", -100, -34, 0xFF5555, "§4"),
    NEUTRAL("neutral", -33, 33, 0xFFFFFF, "§f"),
    SAINT("saint", 34, 100, 0x55FF55, "§2");

    public static final int MIN_KARMA = -100;
    public static final int MAX_KARMA = 100;

    private final String id;
    private final int minValue;
    private final int maxValue;
    private final int color;
    private final String chatColor;

    public static final Codec<KarmaRank> CODEC = Codec.STRING.xmap(
            KarmaRank::fromId,
            KarmaRank::getId
    );

    KarmaRank(String id, int min, int max, int color, String chatColor) {
        this.id = id;
        this.minValue = min;
        this.maxValue = max;
        this.color = color;
        this.chatColor = chatColor;
    }

    public String getId() { return id; }
    public int getMinValue() { return minValue; }
    public int getMaxValue() { return maxValue; }
    public int getColor() { return color; }
    public String getChatColor() { return chatColor; }

    public Component getDisplayName() {
        return Component.literal(id.toUpperCase()).withStyle(style -> style.withColor(color));
    }

    public static KarmaRank fromValue(int karmaValue) {
        for (KarmaRank rank : values()) {
            if (karmaValue >= rank.minValue && karmaValue <= rank.maxValue) {
                return rank;
            }
        }
        return NEUTRAL;
    }

    public static KarmaRank fromId(String id) {
        for (KarmaRank rank : values()) {
            if (rank.id.equals(id)) {
                return rank;
            }
        }
        return NEUTRAL;
    }
}