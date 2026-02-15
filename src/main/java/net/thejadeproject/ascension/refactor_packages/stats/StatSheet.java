package net.thejadeproject.ascension.refactor_packages.stats;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;

public class StatSheet implements IDataInstance {

    private HashMap<Stat,StatInstance> sheetStats = new HashMap<>();


    public StatInstance getStatInstance(Stat stat){
        //TODO
        return null;
    }



    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag stats = new ListTag();
        for(StatInstance instance : sheetStats.values()){
            stats.add(instance.write());
        }
        tag.put("stats",stats);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(sheetStats.size());
        for(StatInstance instance : sheetStats.values()){
            instance.encode(buf);
        }
    }
    public void read(CompoundTag tag){
        ListTag statsTag = tag.getList("stats", Tag.TAG_COMPOUND);
        for(int i=0;i<statsTag.size();i++){
            StatInstance instance = StatInstance.read(statsTag.getCompound(i));
            sheetStats.put(instance.getStat(),instance);
        }
    }
    public void decode(RegistryFriendlyByteBuf buf){
        sheetStats.clear();
        for(int i =0;i<buf.readInt();i++){
            StatInstance instance = StatInstance.decode(buf);
            sheetStats.put(instance.getStat(),instance);
        }
    }
}
