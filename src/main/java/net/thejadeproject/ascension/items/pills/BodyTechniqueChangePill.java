package net.thejadeproject.ascension.items.pills;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.cultivation.player.PlayerDataChangeHandler;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class BodyTechniqueChangePill extends PillCooldownItem {
    private static final String BODY_PATH_ID = "ascension:body";

    public BodyTechniqueChangePill(Properties properties, Integer value) {
        super(properties, value);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            if (level.isClientSide()) {
                return super.finishUsingItem(stack, level, livingEntity);
            }

            if (!hasBodyTechnique(player)) {
                player.displayClientMessage(Component.translatable("message.ascension.no_body_technique")
                        .withStyle(net.minecraft.ChatFormatting.RED), true);

                return stack;
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            PlayerDataChangeHandler.removeTechnique(player, BODY_PATH_ID);

            player.displayClientMessage(Component.translatable("message.ascension.body_technique_removed")
                    .withStyle(net.minecraft.ChatFormatting.GREEN), true);
        }

        return super.finishUsingItem(stack, level, livingEntity);
    }

    private boolean hasBodyTechnique(Player player) {
        PlayerData playerData = player.getData(ModAttachments.PLAYER_DATA);
        CultivationData cultivationData = playerData.getCultivationData();

        CultivationData.PathData bodyPathData = cultivationData.getPathData(BODY_PATH_ID);

        if (bodyPathData == null ||
                bodyPathData.technique == null ||
                bodyPathData.technique.isEmpty() ||
                bodyPathData.technique.equals("ascension:none")) {
            return false;
        }

        net.minecraft.resources.ResourceLocation techniqueId = net.minecraft.resources.ResourceLocation.tryParse(bodyPathData.technique);
        if (techniqueId == null) {
            return false;
        }

        return AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.containsKey(techniqueId);
    }
}