package net.thejadeproject.ascension.menus;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.custom.pill_cauldron.PillCauldronLowHumanMenu;
import net.thejadeproject.ascension.menus.spatialrings.SRContainer;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, AscensionCraft.MOD_ID);



    public static final DeferredHolder<MenuType<?>, MenuType<PillCauldronLowHumanMenu>> PILL_CAULDRON_LOW_HUMAN_MENU =
            registerMenuType("pill_cauldron_low_human_menu", PillCauldronLowHumanMenu::new);

    public static final DeferredHolder<MenuType<?>, MenuType<SRContainer>> SR_CONTAINER =
            registerMenuType("sr_container", SRContainer::fromNetwork);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
