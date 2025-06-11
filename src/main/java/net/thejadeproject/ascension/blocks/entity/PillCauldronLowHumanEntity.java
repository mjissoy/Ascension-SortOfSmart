package net.thejadeproject.ascension.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.ascension.recipe.ModRecipes;
import net.thejadeproject.ascension.recipe.PillCauldronLowHumanRecipe;
import net.thejadeproject.ascension.recipe.PillCauldronLowHumanRecipeInput;
import net.thejadeproject.ascension.screen.custom.PillCauldronLowHumanMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
    private static final int OUTPUT_SLOT0 = 3;
    private static final int OUTPUT_SLOT1 = 4;
    private int heatLevel = 0;
    private static final int MAX_HEAT = 1000;

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
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: PillCauldronLowHumanEntity.this.progress = value;
                    case 1: PillCauldronLowHumanEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
        heatLevel = Math.min(MAX_HEAT, heatLevel + amount);
        setChanged();
    }

    public int getHeatLevel() {
        return heatLevel;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("pill_cauldron.progress", progress);
        pTag.putInt("pill_cauldron.max_progress", maxProgress);
        pTag.putInt("heatLevel", heatLevel);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("pill_cauldron.progress");
        maxProgress = pTag.getInt("pill_cauldron.max_progress");
        heatLevel = pTag.getInt("heatLevel");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
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

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;
    }

    private void craftItem() {
        Optional<RecipeHolder<PillCauldronLowHumanRecipe>> recipe = getCurrentRecipe();
        ItemStack output = recipe.get().value().output();

        itemHandler.extractItem(INPUT_SLOT0, 1, false);
        itemHandler.extractItem(INPUT_SLOT1, 1, false);
        itemHandler.extractItem(INPUT_SLOT2, 1, false);
        if (OUTPUT_SLOT0 == 0) {
            itemHandler.setStackInSlot(OUTPUT_SLOT0, new ItemStack(output.getItem(),
                    itemHandler.getStackInSlot(OUTPUT_SLOT0).getCount() + output.getCount()));
        } else
            itemHandler.setStackInSlot(OUTPUT_SLOT1, new ItemStack(output.getItem(),
                    itemHandler.getStackInSlot(OUTPUT_SLOT1).getCount() + output.getCount()));
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<PillCauldronLowHumanRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack output = recipe.get().value().output();
        return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output);
    }

    private Optional<RecipeHolder<PillCauldronLowHumanRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.CAULDRON_LOW_HUMAN_TYPE.get(), new PillCauldronLowHumanRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT0)), level);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT0).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT0).getItem() == output.getItem();
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT0).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT0).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT0).getCount();

        return maxCount >= currentCount + count;
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
