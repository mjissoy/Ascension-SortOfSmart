package net.thejadeproject.ascension;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static ModConfigSpec SPEC;

    public static class Common {
        public static ModConfigSpec.DoubleValue MINOR_REALM_MULTIPLIER;
        public static ModConfigSpec.IntValue MAJOR_REALM_MULTIPLIER;


        static {
            BUILDER.push("Multipliers");

            MINOR_REALM_MULTIPLIER = BUILDER
                    .comment(" The amount to increase stats between Minor Realms [Default: 30]")
                    .defineInRange("Minor_Cultivation_Stats_Multiplier", 30, 1f, Integer.MAX_VALUE);
        }



        public Common(ModConfigSpec.Builder builder) {

        }
    }

    static {
        new Common(BUILDER);
        SPEC = BUILDER.build();
    }

}
