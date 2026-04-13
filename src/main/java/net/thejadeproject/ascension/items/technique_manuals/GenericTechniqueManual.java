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
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;

public class GenericTechniqueManual extends Item {
    public ResourceLocation technique;

    public GenericTechniqueManual(Item.Properties properties,ResourceLocation techniqueLocation){
        super(properties);
        technique = techniqueLocation;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        
        ItemStack itemstack = player.getItemInHand(usedHand);
        if(level.isClientSide()) return InteractionResultHolder.fail(itemstack);
        if(learnTechnique(player)) {
            itemstack.shrink(1);
            return InteractionResultHolder.success(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    public ITechnique getTechnique() {
        return AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
    }

    public boolean learnTechnique(Player player){
        //TODO implement
        return false;
    }
}