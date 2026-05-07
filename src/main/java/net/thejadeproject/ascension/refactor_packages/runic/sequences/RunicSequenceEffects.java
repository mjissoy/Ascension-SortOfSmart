package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class RunicSequenceEffects {

    private RunicSequenceEffects() {
    }

    public static void emberMark(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        LivingEntity target = findLookedAtLivingEntity(caster, 8.0D);

        if (target != null) {
            target.hurt(caster.damageSources().magic(), 4.0F);
            target.igniteForSeconds(4);

            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5D,
                    target.getZ(),
                    16,
                    0.35D,
                    0.35D,
                    0.35D,
                    0.02D
            );
            return;
        }

        caster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));

        serverLevel.sendParticles(
                ParticleTypes.FLAME,
                caster.getX(),
                caster.getY() + caster.getBbHeight() * 0.5D,
                caster.getZ(),
                10,
                0.3D,
                0.3D,
                0.3D,
                0.01D
        );
    }

    public static void clearWaterMend(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        caster.heal(6.0F);
        caster.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 0));

        serverLevel.sendParticles(
                ParticleTypes.SPLASH,
                caster.getX(),
                caster.getY() + caster.getBbHeight() * 0.5D,
                caster.getZ(),
                18,
                0.35D,
                0.35D,
                0.35D,
                0.04D
        );
    }

    public static void stoneWard(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        caster.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 140, 0));
        caster.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 140, 0));

        serverLevel.sendParticles(
                ParticleTypes.ENCHANT,
                caster.getX(),
                caster.getY() + caster.getBbHeight() * 0.5D,
                caster.getZ(),
                20,
                0.45D,
                0.45D,
                0.45D,
                0.02D
        );
    }

    public static void windPush(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                caster.getBoundingBox().inflate(4.5D),
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            Vec3 push = target.position().subtract(caster.position()).normalize().scale(0.85D);
            target.push(push.x, 0.25D, push.z);
            target.hurtMarked = true;
        }

        serverLevel.sendParticles(
                ParticleTypes.CLOUD,
                caster.getX(),
                caster.getY() + 0.25D,
                caster.getZ(),
                24,
                0.75D,
                0.2D,
                0.75D,
                0.08D
        );
    }

    public static void frostBind(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        List<LivingEntity> targets = serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                caster.getBoundingBox().inflate(5.0D),
                entity -> entity != caster && entity.isAlive()
        );

        for (LivingEntity target : targets) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 80, 0));
        }

        serverLevel.sendParticles(
                ParticleTypes.SNOWFLAKE,
                caster.getX(),
                caster.getY() + 0.5D,
                caster.getZ(),
                28,
                0.9D,
                0.35D,
                0.9D,
                0.03D
        );
    }

    public static void windStep(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        caster.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 1));

        Vec3 look = caster.getLookAngle().normalize().scale(0.85D);
        caster.push(look.x, 0.15D, look.z);
        caster.hurtMarked = true;

        serverLevel.sendParticles(
                ParticleTypes.CLOUD,
                caster.getX(),
                caster.getY() + 0.2D,
                caster.getZ(),
                18,
                0.45D,
                0.15D,
                0.45D,
                0.06D
        );
    }

    public static void thunderCutBolt(LivingEntity caster) {
        if (!(caster.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        LivingEntity target = findLookedAtLivingEntity(caster, 10.0D);

        if (target == null) {
            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    caster.getX(),
                    caster.getY() + caster.getBbHeight() * 0.5D,
                    caster.getZ(),
                    12,
                    0.35D,
                    0.35D,
                    0.35D,
                    0.04D
            );
            return;
        }

        target.hurt(caster.damageSources().magic(), 8.0F);
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);

        if (lightning != null) {
            lightning.moveTo(target.getX(), target.getY(), target.getZ());
            lightning.setVisualOnly(true);
            serverLevel.addFreshEntity(lightning);
        }

        serverLevel.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                target.getX(),
                target.getY() + target.getBbHeight() * 0.5D,
                target.getZ(),
                24,
                0.45D,
                0.45D,
                0.45D,
                0.08D
        );
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
}