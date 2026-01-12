package net.thejadeproject.ascension.data_attachments.attachments;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.thejadeproject.ascension.constants.LivingEntityState;
import net.thejadeproject.ascension.cultivation.player.EntityAttributeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
//TODO set up compound tags
public class AscensionAttributeWrapper {
    public final LivingEntity entity;

    //only used for "caching"
    public final MappedModifierHolder<String> entityStateDependantModifiers = new MappedModifierHolder<>();
    public final MappedModifierHolder<String> entityStateDependantAscensionModifiers = new MappedModifierHolder<>();


    public final ModifierHolder entityModifiers = new ModifierHolder();
    public final ModifierHolder permanentEntityModifiers = new ModifierHolder();
    public final ModifierHolder entityPhysiqueModifiers = new ModifierHolder();
    public final PathModifiersHolder entityPathModifiers = new PathModifiersHolder();

    public final ModifierHolder entityAscensionModifiers = new ModifierHolder();
    public final ModifierHolder permanentEntityAscensionModifiers = new ModifierHolder();
    public final ModifierHolder entityPhysiqueAscensionModifiers = new ModifierHolder();
    public final PathModifiersHolder entityPathAscensionModifiers = new PathModifiersHolder();
    //just holds the modifier data
    public interface IStateModifier{
        ResourceLocation getId();
        String getState();
        CompoundTag writeStateModifier();

    }
    public record AscensionAttributeModifier(String state,ResourceLocation id, double val) implements IStateModifier {
        @Override
        public ResourceLocation getId() {
            return id();
        }

        @Override
        public String getState() {
            return state();
        }

        @Override
        public CompoundTag writeStateModifier() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("state",state);
            compoundTag.putString("id",id.toString());
            compoundTag.putDouble("value",val);
            return compoundTag;
        }

        public static IStateModifier readStateModifier(CompoundTag tag) {
            return new AscensionAttributeModifier(tag.getString("state"), ResourceLocation.bySeparator(tag.getString("id"), ':'),tag.getDouble("value"));

        }
    }
    public record BuiltInModifier(String state,ResourceLocation id) implements IStateModifier{
        @Override
        public ResourceLocation getId() {
            return id();
        }

        @Override
        public String getState() {
            return state();
        }

        @Override
        public CompoundTag writeStateModifier() {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("state",state);
            compoundTag.putString("id",id.toString());
            return compoundTag;
        }
        public static IStateModifier readStateModifier(CompoundTag tag) {
            return new BuiltInModifier(tag.getString("state"), ResourceLocation.bySeparator(tag.getString("id"), ':'));

        }
    }
    public static class ModifierHolder{
        public final HashMap<Holder<Attribute>, HashMap<ResourceLocation,IStateModifier>> modifiers = new HashMap<>();

        public boolean modifierDisabled = false;
        public void putModifier(Holder<Attribute> attribute,IStateModifier modifier){
            if(!modifiers.containsKey(attribute)) modifiers.put(attribute,new HashMap<>());
            modifiers.get(attribute).put(modifier.getId(),modifier);

        }
        public IStateModifier removeModifier(Holder<Attribute> attribute,ResourceLocation id){
            return modifiers.get(attribute).remove(id);
        }
        public IStateModifier getModifier(Holder<Attribute> attribute,ResourceLocation id){
            return modifiers.get(attribute).get(id);
        }
        public Set<ResourceLocation> getAttributeModifierKeySet(Holder<Attribute> attribute){
            if(!modifiers.containsKey(attribute)) return Set.of();
            return modifiers.get(attribute).keySet();
        }
        public List<IStateModifier> getAttributeModifiers(Holder<Attribute> attribute){
            if(!modifiers.containsKey(attribute)) return List.of();
            return (List<IStateModifier>) modifiers.get(attribute).values();
        }
        public ListTag write(){
            ListTag modifierListTag = new ListTag();
            for(Holder<Attribute> attribute : modifiers.keySet()){
                ListTag listTag = new ListTag();
                for(ResourceLocation id : modifiers.get(attribute).keySet()){
                    listTag.add(getModifier(attribute,id).writeStateModifier());
                }
                CompoundTag attributeTag = new CompoundTag();

                attributeTag.putString("attribute",attribute.getKey().location().toString());
                attributeTag.put("modifiers",listTag);
                modifierListTag.add(attributeTag);
            }
            return modifierListTag;
        }
    }
    public static class MappedModifierHolder<T>{
        public final HashMap<T,ModifierHolder> modifiers = new HashMap<>();
        public void putModifier(T key,Holder<Attribute> attribute,IStateModifier modifier){
            if(!modifiers.containsKey(key)) modifiers.put(key,new ModifierHolder());
            modifiers.get(key).putModifier(attribute,modifier);
        }
        public IStateModifier removeModifier(T key,Holder<Attribute> attribute,ResourceLocation id){
            return modifiers.get(key).removeModifier(attribute,id);
        }
        public IStateModifier getModifier(T key,Holder<Attribute> attribute,ResourceLocation id){
            return modifiers.get(key).getModifier(attribute,id);
        }
        public Set<ResourceLocation> getAttributeModifierKeySet(T key,Holder<Attribute> attribute){
            if(!modifiers.containsKey(key)) return Set.of();
            return modifiers.get(key).getAttributeModifierKeySet(attribute);
        }
        public List<IStateModifier> getAttributeModifiers(T key,Holder<Attribute> attribute){
            if(!modifiers.containsKey(key)) return List.of();
            return modifiers.get(key).getAttributeModifiers(attribute);
        }
    }
    public static class PathModifiersHolder{
        //key is total minor realms so realm 2 minor 1 is actual 9*2 + 1
        public final HashMap<ResourceLocation,MappedModifierHolder<Integer>> minorRealmModifiers = new HashMap<>();
        //all realm breakthrough stats modifiers are held here
        public final HashMap<ResourceLocation,MappedModifierHolder<Integer>> majorRealmModifiers = new HashMap<>();

        public void putMinorRealmModifier(ResourceLocation path,int minorRealm,Holder<Attribute> attribute,IStateModifier modifier){
            if(!minorRealmModifiers.containsKey(path))minorRealmModifiers.put(path,new MappedModifierHolder<>());
            minorRealmModifiers.get(path).putModifier(minorRealm,attribute,modifier);
        }
        public IStateModifier removeMinorRealmModifier(ResourceLocation path,int minorRealm,Holder<Attribute> attribute,ResourceLocation id){
            return minorRealmModifiers.get(path).removeModifier(minorRealm,attribute,id);
        }
        public IStateModifier getMinorRealmModifier(ResourceLocation path,int minorRealm,Holder<Attribute> attribute,ResourceLocation id){
            return minorRealmModifiers.get(path).getModifier(minorRealm,attribute,id);
        }
        public Set<ResourceLocation> getMinorRealmAttributeModifierKeySet(ResourceLocation path,int minorRealm,Holder<Attribute> attribute){
            if(!minorRealmModifiers.containsKey(path)) return Set.of();
            return minorRealmModifiers.get(path).getAttributeModifierKeySet(minorRealm,attribute);
        }
        public List<IStateModifier> getMinorRealmAttributeModifiers(ResourceLocation path,int minorRealm,Holder<Attribute> attribute){
            if(!minorRealmModifiers.containsKey(path)) return List.of();
            return minorRealmModifiers.get(path).getAttributeModifiers(minorRealm,attribute);
        }


        public void putMajorRealmModifier(ResourceLocation path,int majorRealm,Holder<Attribute> attribute,IStateModifier modifier){
            if(!majorRealmModifiers.containsKey(path))majorRealmModifiers.put(path,new MappedModifierHolder<>());
            majorRealmModifiers.get(path).putModifier(majorRealm,attribute,modifier);
        }
        public IStateModifier removeMajorRealmModifier(ResourceLocation path,int majorRealm,Holder<Attribute> attribute,ResourceLocation id){
            return majorRealmModifiers.get(path).removeModifier(majorRealm,attribute,id);
        }
        public IStateModifier getMajorRealmModifier(ResourceLocation path,int majorRealm,Holder<Attribute> attribute,ResourceLocation id){
            return majorRealmModifiers.get(path).getModifier(majorRealm,attribute,id);
        }
        public Set<ResourceLocation> getMajorRealmAttributeModifierKeySet(ResourceLocation path,int majorRealm,Holder<Attribute> attribute){
            if(!majorRealmModifiers.containsKey(path)) return Set.of();
            return majorRealmModifiers.get(path).getAttributeModifierKeySet(majorRealm,attribute);
        }
        public List<IStateModifier> getMajorRealmAttributeModifiers(ResourceLocation path,int majorRealm,Holder<Attribute> attribute){
            if(!majorRealmModifiers.containsKey(path)) return List.of();
            return majorRealmModifiers.get(path).getAttributeModifiers(majorRealm,attribute);
        }


        public CompoundTag write(){
            CompoundTag pathTag = new CompoundTag();

            for(ResourceLocation path : minorRealmModifiers.keySet()){
                CompoundTag minorRealmModifiersTag = new CompoundTag();

                for(Integer integer : minorRealmModifiers.get(path).modifiers.keySet()){
                    minorRealmModifiersTag.put(integer.toString(),minorRealmModifiers.get(path).modifiers.get(integer).write());
                }
                pathTag.put(path.toString(),minorRealmModifiersTag);
            }


            for(ResourceLocation path : majorRealmModifiers.keySet()){

                CompoundTag majorRealmModifiersTag = new CompoundTag();
                if(pathTag.hasUUID(path.toString())) majorRealmModifiersTag = pathTag.getCompound(path.toString());
                for(Integer integer : majorRealmModifiers.get(path).modifiers.keySet()){
                    majorRealmModifiersTag.put(integer.toString(),majorRealmModifiers.get(path).modifiers.get(integer).write());
                }
                pathTag.put(path.toString(),majorRealmModifiersTag);
            }
            return pathTag;
        }
    }
    public AscensionAttributeWrapper(LivingEntity entity) {
        this.entity = entity;
    }


    //TODO check for state before applying
    /**
     *  creates a base value modifier that is not lost on rebirth and not linked to cultivation or physique
    */
    
    public void addPermanentAscensionModifier(Holder<Attribute> attribute,AscensionAttributeModifier modifier){
        permanentEntityAscensionModifiers.putModifier(attribute,modifier);
        if(!modifier.getState().equals(LivingEntityState.ALL))entityStateDependantAscensionModifiers.putModifier(modifier.getState(),attribute,modifier);
        EntityAttributeManager.increaseAttribute(entity,modifier.val,attribute);

    }

    public void addPermanentModifier(String state,Holder<Attribute> attribute, AttributeModifier modifier){
        IStateModifier stateModifier = new BuiltInModifier(state,modifier.id());
        permanentEntityModifiers.putModifier(attribute,stateModifier);
        if(!state.equals(LivingEntityState.ALL)) entityStateDependantModifiers.putModifier(state,attribute,stateModifier);
        entity.getAttribute(attribute).addPermanentModifier(modifier);

    }
    /**
     *  creates a base value modifier that is lost on rebirth, but not linked to cultivation or physique
     */
    public void addAscensionModifier(Holder<Attribute> attribute,AscensionAttributeModifier modifier){
        entityAscensionModifiers.putModifier(attribute,modifier);
        if(!modifier.getState().equals(LivingEntityState.ALL))entityStateDependantAscensionModifiers.putModifier(modifier.getState(),attribute,modifier);
        EntityAttributeManager.increaseAttribute(entity,modifier.val,attribute);
    }
    
    public void addModifier(String state,Holder<Attribute> attribute, AttributeModifier modifier){
        IStateModifier stateModifier = new BuiltInModifier(state,modifier.id());
        entityModifiers.putModifier(attribute,stateModifier);
        if(!state.equals(LivingEntityState.ALL)) entityStateDependantModifiers.putModifier(state,attribute,stateModifier);
        entity.getAttribute(attribute).addPermanentModifier(modifier);
        
    }

    public void addPhysiqueAscensionModifier(Holder<Attribute> attribute,AscensionAttributeModifier modifier){
        entityPhysiqueAscensionModifiers.putModifier(attribute,modifier);
        if(!modifier.getState().equals(LivingEntityState.ALL))entityStateDependantAscensionModifiers.putModifier(modifier.getState(),attribute,modifier);
        EntityAttributeManager.increaseAttribute(entity,modifier.val,attribute);
    }

    public void addPhysiqueModifier(String state,Holder<Attribute> attribute,AttributeModifier modifier){
        IStateModifier stateModifier = new BuiltInModifier(state,modifier.id());
        entityPhysiqueModifiers.putModifier(attribute,stateModifier);
        if(!state.equals(LivingEntityState.ALL))entityPhysiqueModifiers.putModifier(attribute,stateModifier);

        entity.getAttribute(attribute).addPermanentModifier(modifier);
    }

    public void addMinorRealmAscensionModifier(ResourceLocation path,int minorRealm,Holder<Attribute> attribute,AscensionAttributeModifier modifier){
        entityPathAscensionModifiers.putMinorRealmModifier(path,minorRealm,attribute,modifier);
        if(!modifier.getState().equals(LivingEntityState.ALL)) entityStateDependantAscensionModifiers.putModifier(modifier.state(),attribute,modifier);
        EntityAttributeManager.increaseAttribute(entity,modifier.val,attribute);
    }

    public void addMinorRealmModifier(ResourceLocation path,int minorRealm,String state,Holder<Attribute> attribute,AttributeModifier modifier){
        IStateModifier stateModifier = new BuiltInModifier(state,modifier.id());
        entityPathModifiers.putMinorRealmModifier(path,minorRealm,attribute,stateModifier);
        if(!state.equals(LivingEntityState.ALL)) entityStateDependantModifiers.putModifier(state,attribute,stateModifier);
        entity.getAttribute(attribute).addPermanentModifier(modifier);
    }

    public void addMajorRealmAscensionModifier(ResourceLocation path,int majorRealm,Holder<Attribute> attribute,AscensionAttributeModifier modifier){
        entityPathAscensionModifiers.putMajorRealmModifier(path,majorRealm,attribute,modifier);
        if(!modifier.getState().equals(LivingEntityState.ALL)) entityStateDependantAscensionModifiers.putModifier(modifier.state(),attribute,modifier);
        EntityAttributeManager.increaseAttribute(entity,modifier.val,attribute);
    }

    public void addMajorRealmModifier(ResourceLocation path,int majorRealm,String state, Holder<Attribute> attribute,AttributeModifier modifier){
        IStateModifier stateModifier = new BuiltInModifier(state,modifier.id());
        entityPathModifiers.putMajorRealmModifier(path,majorRealm,attribute,stateModifier);
        if(!state.equals(LivingEntityState.ALL)) entityStateDependantModifiers.putModifier(state,attribute,stateModifier);
        entity.getAttribute(attribute).addPermanentModifier(modifier);
    }

    

    public void loadNBTData(CompoundTag compoundTag, HolderLookup.Provider provider) {
    }

    public void saveNBTData(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag ascensionModifiersTag = new CompoundTag();
        //BASE VALUE MODIFIERS THAT RESET ON REBIRTH
        ascensionModifiersTag.put("ascension_modifiers",entityAscensionModifiers.write());
        //modifiers that reset on Rebirth
        ascensionModifiersTag.put("modifiers",entityModifiers.write());

        //physique ascension modifiers
        ascensionModifiersTag.put("physique_ascension_modifiers",entityPhysiqueAscensionModifiers.write());
        //physique  modifiers
        ascensionModifiersTag.put("physique_modifiers",entityPhysiqueModifiers.write());

        //permanent ascension modifiers
        ascensionModifiersTag.put("permanent_ascension_modifiers",permanentEntityAscensionModifiers.write());
        //permanent  modifiers
        ascensionModifiersTag.put("permanent_modifiers",permanentEntityModifiers.write());

        //path ascension modifiers
        ascensionModifiersTag.put("path_ascension_modifiers",entityPathAscensionModifiers.write());
        //path modifiers
        ascensionModifiersTag.put("path_modifiers",entityPathModifiers.write());

        tag.put("wrapped_entity_modifiers",ascensionModifiersTag);
    }
}
