package net.thejadeproject.ascension.data_attachments.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.data_attachments.AscensionAttributeWrapper;
import org.jetbrains.annotations.Nullable;

public class AscensionAttributeWrapperProvider implements IAttachmentSerializer<CompoundTag, AscensionAttributeWrapper> {
    @Override
    public AscensionAttributeWrapper read(IAttachmentHolder holder, CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(holder instanceof LivingEntity entity){
            AscensionAttributeWrapper ascensionAttributeWrapper = new AscensionAttributeWrapper(entity);
            ascensionAttributeWrapper.loadNBTData(compoundTag,provider);
            return ascensionAttributeWrapper;
        }
        return null;
    }

    @Override
    public @Nullable CompoundTag write(AscensionAttributeWrapper ascensionAttributeWrapper, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        ascensionAttributeWrapper.saveNBTData(tag, provider);
        return tag;
    }
}
