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
            4));
    public static final Holder<Attribute> PLAYER_QI_INSTANCE = ATTRIBUTES.register("player_qi", () -> new RangedAttribute(
            // The translation key to use.
            "attributes.ascension.player_qi",
            // The default value.
            100,
            // Min and max values.
            0,
            1000000000));

    public static void register(IEventBus modEventBus){
        ATTRIBUTES.register(modEventBus);
    }
}
