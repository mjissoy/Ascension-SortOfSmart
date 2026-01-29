package net.thejadeproject.ascension.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class ModVillagerTrades {


    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_WATER_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_METAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_EARTH_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_WOOD_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_SWORD_INTENT.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_FIST_INTENT.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_AXE_INTENT.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_BLADE_INTENT.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.PURE_SPEAR_INTENT.manual.get(), 1), 2, 3, 0.01f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.WOOD_ELEMENTAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.FIRE_ELEMENTAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.EARTH_ELEMENTAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.WATER_ELEMENTAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 8),
                    new ItemStack(ModTechniques.METAL_ELEMENTAL_TECHNIQUE.manual.get(), 1), 2, 3, 0.01f));

        }
    }


}
