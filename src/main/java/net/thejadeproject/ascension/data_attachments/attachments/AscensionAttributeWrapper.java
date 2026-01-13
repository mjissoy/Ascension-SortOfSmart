package net.thejadeproject.ascension.data_attachments.attachments;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.constants.LivingEntityState;
import net.thejadeproject.ascension.cultivation.player.EntityAttributeManager;
import net.thejadeproject.ascension.util.NBTUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO setup so when adding an attribute it checks state before adding the value
public class AscensionAttributeWrapper {
    public final LivingEntity entity;

    public HashMap<String, HashSet<IAttributeModifier>> stateGroupedAttributes = new HashMap<>();
    public HashMap<ResourceLocation, HashSet<IAttributeModifier>> idGroupedAttributes = new HashMap<>();
    public HashMap<ResourceLocation, HashSet<IAttributeModifier>> groupGroupedAttributes = new HashMap<>();
    public HashMap<Holder<Attribute>, HashSet<IAttributeModifier>> attributeGroupedAttributes = new HashMap<>();

    public interface IAttributeModifier{
        String getLivingEntityState();
        ResourceLocation getModifierId();
        ResourceLocation getGroupId();
        void setGroupId(ResourceLocation id);
        Holder<Attribute> getAttribute();
        boolean isPermanent(); //no need for fixed since they can just add without the realm group
        boolean isDisabled();
        void setDisabled(boolean disabled, LivingEntity entity);
        void remove(LivingEntity entity);
        void add(LivingEntity entity);
        void write(CompoundTag tag);
        IAttributeModifier clone();
    }
    public static class AscensionAttributeModifier implements IAttributeModifier{
        private final String livingEntityState;
        private final ResourceLocation id;
        private ResourceLocation groupId = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"none");
        private final Holder<Attribute> attribute;;
        private final boolean permanent;
        private boolean disabled;
        private final double val;
        public AscensionAttributeModifier(String livingEntityState, ResourceLocation id, Holder<Attribute> attribute, boolean permanent, double val) {
            this.livingEntityState = livingEntityState;
            this.id = id;
            this.attribute = attribute;
            this.permanent = permanent;
            this.val = val;
        }
        public AscensionAttributeModifier(CompoundTag tag){
            this.livingEntityState = NBTUtil.getStringWithDefault(tag,"living_entity_state",LivingEntityState.ALL);
            this.id = ResourceLocation.bySeparator(tag.getString("modifier_id"),':');
            this.groupId = ResourceLocation.bySeparator(tag.getString("group_id"),':');
            Attribute attribute =  BuiltInRegistries.ATTRIBUTE.get(
                    ResourceLocation.bySeparator(
                            tag.getString("attribute"),
                            ':'
                    )
            );
            if(attribute != null){
                this.attribute = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                        attribute
                );
            }else this.attribute = null; //TODO add error log here

            this.permanent = NBTUtil.getBooleanWithDefault(tag,"permanent",false);
            this.disabled = NBTUtil.getBooleanWithDefault(tag,"disabled",true);
            this.val = tag.getDouble("value");
        }
        @Override
        public void write(CompoundTag tag) {
            tag.putString("type","ascension_attribute"); //allows it to decode which class to use
            tag.putString("living_entity_state",getLivingEntityState());
            tag.putString("modifier_id",getModifierId().toString());
            tag.putString("group_id",getGroupId().toString());
            tag.putString("attribute",BuiltInRegistries.ATTRIBUTE.getKey(getAttribute().value()).toString());
            tag.putBoolean("permanent",isPermanent());
            tag.putBoolean("disabled",isDisabled());
            tag.putDouble("value",getVal());
        }

        @Override
        public IAttributeModifier clone() {
            IAttributeModifier modifier = new AscensionAttributeModifier(getLivingEntityState(),getModifierId(),getAttribute(),isPermanent(),getVal());
            modifier.setGroupId(groupId);
            return modifier;
        }

        @Override
        public String getLivingEntityState() {
            return livingEntityState;
        }

        @Override
        public ResourceLocation getModifierId() {
            return id;
        }

        @Override
        public ResourceLocation getGroupId() {
            return groupId;
        }

        @Override
        public void setGroupId(ResourceLocation id) {
            this.groupId = id;
        }

        @Override
        public Holder<Attribute> getAttribute() {
            return attribute;
        }

        @Override
        public boolean isPermanent() {
            return permanent;
        }

        @Override
        public boolean isDisabled() {
            return disabled;
        }

        @Override
        public void setDisabled(boolean disabled, LivingEntity entity) {
            if(!isDisabled() && disabled){
                remove(entity);
            }else if(isDisabled() && !disabled){
                add(entity);
            }

            this.disabled = disabled;
        }
        public double getVal(){return val;}
        @Override
        public void remove(LivingEntity entity) {
            EntityAttributeManager.decreaseAttribute(entity,val,getAttribute());
        }

        @Override
        public void add(LivingEntity entity) {
            EntityAttributeManager.increaseAttribute(entity,val,getAttribute());
        }


    }
    public class MinecraftAttributeModifier implements IAttributeModifier{
        private final String livingEntityState;
        private ResourceLocation groupId = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"none");
        private final Holder<Attribute> attribute;;
        private final boolean permanent;
        private final AttributeModifier modifier;
        private boolean disabled;

        public MinecraftAttributeModifier(String livingEntityState, AttributeModifier modifier, Holder<Attribute> attribute, boolean permanent) {
            this.livingEntityState = livingEntityState;


            this.attribute = attribute;
            this.permanent = permanent;
            this.modifier = modifier;
        }
        public MinecraftAttributeModifier(CompoundTag tag){
            this.livingEntityState = NBTUtil.getStringWithDefault(tag,"living_entity_state",LivingEntityState.ALL);
            this.groupId = ResourceLocation.bySeparator(tag.getString("group_id"),':');
            Attribute attribute =  BuiltInRegistries.ATTRIBUTE.get(
                    ResourceLocation.bySeparator(
                            tag.getString("attribute"),
                            ':'
                    )
            );
            if(attribute != null){
                this.attribute = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(
                        attribute
                );
            }else this.attribute = null; //TODO add error log here

            this.permanent = NBTUtil.getBooleanWithDefault(tag,"permanent",false);
            this.disabled = NBTUtil.getBooleanWithDefault(tag,"disabled",true);

            this.modifier = AttributeModifier.load(tag.getCompound("modifier"));

        }
        @Override
        public void write(CompoundTag tag) {
            tag.putString("type","minecraft_attribute"); //allows it to decode which class to use
            tag.putString("living_entity_state",getLivingEntityState());
            tag.putString("group_id",getGroupId().toString());
            tag.putString("attribute",BuiltInRegistries.ATTRIBUTE.getKey(getAttribute().value()).toString());
            tag.putBoolean("permanent",isPermanent());
            tag.putBoolean("disabled",isDisabled());
            tag.put("modifier",modifier.save());
        }

        @Override
        public IAttributeModifier clone() {
            MinecraftAttributeModifier modifier = new MinecraftAttributeModifier(getLivingEntityState(),this.modifier,getAttribute(),isPermanent());
            modifier.setGroupId(getGroupId());
            return modifier;
        }

        @Override
        public String getLivingEntityState() {
            return livingEntityState;
        }

        @Override
        public ResourceLocation getModifierId() {
            return modifier.id();
        }

        @Override
        public ResourceLocation getGroupId() {
            return groupId;
        }

        @Override
        public void setGroupId(ResourceLocation id) {
            this.groupId = id;
        }

        @Override
        public Holder<Attribute> getAttribute() {
            return attribute;
        }

        @Override
        public boolean isPermanent() {
            return permanent;
        }

        @Override
        public boolean isDisabled() {
            return disabled;
        }

        @Override
        public void setDisabled(boolean disabled, LivingEntity entity) {
            if(!isDisabled() && disabled){
                remove(entity);
            }else if(isDisabled() && !disabled){
                add(entity);
            }

            this.disabled = disabled;
        }

        @Override
        public void remove(LivingEntity entity) {
            entity.getAttribute(getAttribute()).removeModifier(getModifierId());
        }

        @Override
        public void add(LivingEntity entity) {
            entity.getAttribute(getAttribute()).addPermanentModifier(modifier);
        }
    }
    public AscensionAttributeWrapper(LivingEntity entity) {
        this.entity = entity;
    }

    public static Set<IAttributeModifier> union(Set<IAttributeModifier> set1,Set<IAttributeModifier> set2){
        return Stream.concat(set1.stream(),set2.stream()).collect(Collectors.toSet());
    }
    public static Set<IAttributeModifier> intersect(Set<IAttributeModifier> set1,Set<IAttributeModifier> set2){
        return set1.stream().filter(set2::contains).collect(Collectors.toSet());
    }
    public Set<IAttributeModifier> getAllModifiers(){
        Set<IAttributeModifier> finalSet = new HashSet<>();
        for(HashSet<IAttributeModifier> set : stateGroupedAttributes.values()){
            finalSet = union(finalSet,set);
        }
        return finalSet;
    }
    public void validateGroups(IAttributeModifier modifier){
        stateGroupedAttributes.computeIfAbsent(modifier.getLivingEntityState(),(key)->new HashSet<>());
        idGroupedAttributes.computeIfAbsent(modifier.getModifierId(),(key)->new HashSet<>());
        groupGroupedAttributes.computeIfAbsent(modifier.getGroupId(),(key)->new HashSet<>());
        attributeGroupedAttributes.computeIfAbsent(modifier.getAttribute(),(key)->new HashSet<>());

    }

    public void addAttributeModifier(IAttributeModifier modifier){
        validateGroups(modifier);
        stateGroupedAttributes.get(modifier.getLivingEntityState()).add(modifier);
        idGroupedAttributes.get(modifier.getModifierId()).add(modifier);
        groupGroupedAttributes.get(modifier.getGroupId()).add(modifier);
        attributeGroupedAttributes.get(modifier.getAttribute()).add(modifier);
        if(!modifier.isDisabled())modifier.add(entity);
    }

    public void removeAttributeModifier(IAttributeModifier modifier){
        validateGroups(modifier);
        stateGroupedAttributes.get(modifier.getLivingEntityState()).remove(modifier);
        idGroupedAttributes.get(modifier.getModifierId()).remove(modifier);
        groupGroupedAttributes.get(modifier.getGroupId()).remove(modifier);
        attributeGroupedAttributes.get(modifier.getAttribute()).remove(modifier);

        if(!modifier.isDisabled()) modifier.remove(entity);
    }
    public void loadNBTData(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag listTag = compoundTag.getList("modifiers",Tag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size();i++){
            CompoundTag modifierTag = listTag.getCompound(i);
            if(modifierTag.getString("type").equals("minecraft_attribute")){
                addAttributeModifier(new MinecraftAttributeModifier(modifierTag));
            }else {
                addAttributeModifier(new AscensionAttributeModifier(modifierTag));
            }
        }
    }

    public void saveNBTData(CompoundTag tag, HolderLookup.Provider provider) {


        ListTag listTag = new ListTag();
        for(IAttributeModifier modifier : getAllModifiers()){
            CompoundTag modifierTag = new CompoundTag();
            modifier.write(modifierTag);
            listTag.add(modifierTag);
        }
        tag.put("modifiers",listTag);
    }
}
