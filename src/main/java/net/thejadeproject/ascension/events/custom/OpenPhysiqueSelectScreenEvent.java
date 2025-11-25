package net.thejadeproject.ascension.events.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class OpenPhysiqueSelectScreenEvent extends Event {
    public  final boolean state;
    public OpenPhysiqueSelectScreenEvent(boolean state){
        this.state = state;

    }
}