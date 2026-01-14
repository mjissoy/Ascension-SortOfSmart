package net.thejadeproject.ascension.data_attachments.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.thejadeproject.ascension.data_attachments.attachments.PhysiqueAttachment;
import org.jetbrains.annotations.Nullable;

public class PhysiqueProvider implements IAttachmentSerializer<CompoundTag, PhysiqueAttachment> {
    @Override
    public PhysiqueAttachment read(IAttachmentHolder holder, CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(holder instanceof ServerPlayer player){
            PhysiqueAttachment physiqueAttachment = new PhysiqueAttachment(player);
            physiqueAttachment.loadNBTData(compoundTag,provider);
            return physiqueAttachment;
        }
        return null;
    }

    @Override
    public @Nullable CompoundTag write(PhysiqueAttachment physiqueAttachment, HolderLookup.Provider provider) {
        var tag = new CompoundTag();
        physiqueAttachment.saveNBTData(tag, provider);
        return tag;
    }
}
