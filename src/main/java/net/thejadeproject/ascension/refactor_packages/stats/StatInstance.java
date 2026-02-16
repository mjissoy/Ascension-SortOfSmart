package net.thejadeproject.ascension.refactor_packages.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.modifiers.AscensionModifier;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.HashMap;

public class StatInstance implements IDataInstance {
    private final ResourceLocation statId;
    private final HashMap<ResourceLocation, AscensionModifier> modifiers = new HashMap<>();
    private final int baseValue;
    private int cachedValue;
    private int cachedBaseValue;
    public StatInstance(ResourceLocation statId, int baseValue) {
        this.statId = statId;
        this.baseValue = baseValue;
        this.cachedBaseValue = baseValue;
        this.cachedValue = baseValue;
    }
    private void calculateCachedValue(){
        double val = AscensionModifier.calculateValue(modifiers.values(),baseValue);
        cachedValue = (int)  Math.clamp(val,getStat().getMinValue(),getStat().getMaxValue());
    }
    private void calculateCachedBaseValue(){
        double val = AscensionModifier.calculateBaseValue(modifiers.values(),baseValue);
        cachedBaseValue = (int)  Math.clamp(val,getStat().getMinValue(),getStat().getMaxValue());
    }
    public int getValue(){
        return cachedValue;
    }
    public int getBaseValue(){
        return cachedBaseValue;
    }
    public ResourceLocation getStatId(){return statId;}
    public Stat getStat(){return AscensionRegistries.Stats.STATS_REGISTRY.get(statId);}

    public void addModifier(AscensionModifier modifier){
        modifiers.put(modifier.getModifierId(),modifier);
        if(modifier.getOperator() == AscensionModifier.Operator.TRUE_ADD_BASE) calculateCachedBaseValue();
        calculateCachedValue();
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("stat_id",statId.toString());
        tag.putInt("base_value",baseValue);
        tag.putInt("cached_base_value",cachedBaseValue);
        tag.putInt("cached_value",cachedValue);
        ListTag modifierTag = new ListTag();
        for(AscensionModifier modifier : modifiers.values()){
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
        for(AscensionModifier modifier:modifiers.values()){
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
            AscensionModifier modifier = AscensionModifier.read(modifiers.getCompound(i));
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
            AscensionModifier modifier = AscensionModifier.decode(buf);
            instance.modifiers.put(modifier.getModifierId(),modifier);
        }
        return instance;
    }
}
