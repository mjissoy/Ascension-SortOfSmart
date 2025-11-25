package net.thejadeproject.ascension.progression.skills.active_skills.fire_dao;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.entity.custom.spell_entities.FireBallSkill;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.CastSource;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

public class BasicFireBall extends AbstractActiveSkill {
    public BasicFireBall() {
        super(Component.literal("Fire Ball"));
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


    @Override
    public int maxCastingTicks() {
        return 40;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level,Player player) {
        //TODO create fireball
        System.out.println("CAST A FUCKING FIREBALL");
        createFireBall(level,player);
    }

    @Override
    public void onPreCast() {
    }



    public void createFireBall(Level level, Player player){
        Vec3 vec3 = player.getViewVector(0);
        FireBallSkill fireBallSkill = new FireBallSkill(level,player, vec3.normalize());
        fireBallSkill.setPos(fireBallSkill.getX(), player.getY()+ player.getEyeHeight() , fireBallSkill.getZ());
        level.addFreshEntity(fireBallSkill);
    }
}
