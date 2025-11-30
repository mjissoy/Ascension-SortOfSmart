package net.thejadeproject.ascension.sects;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class SectMember {
    private final UUID playerId;
    private final String playerName;
    private final SectRank rank;
    private final String title;

    public SectMember(UUID playerId, String playerName, SectRank rank, String title) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.rank = rank;
        this.title = title;
    }

    public UUID getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public SectRank getRank() { return rank; }
    public String getTitle() { return title; }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("playerId", playerId);
        tag.putString("playerName", playerName);
        tag.putString("rank", rank.name());
        tag.putString("title", title);
        return tag;
    }



    public static SectMember fromNBT(CompoundTag tag) {
        UUID playerId = tag.getUUID("playerId");
        String playerName = tag.getString("playerName");
        SectRank rank = SectRank.valueOf(tag.getString("rank"));
        String title = tag.getString("title");
        return new SectMember(playerId, playerName, rank, title);
    }
}
