package net.thejadeproject.ascension.events;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {


    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();


            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get(), 1), 1, 10, 10.0f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModTechniques.PURE_WATER_TECHNIQUE.manual.get(), 1), 1, 10, 10.0f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModTechniques.PURE_SWORD_INTENT.manual.get(), 1), 1, 10, 10.0f
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.SPIRITUAL_STONE, 2),
                    new ItemStack(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get(), 1), 1, 10, 10.0f
            ));


        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();


        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ModItems.SPIRITUAL_STONE, 16),
                new ItemStack(ModTechniques.VOID_SWALLOWING_TECHNIQUE.manual.get(), 1), 1, 25, 10.0f
        ));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ModItems.SPIRITUAL_STONE, 16),
                new ItemStack(ModTechniques.DIVINE_PHOENIX_TECHNIQUE.manual.get(), 1), 1, 25, 10.0f
        ));
    }
}
