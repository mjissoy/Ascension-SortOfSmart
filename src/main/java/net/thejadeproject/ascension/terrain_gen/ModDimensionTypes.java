package net.thejadeproject.ascension.terrain_gen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class ModDimensionTypes {

    // registers the custom overworld dimension type with this mod's height and world rules.
    public static void bootstrap(BootstrapContext<DimensionType> context) {
        context.register(
                ModTerrainGenKeys.ASCENSION_OVERWORLD_TYPE,
                new DimensionType(
                        OptionalLong.empty(),
                        true,
                        false,
                        false,
                        true,
                        1.0,
                        true,
                        false,
                        -64,
                        512,
                        512,
                        BlockTags.INFINIBURN_OVERWORLD,
                        BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                        0.0f,
                        new DimensionType.MonsterSettings(
                                false,
                                true,
                                UniformInt.of(0, 7),
                                0
                        )
                )
        );
    }

}
