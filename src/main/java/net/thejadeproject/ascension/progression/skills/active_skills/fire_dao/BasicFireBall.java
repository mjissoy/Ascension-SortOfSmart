package net.thejadeproject.ascension.progression.skills.active_skills.fire_dao;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.entity.custom.spell_entities.FireBallSkill;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;

public class BasicFireBall extends AbstractActiveSkill {
    public final double speed;
    public final float baseDamage;
    public final int cooldown;
    public BasicFireBall(double speed,float baseDamage,String name,double qiCost,int cooldown) {

        super(Component.literal(name));
        this.speed = speed;
        this.baseDamage = baseDamage;
        this.qiCost = qiCost;
        this.cooldown = cooldown;

    }

    @Override
    public int getCooldown() {
        return cooldown;
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
    public boolean continueCasting(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        boolean state  = super.continueCasting(castingTicksElapsed, level, player,castData);
        if(!state) return false;
        if(castingTicksElapsed % 10 == 0) createFireBall(level,player);
        return player.getData(ModAttachments.INPUT_STATES).isHeld("cast_skill_input");
    }

    @Override
    public void cast(int castingTicksElapsed, Level level,Player player, ICastData castData) {
        //TODO create fireball
        
        createFireBall(level,player);
    }

    @Override
    public void onPreCast() {
    }

    public void createFireBall(Level level, Player player){
        Vec3 vec3 = player.getViewVector(0);
        FireBallSkill fireBallSkill = new FireBallSkill(level,player, vec3.normalize().scale(speed),baseDamage);
        fireBallSkill.setPos(fireBallSkill.getX(), player.getY()+ player.getEyeHeight() , fireBallSkill.getZ());
        level.addFreshEntity(fireBallSkill);
    }
}
