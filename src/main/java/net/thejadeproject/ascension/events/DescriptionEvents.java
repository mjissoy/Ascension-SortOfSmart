package net.thejadeproject.ascension.events;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModTags;

public class DescriptionEvents {

    @EventBusSubscriber
    public class TagDescriptionEvents {
        @SubscribeEvent
        public static void onHoverText(RenderTooltipEvent.GatherComponents event) {
            event.getItemStack().getTags().forEach(itemTagKey -> {
                if(ModTags.Items.daoItemTags.containsKey(itemTagKey.location().toString())) {
                    var list = event.getTooltipElements();
                    Component component = AscensionRegistries.Dao.DAO_REGISTRY.get(itemTagKey.location()).getDisplayTitle();
                    if(list.size() > 2){
                        event.getTooltipElements().add(1,Either.left(component));

                    }else{
                        event.getTooltipElements().add(Either.left(component));

                    }
                 }
            });
        }
    }
}
