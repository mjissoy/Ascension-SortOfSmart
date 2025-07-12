package net.thejadeproject.ascension.util;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AscensionCraft.MOD_ID);
    public static final Supplier<AttachmentType<Double>> MOVEMENT_SPEED = ATTACHMENT_TYPES.register(
            "movement_speed", () -> AttachmentType.builder(() -> 0.1).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<String>> PHYSIQUE = ATTACHMENT_TYPES.register(
            "physique", () -> AttachmentType.builder(() -> "heavens_forgotten").serialize(Codec.STRING).copyOnDeath().build()
    );

    public static void register(IEventBus modEventBus){
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
