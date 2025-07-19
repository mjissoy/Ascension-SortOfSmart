package net.thejadeproject.ascension.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.internal.NeoForgeAdvancementProvider;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends NeoForgeAdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper);
    }

    public static class QiGeneration implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            Advancement.Builder.advancement()
                    .display(
                            ModItems.ASCENSION_ICON, // Ensure this item is defined in your mod
                            Component.translatable("advancement.ascension.root.title"),
                            Component.translatable("achievement.ascension.root.desc"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    ).addCriterion("joined_world", PlayerTrigger.TriggerInstance.tick())
                    .save(consumer, ResourceLocation.fromNamespaceAndPath("ascension", "main/root"), existingFileHelper);
        }
    }
}
