package net.thejadeproject.ascension.physiques;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import java.util.List;

//TODO have a get skill List method
//TODO requires skills completed
public interface IPhysique {


    //run when a players minor realm increases
    default void onMinorRealmIncrease(Player player){}
    //run when a players major realm increases
    default void onMajorRealmIncrease(Player player){}

    //run when a player logs in (if you need to load data)
     default void onPlayerLogIn(Player player){}

    //run when a player logs out (if you need to save data)
    //try use data attachments where possible
    default void onPlayerLogOut(Player player){}

    void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event);

    ResourceLocation getPhysiqueImage();

    //run when a player acquires a constitution
    default void onPhysiqueAcquisition(Player player){}

    default void onRemoveSpiritRoot(Player player){}

    String getDisplayTitle();
    List<String> getDisplayPathBonuses();
    List<String> getDisplayOtherBonuses();
}
