package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.progression.physiques.data.IPhysiqueData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.List;
import java.util.function.Supplier;

//TODO have a get skill List method
//TODO requires skills completed
public interface IPhysique {
    IRealmChangeHandler getRealmChangeHandler();

    default void onRealmChangeEvent(RealmChangeEvent event) {
        if(getRealmChangeHandler() ==null) return;
        IRealmChangeHandler changeHandler = getRealmChangeHandler();
        String techniqueId = event.player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(event.pathId).technique;
        if(techniqueId.equals("ascension:none")) return;
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.pathId,':'));
        //major realms
        if (event.oldMajorRealm < event.newMajorRealm) changeHandler.onMajorRealmIncrease(event.player, technique,event.pathId, event.oldMajorRealm,event.newMajorRealm);
        else if(event.oldMajorRealm > event.newMajorRealm) changeHandler.onMajorRealmDecrease(event.player, technique,event.pathId,  event.oldMajorRealm,event.newMajorRealm);

        int oldMinorRealms =  technique.getTotalMinorRealmsTo(event.oldMajorRealm,event.oldMinorRealm);
        int newMinorRealms =  technique.getTotalMinorRealmsTo(event.newMajorRealm,event.newMinorRealm);

        //minor realms
        if (oldMinorRealms < newMinorRealms) changeHandler.onMinorRealmIncrease(event.player,technique, event.pathId,oldMinorRealms,newMinorRealms);
        else if(oldMinorRealms > newMinorRealms) changeHandler.onMinorRealmDecrease(event.player,technique,event.pathId,oldMinorRealms,newMinorRealms);
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
    void onSkillRemoveEvent(PlayerSkillRemoveEvent event);

    Component getDisplayTitle();
    Component getDisplayPaths();
    List<Label> getDisplayEfficiencies(IEasyGuiScreen screen);
    Component getDescription();


    Component getFullDescription();
    IPhysique setDataSupplier(Supplier<IPhysiqueData> dataSupplier);
    IPhysiqueData getPhysiqueDataInstance(RegistryFriendlyByteBuf buf);
    IPhysiqueData getPhysiqueDataInstance(CompoundTag tag);
}
