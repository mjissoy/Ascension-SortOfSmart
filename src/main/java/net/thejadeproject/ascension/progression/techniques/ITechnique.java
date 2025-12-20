package net.thejadeproject.ascension.progression.techniques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.List;
import java.util.Set;

//TODO include realm foundation in stat upgrade for major realms
public interface ITechnique {

    IRealmChangeHandler getRealmChangeHandler();

    default void onRealmChangeEvent(RealmChangeEvent event) {
        IRealmChangeHandler changeHandler = getRealmChangeHandler();
        
        
        
        //major realms
        if (event.getMajorRealmsChanged() > 0) changeHandler.onMajorRealmIncrease(event.player, event.pathId, event.getMajorRealmsChanged());
        else if(event.getMajorRealmsChanged() < 0) changeHandler.onMajorRealmDecrease(event.player, event.pathId, event.getMajorRealmsChanged());

        //minor realms
        if (event.getTotalMinorRealmsChanged() > 0) changeHandler.onMinorRealmIncrease(event.player, event.pathId, event.getTotalMinorRealmsChanged());
        else if(event.getTotalMinorRealmsChanged() < 0) changeHandler.onMinorRealmDecrease(event.player,event.pathId,event.getTotalMinorRealmsChanged());
    }

    //run when a player logs in (if you need to load data)
    default void onPlayerLogIn(Player player){}

    //run when a player logs out (if you need to save data)
    //try use data attachments where possible
    default void onPlayerLogOut(Player player){}
    void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event);

    ITextureData getTechniqueImage();

    //run when a player acquires a Technique
    void onTechniqueAcquisition(Player player);

    default void onRemoveTechnique(Player player){}

    String getPath();

    SkillList getSkillList();

    Component getDisplayTitle();

     Component getDescription();

    List<Label> getDisplayDaoEfficiencies(IEasyGuiScreen screen);

    //the elements cultivated
    Set<String> getCultivationAttributes();
    //the elemental affinities
    Set<String> getDaoBonuses();

    StabilityHandler getStabilityHandler();
    IBreakthroughHandler getBreakthroughHandler();

    void tryCultivate(Player player);
    void tryStabiliseRealm(Player player);
    Double getDaoBonus(String attribute);
}
