package net.thejadeproject.ascension.util.ToolTips;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.items.ModItems;

public class ToolTipManager {
    public static void registerAllTooltips() {
        // Example tooltip registrations
        /*ToolTipHandler.registerTooltip(
                ModItems.EXAMPLE_ITEM.get(),
                "This is a simple tooltip",
                "Second line of tooltip"
        );*/

        // Tooltip with custom formatting
        ToolTipHandler.registerTooltip(
                ModItems.TABLET_OF_DESTRUCTION_HUMAN.get(), Component.translatable("tooltip.ascension.todh")
        );

            // Fasting Pills - register each individually
        ToolTipHandler.registerTooltip(
                    ModItems.FASTING_PILL_T1.get(), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.YELLOW)
            );

        ToolTipHandler.registerTooltip(
                    ModItems.FASTING_PILL_T2.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW)
            );

        ToolTipHandler.registerTooltip(
                    ModItems.FASTING_PILL_T3.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW)
            );

        // Advanced example with conditional logic
        registerAdvancedTooltips();
    }

    private static void registerAdvancedTooltips() {
        // You can add more complex tooltip logic here
        //ToolTipHandler.registerTooltip(
                //ModItems.SPECIAL_ITEM.get(),
                //"Special Item Tooltip",
                //"Hold Shift for more info"
        //);
    }
}
