package net.thejadeproject.ascension.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.blocks.entity.PillCauldronLowHumanEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

/*@WailaPlugin
public class JadeModPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        // Server-side registration if needed
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(PillCauldronComponentProvider.INSTANCE,
                ModBlocks.PILL_CAULDRON_HUMAN_LOW.get().getClass());
    }

    public enum PillCauldronComponentProvider implements IBlockComponentProvider {
        INSTANCE;

        private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "pill_cauldron");

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            BlockEntity blockEntity = accessor.getBlockEntity();

            if (blockEntity instanceof PillCauldronLowHumanEntity cauldron) {
                // ALWAYS show heat information
                int currentHeat = cauldron.getHeatLevel();
                int maxHeat = cauldron.getMaxHeat();

                // Add heat information
                tooltip.add(Component.translatable("tooltip.ascension.heat_level", currentHeat, maxHeat));

                // Create heat bar visualization
                String heatBar = createHeatBar(currentHeat, maxHeat);
                tooltip.add(Component.literal(heatBar));

                // Show current output if available
                ItemStack currentOutput = cauldron.getCurrentRecipeOutput();
                if (!currentOutput.isEmpty()) {
                    tooltip.add(Component.translatable("tooltip.ascension.output", currentOutput.getHoverName()));
                }

                // Show input items
                boolean hasInputs = false;
                for (int i = 0; i < 3; i++) {
                    ItemStack input = cauldron.getInputItem(i);
                    if (!input.isEmpty()) {
                        if (!hasInputs) {
                            tooltip.add(Component.translatable("tooltip.ascension.inputs"));
                            hasInputs = true;
                        }
                        tooltip.add(Component.literal("  " + input.getCount() + "x " + input.getHoverName().getString()));
                    }
                }

                // Get progress information
                int progress = cauldron.getProgress();
                int maxProgress = cauldron.getMaxProgress();

                // Show progress information if crafting is in progress
                if (maxProgress > 0 && progress > 0) {
                    int progressPercentage = (progress * 100) / maxProgress;
                    int timeRemainingTicks = maxProgress - progress;
                    int timeRemainingSeconds = (timeRemainingTicks + 19) / 20; // Round up to nearest second

                    // Add progress information
                    tooltip.add(Component.translatable("tooltip.ascension.progress", progressPercentage));

                    if (timeRemainingSeconds > 0) {
                        tooltip.add(Component.translatable("tooltip.ascension.time_remaining", timeRemainingSeconds));
                    } else {
                        tooltip.add(Component.translatable("tooltip.ascension.finishing"));
                    }

                    // Add progress bar visualization
                    String progressBar = createProgressBar(progressPercentage);
                    tooltip.add(Component.literal(progressBar));
                }
            }
        }

        private String createHeatBar(int current, int max) {
            if (max <= 0) return "§c[NO HEAT]";

            int bars = (current * 10) / max;
            StringBuilder bar = new StringBuilder("§c"); // Red color for heat

            for (int i = 0; i < 10; i++) {
                if (i < bars) {
                    bar.append("|");
                } else {
                    bar.append("§7|"); // Gray color for empty
                }
            }

            return bar.toString();
        }

        private String createProgressBar(int percentage) {
            int bars = percentage / 10;
            StringBuilder bar = new StringBuilder("§a"); // Green color for progress

            for (int i = 0; i < 10; i++) {
                if (i < bars) {
                    bar.append("█");
                } else {
                    bar.append("§7█"); // Gray color for empty
                }
            }

            return bar.toString();
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }
}*/