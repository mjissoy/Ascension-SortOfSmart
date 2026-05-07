package net.thejadeproject.ascension.refactor_packages.runic.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.List;
public class RunicTomeItem extends Item {

    private final List<ResourceLocation> taughtRunes;

    public RunicTomeItem(Properties properties, List<ResourceLocation> taughtRunes) {
        super(properties);
        this.taughtRunes = List.copyOf(taughtRunes);
    }

    public List<ResourceLocation> getTaughtRunes() {
        return taughtRunes;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        tooltip.add(Component.translatable("ascension.runic.tome.teaches")
                .withStyle(ChatFormatting.DARK_PURPLE));

        for (ResourceLocation runeId : taughtRunes) {
            tooltip.add(Component.literal("  - ").withStyle(ChatFormatting.DARK_GRAY)
                    .append(Component.empty()
                            .append(getRuneDisplayName(runeId))
                            .withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
    }

    private Component getRuneDisplayName(ResourceLocation runeId) {
        IRunicRune rune = ModRunicRunes.get(runeId);

        if (rune != null) {
            return rune.getName();
        }

        return Component.literal(runeId.toString());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!RunicPathHelper.hasEnteredRunicPath(player)) {
            player.sendSystemMessage(Component.translatable("ascension.runic.tome.not_on_path"));
            return InteractionResultHolder.success(stack);
        }

        int learned = RunicPathHelper.learnRunes(player, taughtRunes);

        if (learned <= 0) {
            player.sendSystemMessage(Component.translatable("ascension.runic.tome.no_new_runes"));
            return InteractionResultHolder.success(stack);
        }

        player.sendSystemMessage(Component.translatable("ascension.runic.tome.learned", learned));

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.success(stack);
    }
}