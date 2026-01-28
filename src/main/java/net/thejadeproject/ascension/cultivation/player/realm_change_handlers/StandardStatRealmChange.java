package net.thejadeproject.ascension.cultivation.player.realm_change_handlers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.EntityAttributeManager;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.data_attachments.attachments.AscensionAttributeWrapper;
import net.thejadeproject.ascension.progression.techniques.ITechnique;

import java.util.List;
import java.util.Map;

public class StandardStatRealmChange implements IRealmChangeHandler{

    public final List<AscensionAttributeWrapper.IAttributeModifier> minorRealmAttributeIncrease;
    public final List<AscensionAttributeWrapper.IAttributeModifier> majorRealmAttributeIncrease;


    public StandardStatRealmChange(List<AscensionAttributeWrapper.IAttributeModifier> minorRealmAttributeIncrease, List<AscensionAttributeWrapper.IAttributeModifier> majorRealmAttributeIncrease) {
        this.minorRealmAttributeIncrease = minorRealmAttributeIncrease;
        this.majorRealmAttributeIncrease = majorRealmAttributeIncrease;
    }

    @Override
    public void onMinorRealmIncrease(Player player, ITechnique technique, String path, int oldRealm, int newRealm) {
        AscensionAttributeWrapper wrapper = player.getData(ModAttachments.ATTRIBUTE_WRAPPER);
        for(int i = oldRealm+1;i<newRealm+1;i++){
            for(AscensionAttributeWrapper.IAttributeModifier modifier : minorRealmAttributeIncrease){
                ResourceLocation id = ResourceLocation.bySeparator(path+"_realm_"+i+"_"+modifier.getAttribute().getKey().location().getPath(),':');
                AscensionAttributeWrapper.IAttributeModifier newModifier = modifier.clone(id);

                newModifier.setGroupId(ResourceLocation.bySeparator(path+"_realm_"+i,':'));
                wrapper.addAttributeModifier(newModifier);
            }
        }
    }

    @Override
    public void onMajorRealmIncrease(Player player, ITechnique technique, String path, int oldRealm, int newRealm) {
        AscensionAttributeWrapper wrapper = player.getData(ModAttachments.ATTRIBUTE_WRAPPER);
        for(int i = oldRealm+1;i<newRealm+1;i++){
            for(AscensionAttributeWrapper.IAttributeModifier modifier : majorRealmAttributeIncrease){
                ResourceLocation id = ResourceLocation.bySeparator(path+"_realm_"+technique.getTotalMinorRealmsTo(i,0)+"_"+modifier.getAttribute().getKey().location().getPath(),':');
                AscensionAttributeWrapper.IAttributeModifier newModifier = modifier.clone(id);

                newModifier.setGroupId(ResourceLocation.bySeparator(path+"_realm_"+technique.getTotalMinorRealmsTo(i,0),':'));
                wrapper.addAttributeModifier(newModifier);
            }
        }
    }

    @Override
    public void onMinorRealmDecrease(Player player, ITechnique technique, String path, int oldRealm, int newRealm) {
        AscensionAttributeWrapper wrapper = player.getData(ModAttachments.ATTRIBUTE_WRAPPER);
        for(int i = newRealm+1;i<oldRealm+1;i++){
            for(AscensionAttributeWrapper.IAttributeModifier modifier : minorRealmAttributeIncrease){
                ResourceLocation id = ResourceLocation.bySeparator(path+"_realm_"+i+"_"+modifier.getAttribute().getKey().location().getPath(),':');

                wrapper.removeAttributeOfId(id);
            }
        }
    }

    @Override
    public void onMajorRealmDecrease(Player player, ITechnique technique, String path, int oldRealm, int newRealm) {
        AscensionAttributeWrapper wrapper = player.getData(ModAttachments.ATTRIBUTE_WRAPPER);

        for(int i = newRealm+1;i<oldRealm+1;i++){

            for(AscensionAttributeWrapper.IAttributeModifier modifier : majorRealmAttributeIncrease){
                ResourceLocation id = ResourceLocation.bySeparator(path+"_realm_"+i+"_"+modifier.getAttribute().getKey().location().getPath(),':');

                wrapper.removeAttributeOfId(id);
                System.out.println("temp");
            }
        }
    }
}
