package net.thejadeproject.ascension.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscensionCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> ASCENSION_ITEMS_TAB = CREATIVE_MODE_TAB.register("ascension_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.JADE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_blocks_tab"))
                    .title(Component.translatable("creativetab.ascension.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RAW_JADE);
                        output.accept(ModItems.JADE_NUGGET);
                        output.accept(ModItems.JADE);
                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_BLOCKS_TAB = CREATIVE_MODE_TAB.register("ascension_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.JADE_ORE.get()))
                    .title(Component.translatable("creativetab.ascension.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.JADE_ORE);
                        output.accept(ModBlocks.JADE_BLOCK);


                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
