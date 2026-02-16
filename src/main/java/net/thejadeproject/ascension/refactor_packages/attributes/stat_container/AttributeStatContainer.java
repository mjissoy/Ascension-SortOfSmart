package net.thejadeproject.ascension.refactor_packages.attributes.stat_container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.player_data.PlayerData;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.StatInstance;
import net.thejadeproject.ascension.refactor_packages.stats.events.StatChangedEvent;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    TODO update to listen to Stat changed so it can re calculate the cached value;
 */
public class AttributeStatContainer implements IDataInstance {
    private final StatInstance statInstance;
    private ResourceLocation stat;
    private final HashMap<ResourceLocation,StatContainerModifier> modifiers = new HashMap<>();
    private double cachedScaling;
    private double cachedValue; // does not yet include the actual stat
    public AttributeStatContainer(StatInstance statInstance){
        this.statInstance = statInstance;
        this.stat=statInstance.getStatId();

    }
    public void addModifier(StatContainerModifier modifier){
        modifiers.put(modifier.getId(),modifier);
        calculateCachedValue();
    }
    public void onStatChanged(StatChangedEvent event){
        calculateCachedValue();
    }
    public Stat getStat(){
        return statInstance.getStat();
    }
    private void calculateCachedValue(){
        double basePercentage = 0;
        double effectiveness = 1;
        for(StatContainerModifier modifier: modifiers.values()){
            if(modifier.getOperator() == StatContainerModifier.Operator.PERCENTAGE) basePercentage += modifier.getValue();
            else effectiveness += modifier.getValue();
        }

        cachedScaling = basePercentage*effectiveness;

        cachedValue = statInstance.getValue() * (basePercentage*effectiveness);
    }

    public double getScaling(){return cachedScaling;}
    public double getValue(){return cachedValue;}

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("stat_id",stat.toString());
        tag.putDouble("cached_scaling",getScaling());
        tag.putDouble("cached_value",getValue());
        ListTag modifiersTag = new ListTag();
        for(StatContainerModifier containerModifier : modifiers.values()){
            modifiersTag.add(containerModifier.write());
        }
        tag.put("modifiers",modifiersTag);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        ByteBufHelper.encodeString(buf,stat.toString());
        buf.writeDouble(cachedScaling);
        buf.writeDouble(cachedValue);
        buf.writeInt(modifiers.size());
        for(StatContainerModifier containerModifier : modifiers.values()){
            containerModifier.encode(buf);
        }
    }
    public static AttributeStatContainer read(CompoundTag tag,StatInstance statInstance){
        ResourceLocation id = ResourceLocation.bySeparator(tag.getString("stat_id"),':');
        double cachedScaling = tag.getDouble("cached_scaling");
        double cachedValue = tag.getDouble("cached_value");
        AttributeStatContainer statContainer = new AttributeStatContainer(statInstance);
        statContainer.cachedValue = cachedValue;
        statContainer.cachedScaling = cachedScaling;
        ListTag modifiers = tag.getList("modifiers", Tag.TAG_COMPOUND);
        for(int i = 0;i<modifiers.size();i++){
            StatContainerModifier modifier = StatContainerModifier.read(tag);
            statContainer.modifiers.put(modifier.getId(),modifier);
        }
        return statContainer;
    }
    public static AttributeStatContainer decode(RegistryFriendlyByteBuf buf,StatInstance statInstance){
        ResourceLocation id = ByteBufHelper.readResourceLocation(buf);
        double cachedScaling = buf.readDouble();
        double cachedValue =  buf.readDouble();
        AttributeStatContainer statContainer = new AttributeStatContainer(statInstance);
        statContainer.cachedScaling = cachedScaling;
        statContainer.cachedValue = cachedValue;
        for(int i = 0;i<buf.readInt();i++){
            StatContainerModifier modifier = StatContainerModifier.decode(buf);
            statContainer.modifiers.put(modifier.getId(),modifier);
        }
        return statContainer;
    }
}
