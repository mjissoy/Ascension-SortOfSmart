package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

/*
    Registry object
    allows me to link an attribute to the ascension equivalent for easier access
    TODO add min max val
 */
public record AscensionAttribute(Holder<Attribute> attribute, Component displayName, Component description) {
    public static AscensionAttribute MAX_HEALTH = new AscensionAttribute(Attributes.MAX_HEALTH,Component.literal("Max HP"),Component.literal("the max amount of hp an entity has"));

    public static HashMap<Holder<Attribute>,AscensionAttribute> ASCENSION_ATTRIBUTES = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,MAX_HEALTH);
    }};


}
