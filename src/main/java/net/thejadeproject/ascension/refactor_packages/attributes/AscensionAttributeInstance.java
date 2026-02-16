package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.attributes.stat_container.AttributeStatContainer;
import net.thejadeproject.ascension.refactor_packages.attributes.stat_container.StatContainerModifier;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.modifiers.AscensionModifier;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.events.StatChangedEvent;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.HashMap;
/*
    TODO add modifiers unique to this stat instance
 */
public class AscensionAttributeInstance implements IDataInstance {
    private final IEntityFormData formData; //so we can access stat sheet
    private final ResourceLocation ascensionAttributeId;
    private final double baseValue;
    private double cachedValue;
    private double suppressionValue = 1;
    private final HashMap<Stat, AttributeStatContainer> statContainers = new HashMap<>();
    private final HashMap<ResourceLocation, AscensionModifier> modifiers = new HashMap<>();
    public AscensionAttributeInstance(IEntityFormData formData, ResourceLocation ascensionAttributeId, double baseValue){
        this.formData = formData;
        this.ascensionAttributeId = ascensionAttributeId;
        this.baseValue = baseValue;

    }

    public AscensionAttribute getAscensionAttribute(){
        return null;//TODO
    }

    public void addStatContainerModifier(Stat stat, StatContainerModifier modifier){
        statContainers.computeIfAbsent(stat,key->new AttributeStatContainer(formData.getStatSheet().getStatInstance(stat)));
        statContainers.get(stat).addModifier(modifier);
        calculateCachedValue();
    }
    public void addAttributeModifier(AscensionModifier modifier){
        modifiers.put(modifier.getModifierId(),modifier);
        calculateCachedValue();
    }

    public void onStatChanged(StatChangedEvent event){
        for(AttributeStatContainer statContainer : statContainers.values()){
            if(event.getStat() == statContainer.getStat()) statContainer.onStatChanged(event);
        }
        calculateCachedValue();
    }

    private void calculateCachedValue(){
        //TODO
    }

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {

    }
    public static AscensionAttributeInstance read(CompoundTag tag,IEntityFormData formData){
        return null;//TODO
    }
    public static AscensionAttributeInstance decode(RegistryFriendlyByteBuf buf,IEntityFormData formData){
        return null;//TODO
    }
}
