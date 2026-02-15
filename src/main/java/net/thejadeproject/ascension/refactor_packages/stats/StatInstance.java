package net.thejadeproject.ascension.refactor_packages.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;

public class StatInstance implements IDataInstance {
    private final ResourceLocation statId;
    private final HashMap<ResourceLocation,StatModifier> modifiers = new HashMap<>();
    private final int baseValue;
    private int cachedValue;
    private int cachedBaseValue;
    public StatInstance(ResourceLocation statId, int baseValue) {
        this.statId = statId;
        this.baseValue = baseValue;
        this.cachedBaseValue = baseValue;
        this.cachedValue = baseValue;
    }
    public void calculateCachedValue(){

        double flatBase = 0;
        HashMap<ResourceLocation,Double> groupedBaseMultipliers = new HashMap<>();
        HashMap<ResourceLocation,Double> groupedFinalMultipliers = new HashMap<>();
        double flatTrueBase = 0;

        for(StatModifier modifier : modifiers.values()){
            if(modifier.getOperator() == StatModifier.Operator.ADD_BASE) flatBase += modifier.getValue();
            else if(modifier.getOperator() == StatModifier.Operator.TRUE_ADD_BASE) flatTrueBase += modifier.getValue();
            else if(modifier.getOperator() == StatModifier.Operator.MULTIPLY_BASE){
                groupedBaseMultipliers.put(modifier.getGroupId(),modifier.getValue());
            } else if (modifier.getOperator() == StatModifier.Operator.MULTIPLY_FINAL) {
                groupedFinalMultipliers.put(modifier.getGroupId(),modifier.getValue());
            }
        }
        double val = baseValue+flatTrueBase;
        cachedBaseValue = (int) Math.clamp(val,getStat().getMinValue(),getStat().getMaxValue());
        double totalBaseMultiplier = 1;
        for(Double mul : groupedBaseMultipliers.values()){
            totalBaseMultiplier *= (1+mul);
        }
        double totalFinalMultiplier = 1;
        for(Double mul : groupedFinalMultipliers.values()){
            totalFinalMultiplier *= (1+mul);
        }
        val = (val*totalBaseMultiplier+flatBase)*totalFinalMultiplier;
        cachedValue = (int)  Math.clamp(val,getStat().getMinValue(),getStat().getMaxValue());
    }

    public int getValue(){
        return cachedValue;
    }
    public int getBaseValue(){
        return cachedBaseValue;
    }
    public ResourceLocation getStatId(){return statId;}
    public Stat getStat(){return null;}


    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("stat_id",statId.toString());
        tag.putInt("base_value",baseValue);
        tag.putInt("cached_base_value",cachedBaseValue);
        tag.putInt("cached_value",cachedValue);
        ListTag modifierTag = new ListTag();
        for(StatModifier modifier : modifiers.values()){
            modifierTag.add(modifier.write());
        }
        tag.put("modifiers",modifierTag);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        ByteBufHelper.encodeString(buf,statId.toString());
        buf.writeInt(baseValue);
        buf.writeInt(cachedBaseValue);
        buf.writeInt(cachedValue);
        buf.writeInt(modifiers.size());
        for(StatModifier modifier:modifiers.values()){
            modifier.encode(buf);
        }
    }
    public static StatInstance read(CompoundTag tag){
        ResourceLocation statId = ResourceLocation.bySeparator(tag.getString("stat_id"),':');
        int base = tag.getInt("base_value");

        StatInstance instance = new StatInstance(statId,base);

        instance.cachedBaseValue = tag.getInt("cached_base_value");
        instance.cachedValue = tag.getInt("cached_value");
        ListTag modifiers = tag.getList("modifiers", Tag.TAG_COMPOUND);
        for(int i = 0;i<modifiers.size();i++){
            StatModifier modifier = StatModifier.read(modifiers.getCompound(i));
            instance.modifiers.put(modifier.getModifierId(),modifier);
        }
        return instance;
    }

    public static StatInstance decode(RegistryFriendlyByteBuf buf){
        ResourceLocation id = ByteBufHelper.readResourceLocation(buf);
        int base = buf.readInt();
        StatInstance instance = new StatInstance(id,base);
        instance.cachedBaseValue = buf.readInt();
        instance.cachedValue = buf.readInt();
        for(int i=0;i<buf.readInt();i++){
            StatModifier modifier = StatModifier.decode(buf);
            instance.modifiers.put(modifier.getModifierId(),modifier);
        }
        return instance;
    }
}
