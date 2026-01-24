package net.thejadeproject.ascension.capabilities.implementation;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.capabilities.IPlayerFilter;

public class SinglePlayerFilterToken implements IPlayerFilter {
    @Override
    public boolean filterPlayer(Player player, ItemStack itemStack) {
        var customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return false;

        var tag = customData.copyTag();
        return tag.contains("linked_player") && tag.getUUID("linked_player").equals(player.getUUID());
    }
}
