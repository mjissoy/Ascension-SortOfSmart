package net.thejadeproject.ascension.capabilities;

import net.lucent.formation_arrays.api.capability.Capabilities;
import net.lucent.formation_arrays.capabilities.ConsumeItemFormationFuel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.capabilities.implementation.FormationHolder;
import net.thejadeproject.ascension.capabilities.implementation.SinglePlayerFilterToken;
import net.thejadeproject.ascension.items.ModItems;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){

        event.registerItem(
                Capabilities.FORMATION_FUEL_CAPABILITY,
                (itemStack, context)->new ConsumeItemFormationFuel(),
                ModItems.SPIRITUAL_STONE

        );
        event.registerItem(
                Capabilities.FORMATION_HOLDER_CAPABILITY,
                (itemStack, context)->new FormationHolder(),
                ModItems.FORMATION_PLATE

        );
        event.registerItem(
                AscensionCapabilities.PLAYER_FILTER_CAPABILITY,
                (itemStack, context)->new SinglePlayerFilterToken(),
                ModItems.FORMATION_SLIP_ACACIA,
                ModItems.FORMATION_SLIP_BAMBOO,
                ModItems.FORMATION_SLIP_BIRCH,
                ModItems.FORMATION_SLIP_CHERRY,
                ModItems.FORMATION_SLIP_CRIMSON,
                ModItems.FORMATION_SLIP_GOLDEN_PALM,
                ModItems.FORMATION_SLIP_DARK_OAK,
                ModItems.FORMATION_SLIP_IRONWOOD,
                ModItems.FORMATION_SLIP_MANGROVE,
                ModItems.FORMATION_SLIP_OAK,
                ModItems.FORMATION_SLIP_JUNGLE,
                ModItems.FORMATION_SLIP_SPRUCE,
                ModItems.FORMATION_SLIP_WARPED

        );



    }

}
