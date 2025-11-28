package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.Config;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncGeneratedPhysique;
import net.thejadeproject.ascension.util.ModAttachments;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public record TriggerGeneratePhysique(String physiquePath,int numberOfExtra) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TriggerGeneratePhysique> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "trigger_generate_physique"));
    public static final StreamCodec<ByteBuf, TriggerGeneratePhysique> STREAM_CODEC = StreamCodec.composite(

            ByteBufCodecs.STRING_UTF8,
            TriggerGeneratePhysique::physiquePath,
            ByteBufCodecs.INT,
            TriggerGeneratePhysique::numberOfExtra,
            TriggerGeneratePhysique::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(TriggerGeneratePhysique payload, IPayloadContext context) {
        
        List<? extends String> physiqueList = new ArrayList<>();

        if(payload.physiquePath.equals("Essence")) physiqueList = new ArrayList<>(Config.CULTIVATION.AVAILABLE_STARTING_ESSENCE_PHYSIQUE.get());
        else if(payload.physiquePath.equals("Body")) physiqueList =new ArrayList<>(Config.CULTIVATION.AVAILABLE_STARTING_BODY_PHYSIQUE.get());
        else if(payload.physiquePath.equals("Intent")) physiqueList = new ArrayList<>(Config.CULTIVATION.AVAILABLE_STARTING_INTENT_PHYSIQUE.get());
        
        
        int genPhysique = ThreadLocalRandom.current().nextInt(0,physiqueList.size());
        String generatedPhysique = physiqueList.remove(genPhysique);
        String[] extra = new String[payload.numberOfExtra];


        for(int i = 0;i<payload.numberOfExtra;i++){
            if(physiqueList.isEmpty()) continue;
            int index = ThreadLocalRandom.current().nextInt(0,physiqueList.size());
            extra[i] = physiqueList.remove(index);
        }

        context.player().setData(ModAttachments.PHYSIQUE,generatedPhysique);
        PhysiqueChangeEvent event = new PhysiqueChangeEvent(context.player(), "ascension:empty_vessel",generatedPhysique);
        NeoForge.EVENT_BUS.post(event);
        PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new SyncGeneratedPhysique(
                generatedPhysique,
                String.join(";",extra).getBytes(StandardCharsets.UTF_8)
        ));
    }
}