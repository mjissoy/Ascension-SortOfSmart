package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.physiques.data.IPhysiqueData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;

import java.util.List;
import java.util.function.Supplier;

//TODO have a get skill List method
//TODO requires skills completed
public interface IPhysique {
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

    ITextureData getPhysiqueImage();

    //run when a player acquires a constitution
    default void onPhysiqueAcquisition(Player player){}

    default void onRemovePhysique(Player player){}

    SkillList getSkillList();

    Component getDisplayTitle();
    Component getDisplayPaths();
    List<Label> getDisplayEfficiencies(IEasyGuiScreen screen);
    Component getDescription();


    Component getFullDescription();
    IPhysique setDataSupplier(Supplier<IPhysiqueData> dataSupplier);
    IPhysiqueData getPhysiqueDataInstance(RegistryFriendlyByteBuf buf);
    IPhysiqueData getPhysiqueDataInstance(CompoundTag tag);
}
