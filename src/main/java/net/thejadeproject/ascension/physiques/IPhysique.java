package net.thejadeproject.ascension.physiques;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

public interface IPhysique {


    //run when a players minor realm increases
    void onMinorRealmIncrease(Player player);
    //run when a players major realm increases
    void onMajorRealmIncrease(Player player);

    //run when a player logs in (if you need to load data)
    void onPlayerLogIn(Player player);

    //run when a player logs out (if you need to save data)
    //try use data attachments where possible
    void onPlayerLogOut(Player player);

    void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event);

    ResourceLocation getPhysiqueImage();

    //run when a player acquires a constitution
    void onPhysiqueAcquisition(Player player);
}
