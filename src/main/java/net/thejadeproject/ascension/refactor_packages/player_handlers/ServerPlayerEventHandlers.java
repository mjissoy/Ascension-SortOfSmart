package net.thejadeproject.ascension.refactor_packages.player_handlers;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.data_attachments.attachments.PlayerInputStates;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class ServerPlayerEventHandlers {




    @SubscribeEvent
    public static void onInputStateChanged(PlayerInputStates.InputStateChanged inputStateChanged){
        if(inputStateChanged.input.equals("skill_cast") && inputStateChanged.state == PlayerInputStates.InputState.PRESSED){
            IEntityData entityData = inputStateChanged.player.getData(ModAttachments.ENTITY_DATA);
            System.out.println("trying to cast active slot: "+entityData.getSkillCastHandler().getHotBar().getActiveSlot());
            entityData.getSkillCastHandler().tryCast(inputStateChanged.player);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event){
        for(ServerPlayer player : event.getServer().getPlayerList().getPlayers()){
            player.getData(ModAttachments.ENTITY_DATA).getSkillCastHandler().tick(player);
        }
    }
}
