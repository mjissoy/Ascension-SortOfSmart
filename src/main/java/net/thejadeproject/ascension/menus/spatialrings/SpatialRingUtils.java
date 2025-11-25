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

public class SpatialRingUtils {
    public static boolean curiosLoaded = false;

    // Call this method during mod initialization
    public static void checkCuriosLoaded() {
        try {
            // More reliable way to check if Curios is loaded
            curiosLoaded = net.neoforged.fml.ModList.get().isLoaded("curios");
        } catch (Exception e) {
            curiosLoaded = false;
        }
    }

    public static ResourceLocation getRegistryName(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ItemStack findSpatialringForHotkeys(Player player, boolean includeHands) {
        // First priority: Check Curios ring slot
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

        // Second priority: Check hands if enabled
        if (includeHands) {
            if (player.getMainHandItem().getItem() instanceof SpatialRingItem) {
                return player.getMainHandItem();
            }
            if (player.getOffhandItem().getItem() instanceof SpatialRingItem) {
                return player.getOffhandItem();
            }
        }

        // Third priority: Check inventory
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

    public record Confirmation(String code, UUID player, UUID backpack){}

    public static String generateCode(RandomSource random) {
        return "%08x".formatted(random.nextInt(Integer.MAX_VALUE));
    }

    private static final HashMap<String, Confirmation> confirmationMap = new HashMap<>();

    public static void addConfirmation(String code, UUID player, UUID backpack) {
        confirmationMap.put(code, new Confirmation(code, player, backpack));
    }

    public static void removeConfirmation(String code) {
        confirmationMap.remove(code);
    }

    public static Optional<Confirmation> getConfirmation(String code) {
        return Optional.ofNullable(confirmationMap.get(code));
    }

    public static boolean isValidResourceLocation(String string) {
        return ResourceLocation.tryParse(string) != null;
    }
}