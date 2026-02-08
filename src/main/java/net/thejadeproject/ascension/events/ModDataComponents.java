package net.thejadeproject.ascension.events;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.karma.KarmaData;

import java.util.List;
import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AscensionCraft.MOD_ID);

    public static final Supplier<DataComponentType<String>> PHYSIQUE_ID = DATA_COMPONENTS.register(
            "physique_id",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final Supplier<DataComponentType<Integer>> PURITY = DATA_COMPONENTS.register(
            "purity",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<DataComponentType<Boolean>> PERMANENT = DATA_COMPONENTS.register(
            "permanent",
            () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );

    public static final Supplier<DataComponentType<Integer>> RECHARGE_PROGRESS = DATA_COMPONENTS.register(
            "recharge_progress",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<DataComponentType<Integer>> KARMA_VALUE = DATA_COMPONENTS.register(
            "karma_value",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );

    public static final Supplier<DataComponentType<String>> KARMA_RANK = DATA_COMPONENTS.register(
            "karma_rank",
            () -> DataComponentType.<String>builder()
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build()
    );

    public static final Supplier<DataComponentType<Boolean>> KARMA_INITIALIZED = DATA_COMPONENTS.register(
            "karma_initialized",
            () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build()
    );


    //Spirit Sealing Ring - Stores captured entity data
    public static final Supplier<DataComponentType<SealedEntityData>> SEALED_ENTITY = DATA_COMPONENTS.register(
            "sealed_entity",
            () -> DataComponentType.<SealedEntityData>builder()
                    .persistent(SealedEntityData.CODEC)
                    .networkSynchronized(SealedEntityData.STREAM_CODEC)
                    .cacheEncoding()
                    .build()
    );



    //Spatial Ring Stuff

    public static final DataComponentType<Integer> EXTRA_ROWS = DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .cacheEncoding()
            .build();

    public static final DataComponentType<Integer> STACK_MULTIPLIER = DataComponentType.<Integer>builder()
            .persistent(Codec.INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .cacheEncoding()
            .build();

    public static final DataComponentType<Boolean> AUTO_PICKUP = DataComponentType.<Boolean>builder()
            .persistent(Codec.BOOL)
            .networkSynchronized(ByteBufCodecs.BOOL)
            .cacheEncoding()
            .build();

    public static final Supplier<DataComponentType<List<ItemStack>>> UPGRADE_ITEMS = DATA_COMPONENTS.register(
            "upgrade_items",
            () -> DataComponentType.<List<ItemStack>>builder()
                    .persistent(ItemStack.CODEC.listOf())
                    .networkSynchronized(ByteBufCodecs.fromCodec(ItemStack.CODEC.listOf()))
                    .build()
    );




    //Physiques
    // Add this with the other data components
    public static final Supplier<DataComponentType<Integer>> TAIL_COUNT = DATA_COMPONENTS.register(
            "tail_count",
            () -> DataComponentType.<Integer>builder()
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build()
    );




    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}