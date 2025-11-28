package net.thejadeproject.ascension.util;

import com.mojang.serialization.Codec;
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
    public static final Supplier<AttachmentType<String>> PHYSIQUE = ATTACHMENT_TYPES.register(
            "physique", () -> AttachmentType.builder(() -> "ascension:empty_vessel").serialize(Codec.STRING).copyOnDeath().build()
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
