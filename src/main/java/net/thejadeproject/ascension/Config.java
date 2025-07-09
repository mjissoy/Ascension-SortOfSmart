package net.thejadeproject.ascension;

import io.netty.util.Attribute;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.thejadeproject.ascension.cultivation.CultivationSystem;


public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;

    public static class Common {
        public static ModConfigSpec.DoubleValue PROGRESS_SPEED;
        public static ModConfigSpec.DoubleValue MINOR_REALM_MULTIPLIER;
        public static ModConfigSpec.DoubleValue MAJOR_REALM_MULTIPLIER;
        public static ModConfigSpec.IntValue FLIGHT_REALM;
        public static ModConfigSpec.DoubleValue MAX_SPEED_MULT;


        static {
            BUILDER.push("Multipliers");
                BUILDER.push("CultivationMultipliers");

                    BUILDER.comment(" The amount to increase cultivation progress when cultivating [Default: 30]");
                    PROGRESS_SPEED = BUILDER.defineInRange("Cultivation_Speed", 1, 1, Double.MAX_VALUE);

                    BUILDER.comment(" The amount to increase stats between Minor Realms [Default: 30]");
                    MINOR_REALM_MULTIPLIER = BUILDER.defineInRange("Minor_Cultivation_Stats_Multiplier", 30, 1f, Float.MAX_VALUE);

                    BUILDER.comment(" The amount to increase stats between Major Realms [Default: 30]");
                    MAJOR_REALM_MULTIPLIER = BUILDER.defineInRange("Major_Cultivation_Stats_Multiplier", 30, 1f, Double.MAX_VALUE);

                    BUILDER.comment("Which Major Realm should give Creative Flight 0 = Mortal, 10 = Golden Immortal");
                    FLIGHT_REALM = BUILDER.defineInRange("Flight_Realm", 3, 0, CultivationSystem.RealmAmount-1);
                BUILDER.pop();

                BUILDER.push("AttributeMultipliers");

                    BUILDER.comment(" The multiplier amount at which speed stops increasing [Default: 30]");
                    MAX_SPEED_MULT = BUILDER.defineInRange("Speed_Multiplier_Max", 0.5, 1f, Double.MAX_VALUE);
        }



        public Common(ModConfigSpec.Builder builder) {

        }
    }

    static {
        new Common(BUILDER);
        SPEC = BUILDER.build();
    }

}
