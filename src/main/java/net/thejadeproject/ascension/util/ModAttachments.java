package net.thejadeproject.ascension.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.cultivation.player.providers.PlayerDataProvider;
import net.thejadeproject.ascension.cultivation.player.providers.PlayerSkillDataProvider;
import net.thejadeproject.ascension.cultivation.player.sync_handler.PlayerSkillDataSyncHandler;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AscensionCraft.MOD_ID);
    public static final Supplier<AttachmentType<Double>> MOVEMENT_SPEED = ATTACHMENT_TYPES.register(
            "movement_speed", () -> AttachmentType.builder(() -> 0.1).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> ATTACK_DAMAGE = ATTACHMENT_TYPES.register(
            "attack_damage", () -> AttachmentType.builder(() -> 1.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> JUMP_HEIGHT = ATTACHMENT_TYPES.register(
            "jump_height", () -> AttachmentType.builder(() -> 0.42).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> MAX_HEALTH = ATTACHMENT_TYPES.register(
            "max_health", () -> AttachmentType.builder(() -> 20.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> STEP_HEIGHT = ATTACHMENT_TYPES.register(
            "step_height", () -> AttachmentType.builder(() -> 0.6).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> SAFE_FALL_DISTANCE = ATTACHMENT_TYPES.register(
            "safe_fall_distance", () -> AttachmentType.builder(() -> 3.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> ARMOR = ATTACHMENT_TYPES.register(
            "armor", () -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> OXYGEN_BONUS = ATTACHMENT_TYPES.register(
            "oxygen_bonus", () -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<Double>> WATER_MOVEMENT_EFFICIENCY = ATTACHMENT_TYPES.register(
            "water_movement_efficiency", () -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).copyOnDeath().build()
    );
    public static final Supplier<AttachmentType<String>> PHYSIQUE = ATTACHMENT_TYPES.register(
            "physique", () -> AttachmentType.builder(() -> "ascension:empty_vessel")
                    .serialize(Codec.STRING)
                    .sync(ByteBufCodecs.STRING_UTF8)
                    .copyOnDeath().build()
    );


    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerData>> PLAYER_DATA = ATTACHMENT_TYPES.register("player_data",
            () -> AttachmentType.builder((holder) -> holder instanceof Player player ? new PlayerData(player):null)
                    .serialize(new PlayerDataProvider()).copyOnDeath().build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSkillData>> PLAYER_SKILL_DATA = ATTACHMENT_TYPES.register("player_skill_data",
            () -> AttachmentType.builder((holder) -> holder instanceof Player player ? new PlayerSkillData(player):null)
                    .serialize(new PlayerSkillDataProvider())
                    .sync(new PlayerSkillDataSyncHandler())
                    .copyOnDeath().build());

    public static void register(IEventBus modEventBus){
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
