package net.thejadeproject.ascension.events.karma;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.util.ModAttachments;

public class KarmaManager {

    public static void initializeKarma(Player player) {
        // Check if player has karma data, if not create it
        if (!player.hasData(ModAttachments.PLAYER_KARMA)) {
            player.setData(ModAttachments.PLAYER_KARMA, new KarmaData());
        }
    }

    public static boolean hasKarma(Player player) {
        return player.hasData(ModAttachments.PLAYER_KARMA);
    }

    public static int getKarma(Player player) {
        return player.getData(ModAttachments.PLAYER_KARMA).getKarmaValue();
    }

    public static KarmaRank getKarmaRank(Player player) {
        return player.getData(ModAttachments.PLAYER_KARMA).getRank();
    }

    public static void addKarma(Player player, int amount) {
        KarmaData karmaData = player.getData(ModAttachments.PLAYER_KARMA);
        KarmaRank oldRank = karmaData.getRank();
        karmaData.addKarma(amount);
        KarmaRank newRank = karmaData.getRank();

        // Notify player of rank change
        if (!oldRank.equals(newRank) && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal(
                    newRank.getChatColor() + "Your karma has reached the " +
                            newRank.getId().toUpperCase() + " rank!"
            ));
        }
    }

    public static void applyKarmaEffects(Player player) {
        KarmaRank rank = getKarmaRank(player);

        // Apply effects based on karma rank
        // You can integrate this with your existing effect system
        switch (rank) {
            case DEMONIC:
                // Demonic cultivators get strength but negative effects
                // Example: Increased damage but slower health regen
                break;
            case SAINT:
                // Saint cultivators get regeneration and positive effects
                // Example: Regeneration and damage resistance
                break;
            case NEUTRAL:
                // Neutral cultivators get balanced minor benefits
                // Example: Slight speed boost
                break;
        }
    }

    public static void setKarma(Player player, int value) {
        KarmaData karmaData = player.getData(ModAttachments.PLAYER_KARMA);
        karmaData.setKarmaValue(value);
    }

    public static int calculateKarmaChange(LivingEntity victim, Player killer) {
        // Killing passive mobs/villagers reduces karma
        if (victim instanceof Villager) {
            return -10;
        } else if (victim instanceof Animal) {
            return -5;
        }
        // Killing monsters increases karma
        else if (victim instanceof Monster) {
            return 2;
        }
        // Killing players based on their karma
        else if (victim instanceof Player victimPlayer && hasKarma(victimPlayer)) {
            KarmaRank victimRank = getKarmaRank(victimPlayer);

            switch (victimRank) {
                case SAINT: return -15;
                case DEMONIC: return 10;
                case NEUTRAL: return -3;
                default: return -3;
            }
        }
        return 0;
    }
}