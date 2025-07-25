package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;

import java.awt.*;
import java.util.List;

//TODO have a get skill List method
//TODO requires skills completed
public interface IPhysique {


    //run when a players minor realm increases
    default void onMinorRealmIncrease(MinorRealmChangeEvent event){}
    //run when a players major realm increases
    default void onMajorRealmIncrease(MajorRealmChangeEvent event){}

    //run when a player logs in (if you need to load data)
     default void onPlayerLogIn(Player player){}

    //run when a player logs out (if you need to save data)
    //try use data attachments where possible
    default void onPlayerLogOut(Player player){}

    void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event);

    ITextureData getPhysiqueImage();

    //run when a player acquires a constitution
    default void onPhysiqueAcquisition(Player player){}

    default void onRemovePhysique(Player player){}

    SkillList getSkillList();

    String getDisplayTitle();
    List<Component> getDisplayPaths();
    List<Label> getDisplayEfficiencies(IEasyGuiScreen screen);
    List<Component> getDescription();

    List<Component> getFullDescription();
}
