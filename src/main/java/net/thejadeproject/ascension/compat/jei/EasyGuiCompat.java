package net.thejadeproject.ascension.compat.jei;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.thejadeproject.ascension.AscensionCraft;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID,value = Dist.CLIENT)
public class EasyGuiCompat {


    @SubscribeEvent
    public static void renderLayer(RenderGuiLayerEvent.Pre event){

        if(event.getName().getNamespace().equals("jei")){
            //TODO

        }
    }
}
