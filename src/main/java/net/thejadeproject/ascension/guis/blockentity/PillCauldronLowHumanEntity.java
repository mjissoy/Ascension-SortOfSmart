package net.thejadeproject.ascension.guis.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.ModBlockEntities;
import net.thejadeproject.ascension.recipe.PillCauldronHumanRecipe;

import java.util.Optional;

public class PillCauldronLowHumanEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOTS = 3;
    public static final int OUTPUT_SLOTS = 2;
    public static ItemStackHandler inventory = new ItemStackHandler(INPUT_SLOTS + OUTPUT_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private int heatLevel = 0;
    private static final int MAX_HEAT = 1000;

    public PillCauldronLowHumanEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PILL_CAULDRON_HUMAN.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PillCauldronLowHumanEntity entity) {
        if (level.isClientSide) return;

        //Decrease heat over time
        if (entity.heatLevel > 0) {
            entity.heatLevel = Math.max(0, entity.heatLevel -1);
            entity.setChanged();
        }

        //Check for recipes
        if (entity.canProcessRecipe()) {
            entity.processRecipe();
        }
    }

    private boolean canProcessRecipe() {
        RecipeManager recipeManager = level.getRecipeManager();
        Optional<PillCauldronHumanRecipe> recipe = recipeManager.getRecipeFor(PillCauldronHumanRecipe.Type.INSTANCE, getInputInventory(), level);

        if (recipe.isEmpty() || heatLevel < recipe.get().getHeatRequired()) {
            return false;
        }

        ItemStack[] outputs = recipe.get().getOutputs();
        for (int i = 0; i < OUTPUT_SLOTS; i++) {
            ItemStack outputSlot = inventory.getStackInSlot(INPUT_SLOTS + i);
            ItemStack recipeOutput = outputs[i];
            if (!outputSlot.isEmpty() && (!outputSlot.is(recipeOutput.getItem()) || outputSlot.getCount() + recipeOutput.getCount() > outputSlot.getMaxStackSize())) {
                return false;
            }
        }
        return true;
    }

    private void processRecipe(Level level) {
        RecipeManager recipeManager = level.getRecipeManager();
        Optional<PillCauldronHumanRecipe> recipe = recipeManager.getRecipeFor(PillCauldronHumanRecipe.Type.INSTANCE, PillCauldronHumanRecipe(), level);

        if (recipe.isPresent()) {
            // Consume inputs
            for (int i = 0; i < INPUT_SLOTS; i++) {
                inventory.getStackInSlot(i).shrink(1);
            }

            // Produce outputs
            ItemStack[] outputs = recipe.get().getOutputs();
            for (int i = 0; i < OUTPUT_SLOTS; i++) {
                ItemStack outputSlot = inventory.getStackInSlot(INPUT_SLOTS + i);
                ItemStack recipeOutput = outputs[i];
                if (outputSlot.isEmpty()) {
                    inventory.setStackInSlot(INPUT_SLOTS + i, recipeOutput.copy());
                } else {
                    outputSlot.grow(recipeOutput.getCount());
                }
            }
            setChanged();
        }
    }

    private Inventory getInputInventory() {
        return new Inventory() {
            @Override
            public int getContainerSize() {
                return INPUT_SLOTS;
            }

            @Override
            public boolean isEmpty() {
                for (int i = 0; i < INPUT_SLOTS; i++) {
                    if (!inventory.getStackInSlot(i).isEmpty()) return false;
                }
                return true;
            }

            @Override
            public ItemStack getItem(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                return ItemStack.EMPTY; // Not needed for recipe checking
            }

            @Override
            public ItemStack removeItemNoUpdate(int slot) {
                return ItemStack.EMPTY; // Not needed for recipe checking
            }

            @Override
            public void setItem(int slot, ItemStack stack) {
                // Not needed for recipe checking
            }

            @Override
            public int getMaxStackSize() {
                return 64;
            }

            @Override
            public void setChanged() {
                // Not needed for recipe checking
            }

            @Override
            public boolean stillValid(Player player) {
                return true; // Simplified for recipe checking
            }

            @Override
            public void clearContent() {
                // Not needed for recipe checking
            }
        };
    }
    public void addHeat(int amount) {
        heatLevel = Math.min(MAX_HEAT, heatLevel + amount);
        setChanged();
    }
    public int getHeatLevel() {
        return heatLevel;
    }
    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        heatLevel = tag.getInt("HeatLevel");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("HeatLevel", heatLevel);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Heat Processor");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PillCauldronHumanMenu(containerId, playerInventory, this);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean stillValid(Player player) {
        return false;
    }
}
