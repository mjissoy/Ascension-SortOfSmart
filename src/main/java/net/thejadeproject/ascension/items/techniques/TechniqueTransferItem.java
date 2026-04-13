package net.thejadeproject.ascension.items.techniques;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.events.ModDataComponents;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;

public class TechniqueTransferItem extends Item {
    public TechniqueTransferItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        System.out.println("trying to learn technique");
        if (!level.isClientSide() && player.isShiftKeyDown()) {
            String techniqueId = stack.get(ModDataComponents.TECHNIQUE_ID.get());

            if (techniqueId == null) {
                System.out.println("no held technique");
                return InteractionResultHolder.fail(stack);
            }



            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if(player.getData(ModAttachments.ENTITY_DATA).setTechnique(ResourceLocation.parse(techniqueId))){
                // Send feedback message
                System.out.println("technique learned");
                player.sendSystemMessage(
                        Component.literal("Successfully Learned:  ")
                                .append(getName(player.getItemInHand(hand)))
                                .append(Component.literal("!"))
                );
            }else{
                // Send feedback message
                System.out.println("failed to learn");
                player.sendSystemMessage(
                        Component.literal("unable to learn ")
                                .append(getName(player.getItemInHand(hand)))
                                .append(Component.literal("!"))
                );

            }


            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }
    @Override
    public Component getName(ItemStack stack) {
        String targetTechnique = stack.get(ModDataComponents.TECHNIQUE_ID.get());
        if(targetTechnique != null){
            ResourceLocation techniqueId = ResourceLocation.parse(targetTechnique);
            ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueId);
            if(technique != null) return technique.getDisplayTitle();
        }
        return Component.literal("Manual");
    }
    public static ItemStack createWithTechnique(String techniqueId) {
        ItemStack stack = new ItemStack(ModItems.TECHNIQUE_MANUAL.get());
        stack.set(ModDataComponents.TECHNIQUE_ID.get(), techniqueId);
        return stack;
    }

}
