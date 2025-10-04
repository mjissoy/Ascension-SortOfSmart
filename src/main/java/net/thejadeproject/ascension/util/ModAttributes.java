package net.thejadeproject.ascension.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
            BuiltInRegistries.ATTRIBUTE, AscensionCraft.MOD_ID);
    public static final Holder<Attribute> MAX_CASTING_INSTANCES = ATTRIBUTES.register("max_casting_instances", () -> new RangedAttribute(
            // The translation key to use.
            "attributes.ascension.max_casting_instances",
            // The default value.
            0,
            // Min and max values.
            0,
            10));


    public static void register(IEventBus modEventBus){
        ATTRIBUTES.register(modEventBus);
    }
}
