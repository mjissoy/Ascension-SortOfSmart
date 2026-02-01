package net.thejadeproject.ascension.util;

import net.minecraft.nbt.CompoundTag;

public class NBTUtil {
    public static boolean getBooleanWithDefault(CompoundTag tag,String id, boolean defaultValue){
        if(tag.contains(id)) return tag.getBoolean(id);
        return defaultValue;
    }
    public static int getIntWithDefault(CompoundTag tag,String id, int defaultValue){
        if(tag.contains(id)) return tag.getInt(id);
        return defaultValue;
    }
    public static String getStringWithDefault(CompoundTag tag,String id, String  defaultValue){
        if(tag.contains(id)) return tag.getString(id);
        return defaultValue;
    }
}
