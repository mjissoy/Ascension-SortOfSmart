package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.stats.events.StatChangedEvent;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.HashMap;

public class AscensionAttributesContainer implements IDataInstance {
    private final IEntityFormData formData;
    private final HashMap<AscensionAttribute,AscensionAttributeInstance> attributes = new HashMap<>();

    public AscensionAttributesContainer(IEntityFormData formData) {
        this.formData = formData;
    }

    public AscensionAttributeInstance getAttribute(AscensionAttribute attribute){
        return attributes.get(attribute);
    }
    public AscensionAttributeInstance getAttribute(Holder<Attribute> vanillaAttribute){
        return attributes.get(AscensionAttribute.getAscensionAttribute(vanillaAttribute));
    }


    public void onStatChanged(StatChangedEvent event){
        for(AscensionAttributeInstance instance : attributes.values()){
            instance.onStatChanged(event);
        }
    }

    //TODO

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag listAttributes = new ListTag();
        for(AscensionAttributeInstance instance : attributes.values()){
            listAttributes.add(instance.write());
        }
        tag.put("ascension_attributes",listAttributes);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(attributes.size());
        for(AscensionAttributeInstance instance : attributes.values()){
            instance.encode(buf);
        }
    }


    private void readAttributes(ListTag listTag){
        for(int i = 0;i<listTag.size();i++){
            AscensionAttributeInstance instance = AscensionAttributeInstance.read(listTag.getCompound(i),formData);
            attributes.put(instance.getAscensionAttribute(),instance);
        }
    }
    private void createFreshAscensionAttributesFrom(LivingEntity entity){
        for(Holder<Attribute> attributeHolder : entity.getAttributes().attributes.keySet()){
            attributes.put(AscensionAttribute.getAscensionAttribute(attributeHolder),
                    new AscensionAttributeInstance(formData,AscensionAttribute.getAscensionAttribute(attributeHolder),
                            entity.getAttributeBaseValue(attributeHolder)));
        }
    }
    public void read(CompoundTag tag, LivingEntity entity){
        if(tag.contains("ascension_attributes")){
            readAttributes(tag.getList("ascension_attributes", Tag.TAG_COMPOUND));
        }else{
            createFreshAscensionAttributesFrom(entity);
        }
    }
    public void decode(RegistryFriendlyByteBuf buf){
        attributes.clear();
        for(int i = 0;i<buf.readInt();i++){
            AscensionAttributeInstance ascensionAttributeInstance = AscensionAttributeInstance.decode(buf,formData);
            attributes.put(ascensionAttributeInstance.getAscensionAttribute(),ascensionAttributeInstance);
        }
    }
}
