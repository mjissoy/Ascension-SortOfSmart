package net.thejadeproject.ascension;

import net.lucent.easygui.EasyGui;
import net.lucent.easygui.elements.other.SquareRenderable;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.overlays.EasyGuiOverlayManager;
import net.lucent.easygui.templating.registry.EasyGuiRegistries;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.thejadeproject.ascension.guis.easygui.ModActions;
import net.thejadeproject.ascension.guis.easygui.ModOverlays;
import net.thejadeproject.ascension.menus.ModMenuTypes;
import net.thejadeproject.ascension.menus.custom.pill_cauldron.PillCauldronLowHumanScreen;
import net.thejadeproject.ascension.network.ModPayloads;
import net.thejadeproject.ascension.util.KeyBindHandler;

@Mod(value = AscensionCraft.MOD_ID,dist = Dist.CLIENT)
public class AscensionCraftClient {
    public AscensionCraftClient(IEventBus modEventBus, ModContainer modContainer)
    {
        KeyBindHandler.register();
        ModActions.register(modEventBus);
        ModOverlays.register();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PILL_CAULDRON_LOW_HUMAN_MENU.get(), PillCauldronLowHumanScreen::new);
    }

}
