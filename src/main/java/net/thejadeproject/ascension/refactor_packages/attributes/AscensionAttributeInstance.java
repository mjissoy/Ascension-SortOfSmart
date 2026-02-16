package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.attributes.stat_container.AttributeStatContainer;
import net.thejadeproject.ascension.refactor_packages.attributes.stat_container.StatContainerModifier;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.modifiers.AscensionModifier;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.events.StatChangedEvent;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.HashMap;
/*
    TODO add modifiers unique to this stat instance
 */
public class AscensionAttributeInstance implements IDataInstance {
    private final IEntityFormData formData; //so we can access stat sheet
    private final AscensionAttribute ascensionAttribute;
    private final double baseValue;

    private double cachedValue;
    private double suppressionValue = 1;
    private final HashMap<Stat, AttributeStatContainer> statContainers = new HashMap<>();
    private final HashMap<ResourceLocation, AscensionModifier> modifiers = new HashMap<>();
    public AscensionAttributeInstance(IEntityFormData formData, AscensionAttribute ascensionAttribute, double baseValue){
        this.formData = formData;
        this.ascensionAttribute = ascensionAttribute;
        this.baseValue = baseValue;

    }

    public AscensionAttribute getAscensionAttribute(){
        return ascensionAttribute;
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
        double val = baseValue;
        for(AttributeStatContainer statContainer : statContainers.values()){
            val += statContainer.getValue();
        }
        cachedValue = AscensionModifier.calculateValue(modifiers.values(),val);
        cachedValue = cachedValue*suppressionValue;

    }
    public void setSuppression(double val){
        suppressionValue = Math.clamp(val,0,1);
        calculateCachedValue();
    }

    public double getSuppression() {
        return suppressionValue;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public double getValue() {
        return cachedValue;
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("attribute", BuiltInRegistries.ATTRIBUTE.getKey(ascensionAttribute.attribute().value()).toString());
        tag.putDouble("cached_value",getValue());
        tag.putDouble("suppression",getSuppression());
        ListTag containers = new ListTag();
        for(AttributeStatContainer statContainer : statContainers.values()){
            containers.add(statContainer.write());
        }
        ListTag modifiers = new ListTag();
        for(AscensionModifier modifier :this.modifiers.values()){
            modifiers.add(modifier.write());
        }
        tag.put("stat_containers",containers);
        tag.put("modifiers",modifiers);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        ByteBufHelper.encodeString(buf,BuiltInRegistries.ATTRIBUTE.getKey(ascensionAttribute.attribute().value()).toString());
        buf.writeDouble(cachedValue);
        buf.writeDouble(suppressionValue);
    }
    public static AscensionAttributeInstance read(CompoundTag tag,IEntityFormData formData){
        return null;//TODO
    }
    public static AscensionAttributeInstance decode(RegistryFriendlyByteBuf buf,IEntityFormData formData){
        return null;//TODO
    }
}
