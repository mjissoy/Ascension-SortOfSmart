package net.thejadeproject.ascension.progression.techniques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MinorRealmChangeEvent;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;

import java.util.List;
import java.util.Set;

//TODO include realm foundation in stat upgrade for major realms
public interface ITechnique {
    default void onMinorRealmIncrease(MinorRealmChangeEvent event){}
    //run when a players major realm increases
    default void onMajorRealmIncrease(MajorRealmChangeEvent event){}

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

    String getDisplayTitle();

    List<MutableComponent> getFullDescription();
    List<MutableComponent> getDescription();

    List<Label> getDisplayDaoEfficiencies(IEasyGuiScreen screen);

    //the elements cultivated
    Set<String> getCultivationAttributes();
    //the elemental affinities
    Set<String> getDaoBonuses();

    StabilityHandler getStabilityHandler();
    IBreakthroughHandler getBreakthroughHandler();

    void tryCultivate(Player player);
    void tryStabiliseRealm(Player player);
    Double getEfficiencyValue(String attribute);
}
