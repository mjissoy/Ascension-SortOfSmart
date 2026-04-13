package net.thejadeproject.ascension.refactor_packages.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.Charset;

public class ByteBufHelper {

    public static void encodeString(RegistryFriendlyByteBuf buf,String string){
        buf.writeInt(string.length());
        buf.writeCharSequence(string, Charset.defaultCharset());
    }
    public static String readString(RegistryFriendlyByteBuf buf){
        return (String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset());
    }
    public static ResourceLocation readResourceLocation(RegistryFriendlyByteBuf buf){
        return ResourceLocation.bySeparator(readString(buf),':');
    }
}
