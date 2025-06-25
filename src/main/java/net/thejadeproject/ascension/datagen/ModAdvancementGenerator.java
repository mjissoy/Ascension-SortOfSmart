/*package net.thejadeproject.ascension.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.ascension.items.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new ModAdvancementGenerator()));
    }
}

public class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {




    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        AdvancementHolder advancement = Advancement.Builder.advancement()
                .display(
                        ModItems.ASCENSION_ICON, // Ensure this item is defined in your mod
                        Component.translatable("achievement.ascension.root"),
                        Component.translatable("achievement.ascension.root.desc"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("mod_loaded", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.JADE))
                .save(consumer, "ascension:main/root");
    }
}*/
