package net.thejadeproject.ascension.events;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.thejadeproject.ascension.util.ModTags;

public class DescriptionEvents {

    @EventBusSubscriber
    public class TagDescriptionEvents {
        @SubscribeEvent
        public static void onHoverText(RenderTooltipEvent.GatherComponents event) {
            if (event.getItemStack().is(ModTags.Items.MEDICINAL)) {
                event.getTooltipElements().add(Either.left(Component.literal("§a[Medicinal]")));
            }
            if (event.getItemStack().is(ModTags.Items.FIRE)) {
                event.getTooltipElements().add(Either.left(Component.literal("§4Fire")));
            }
        }
    }
}
