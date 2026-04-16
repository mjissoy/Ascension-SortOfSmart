package net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.physique;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.path_data.SyncPathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;

public record SyncPhysique(ResourceLocation form, ResourceLocation physique, IPhysiqueData physiqueData) implements CustomPacketPayload {

    public static final Type<SyncPhysique> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"sync_physique"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPhysique> STREAM_CODEC =
            StreamCodec.of(SyncPhysique::encode, SyncPhysique::decode);

    public static void encode(RegistryFriendlyByteBuf buf,SyncPhysique packet){
        ByteBufHelper.encodeString(buf,packet.form.toString());
        buf.writeBoolean(packet.physique != null);
        if(packet.physique != null) ByteBufHelper.encodeString(buf,packet.physique.toString());
        if(packet.physiqueData != null) packet.physiqueData.encode(buf);
    }
    public static SyncPhysique decode(RegistryFriendlyByteBuf buf){
        ResourceLocation form = ByteBufHelper.readResourceLocation(buf);
        ResourceLocation physique = null;
        IPhysiqueData physiqueData = null;
        if(buf.readBoolean()){
            physique = ByteBufHelper.readResourceLocation(buf);
            physiqueData = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physique).fromNetwork(buf);
        }
        return new SyncPhysique(form,physique,physiqueData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncPhysique payload, IPayloadContext context) {
        context.enqueueWork(()->{
            IEntityData entityData = context.player().getData(ModAttachments.ENTITY_DATA);
            IEntityFormData formData = entityData.getEntityFormData(payload.form);
            if (formData == null) return;

            formData.setPhysique(payload.physique,payload.physiqueData);
            entityData.setPhysiqueForm(payload.form);


        });
    }

}