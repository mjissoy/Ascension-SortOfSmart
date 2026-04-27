package net.thejadeproject.ascension.mob_ranks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;

public class MobCategoryResolver {

    private MobCategoryResolver() {}

    public static MobRankCategory resolve(LivingEntity entity) {
        if (isBoss(entity)) return MobRankCategory.BOSS;
        if (isHostile(entity)) return MobRankCategory.HOSTILE;
        return MobRankCategory.PASSIVE;
    }

    private static boolean isHostile(LivingEntity entity) {
        return entity instanceof Monster;
    }

    private static boolean isBoss(LivingEntity entity) {
        return entity instanceof EnderDragon
                || entity instanceof WitherBoss
                || entity instanceof Warden;
    }

}
