package net.thejadeproject.ascension.cultivation.player.realm_change_handlers;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

public interface IRealmChangeHandler {
    //start minor realm and end minor realm, counted as total minor realms
    void onMinorRealmIncrease(Player player, ITechnique technique, String path, int oldRealm, int newRealm);
    void onMajorRealmIncrease(Player player, ITechnique technique,String path,int oldRealm,int newRealm);
    void onMinorRealmDecrease(Player player, ITechnique technique,String path,int oldRealm,int newRealm );
    void onMajorRealmDecrease(Player player, ITechnique technique,String path,int oldRealm,int newRealm);

}
