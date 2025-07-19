package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamageEvent(LivingDamageEvent.Pre event){
        if(event.getSource().getEntity() != null){
            if(!(event.getSource().getEntity() instanceof Player player)) return;;
            List<String> attributes = new ArrayList<>();
            if(event.getSource().getWeaponItem() == ItemStack.EMPTY) attributes.add("ascension:fist_intent");
            event.getSource().getWeaponItem().getTags().forEach(itemTagKey -> {

                if(ModTags.Items.ASCENSION_ATTRIBUTES.contains(itemTagKey)) attributes.add(itemTagKey.location().toString());
            });

            GatherEfficiencyModifiersEvent effEvent = new GatherEfficiencyModifiersEvent(player,"ascension:intent",attributes);
            NeoForge.EVENT_BUS.post(effEvent);
            System.out.println("applicable attributes: "+ attributes);
            System.out.println("damage before : "+event.getNewDamage());
            event.setNewDamage((float) (event.getNewDamage()*effEvent.getTotalEfficiencyMultiplier()));
            System.out.println("damage after : "+event.getNewDamage());
         }
    }
    @SubscribeEvent
    public static void postDamageListener(LivingDamageEvent.Post event){
        if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player player) {

            //gather attributes

            System.out.println("Trying to cultivate intent");


            if(player.getData(ModAttachments.PLAYER_DATA).getPathData("ascension:intent").technique.equals("ascension:none")) return;
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                    player.getData(ModAttachments.PLAYER_DATA).getPathData("ascension:intent").technique,
                    ':'
            )).tryCultivate(player);
        }
        System.out.println("damaged entity : "+ event.getEntity());
        if(event.getEntity() instanceof Player player){
            //cultivate body
            System.out.println("trying to cultivate body");
            if(player.getData(ModAttachments.PLAYER_DATA).getPathData("ascension:body").technique.equals("ascension:none")) return;
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                    player.getData(ModAttachments.PLAYER_DATA).getPathData("ascension:body").technique,
                    ':'
            )).tryCultivate(player);
        }
    }

}
