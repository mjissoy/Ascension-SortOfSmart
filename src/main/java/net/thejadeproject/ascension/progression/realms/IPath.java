package net.thejadeproject.ascension.progression.realms;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;

import java.util.Set;

public interface IPath {

    ResourceLocation getPathId();
    Component getPathDisplayName();
    Component getMajorRealmName(int majorRealm);
    Component getMinorRealmName(int majorRealm,int minorRealm);
    int getMaxMajorRealm();
    int getMaxMinorRealm(int majorRealm);

    //which states path can be cultivated in
    //and if it does not have soul and player loses body they lose this path
    Set<String> getPathLivingEntityStates();
}
