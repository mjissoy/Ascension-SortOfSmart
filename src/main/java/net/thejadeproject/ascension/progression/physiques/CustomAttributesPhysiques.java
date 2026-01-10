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

    public static HashMap<Holder<Attribute>,Double> GENERIC_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,5.0);
        put(Attributes.ATTACK_DAMAGE,1.0);
        put(Attributes.MOVEMENT_SPEED,0.01);
        put(Attributes.JUMP_STRENGTH,0.01);
        put(Attributes.STEP_HEIGHT,0.01);

    }};
    public static HashMap<Holder<Attribute>,Double> ENHANCED_MINOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);

    }};

    public static HashMap<Holder<Attribute>,Double> GENERIC_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,2.0);
        put(Attributes.MOVEMENT_SPEED,0.5);
        put(Attributes.JUMP_STRENGTH,0.02);
        put(Attributes.STEP_HEIGHT,0.02);

    }};
    public static HashMap<Holder<Attribute>,Double> ENHANCED_MAJOR_REALM_STATS_1 = new HashMap<>(){{
        put(Attributes.MAX_HEALTH,10.0);
        put(Attributes.ATTACK_DAMAGE,1.8);
        put(Attributes.MOVEMENT_SPEED,0.2);
        put(Attributes.JUMP_STRENGTH,0.04);
        put(Attributes.STEP_HEIGHT,0.04);

    }};

    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MINOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 20.0);  // High HP
        put(Attributes.ATTACK_DAMAGE, 2.5); // High damage
        put(Attributes.MOVEMENT_SPEED, 0.01); // Slow base, but compensated by abilities
        put(Attributes.JUMP_STRENGTH, 0.1); // Better jump
        put(Attributes.KNOCKBACK_RESISTANCE, 0.5); // Can't be knocked back easily
        put(Attributes.STEP_HEIGHT, 0.02);
    }};

    public static HashMap<Holder<Attribute>,Double> STONE_MONKEY_MAJOR_REALM_STATS = new HashMap<>(){{
        put(Attributes.MAX_HEALTH, 30.0);
        put(Attributes.ATTACK_DAMAGE, 4.0);
        put(Attributes.MOVEMENT_SPEED, 0.02);
        put(Attributes.JUMP_STRENGTH, 0.2);
        put(Attributes.KNOCKBACK_RESISTANCE, 1.0); // Complete knockback immunity at high realms
        put(Attributes.STEP_HEIGHT, 0.1);
    }};



}
