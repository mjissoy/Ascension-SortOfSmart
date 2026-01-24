package net.thejadeproject.ascension.capabilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IPlayerFilter {

    boolean filterPlayer(Player player, ItemStack itemStack);

}
