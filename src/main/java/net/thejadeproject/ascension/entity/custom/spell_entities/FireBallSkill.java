package net.thejadeproject.ascension.entity.custom.spell_entities;

import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.custom.AscensionSkillEntity;
import net.thejadeproject.ascension.util.ModTags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FireBallSkill extends SmallFireball implements AscensionSkillEntity {

    float baseDamage;

    public FireBallSkill(Level level, LivingEntity owner, Vec3 movement,float baseDamage) {
        super(level, owner, movement);
        this.baseDamage = baseDamage;

    }

    @Override
    public Set<String> getDaoTags() {
        return new HashSet<>(){{
            add("ascension:fire");
        }};
    }




    @Override
    protected void onHitEntity(EntityHitResult result) {
        DamageSource source = ModTags.DamageTypes.dao(damageSources(),getOwner(),this);
        if(result.getEntity().hurt(source,baseDamage)){
            EnchantmentHelper.doPostAttackEffects((ServerLevel) level(), result.getEntity(), source);
        }
    }
}
