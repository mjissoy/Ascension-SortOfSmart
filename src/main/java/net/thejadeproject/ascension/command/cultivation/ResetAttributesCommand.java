package net.thejadeproject.ascension.command.cultivation;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class ResetAttributesCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resetattributes")
                .requires(source -> source.hasPermission(2))
                .executes(context -> resetAttributes(context, Collections.singleton(context.getSource().getPlayerOrException())))
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(context -> resetAttributes(context, EntityArgument.getPlayers(context, "targets")))
                )
        );
    }

    private static int resetAttributes(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        int successCount = (int) players.stream().filter(ResetAttributesCommand::resetPlayerAttributes).count();

        if (players.size() == 1) {
            context.getSource().sendSuccess(() ->
                            Component.literal("Reset all vanilla attributes for " + players.iterator().next().getName().getString()),
                    true);
        } else {
            context.getSource().sendSuccess(() ->
                            Component.literal("Reset all vanilla attributes for " + successCount + " players"),
                    true);
        }

        return successCount;
    }

    private static boolean resetPlayerAttributes(ServerPlayer player) {
        boolean modified = false;

        // ============================================
        // VANILLA ATTRIBUTES - Pass Holder<Attribute> directly, don't cast
        // ============================================

        // MAX_HEALTH - Default: 20.0
        resetAttribute(player, Attributes.MAX_HEALTH, 20.0);
        player.setHealth(player.getMaxHealth());
        modified = true;

        // JUMP_STRENGTH - Default: 0.8
        resetAttribute(player, Attributes.JUMP_STRENGTH, 0.42);
        modified = true;

        // KNOCKBACK_RESISTANCE - Default: 0.0
        resetAttribute(player, Attributes.KNOCKBACK_RESISTANCE, 0.0);
        modified = true;

        // MOVEMENT_SPEED - Default: 0.1
        resetAttribute(player, Attributes.MOVEMENT_SPEED, 0.1);
        modified = true;

        // FLYING_SPEED - Default: 0.4
        resetAttribute(player, Attributes.FLYING_SPEED, 0.4);
        modified = true;

        // ATTACK_DAMAGE - Default: 1.0
        resetAttribute(player, Attributes.ATTACK_DAMAGE, 1.0);
        modified = true;

        // ATTACK_KNOCKBACK - Default: 0.0
        resetAttribute(player, Attributes.ATTACK_KNOCKBACK, 0.0);
        modified = true;

        // ATTACK_SPEED - Default: 4.0
        resetAttribute(player, Attributes.ATTACK_SPEED, 4.0);
        modified = true;

        // ARMOR - Default: 0.0
        resetAttribute(player, Attributes.ARMOR, 0.0);
        modified = true;

        // ARMOR_TOUGHNESS - Default: 0.0
        resetAttribute(player, Attributes.ARMOR_TOUGHNESS, 0.0);
        modified = true;

        // LUCK - Default: 0.0
        resetAttribute(player, Attributes.LUCK, 0.0);
        modified = true;


        // ============================================
        // HOW TO ADD MORE ATTRIBUTES
        // ============================================
        /*
         * 1. VANILLA ATTRIBUTES (already built into Minecraft):
         *    resetAttribute(player, Attributes.ATTRIBUTE_NAME, defaultValue);
         *    Available attributes: MAX_HEALTH, FOLLOW_RANGE, KNOCKBACK_RESISTANCE,
         *    MOVEMENT_SPEED, FLYING_SPEED, ATTACK_DAMAGE, ATTACK_KNOCKBACK,
         *    ATTACK_SPEED, ARMOR, ARMOR_TOUGHNESS, LUCK, SPAWN_REINFORCEMENTS,
         *    JUMP_STRENGTH, SAFE_FALL_DISTANCE, FALL_DAMAGE_MULTIPLIER,
         *    GRAVITY, STEP_HEIGHT, BLOCK_INTERACTION_RANGE, ENTITY_INTERACTION_RANGE
         *
         * 2. YOUR MOD'S ATTRIBUTES:
         *    If your DeferredHolder is Holder<Attribute>, pass it directly:
         *    resetAttribute(player, ModAttributes.QI_CAPACITY.get(), 100.0);
         *
         * 3. MODDED ATTRIBUTES FROM OTHER MODS:
         *    Holder<Attribute> modAttr = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.fromNamespaceAndPath("modid", "attribute_name")).orElse(null);
         *    if (modAttr != null) resetAttribute(player, modAttr, defaultValue);
         */

        return modified;
    }

    /**
     * Accepts Holder<Attribute> which is what Attributes.* returns in 1.21.1
     */
    private static void resetAttribute(ServerPlayer player, Holder<Attribute> attributeHolder, double defaultValue) {
        AttributeInstance instance = player.getAttribute(attributeHolder);
        if (instance == null) return;

        // Remove all modifiers
        for (AttributeModifier modifier : instance.getModifiers()) {
            instance.removeModifier(modifier);
        }

        // Set base value to default
        instance.setBaseValue(defaultValue);
    }

    /**
     * Alternative that removes modifiers by UUID
     */
    private static void removeModifierById(AttributeInstance instance, UUID modifierId) {
        for (AttributeModifier modifier : instance.getModifiers()) {
            if (modifier.id().equals(modifierId)) {
                instance.removeModifier(modifier);
                break;
            }
        }
    }

    /**
     * Alternative: Reset to vanilla default using DefaultAttributes
     */
    private static void resetToEntityDefault(ServerPlayer player, Holder<Attribute> attributeHolder) {
        double defaultValue = DefaultAttributes.getSupplier(net.minecraft.world.entity.EntityType.PLAYER)
                .getBaseValue(attributeHolder);

        AttributeInstance instance = player.getAttribute(attributeHolder);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                instance.removeModifier(modifier);
            }
            instance.setBaseValue(defaultValue);
        }
    }
}