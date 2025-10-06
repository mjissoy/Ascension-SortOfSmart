package net.thejadeproject.ascension.cultivation.player.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import org.jetbrains.annotations.Nullable;

public class PlayerSkillDataProvider  implements IAttachmentSerializer<CompoundTag, PlayerSkillData> {
    @Override
    public PlayerSkillData read(IAttachmentHolder holder, CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(holder instanceof ServerPlayer player){
            PlayerSkillData playerData = new PlayerSkillData(player);
            playerData.loadSkillNBTData(compoundTag);
            return playerData;
        }
        return null;
    }

    @Override
    public @Nullable CompoundTag write(PlayerSkillData playerData, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        playerData.writeSkillNBTData(tag);
        return tag;
    }
}
