package net.thejadeproject.ascension.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class QiChannelingRecipeInput implements RecipeInput {
    private final Player player;
    private final ItemStack mainHandItem;
    private final double currentQi;

    public QiChannelingRecipeInput(Player player) {
        this.player = player;
        this.mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        //TODO update
        this.currentQi = 0;
    }

    public double getCurrentQi() {
        return currentQi;
    }

    public ItemStack getMainHandItem() {
        return mainHandItem;
    }

    /**
     * Checks if the player has the required item in main hand.
     */
    public boolean hasIngredients(NonNullList<SizedIngredient> requiredIngredients) {
        if (requiredIngredients.isEmpty()) return false;

        SizedIngredient required = requiredIngredients.get(0);
        return !mainHandItem.isEmpty() && required.test(mainHandItem) && mainHandItem.getCount() >= required.count();
    }

    /**
     * Consumes the required item from main hand.
     */
    public void consumeIngredients(NonNullList<SizedIngredient> requiredIngredients) {
        if (requiredIngredients.isEmpty()) return;

        SizedIngredient required = requiredIngredients.get(0);
        if (required.test(mainHandItem) && mainHandItem.getCount() >= required.count()) {
            mainHandItem.shrink(required.count());
        }
    }

    @Override
    public boolean isEmpty() {
        return mainHandItem.isEmpty();
    }

    @Override
    public @Nullable ItemStack getItem(int i) {
        // Only return main hand item for index 0
        return i == 0 ? mainHandItem : null;
    }

    @Override
    public int size() {
        return 1; // Only one item slot
    }

    public Player getPlayer() {
        return player;
    }
}