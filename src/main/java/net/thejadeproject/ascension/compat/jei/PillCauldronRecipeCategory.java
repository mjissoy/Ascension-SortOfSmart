package net.thejadeproject.ascension.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.recipe.LowHumanPillCauldronRecipe;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.ArrayList;
import java.util.List;


public class PillCauldronRecipeCategory implements IRecipeCategory<LowHumanPillCauldronRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pill_cauldron_low_human");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/jei/cauldron.png");

    public static final RecipeType<LowHumanPillCauldronRecipe> CAULDRON_RECIPE_TYPE =
            new RecipeType<>(UID, LowHumanPillCauldronRecipe.class);

    // Configurable positions - adjust these to fit your custom texture
    private static final int BACKGROUND_WIDTH = 100;  // Width of the background texture
    private static final int BACKGROUND_HEIGHT = 81;  // Height of the background texture

    private static final int INPUT1_X = 10;      // X position for first input slot
    private static final int INPUT1_Y = 12;      // Y position for first input slot
    private static final int INPUT2_X = 37;      // X position for second input slot
    private static final int INPUT2_Y = 1;      // Y position for second input slot
    private static final int INPUT3_X = 64;      // X position for third input slot
    private static final int INPUT3_Y = 12;      // Y position for third input slot

    private static final int SUCCESS_X = 19;    // X position for success output slot
    private static final int SUCCESS_Y = 51;     // Y position for success output slot
    private static final int FAIL_X = 55;       // X position for fail output slot
    private static final int FAIL_Y = 51;        // Y position for fail output slot

    private static final int HEAT_TEXT_Y_OFFSET = 6; // Offset from bottom for heat text

    private final IDrawable background;
    private final IDrawable icon;

    public PillCauldronRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get()));
    }

    @Override
    public RecipeType<LowHumanPillCauldronRecipe> getRecipeType() {
        return CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.ascension.pill_cauldron_low_human");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LowHumanPillCauldronRecipe recipe, IFocusGroup focuses) {
        // Input slots: up to 3 ingredients, each with individual positions
        int size = recipe.getSizedIngredients().size();
        if (size > 0) {
            SizedIngredient sizedIngredient = recipe.getSizedIngredients().get(0);
            List<ItemStack> inputStacks1 = createItemStacksWithCount(sizedIngredient);
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT1_X, INPUT1_Y)
                    .addItemStacks(inputStacks1);
        }
        if (size > 1) {
            SizedIngredient sizedIngredient = recipe.getSizedIngredients().get(1);
            List<ItemStack> inputStacks2 = createItemStacksWithCount(sizedIngredient);
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT2_X, INPUT2_Y)
                    .addItemStacks(inputStacks2);
        }
        if (size > 2) {
            SizedIngredient sizedIngredient = recipe.getSizedIngredients().get(2);
            List<ItemStack> inputStacks3 = createItemStacksWithCount(sizedIngredient);
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT3_X, INPUT3_Y)
                    .addItemStacks(inputStacks3);
        }

        // Success output
        IRecipeSlotBuilder successSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, SUCCESS_X, SUCCESS_Y)
                .addItemStack(recipe.getSuccess());
        successSlot.addRichTooltipCallback((recipeSlotView, tooltip) ->
                tooltip.add(Component.literal("Success Chance: " + (int)(recipe.getChance() * 100) + "%")));

        // Fail output
        IRecipeSlotBuilder failSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, FAIL_X, FAIL_Y)
                .addItemStack(recipe.getFail());
        failSlot.addRichTooltipCallback((recipeSlotView, tooltip) ->
                tooltip.add(Component.literal("Failure Chance: " + (int)((1 - recipe.getChance()) * 100) + "%")));
    }

    // Helper method to create ItemStacks with the required count
    private List<ItemStack> createItemStacksWithCount(SizedIngredient sizedIngredient) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : sizedIngredient.ingredient().getItems()) {
            ItemStack copy = stack.copy();
            copy.setCount(sizedIngredient.count());
            stacks.add(copy);
        }
        return stacks;
    }

    @Override
    public void draw(LowHumanPillCauldronRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);

        String heatRequired = "Required Heat: " + recipe.getRequiredHeat();
        net.minecraft.client.Minecraft minecraft = net.minecraft.client.Minecraft.getInstance();
        int textWidth = minecraft.font.width(heatRequired);
        int xPos = (background.getWidth() / 2) - (textWidth / 2);
        int yPos = background.getHeight() - HEAT_TEXT_Y_OFFSET;

        // Draw shadow
        guiGraphics.drawString(minecraft.font, heatRequired, xPos + 1, yPos + 1, 0x000000, false);
        // Draw main text
        guiGraphics.drawString(minecraft.font, heatRequired, xPos, yPos, 0xFF5555, false); // Red color for heat
    }
}
