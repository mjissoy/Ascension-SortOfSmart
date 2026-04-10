package net.thejadeproject.ascension.items.pills;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.constants.CultivationSource;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.PlayerDataChangeHandler;
import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.PoisonPillProjectile;
import net.thejadeproject.ascension.events.ModDataComponents;

import java.util.List;

/**
 * Unified pill class that replaces:
 *   PillCooldownItem, RebirthPill, ThrowablePoisonPill, PillAntidote,
 *   BodyTechniqueChangePill (if using BODY_AMNESIA type).
 *
 * ── Pill Types ──────────────────────────────────────────────────────
 *
 *   BODY    – Cultivation pill for the Body path.
 *             Constructor: PillItem(props, BODY, cultivationAmount, source, cooldown)
 *
 *   ESSENCE – Cultivation pill for the Essence path.
 *             Constructor: PillItem(props, ESSENCE, cultivationAmount, source, cooldown)
 *
 *   INTENT  – Cultivation pill for the Intent path.
 *             Constructor: PillItem(props, INTENT, cultivationAmount, source, cooldown)
 *
 *   POISON  – Throwable pill. Right-click throws; shift+right-click eats and applies effect.
 *             Constructor: PillItem(props, POISON, MobEffectInstance)
 *
 *   REBIRTH – Triggers a cultivation rebirth on consume.
 *             Constructor: PillItem(props, REBIRTH, cooldown)
 *
 *   ANTIDOTE – Removes one or more MobEffects on consume.
 *              Constructor: PillItem(props, ANTIDOTE, cooldown, MobEffectInstance...)
 *
 * ── Tooltip ─────────────────────────────────────────────────────────
 *   All pills show Realm / Purity / Bonus tooltips when those data
 *   components are present (applied by the cauldron on craft).
 *   Pills with no cauldron data show "Unrefined".
 *
 * ── Extending ───────────────────────────────────────────────────────
 *   To add a new pill type in the future:
 *     1. Add an entry to the {@link PillType} enum.
 *     2. Add a constructor overload below.
 *     3. Add a case in finishUsingItem() / use().
 */
public class PillItem extends Item {

    // ── Pill Type enum ───────────────────────────────────────────
    public enum PillType {
        BODY,
        ESSENCE,
        INTENT,
        POISON,
        REBIRTH,
        ANTIDOTE
    }

    // ── Fields ───────────────────────────────────────────────────
    private final PillType type;

    // Cultivation pills (BODY / ESSENCE / INTENT)
    private final double   cultivationAmount;
    private final CultivationSource cultivationSource;

    // Cooldown (BODY / ESSENCE / INTENT / REBIRTH / ANTIDOTE)
    private final int cooldown;

    // POISON
    private MobEffectInstance poisonEffect;

    // ANTIDOTE
    private MobEffectInstance[] effectsToRemove;

    // ── Constructors ─────────────────────────────────────────────

    /**
     * BODY / ESSENCE / INTENT cultivation pill.
     *
     * @param cultivationAmount  Raw cultivation progress to grant.
     * @param source             CultivationSource (usually CultivationSource.CONSUMABLE).
     * @param cooldown           Cooldown in ticks after consumption.
     */
    public PillItem(Properties properties, PillType type,
                    double cultivationAmount, CultivationSource source, int cooldown) {
        super(properties);
        if (type != PillType.BODY && type != PillType.ESSENCE && type != PillType.INTENT) {
            throw new IllegalArgumentException("Use the correct constructor for type: " + type);
        }
        this.type               = type;
        this.cultivationAmount  = cultivationAmount;
        this.cultivationSource  = source;
        this.cooldown           = cooldown;
        this.poisonEffect       = null;
        this.effectsToRemove    = new MobEffectInstance[0];
    }

    /**
     * POISON throwable pill.
     *
     * @param effect  The MobEffectInstance applied on hit (thrown) or on eat (shift+click).
     */
    public PillItem(Properties properties, PillType type, MobEffectInstance effect) {
        super(properties);
        if (type != PillType.POISON) {
            throw new IllegalArgumentException("Use the correct constructor for type: " + type);
        }
        this.type              = PillType.POISON;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = 0;
        this.poisonEffect      = effect;
        this.effectsToRemove   = new MobEffectInstance[0];
    }

    /**
     * REBIRTH pill.
     *
     * @param cooldown  Cooldown in ticks after consumption.
     */
    public PillItem(Properties properties, PillType type, int cooldown) {
        super(properties);
        if (type != PillType.REBIRTH) {
            throw new IllegalArgumentException("Use the correct constructor for type: " + type);
        }
        this.type              = PillType.REBIRTH;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = cooldown;
        this.poisonEffect      = null;
        this.effectsToRemove   = new MobEffectInstance[0];
    }

    /**
     * ANTIDOTE pill. Removes the given effects on consumption.
     *
     * @param cooldown         Cooldown in ticks after consumption.
     * @param effectsToRemove  One or more MobEffectInstance whose effect types will be removed.
     */
    public PillItem(Properties properties, PillType type,
                    int cooldown, MobEffectInstance... effectsToRemove) {
        super(properties);
        if (type != PillType.ANTIDOTE) {
            throw new IllegalArgumentException("Use the correct constructor for type: " + type);
        }
        this.type              = PillType.ANTIDOTE;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = cooldown;
        this.poisonEffect      = null;
        this.effectsToRemove   = effectsToRemove;
    }

    // ── Use behaviour ────────────────────────────────────────────

    /**
     * POISON pills launch a projectile on right-click (no sneak).
     * All other types use the normal eat animation.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (type == PillType.POISON) {
            ItemStack stack = player.getItemInHand(hand);

            if (player.isShiftKeyDown()) {
                // Shift + right-click → eat
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }

            // Plain right-click → throw
            if (!level.isClientSide()) {
                PoisonPillProjectile projectile = new PoisonPillProjectile(
                        ModEntities.POISON_PILL.get(),
                        level,
                        player,
                        poisonEffect,
                        this
                );
                projectile.setItem(stack);
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 1.5f, 1f);
                level.addFreshEntity(projectile);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return super.use(level, player, hand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return (type == PillType.POISON) ? 16 : 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        if (level.isClientSide() || !(livingEntity instanceof Player player)) {
            return result;
        }

        // Read purity (used for both cultivation scaling and bonus duration)
        Integer purityComp = stack.get(ModDataComponents.PILL_PURITY.get());
        int     purityInt  = (purityComp != null) ? purityComp : 50;   // default 50 if unrefined
        double  purityScale = purityInt / 100.0;

        switch (type) {

            case BODY -> {
                double scaled = cultivationAmount * purityScale;
                CultivationSystem.cultivate(player, "ascension:body",
                        scaled, new java.util.HashSet<>(), cultivationSource);
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            case ESSENCE -> {
                double scaled = cultivationAmount * purityScale;
                CultivationSystem.cultivate(player, "ascension:essence",
                        scaled, new java.util.HashSet<>(), cultivationSource);
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            case INTENT -> {
                double scaled = cultivationAmount * purityScale;
                CultivationSystem.cultivate(player, "ascension:intent",
                        scaled, new java.util.HashSet<>(), cultivationSource);
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            case POISON -> {
                if (poisonEffect != null) {
                    player.addEffect(new MobEffectInstance(poisonEffect));
                }
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            case REBIRTH -> {
                PlayerDataChangeHandler.rebirth(player);
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }

            case ANTIDOTE -> {
                for (MobEffectInstance effect : effectsToRemove) {
                    player.removeEffect(effect.getEffect());
                }
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
        }

        return result;
    }

    // ── Tooltip ──────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipComponents, flag);

        Integer majorRealm = stack.get(ModDataComponents.PILL_MAJOR_REALM.get());
        String  minorRealm = stack.get(ModDataComponents.PILL_MINOR_REALM.get());
        Integer purity     = stack.get(ModDataComponents.PILL_PURITY.get());
        String  bonus      = stack.get(ModDataComponents.PILL_BONUS_EFFECT.get());

        boolean hasRealm  = majorRealm != null && minorRealm != null;
        boolean hasPurity = purity != null;

        if (!hasRealm && !hasPurity) {
            tooltipComponents.add(
                    Component.literal("Unrefined")
                            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
            );
            return;
        }

        // Realm
        if (hasRealm) {
            tooltipComponents.add(
                    Component.literal("Pill Realm")
                            .withStyle(ChatFormatting.GOLD)
            );
            tooltipComponents.add(
                    Component.literal("  Major: ")
                            .withStyle(ChatFormatting.YELLOW)
                            .append(Component.literal(
                                            majorRealm + " - " + PillRealmData.getMajorRealmName(majorRealm))
                                    .withStyle(ChatFormatting.WHITE))
            );
            tooltipComponents.add(
                    Component.literal("  Minor: ")
                            .withStyle(ChatFormatting.YELLOW)
                            .append(Component.literal(
                                            PillRealmData.getMinorRealmName(minorRealm))
                                    .withStyle(ChatFormatting.WHITE))
            );
        }

        // Purity
        if (hasPurity) {
            tooltipComponents.add(
                    Component.literal("Purity: ")
                            .withStyle(ChatFormatting.YELLOW)
                            .append(Component.literal(purity + "/100")
                                    .withStyle(getPurityColor(purity)))
            );
        }

        // Bonus effect
        if (bonus != null && !bonus.isEmpty()) {
            tooltipComponents.add(
                    Component.literal("✦ Bonus: ")
                            .withStyle(ChatFormatting.AQUA)
                            .append(Component.literal(
                                            PillRealmData.getBonusEffectDisplay(bonus))
                                    .withStyle(ChatFormatting.LIGHT_PURPLE))
            );
        }
    }

    private ChatFormatting getPurityColor(int purity) {
        if (purity >= 90) return ChatFormatting.AQUA;
        if (purity >= 70) return ChatFormatting.GREEN;
        if (purity >= 50) return ChatFormatting.YELLOW;
        if (purity >= 25) return ChatFormatting.GOLD;
        return ChatFormatting.DARK_RED;
    }

    // ── Static cauldron utility ──────────────────────────────────

    /**
     * Applied by the cauldron block entity after crafting.
     * Sets realm, purity, and optional bonus effect on the output stack.
     */
    public static ItemStack applyPillData(ItemStack stack, int majorRealm, String minorRealm,
                                          int purity, String bonusEffect) {
        stack.set(ModDataComponents.PILL_MAJOR_REALM.get(), majorRealm);
        stack.set(ModDataComponents.PILL_MINOR_REALM.get(), minorRealm);
        stack.set(ModDataComponents.PILL_PURITY.get(), purity);
        if (bonusEffect != null && !bonusEffect.isEmpty()) {
            stack.set(ModDataComponents.PILL_BONUS_EFFECT.get(), bonusEffect);
        }
        return stack;
    }
}