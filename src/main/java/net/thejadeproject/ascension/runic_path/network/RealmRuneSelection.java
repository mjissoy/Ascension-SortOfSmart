package net.thejadeproject.ascension.runic_path.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record RealmRuneSelection(int majorRealm, List<ResourceLocation> runes) {

    public static final StreamCodec<RegistryFriendlyByteBuf, RealmRuneSelection> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    RealmRuneSelection::majorRealm,
                    ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    RealmRuneSelection::runes,
                    RealmRuneSelection::new
            );
}