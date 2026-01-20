package net.thejadeproject.ascension.menus.spatialrings;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpatialRingUtils {
    public static boolean curiosLoaded = false;
    private static final ConcurrentHashMap<String, Confirmation> confirmationMap = new ConcurrentHashMap<>();
    private static final long CONFIRMATION_EXPIRY = 10 * 60 * 1000; // 10 minutes

    public static void checkCuriosLoaded() {
        try {
            curiosLoaded = net.neoforged.fml.ModList.get().isLoaded("curios");
        } catch (Exception e) {
            curiosLoaded = false;
        }
    }

    public static ResourceLocation getRegistryName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ItemStack findSpatialringForHotkeys(Player player, boolean includeHands) {
        if (curiosLoaded) {
            var curiosInv = CuriosApi.getCuriosInventory(player);
            if (curiosInv.isPresent()) {
                ItemStack stack = curiosInv.get().findFirstCurio(SpatialRingItem::isSpatialring).map(slot -> {
                    if (slot.stack().getItem() instanceof SpatialRingItem) {
                        return slot.stack();
                    }
                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY);
                if (!stack.isEmpty()) {
                    return stack;
                }
            }
        }

        if (includeHands) {
            if (player.getMainHandItem().getItem() instanceof SpatialRingItem) {
                return player.getMainHandItem();
            }
            if (player.getOffhandItem().getItem() instanceof SpatialRingItem) {
                return player.getOffhandItem();
            }
        }

        Inventory inventory = player.getInventory();
        for (int i = 0; i <= 35; i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() instanceof SpatialRingItem) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public static Optional<UUID> getUUID(@Nonnull ItemStack stack) {
        if (stack.has(AscensionCraft.SPATIALRING_UUID)) {
            return Optional.ofNullable(stack.get(AscensionCraft.SPATIALRING_UUID));
        }
        else if (stack.getItem() instanceof SpatialRingItem && stack.has(DataComponents.CUSTOM_DATA) && stack.get(DataComponents.CUSTOM_DATA).contains("UUID"))
            return Optional.of(stack.get(DataComponents.CUSTOM_DATA).getUnsafe().getUUID("UUID"));
        else
            return Optional.empty();
    }

    public static Set<String> getUUIDSuggestions(CommandContext<CommandSourceStack> commandSource) {
        SpatialRingManager backpacks = SpatialRingManager.get();
        Set<String> list = new HashSet<>();
        backpacks.getMap().forEach((uuid, backpackData) -> list.add(uuid.toString()));
        return list;
    }

    public record Confirmation(String code, UUID player, UUID backpack, long timestamp){}

    public static String generateCode(RandomSource random) {
        return "%08x".formatted(random.nextInt(Integer.MAX_VALUE));
    }

    public static void addConfirmation(String code, UUID player, UUID backpack) {
        cleanupExpiredConfirmations();
        confirmationMap.put(code, new Confirmation(code, player, backpack, System.currentTimeMillis()));
    }

    public static void removeConfirmation(String code) {
        confirmationMap.remove(code);
    }

    public static Optional<Confirmation> getConfirmation(String code) {
        cleanupExpiredConfirmations();
        return Optional.ofNullable(confirmationMap.get(code));
    }

    private static void cleanupExpiredConfirmations() {
        long currentTime = System.currentTimeMillis();
        confirmationMap.entrySet().removeIf(entry ->
                currentTime - entry.getValue().timestamp() > CONFIRMATION_EXPIRY);
    }

    public static boolean isValidResourceLocation(String string) {
        return ResourceLocation.tryParse(string) != null;
    }
}