package net.thejadeproject.ascension.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.ascension.Config;
import net.thejadeproject.ascension.menus.custom.pill_cauldron.PillCauldronLowHumanMenu;
import net.thejadeproject.ascension.recipe.LowHumanPillCauldronRecipe;
import net.thejadeproject.ascension.recipe.ModRecipes;
import net.thejadeproject.ascension.recipe.PillCauldronInput;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class PillCauldronLowHumanEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int INPUT_SLOT0 = 0;
    private static final int INPUT_SLOT1 = 1;
    private static final int INPUT_SLOT2 = 2;
    private static final int OUTPUT_SLOT_SUCCESS = 3; // Left slot - always for success items
    private static final int OUTPUT_SLOT_FAIL = 4;   // Right slot - always for fail items

    private int heatLevel = 0;
    private int heatLossTimer = 0;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public PillCauldronLowHumanEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PILL_CAULDRON_LOW_HUMAN.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> PillCauldronLowHumanEntity.this.progress;
                    case 1 -> PillCauldronLowHumanEntity.this.maxProgress;
                    case 2 -> PillCauldronLowHumanEntity.this.heatLevel;
                    case 3 -> Config.COMMON.PILL_CAULDRON_MAX_HEAT.get();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0:
                        PillCauldronLowHumanEntity.this.progress = value;
                        break;
                    case 1:
                        PillCauldronLowHumanEntity.this.maxProgress = value;
                        break;
                    case 2:
                        PillCauldronLowHumanEntity.this.heatLevel = value;
                        break;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ascension.pill_cauldron");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PillCauldronLowHumanMenu(i, inventory, this, this.data);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void addHeat(int amount) {
        int maxHeat = Config.COMMON.PILL_CAULDRON_MAX_HEAT.get();
        heatLevel = Math.min(maxHeat, heatLevel + amount);
        setChanged();
    }

    public int getHeatLevel() {
        return this.data.get(2);
    }

    public int getMaxHeat() {
        return this.data.get(3);
    }

    public int getProgress() {
        return this.data.get(0);
    }

    public int getMaxProgress() {
        return this.data.get(1);
    }

    public ItemStack getCurrentRecipeOutput() {
        ItemStack successOutput = this.itemHandler.getStackInSlot(OUTPUT_SLOT_SUCCESS);
        ItemStack failOutput = this.itemHandler.getStackInSlot(OUTPUT_SLOT_FAIL);

        if (!successOutput.isEmpty()) {
            return successOutput;
        } else if (!failOutput.isEmpty()) {
            return failOutput;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getInputItem(int slot) {
        if (slot >= 0 && slot < 3) {
            return this.itemHandler.getStackInSlot(slot);
        }
        return ItemStack.EMPTY;
    }

    public boolean isCrafting() {
        return getProgress() > 0;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("pill_cauldron.progress", progress);
        pTag.putInt("pill_cauldron.max_progress", maxProgress);
        pTag.putInt("heatLevel", heatLevel);
        pTag.putInt("heatLossTimer", heatLossTimer);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("pill_cauldron.progress");
        maxProgress = pTag.getInt("pill_cauldron.max_progress");
        heatLevel = pTag.getInt("heatLevel");
        heatLossTimer = pTag.getInt("heatLossTimer");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        // Handle heat loss over time using config values
        heatLossTimer++;
        int heatLossInterval = Config.COMMON.PILL_CAULDRON_HEAT_LOSS_INTERVAL.get();
        if (heatLossTimer >= heatLossInterval) {
            if (heatLevel > 0) {
                int heatLossAmount = Config.COMMON.PILL_CAULDRON_HEAT_LOSS_AMOUNT.get();
                heatLevel = Math.max(0, heatLevel - heatLossAmount);
                setChanged();
            }
            heatLossTimer = 0;
        }

        if (hasRecipe() && hasRequiredHeat()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasRequiredHeat() {
        Optional<RecipeHolder<LowHumanPillCauldronRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        int requiredHeat = recipe.get().value().getRequiredHeat();
        return heatLevel >= requiredHeat;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    private void craftItem() {
        Optional<RecipeHolder<LowHumanPillCauldronRecipe>> recipe = getCurrentRecipe();
        LowHumanPillCauldronRecipe recipeValue = recipe.get().value();
        NonNullList<SizedIngredient> ingredients = recipeValue.getSizedIngredients();

        // Consume input items
        itemHandler.extractItem(INPUT_SLOT0, ingredients.get(INPUT_SLOT0).count(), false);
        itemHandler.extractItem(INPUT_SLOT1, ingredients.get(INPUT_SLOT1).count(), false);
        itemHandler.extractItem(INPUT_SLOT2, ingredients.get(INPUT_SLOT2).count(), false);

        // Determine success or fail based on chance
        boolean isSuccess = ThreadLocalRandom.current().nextDouble() < recipeValue.getChance();
        ItemStack output = isSuccess ? recipeValue.getSuccess() : recipeValue.getFail();
        int targetSlot = isSuccess ? OUTPUT_SLOT_SUCCESS : OUTPUT_SLOT_FAIL;

        ItemStack currentStack = itemHandler.getStackInSlot(targetSlot);

        if (currentStack.isEmpty()) {
            // Slot is empty, place new stack
            itemHandler.setStackInSlot(targetSlot, output.copy());
        } else if (currentStack.getItem() == output.getItem() &&
                currentStack.getCount() + output.getCount() <= currentStack.getMaxStackSize()) {
            // Same item and has space, add to existing stack
            currentStack.grow(output.getCount());
            itemHandler.setStackInSlot(targetSlot, currentStack);
        }
        // If neither condition is met, the output is lost
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<LowHumanPillCauldronRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        LowHumanPillCauldronRecipe recipeValue = recipe.get().value();
        ItemStack successOutput = recipeValue.getSuccess();
        ItemStack failOutput = recipeValue.getFail();

        ItemStack successSlot = itemHandler.getStackInSlot(OUTPUT_SLOT_SUCCESS);
        ItemStack failSlot = itemHandler.getStackInSlot(OUTPUT_SLOT_FAIL);

        // Check if success output can go to success slot
        boolean canInsertSuccess = successSlot.isEmpty() ||
                (successSlot.getItem() == successOutput.getItem() &&
                        successSlot.getCount() + successOutput.getCount() <= successSlot.getMaxStackSize());

        // Check if fail output can go to fail slot
        boolean canInsertFail = failSlot.isEmpty() ||
                (failSlot.getItem() == failOutput.getItem() &&
                        failSlot.getCount() + failOutput.getCount() <= failSlot.getMaxStackSize());

        return canInsertSuccess && canInsertFail;
    }

    private Optional<RecipeHolder<LowHumanPillCauldronRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get(), new PillCauldronInput(3, List.of(
                        itemHandler.getStackInSlot(INPUT_SLOT0),
                        itemHandler.getStackInSlot(INPUT_SLOT1),
                        itemHandler.getStackInSlot(INPUT_SLOT2)
                )), level);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}