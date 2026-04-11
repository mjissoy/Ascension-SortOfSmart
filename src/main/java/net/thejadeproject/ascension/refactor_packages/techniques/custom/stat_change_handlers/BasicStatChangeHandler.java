package net.thejadeproject.ascension.refactor_packages.techniques.custom.stat_change_handlers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

import java.util.HashMap;

public class BasicStatChangeHandler{

    private HashMap<Stat, ValueContainerModifier> minorRealmStatModifierMap = new HashMap<>();
    private HashMap<Stat, ValueContainerModifier> majorRealmStatModifierMap = new HashMap<>();

    private HashMap<Holder<Attribute>,ValueContainerModifier> minorRealmAttributeModifierMap = new HashMap<>();
    private HashMap<Holder<Attribute>,ValueContainerModifier> majorRealmAttributeModifierMap = new HashMap<>();


    public ResourceLocation create(String prefix,int majorRealm,int minorRealm){
        return ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,prefix+majorRealm+"_"+minorRealm);
    }
    public void addMinorRealmStatModifier(Stat stat,ValueContainerModifier modifier){
        minorRealmStatModifierMap.put(stat,modifier);
    }
    public void addMajorRealmStatModifier(Stat stat,ValueContainerModifier modifier){
        majorRealmStatModifierMap.put(stat,modifier);
    }
    public void addMinorRealmAttributeModifier(Holder<Attribute> attributeHolder,ValueContainerModifier modifier){
        minorRealmAttributeModifierMap.put(attributeHolder,modifier);
    }
    public void addMajorRealmAttributeModifier(Holder<Attribute> attributeHolder,ValueContainerModifier modifier){
        majorRealmAttributeModifierMap.put(attributeHolder,modifier);
    }

    public void applyStatChanges(IEntityData entityData,int oldMajorRealm,int oldMinorRealm,int newMajorRealm,int newMinorRealm){
        if(oldMajorRealm < newMajorRealm || (oldMajorRealm == newMajorRealm && newMinorRealm>oldMinorRealm)){
            int majorRealmsChanged = newMajorRealm-oldMajorRealm;
            for(int i =0;i<majorRealmsChanged;i++){
                ResourceLocation identifier = create("major",oldMajorRealm+i,0);
            }

        }else{

        }
    }
}
