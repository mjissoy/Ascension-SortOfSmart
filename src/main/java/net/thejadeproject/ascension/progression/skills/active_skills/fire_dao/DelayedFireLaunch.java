package net.thejadeproject.ascension.progression.skills.active_skills.fire_dao;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;

public class DelayedFireLaunch extends AbstractActiveSkill {

    public DelayedFireLaunch(String name) {

        super(Component.literal(name));



    }

    @Override
    public boolean isPrimarySkill() {
        return false;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


    @Override
    public int maxCastingTicks() {
        return 80;
    }


    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        //TODO create fireball
        
        launchPlayer(level,player);
    }

    @Override
    public void onPreCast() {
    }



    public void launchPlayer(Level level, Player player){
        Vec3 currentMotion = player.getDeltaMovement();
        Vec3 upwardMotion = new Vec3(currentMotion.x, 1, currentMotion.z);

        player.setOnGround(false);
        player.setDeltaMovement(upwardMotion);

        player.hasImpulse = true;
        player.hurtMarked = true;
        CommonHooks.onLivingJump(player);


    }
}