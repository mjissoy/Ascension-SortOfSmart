package net.thejadeproject.ascension.cultivation.player.realm_change_handlers;

import net.minecraft.world.entity.player.Player;

public interface IRealmChangeHandler {

    void onMinorRealmIncrease(Player player,String path,int numberOfMinorRealms);
    void onMajorRealmIncrease(Player player,String path,int numberOfMajorRealms);
    void onMinorRealmDecrease(Player player,String path,int numberOfMinorRealms);
    void onMajorRealmDecrease(Player player,String path,int numberOfMajorRealms);

}
