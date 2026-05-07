package net.thejadeproject.ascension.common.items.herbs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * Base class for herb items that are also block items (plantable via ItemNameBlockItem).
 *
 * Use instead of plain {@code ItemNameBlockItem} for any herb that participates in the
 * cauldron quality system.
 *
 * Registration example:
 *   public static final DeferredItem<Item> WHITE_JADE_ORCHID = ITEMS.register("white_jade_orchid",
 *       () -> new HerbBlockItem(ModBlocks.WHITE_JADE_ORCHID_CROP.get(),
 *               new Item.Properties().food(ModFoodProperties.WHITE_JADE_ORCHID)));
 *
 * For herbs that need custom finishUsingItem logic (HundredYearFireGinseng etc.),
 * extend HerbBlockItem directly and call super.appendHoverText() — the tooltip
 * is inherited automatically.
 */
public class HerbBlockItem extends ItemNameBlockItem {

    public HerbBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        HerbQuality.appendHerbTooltip(stack, tooltip);
    }
}