package net.thejadeproject.ascension.items.tools;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class SpiritualPaxelItem extends DiggerItem {

    public SpiritualPaxelItem(Tier tier, Properties properties) {
        super(tier, tier.getIncorrectBlocksForDrops(), properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide() || !(entity instanceof Player player)) return;
        if (slotId < 0 || slotId >= 9) return;
        if (!stack.isDamaged()) return;

        if (player.tickCount % 20 == 0) {
            PlayerData playerData = player.getData(ModAttachments.PLAYER_DATA);
            int damage = stack.getDamageValue();
            int maxRepair = Math.min(damage, 4);
            double qiCost = maxRepair * 5.0;

            if (playerData.tryConsumeQi(qiCost)) {
                stack.setDamageValue(damage - maxRepair);
                level.playSound(null, player.blockPosition(),
                        net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                        net.minecraft.sounds.SoundSource.PLAYERS, 0.1f, 1.0f);
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    // Check if block is mineable by pickaxe/axe/shovel AND not in our tier's "incorrect" list
    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        // Must be a tool block (pickaxe/axe/shovel)
        if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE) &&
                !state.is(BlockTags.MINEABLE_WITH_AXE) &&
                !state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
            return false;
        }

        // Check if tier can handle this block (not in the incorrect blocks tag)
        return !state.is(getTier().getIncorrectBlocksForDrops());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (isCorrectToolForDrops(stack, state)) {
            return getTier().getSpeed();
        }
        return 1.0f; // Hand mining speed if not correct
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility action) {
        return action == ItemAbilities.PICKAXE_DIG
                || action == ItemAbilities.AXE_DIG
                || action == ItemAbilities.SHOVEL_DIG
                || action == ItemAbilities.AXE_STRIP
                || action == ItemAbilities.AXE_SCRAPE
                || action == ItemAbilities.AXE_WAX_OFF
                || action == ItemAbilities.SHOVEL_FLATTEN
                || action == ItemAbilities.SHOVEL_DOUSE;
    }

    public static ItemAttributeModifiers createPaxelAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
}