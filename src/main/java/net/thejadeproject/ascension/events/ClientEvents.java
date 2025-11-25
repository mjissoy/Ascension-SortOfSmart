package net.thejadeproject.ascension.events;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.OpenPhysiqueSelectScreenEvent;
import net.thejadeproject.ascension.events.custom.PhysiqueGeneratedEvent;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.network.clientBound.OpenPickPhysiqueScreen;
import net.thejadeproject.ascension.util.ModAttachments;

import java.nio.charset.StandardCharsets;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onOpenPhysiqueSelectScreenEvent(OpenPhysiqueSelectScreenEvent event){
        Minecraft.getInstance().setScreen(new GeneratePhysiqueScreen(Component.literal("generate")));
    }
    @SubscribeEvent
    public static void onPhysiqueGeneratedEvent(PhysiqueGeneratedEvent event){
        Minecraft.getInstance().player.setData(ModAttachments.PHYSIQUE,event.generatedPhysique);

        if(Minecraft.getInstance().screen instanceof GeneratePhysiqueScreen physiqueScreen){
            physiqueScreen.updateGeneratedPhysiques(event.generatedPhysique, event.otherPhysiques);
        }
    }
}