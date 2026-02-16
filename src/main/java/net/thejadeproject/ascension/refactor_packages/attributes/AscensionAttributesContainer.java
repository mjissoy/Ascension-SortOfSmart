package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
        return attributes.get(AscensionAttribute.ASCENSION_ATTRIBUTES.get(vanillaAttribute));
    }


    public void onStatChanged(StatChangedEvent event){
        for(AscensionAttributeInstance instance : attributes.values()){
            instance.onStatChanged(event);
        }
    }

    //TODO

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {

    }
    public void read(CompoundTag tag){}
    public void decode(CompoundTag tag){
        attributes.clear();
    }
}
