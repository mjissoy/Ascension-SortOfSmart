package net.thejadeproject.ascension.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;
import net.thejadeproject.ascension.villager.ModVillagers;

import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {


    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();


            //Body Tech
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(  ModTechniques.FIRE_ELEMENTAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.WATER_ELEMENTAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.WOOD_ELEMENTAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.EARTH_ELEMENTAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.METAL_ELEMENTAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));

            //Intent Tech
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_SWORD_INTENT.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_SPEAR_INTENT.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_AXE_INTENT.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_BLADE_INTENT.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_FIST_INTENT.manual.get(), 1), 4, 10, 0.2f
            ));

            //Essence Tech
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_WATER_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_EARTH_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_METAL_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 32),
                    new ItemStack(ModTechniques.PURE_WOOD_TECHNIQUE.manual.get(), 1), 4, 10, 0.2f
            ));


        }

        if (event.getType() == ModVillagers.HERBALIST.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();


            //Level 1 Herbalist
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.CARROT, 16), 16, 10, 0.5f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.POTATO, 16), 16, 10, 0.5f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.WHEAT, 16), 16, 10, 0.5f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.CARROT, 48),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1), 16, 10, 0.5f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.WHEAT, 48),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1), 16, 10, 0.5f
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.POTATO, 48),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1), 16, 10, 0.5f
            ));

            //Level 2 Herbalist
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.PUMPKIN, 8), 16, 10, 0.5f
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.MELON, 8), 16, 10, 0.5f
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModBlocks.GOLDEN_PALM_SAPLING, 1), 16, 10, 0.5f
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.PUMPKIN, 64),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 2), 16, 10, 0.5f
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.MELON, 64),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 2), 16, 10, 0.5f
            ));

            //Level 3 Herbalist
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(Items.NETHERITE_HOE, 1), 16, 10, 0.5f
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(ModBlocks.PILL_CAULDRON_HUMAN_LOW, 1), 16, 10, 0.5f
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.FASTING_PILL_T1, 4),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1), 8, 10, 0.5f
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(ModItems.GOLDEN_SUN_LEAF.get(), 2), 16, 10, 0.5f
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(ModItems.JADE_BAMBOO_OF_SERENITY.get(), 2), 16, 10, 0.5f
            ));

            //Level 4 Herbalist
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModItems.HUNDRED_YEAR_GINSENG.get(), 1), 16, 10, 0.5f
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModItems.HUNDRED_YEAR_FIRE_GINSENG.get(), 1), 16, 10, 0.5f
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModItems.HUNDRED_YEAR_SNOW_GINSENG.get(), 1), 16, 10, 0.5f
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 1),
                    new ItemStack(ModItems.IRONWOOD_SPROUT.get(), 1), 16, 10, 0.5f
            ));


            //Level 5 Herbalist
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.CRIMSON_LOTUS_FLAME, 16),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 8), 16, 10, 0.5f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 13),
                    new ItemStack(ModItems.WHITE_JADE_ORCHID.get(), 1), 16, 10, 0.5f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Blocks.NETHERITE_BLOCK, 4),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 16), 16, 10, 0.5f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Blocks.DIAMOND_BLOCK, 2),
                    new ItemStack(ModItems.SPIRITUAL_STONE.get(), 4), 16, 10, 0.5f
            ));

        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();


        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ModItems.SPIRITUAL_STONE, 64),
                new ItemStack(ModTechniques.VOID_SWALLOWING_TECHNIQUE.manual.get(), 1), 2, 25, 10.0f
        ));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ModItems.SPIRITUAL_STONE, 64),
                new ItemStack(ModTechniques.DIVINE_PHOENIX_TECHNIQUE.manual.get(), 1), 2, 25, 10.0f
        ));
    }
}
