package net.thejadeproject.ascension.items.techniques;

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
            String techniqueIdString = stack.get(ModDataComponents.TECHNIQUE_ID.get());

            if (techniqueIdString == null || techniqueIdString.isEmpty()) {
                System.out.println("no held technique");
                return InteractionResultHolder.fail(stack);
            }

            ResourceLocation techniqueId;
            try {
                techniqueId = ResourceLocation.parse(techniqueIdString);
            } catch (Exception e) {
                System.out.println("invalid technique id: " + techniqueIdString);
                player.sendSystemMessage(Component.literal("Invalid technique manual!"));
                return InteractionResultHolder.fail(stack);
            }

            ITechnique technique = safeGetTechnique(techniqueId);
            if (technique == null) {
                System.out.println("unknown technique: " + techniqueId);
                player.sendSystemMessage(Component.literal("Unknown technique: " + techniqueId));
                return InteractionResultHolder.fail(stack);
            }

            Component techniqueName = technique.getDisplayTitle();

            boolean success = player.getData(ModAttachments.ENTITY_DATA).setTechnique(techniqueId);

            if (success) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                System.out.println("technique learned");
                player.sendSystemMessage(
                        Component.literal("Successfully Learned: ")
                                .append(techniqueName)
                                .append(Component.literal("!"))
                );
                return InteractionResultHolder.success(stack);
            } else {
                System.out.println("failed to learn");
                player.sendSystemMessage(
                        Component.literal("unable to learn ")
                                .append(techniqueName)
                                .append(Component.literal("!"))
                );
                return InteractionResultHolder.fail(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        String targetTechnique = stack.get(ModDataComponents.TECHNIQUE_ID.get());
        if (targetTechnique == null || targetTechnique.isEmpty()) {
            return Component.literal("Manual");
        }

        try {
            ResourceLocation techniqueId = ResourceLocation.parse(targetTechnique);
            ITechnique technique = safeGetTechnique(techniqueId);
            if (technique != null) {
                return technique.getDisplayTitle();
            }
        } catch (Exception ignored) {
        }

        return Component.literal("Unknown Technique Manual");
    }

    private static ITechnique safeGetTechnique(ResourceLocation techniqueId) {
        try {
            return AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueId);
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack createWithTechnique(String techniqueId) {
        ItemStack stack = new ItemStack(ModItems.TECHNIQUE_MANUAL.get());
        stack.set(ModDataComponents.TECHNIQUE_ID.get(), techniqueId);
        return stack;
    }
}