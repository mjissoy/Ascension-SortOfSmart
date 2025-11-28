package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.custom.AscensionSkillEntity;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamageEvent(LivingDamageEvent.Pre event){
        System.out.println("custom damage event");
        if(!(event.getSource().getEntity() instanceof  Player player)) return;
        System.out.println("getting started");
        Set<String> attributes = new HashSet<>();


        //player did it directly with a weapon or hand so use those tags
        if(event.getSource().isDirect()){
            if(event.getSource().getWeaponItem() == ItemStack.EMPTY) attributes.add("ascension:fist_intent");
            event.getSource().getWeaponItem().getTags().forEach(itemTagKey -> {
                if(ModTags.Items.daoItemTags.containsKey(itemTagKey.location().toString())) attributes.add(itemTagKey.location().toString());
            });
        }else{
            //damaged using an entity so use said entities tags

            if(event.getSource().getDirectEntity() instanceof AscensionSkillEntity skillEntity){
                attributes.addAll(skillEntity.getDaoTags());
            }
        }

        GatherEfficiencyModifiersEvent effEvent = new GatherEfficiencyModifiersEvent(player,"ascension:intent",attributes);
        NeoForge.EVENT_BUS.post(effEvent);
        System.out.println("applicable attributes: "+ attributes);
        System.out.println("damage before : "+event.getNewDamage());
        System.out.println("generative: "+effEvent.getTotalGenerativeMultiplier());
        System.out.println("destructive: "+effEvent.getTotalDestructiveMultiplier());
        System.out.println("dao: "+effEvent.getTotalDaoEfficiencyMultiplier());
        System.out.println("multipliers : "+effEvent.getTotalEfficiencyMultiplier());
        event.setNewDamage((float) (event.getNewDamage()*effEvent.getTotalEfficiencyMultiplier()));
        System.out.println("damage after : "+event.getNewDamage());

    }
    @SubscribeEvent
    public static void postDamageListener(LivingDamageEvent.Post event){
        if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Player player) {

            //gather attributes

            System.out.println("Trying to cultivate intent");


            if(player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:intent").technique.equals("ascension:none")) return;
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                    player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:intent").technique,
                    ':'
            )).tryCultivate(player);
        }
        System.out.println("damaged entity : "+ event.getEntity());
        if(event.getEntity() instanceof Player player){
            //cultivate body
            System.out.println("trying to cultivate body");
            if(player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:body").technique.equals("ascension:none")) return;
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                    player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:body").technique,
                    ':'
            )).tryCultivate(player);
        }
    }

}
