package net.thejadeproject.ascension.data_attachments.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.progression.physiques.data.IPhysiqueData;
import net.thejadeproject.ascension.registries.AscensionRegistries;

public class PhysiqueAttachment {

    public final Player player;

    private ResourceLocation physique = ResourceLocation.fromNamespaceAndPath(
            AscensionCraft.MOD_ID,
            "empty_vessel"
    );
    private IPhysiqueData physiqueData;
    public PhysiqueAttachment(Player player) {
        this.player = player;
    }

    public IPhysiqueData getPhysiqueData(){return this.physiqueData;}
    public ResourceLocation getPhysiqueId(){return this.physique;}
    public IPhysique getPhysique(){
        return AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physique);
    }
    //============ Setters ============
    //TODO send update packets through setters
    //TODO send physique change events through this

    public void setPhysique(String physique){
        this.physique = ResourceLocation.bySeparator(physique,':');
    }
    public void setPhysique(ResourceLocation physique){
        this.physique = physique;
    }

    public void setPhysiqueData(IPhysiqueData physiqueData){
        this.physiqueData = physiqueData;
    }
    public void setPhysiqueData(CompoundTag tag){
        setPhysiqueData(getPhysique().getPhysiqueDataInstance(tag));
    }
    public void setPhysiqueData(RegistryFriendlyByteBuf buf){
        setPhysiqueData(getPhysique().getPhysiqueDataInstance(buf));
    }
    //============= NBT Data =============

    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){
        setPhysique(tag.getString("physique_id"));
        if(tag.hasUUID("physique_data")) setPhysiqueData(tag.getCompound("physique_data"));
    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        tag.putString("physique_id",getPhysiqueId().toString());
        if(getPhysiqueData() != null) tag.put("physique_data",getPhysiqueData().writeData());
    }
}
