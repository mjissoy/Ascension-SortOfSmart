package net.thejadeproject.ascension.items.technique_manuals;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.util.ModAttachments;

public class GenericTechniqueManual extends Item {
    public ResourceLocation technique;

    public GenericTechniqueManual(Item.Properties properties,ResourceLocation techniqueLocation){
        super(properties);
        technique = techniqueLocation;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        
        ItemStack itemstack = player.getItemInHand(usedHand);
        itemstack.shrink(1);
        if(level.isClientSide()) return InteractionResultHolder.fail(itemstack);
        if(learnTechnique(player)) return InteractionResultHolder.success(itemstack);
        return InteractionResultHolder.fail(itemstack);
    }

    public ITechnique getTechnique() {
        return AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
    }

    public boolean learnTechnique(Player player){
        if(!player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getTechnique().getPath()).technique.equals("ascension:none"))return false;
        CultivationData.PathData data = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(getTechnique().getPath());
        player.getData(ModAttachments.PLAYER_DATA).getCultivationData().setPathTechnique(
                getTechnique().getPath(),
                technique.toString());

        player.displayClientMessage(
                Component.translatable(
                        "ascension.learnt_technique",
                        getTechnique().getDisplayTitle()
                ),
                true
        );

        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPathDataPayload(data.pathId,data.majorRealm,data.minorRealm,data.pathProgress,data.technique,data.stabilityCultivationTicks));
        return true;
    }
}