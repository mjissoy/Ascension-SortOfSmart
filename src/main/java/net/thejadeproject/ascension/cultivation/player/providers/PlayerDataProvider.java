package net.thejadeproject.ascension.cultivation.player.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import org.jetbrains.annotations.Nullable;

public class PlayerDataProvider implements IAttachmentSerializer<CompoundTag, PlayerData> {
    @Override
    public PlayerData read(IAttachmentHolder holder, CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(holder instanceof ServerPlayer player){
            PlayerData playerData = new PlayerData(player);
            playerData.loadNBTData(compoundTag,provider);
            return playerData;
        }
        return null;
    }

    @Override
    public @Nullable CompoundTag write(PlayerData playerData, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        playerData.saveNBTData(tag, provider);
        return tag;
    }
}
