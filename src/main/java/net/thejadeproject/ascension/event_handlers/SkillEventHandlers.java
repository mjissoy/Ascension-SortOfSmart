package net.thejadeproject.ascension.event_handlers;

import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.util.ModAttachments;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class SkillEventHandlers {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event){
        for(Player player : event.getServer().getPlayerList().getPlayers()){

            player.getData(ModAttachments.PLAYER_DATA).onServerTick(event);
        }
    }

}
