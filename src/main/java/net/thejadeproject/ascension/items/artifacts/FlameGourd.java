package net.thejadeproject.ascension.items.artifacts;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.nbt.CompoundTag;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FlameGourd extends Item {
    private static Map<Block, Item> FIRE_ITEMS;
    private static Set<Block> ABSORBABLE_FIRES;

    private static void initFireItems() {
        if (FIRE_ITEMS != null) return;

        Map<Block, Item> map = Maps.newHashMap();
        map.put(Blocks.FIRE, Items.FIRE_CHARGE);
        map.put(Blocks.SOUL_FIRE, Items.BLAZE_POWDER);
        map.put(ModBlocks.CRIMSON_LOTUS_FIRE.get(), ModItems.CRIMSON_LOTUS_FLAME.get());

        FIRE_ITEMS = Map.copyOf(map);
        ABSORBABLE_FIRES = FIRE_ITEMS.keySet();
    }

    private static Map<Block, Item> getFireItems() {
        initFireItems();
        return FIRE_ITEMS;
    }

    private static Set<Block> getAbsorbableFires() {
        initFireItems();
        return ABSORBABLE_FIRES;
    }

    public FlameGourd(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE).durability(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (getAbsorbableFires().contains(state.getBlock()) && stack.getDamageValue() == 1) { // empty
            if (!level.isClientSide) {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);

                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                CompoundTag nbt = customData != null ? customData.copyTag() : new CompoundTag();
                nbt.putString("AbsorbedFire", BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString());
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                stack.setDamageValue(0); // filled
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching() && stack.getDamageValue() == 0) { // filled
            if (!level.isClientSide) {
                CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    CompoundTag nbt = customData.copyTag();
                    if (nbt.contains("AbsorbedFire")) {
                        String fireStr = nbt.getString("AbsorbedFire");
                        ResourceLocation fireRl = ResourceLocation.tryParse(fireStr);
                        if (fireRl != null) {
                            // Convert back to Block to look up in our map
                            Block fireBlock = BuiltInRegistries.BLOCK.get(fireRl);
                            Item item = getFireItems().get(fireBlock);
                            if (item != null) {
                                ItemStack toGive = new ItemStack(item);
                                if (!player.addItem(toGive)) {
                                    player.drop(toGive, false);
                                }
                                nbt.remove("AbsorbedFire");

                                if (nbt.isEmpty()) {
                                    stack.remove(DataComponents.CUSTOM_DATA);
                                } else {
                                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                                }
                                stack.setDamageValue(1); // empty
                            }
                        }
                    }
                }
            }
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getMaxDamage() > 0;
    }
}