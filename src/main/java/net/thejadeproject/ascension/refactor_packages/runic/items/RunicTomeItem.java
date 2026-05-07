package net.thejadeproject.ascension.refactor_packages.runic.items;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;

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