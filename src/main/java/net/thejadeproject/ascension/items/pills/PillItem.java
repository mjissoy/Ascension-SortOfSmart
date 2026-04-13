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

import net.thejadeproject.ascension.entity.ModEntities;
import net.thejadeproject.ascension.entity.PoisonPillProjectile;
import net.thejadeproject.ascension.events.ModDataComponents;

import java.util.List;

/**
 * Unified pill class.
 *
 * ── Effect scaling ────────────────────────────────────────────────────────
 * Cultivation pills (BODY / ESSENCE / INTENT) scale their effect by both
 * purity and major realm:
 *
 *   effectiveAmount = baseAmount × purityScale × realmMultiplier
 *
 * purityScale   = purity / 100.0   (1-100 → 0.01–1.0)
 * realmMultiplier per major realm:
 *   1 → 1.0×
 *   2 → 3.5×
 *   3 → 3.5² ≈ 12.25×
 *   4 → 3.5³ ≈ 42.9×
 *   ... and so on (exponential)
 *
 * ── Purity display ────────────────────────────────────────────────────────
 * The numeric purity is NOT shown in the tooltip. Instead the named grade is:
 *   Basic / Average / Advanced / Peak
 * determined by PillRealmData.getPurityGrade(purity).
 *
 * ── Pill Types ────────────────────────────────────────────────────────────
 *   BODY    – Cultivation pill for Body path
 *   ESSENCE – Cultivation pill for Essence path
 *   INTENT  – Cultivation pill for Intent path
 *   POISON  – Throwable; shift+click to eat
 *   REBIRTH – Triggers rebirth
 *   ANTIDOTE – Removes MobEffects
 */
public class PillItem extends Item {

    public enum PillType { BODY, ESSENCE, INTENT, POISON, REBIRTH, ANTIDOTE }

    // ── Realm multiplier ──────────────────────────────────────────
    /** Each major realm multiplies effects by this factor over the previous. */
    private static final double REALM_MULTIPLIER = 3.5;

    public static double getRealmMultiplier(int majorRealm) {
        // Realm 1 = 1.0×, Realm 2 = 3.5×, Realm 3 = 12.25×, etc.
        return Math.pow(REALM_MULTIPLIER, Math.max(0, majorRealm - 1));
    }

    // ── Fields ────────────────────────────────────────────────────
    private final PillType          type;
    private final double            cultivationAmount;
    private final CultivationSource cultivationSource;
    private final int               cooldown;
    private       MobEffectInstance poisonEffect;
    private       MobEffectInstance[] effectsToRemove;

    // ── Constructors ──────────────────────────────────────────────

    /** BODY / ESSENCE / INTENT */
    public PillItem(Properties props, PillType type,
                    double cultivationAmount, CultivationSource source, int cooldown) {
        super(props);
        if (type != PillType.BODY && type != PillType.ESSENCE && type != PillType.INTENT)
            throw new IllegalArgumentException("Use the correct constructor for: " + type);
        this.type              = type;
        this.cultivationAmount = cultivationAmount;
        this.cultivationSource = source;
        this.cooldown          = cooldown;
        this.poisonEffect      = null;
        this.effectsToRemove   = new MobEffectInstance[0];
    }

    /** POISON */
    public PillItem(Properties props, PillType type, MobEffectInstance effect) {
        super(props);
        if (type != PillType.POISON) throw new IllegalArgumentException("Use POISON type");
        this.type              = PillType.POISON;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = 0;
        this.poisonEffect      = effect;
        this.effectsToRemove   = new MobEffectInstance[0];
    }

    /** REBIRTH */
    public PillItem(Properties props, PillType type, int cooldown) {
        super(props);
        if (type != PillType.REBIRTH) throw new IllegalArgumentException("Use REBIRTH type");
        this.type              = PillType.REBIRTH;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = cooldown;
        this.poisonEffect      = null;
        this.effectsToRemove   = new MobEffectInstance[0];
    }

    /** ANTIDOTE */
    public PillItem(Properties props, PillType type,
                    int cooldown, MobEffectInstance... effectsToRemove) {
        super(props);
        if (type != PillType.ANTIDOTE) throw new IllegalArgumentException("Use ANTIDOTE type");
        this.type              = PillType.ANTIDOTE;
        this.cultivationAmount = 0;
        this.cultivationSource = null;
        this.cooldown          = cooldown;
        this.poisonEffect      = null;
        this.effectsToRemove   = effectsToRemove;
    }

    // ── Use ───────────────────────────────────────────────────────

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (type == PillType.POISON) {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isShiftKeyDown()) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
            if (!level.isClientSide()) {
                PoisonPillProjectile proj = new PoisonPillProjectile(
                        ModEntities.POISON_PILL.get(), level, player, poisonEffect, this);
                proj.setItem(stack);
                proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 1.5f, 1f);
                level.addFreshEntity(proj);
            }
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return super.use(level, player, hand);
    }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.EAT; }
    @Override public int getUseDuration(ItemStack stack, LivingEntity e) {
        return (type == PillType.POISON) ? 16 : 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if (level.isClientSide() || !(livingEntity instanceof Player player)) return result;

        Integer purityComp = stack.get(ModDataComponents.PILL_PURITY.get());
        Integer majorComp  = stack.get(ModDataComponents.PILL_MAJOR_REALM.get());

        int purity      = (purityComp != null) ? purityComp : 50;
        int majorRealm  = (majorComp  != null) ? majorComp  : 1;

        double purityScale      = purity / 100.0;
        double realmMultiplier  = getRealmMultiplier(majorRealm);

        switch (type) {
            case BODY -> {
                double amt = cultivationAmount * purityScale * realmMultiplier;
                //TODO add cultivation
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            case ESSENCE -> {
                double amt = cultivationAmount * purityScale * realmMultiplier;
                //TODO add cultivation
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            case INTENT -> {
                double amt = cultivationAmount * purityScale * realmMultiplier;
                //TODO add cultivation
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            case POISON -> {
                if (poisonEffect != null) player.addEffect(new MobEffectInstance(poisonEffect));
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            case REBIRTH -> {
                //TODO add cultivation
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            case ANTIDOTE -> {
                for (MobEffectInstance e : effectsToRemove) player.removeEffect(e.getEffect());
                player.getCooldowns().addCooldown(this, cooldown);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
        }

        return result;
    }

    // ── Tooltip ───────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx,
                                List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, list, flag);

        Integer majorRealm = stack.get(ModDataComponents.PILL_MAJOR_REALM.get());
        Integer purity     = stack.get(ModDataComponents.PILL_PURITY.get());
        String  bonus      = stack.get(ModDataComponents.PILL_BONUS_EFFECT.get());

        if (majorRealm == null && purity == null) {
            list.add(Component.literal("Unrefined")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            return;
        }

        // ── Realm line ────────────────────────────────────────────
        if (majorRealm != null) {
            list.add(Component.literal("Pill Realm: ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(Component.literal(
                                    majorRealm + " — " + PillRealmData.getMajorRealmName(majorRealm))
                            .withStyle(ChatFormatting.WHITE)));
        }

        // ── Purity grade line (named, no number shown) ────────────
        if (purity != null) {
            String gradeName = PillRealmData.getPurityGrade(purity);
            ChatFormatting gradeColor = PillRealmData.getPurityGradeColor(purity);
            list.add(Component.literal("Purity: ")
                    .withStyle(ChatFormatting.YELLOW)
                    .append(Component.literal(gradeName).withStyle(gradeColor)));
        }

        // ── Bonus effect ──────────────────────────────────────────
        if (bonus != null && !bonus.isEmpty()) {
            list.add(Component.literal("✦ Bonus: ")
                    .withStyle(ChatFormatting.AQUA)
                    .append(Component.literal(PillRealmData.getBonusEffectDisplay(bonus))
                            .withStyle(ChatFormatting.LIGHT_PURPLE)));
        }
    }

    // ── Static utility ────────────────────────────────────────────

    /**
     * Applied by the cauldron after crafting.
     * No longer takes minorRealm — purity grade is derived at tooltip time.
     */
    public static ItemStack applyPillData(ItemStack stack, int majorRealm,
                                          int purity, String bonusEffect) {
        stack.set(ModDataComponents.PILL_MAJOR_REALM.get(), majorRealm);
        stack.set(ModDataComponents.PILL_PURITY.get(), purity);
        if (bonusEffect != null && !bonusEffect.isEmpty()) {
            stack.set(ModDataComponents.PILL_BONUS_EFFECT.get(), bonusEffect);
        }
        return stack;
    }
}