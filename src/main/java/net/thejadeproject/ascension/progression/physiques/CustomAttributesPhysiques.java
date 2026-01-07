package net.thejadeproject.ascension.progression.physiques;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;

public class CustomAttributesPhysiques extends ModPhysiques{

    public static HashMap<Holder<Attribute>,Double> KITSUNE_BASE_MINOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 6.0);
        put(Attributes.ATTACK_DAMAGE, 1.2);
        put(Attributes.MOVEMENT_SPEED, 0.03);
        put(Attributes.JUMP_STRENGTH, 0.02);
        put(Attributes.STEP_HEIGHT, 0.015);
        put(Attributes.LUCK, 0.5); // Kitsune are naturally lucky
    }};

    public static HashMap<Holder<Attribute>,Double> KITSUNE_BASE_MAJOR_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 12.0);
        put(Attributes.ATTACK_DAMAGE, 2.2);
        put(Attributes.MOVEMENT_SPEED, 0.08);
        put(Attributes.JUMP_STRENGTH, 0.04);
        put(Attributes.STEP_HEIGHT, 0.03);
        put(Attributes.LUCK, 1.0);
    }};

    // Tail bonus stats - applied per tail
    public static HashMap<Holder<Attribute>,Double> KITSUNE_TAIL_BONUS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 1.5); // +1.5 HP per tail
        put(Attributes.ATTACK_DAMAGE, 0.15); // +0.15 damage per tail
        put(Attributes.MOVEMENT_SPEED, 0.005); // +0.005 speed per tail
        put(Attributes.LUCK, 0.1); // +0.1 luck per tail
    }};



}
