package net.thejadeproject.ascension;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.events.ModDataComponents;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;

public class CreativeTabHandler {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscensionCraft.MOD_ID);

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);

        // Register a creative tab for physique transfer items
        CREATIVE_TABS.register("physique_transfers", () -> CreativeModeTab.builder()
                .title(Component.literal("Ascension - Physique Transfers"))
                .icon(() -> {
                    // Create a default item stack for the tab icon
                    ItemStack stack = new ItemStack(ModItems.BLOOD_ESSENCE.get());
                    // Set it to the first physique found (or empty vessel as fallback)
                    String firstPhysiqueId = "ascension:empty_vessel";

                    // Try to find a more interesting physique for the icon
                    for (var entry : AscensionRegistries.Physiques.PHSIQUES_REGISTRY.entrySet()) {
                        if (!entry.getKey().toString().equals("ascension:empty_vessel")) {
                            firstPhysiqueId = entry.getKey().toString();
                            break;
                        }
                    }

                    stack.set(ModDataComponents.PHYSIQUE_ID.get(), firstPhysiqueId);
                    stack.set(ModDataComponents.PURITY.get(), 100); // Full purity for the icon
                    return stack;
                })
                .displayItems((parameters, output) -> {
                    generatePhysiqueTransferItems(output);
                })
                .build());
    }

    private static void generatePhysiqueTransferItems(CreativeModeTab.Output output) {
        List<String> physiqueIds = new ArrayList<>();

        physiqueIds.add("ascension:empty_vessel");

        AscensionRegistries.Physiques.PHSIQUES_REGISTRY.keySet().forEach(resourceLocation -> {
            String id = resourceLocation.toString();
            if (!id.equals("ascension:empty_vessel")) {
                physiqueIds.add(id);
            }
        });

        // Create an item stack for each physique with 1% purity
        for (String physiqueId : physiqueIds) {
            ItemStack stack = new ItemStack(ModItems.BLOOD_ESSENCE.get());
            stack.set(ModDataComponents.PHYSIQUE_ID.get(), physiqueId);
            stack.set(ModDataComponents.PURITY.get(), 1); // Start with 1% purity
            output.accept(stack);

            // Also add a 100% purity version for testing
            ItemStack fullStack = new ItemStack(ModItems.BLOOD_ESSENCE.get());
            fullStack.set(ModDataComponents.PHYSIQUE_ID.get(), physiqueId);
            fullStack.set(ModDataComponents.PURITY.get(), 100);
            output.accept(fullStack);
        }
    }
}