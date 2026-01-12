package net.thejadeproject.ascension.progression.techniques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.constants.CultivationSource;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.physiques.data.IPhysiqueData;
import net.thejadeproject.ascension.progression.realms.ModPaths;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.data.ITechniqueData;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;

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

    void tryCultivate(Player player, CultivationSource source);
    void tryStabiliseRealm(Player player,CultivationSource source);
    Double getDaoBonus(String attribute);

    ITechniqueData getTechniqueDataInstance();
    ITechniqueData getTechniqueDataInstance(RegistryFriendlyByteBuf buf);
    ITechniqueData getTechniqueDataInstance(CompoundTag tag);


    //realm stuff
    default Component getMajorRealmName(int majorRealm){
        return ModPaths.getPath(getPath()).getMajorRealmName(majorRealm);
    }
    default int getMaxMajorRealm(){
        return ModPaths.getPath(getPath()).getMaxMajorRealm();
    }
    default int getMaxMinorRealm(int majorRealm){
        return ModPaths.getPath(getPath()).getMaxMinorRealm(majorRealm);
    }
    default Component getMinorRealmName(int majorRealm,int minorRealm){
        return ModPaths.getPath(getPath()).getMinorRealmName(majorRealm,minorRealm);
    }
    double getQiForRealm(int majorRealm, int minorRealm);

}
