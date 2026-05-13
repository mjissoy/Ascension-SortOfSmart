package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.refactor_packages.handlers.AscensionDamageHandler;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;

import java.util.HashSet;
import java.util.List;

public final class RunicFormulaCaster {

    private RunicFormulaCaster() {
    }

    public static RunicCastingResult tryCast(LivingEntity caster, List<net.minecraft.resources.ResourceLocation> inputRunes) {
        if (!(caster.level() instanceof ServerLevel level)) {
            return RunicCastingResult.failure("not_server_level");
        }

        RunicFormula formula = RunicFormulaParser.parse(inputRunes);

        if (!formula.isValid()) {
            return RunicCastingResult.failure("invalid_formula");
        }



        int runicRealm = RunicPathHelper.getRunicMajorRealm(caster);

        RunicFormulaStats stats = RunicFormulaScaling.calculate(
                formula,
                caster,
                runicRealm
        );

        String form = formula.formPath();

        switch (form) {
            case "veil" -> castVeil(level, caster, formula, stats);
            case "pulse" -> castPulse(level, caster, formula, stats);
            case "circle" -> castPulse(level, caster, formula, stats);
            case "sphere" -> castPulse(level, caster, formula, stats);
            case "mark" -> castMark(level, caster, formula, stats);
            case "wall" -> castWall(level, caster, formula, stats);
            case "line" -> castLine(level, caster, formula, stats);
            case "bolt" -> castBolt(level, caster, formula, stats);
            default -> castBolt(level, caster, formula, stats);
        }

        return RunicCastingResult.success(formula.getFormulaId());
    }

    private static void castVeil(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        applyIntentToSelf(caster, formula, stats);
        spawnSelfParticles(level, caster, particleFor(formula));
    }

    private static void castPulse(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        double range = formula.hasModifier("heavy") ? 6.0D : 4.0D;

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                caster.getBoundingBox().inflate(range),
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            applyIntentToTarget(caster, target, formula, stats, 0.75F);
        }

        spawnSelfParticles(level, caster, particleFor(formula));
    }

    private static void castMark(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        LivingEntity target = findLookedAtLivingEntity(caster, 8.0D);

        if (target == null) {
            applyIntentToSelf(caster, formula, stats);
            spawnSelfParticles(level, caster, particleFor(formula));
            return;
        }

        applyIntentToTarget(caster, target, formula, stats, 0.8F);
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80, 0));
        spawnParticleLine(level, particleFor(formula), caster.getEyePosition(), target.getBoundingBox().getCenter(), 10);
    }

    private static void castWall(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration(formula, stats), formula.hasModifier("stabilise") ? 1 : 0));
        caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, duration(formula, stats), 0));
        spawnSelfParticles(level, caster, particleFor(formula));
    }

    private static void castLine(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        Vec3 start = caster.getEyePosition();
        Vec3 direction = caster.getViewVector(0.0F);
        Vec3 end = start.add(direction.scale(12.0D));

        AABB box = caster.getBoundingBox().expandTowards(direction.scale(12.0D)).inflate(1.0D);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            applyIntentToTarget(caster, target, formula, stats, 0.65F);
        }

        spawnParticleLine(level, particleFor(formula), start, end, 18);
    }

    private static void castBolt(ServerLevel level, LivingEntity caster, RunicFormula formula, RunicFormulaStats stats) {
        LivingEntity target = findLookedAtLivingEntity(caster, 10.0D);

        if (target == null) {
            spawnSelfParticles(level, caster, ParticleTypes.POOF);
            return;
        }

        applyIntentToTarget(caster, target, formula, stats, 1.0F);
        spawnParticleLine(level, particleFor(formula), caster.getEyePosition(), target.getBoundingBox().getCenter(), 14);
    }

    private static void applyIntentToSelf(LivingEntity caster, RunicFormula formula,  RunicFormulaStats stats) {
        int duration = duration(formula, stats);

        switch (formula.intentPath()) {
            case "heal", "gather" -> {
                caster.heal(formula.sourcePath().equals("life") || formula.sourcePath().equals("water") ? 8.0F : 4.0F);
                caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration / 2, 0));
            }
            case "guard", "bind", "compress" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0));
            case "push", "release" -> caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, formula.hasModifier("quicken") ? 1 : 0));
            case "pull" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration / 2, 0));
            case "cut", "pierce" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 0));
        }

        applySourceSelfBonus(caster, formula, stats);
    }

    private static void applyIntentToTarget(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicFormulaStats stats, float multiplier) {
        float damage = baseDamage(formula, stats) * multiplier;

        switch (formula.intentPath()) {
            case "cut" -> hurtWithRunicDamage(caster, target, damage + 2.0F);
            case "pierce" -> hurtWithRunicDamage(caster, target, damage + 4.0F);
            case "compress" -> {
                hurtWithRunicDamage(caster, target, damage + 1.0F);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration(formula, stats) / 2, 1));
            }
            case "bind" -> target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration(formula, stats), formula.hasModifier("heavy") ? 3 : 1));
            case "push" -> pushAway(caster, target, formula);
            case "pull" -> pullToward(caster, target, formula);
            case "guard" -> target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration(formula, stats), 0));
            case "heal", "gather" -> {
                if (isPositiveSource(formula)) {
                    target.heal(3.0F + multiplier * 3.0F);
                } else {
                    hurtWithRunicDamage(caster, target, damage);
                }
            }
            case "release" -> {
                hurtWithRunicDamage(caster, target, damage + 1.0F);
                pushAway(caster, target, formula);
            }
        }

        applySourceTargetBonus(caster, target, formula, stats);
    }

    private static void applySourceSelfBonus(LivingEntity caster, RunicFormula formula,  RunicFormulaStats stats) {
        int duration = duration(formula, stats);

        switch (formula.sourcePath()) {
            case "flame" -> caster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0));
            case "water" -> caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration / 2, 0));
            case "wind" -> caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 0));
            case "earth", "metal" -> caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0));
            case "light" -> caster.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0));
            case "shadow" -> caster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, duration / 2, 0));
            case "life", "wood" -> caster.heal(2.0F);
        }
    }

    private static void applySourceTargetBonus(LivingEntity caster, LivingEntity target, RunicFormula formula, RunicFormulaStats stats) {
        int duration = duration(formula, stats);

        switch (formula.sourcePath()) {
            case "flame" -> target.igniteForSeconds(formula.hasModifier("violent") ? 6 : 3);
            case "frost" -> target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1));
            case "lightning" -> target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration / 2, 0));
            case "light" -> target.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0));
            case "shadow", "hidden" -> target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration / 2, 0));
            case "decay" -> target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration / 2, 0));
            case "life" -> caster.heal(1.5F);
            case "water" -> target.clearFire();
            case "wind" -> pushAway(caster, target, formula);
        }
    }

    private static float baseDamage(RunicFormula formula, RunicFormulaStats stats) {
        float damage = 18.0F + formula.inputRunes().size() * 4.0F;

        if (formula.hasModifier("violent")) damage += 3.0F;
        if (formula.hasModifier("heavy")) damage += 1.5F;
        if (formula.hasModifier("stabilise")) damage -= 1.0F;

        return Math.max(1.0F, damage * stats.damageMultiplier());
    }

    private static int duration(RunicFormula formula, RunicFormulaStats stats) {
        int duration = 100;

        if (formula.hasModifier("quicken")) duration -= 30;
        if (formula.hasModifier("stabilise")) duration += 40;
        if (formula.hasModifier("heavy")) duration += 20;

        return Math.max(40, (int)(duration * stats.durationMultiplier()));
    }

    private static boolean isPositiveSource(RunicFormula formula) {
        return formula.sourcePath().equals("life")
                || formula.sourcePath().equals("water")
                || formula.sourcePath().equals("wood")
                || formula.sourcePath().equals("light");
    }

    private static void pushAway(LivingEntity caster, LivingEntity target, RunicFormula formula) {
        double strength = formula.hasModifier("heavy") ? 1.2D : 0.7D;
        Vec3 direction = target.position().subtract(caster.position()).normalize();

        target.push(direction.x * strength, 0.2D, direction.z * strength);
        target.hurtMarked = true;
    }

    private static void pullToward(LivingEntity caster, LivingEntity target, RunicFormula formula) {
        double strength = formula.hasModifier("heavy") ? 1.0D : 0.55D;
        Vec3 direction = caster.position().subtract(target.position()).normalize();

        target.push(direction.x * strength, 0.1D, direction.z * strength);
        target.hurtMarked = true;
    }

    private static ParticleOptions particleFor(RunicFormula formula) {
        if (formula.hasModifier("hidden")) {
            return ParticleTypes.POOF;
        }

        return switch (formula.sourcePath()) {
            case "flame" -> ParticleTypes.FLAME;
            case "water" -> ParticleTypes.SPLASH;
            case "wind" -> ParticleTypes.CLOUD;
            case "earth", "metal" -> ParticleTypes.CRIT;
            case "lightning" -> ParticleTypes.ELECTRIC_SPARK;
            case "frost" -> ParticleTypes.SNOWFLAKE;
            case "light" -> ParticleTypes.END_ROD;
            case "shadow" -> ParticleTypes.POOF;
            case "life", "wood" -> ParticleTypes.HAPPY_VILLAGER;
            case "decay" -> ParticleTypes.ASH;
            default -> ParticleTypes.ENCHANT;
        };
    }

    private static void spawnSelfParticles(ServerLevel level, LivingEntity caster, ParticleOptions particle) {
        level.sendParticles(
                particle,
                caster.getX(),
                caster.getY() + caster.getBbHeight() * 0.5D,
                caster.getZ(),
                24,
                0.45D,
                0.45D,
                0.45D,
                0.04D
        );
    }

    private static void spawnParticleLine(ServerLevel level, ParticleOptions particle, Vec3 start, Vec3 end, int steps) {
        Vec3 diff = end.subtract(start);

        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            Vec3 pos = start.add(diff.scale(progress));
            level.sendParticles(particle, pos.x, pos.y, pos.z, 1, 0.03D, 0.03D, 0.03D, 0.02D);
        }
    }

    private static LivingEntity findLookedAtLivingEntity(LivingEntity caster, double reach) {
        Vec3 eyePosition = caster.getEyePosition();
        Vec3 viewVector = caster.getViewVector(0.0F);
        Vec3 endPosition = eyePosition.add(viewVector.scale(reach));

        HitResult blockHit = caster.pick(reach, 0.0F, false);

        AABB searchBox = caster.getBoundingBox()
                .expandTowards(viewVector.scale(reach))
                .inflate(1.0D);

        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                caster.level(),
                caster,
                eyePosition,
                endPosition,
                searchBox,
                entity -> isValidLookTarget(caster, entity)
        );

        if (entityHit == null) {
            return null;
        }

        if (blockHit.getType() != HitResult.Type.MISS) {
            double blockDistance = eyePosition.distanceToSqr(blockHit.getLocation());
            double entityDistance = eyePosition.distanceToSqr(entityHit.getLocation());

            if (blockDistance < entityDistance) {
                return null;
            }
        }

        Entity entity = entityHit.getEntity();
        return entity instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    private static boolean isValidLookTarget(LivingEntity caster, Entity entity) {
        return entity instanceof LivingEntity
                && entity != caster
                && entity.isAlive()
                && !entity.isSpectator()
                && entity.isPickable();
    }

    private static void hurtWithRunicDamage(LivingEntity caster, LivingEntity target, float damage) {

        HashSet<ResourceLocation> paths = new HashSet<>();
        paths.add(ModPaths.RUNIC.getId());

        AscensionDamageHandler.AscensionDamageSource source =
                new AscensionDamageHandler.AscensionDamageSource(paths,
                        target.damageSources().source(target.damageSources().magic().typeHolder().getKey(), caster)
                );

        target.hurt(source, damage);
    }
}