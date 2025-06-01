package net.thejadeproject.ascension.cultivation.realms;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.thejadeproject.ascension.AscensionCraft;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Realm {
    public final Types type;
    public final String key;
    public final @Nullable List<Holder<SubRealm>> subRealms;
    public Multimap<Attribute, AttributeModifier> attributes;

    public Realm(Types type, String key, @Nullable List<Holder<SubRealm>> subRealms) {
        this.type = type;
        this.key = key;
        this.subRealms = subRealms;
    }

    public Realm(Types type, String key){
        this.type = type;
        this.key = key;
        this.subRealms = null;
    }

    @SafeVarargs
    public Realm(Types type, String key, Holder<SubRealm>... subRealms) {
        this.type = type;
        this.key = key;
        this.subRealms = Arrays.stream(subRealms).toList();
    }

    public Multimap<Attribute, AttributeModifier> getBonusAttribute(){
        return attributes;
    }

    public enum Types {
        HEAVEN, // fate
        EARTH, // history
        HEART // life/emotions
    }

    public Component getName(){
        return Component.translatable("cultivation."+ AscensionCraft.MOD_ID + ".realm."+key);
    }
}
