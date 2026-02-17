package net.thejadeproject.ascension.refactor_packages.physiques;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IPhysique {


    //used when freshly added
    void onPhysiqueGained(LivingEntity entity,ResourceLocation oldPhysique);

    //used when replaced or removed properly
    void onPhysiqueLost(LivingEntity entity,ResourceLocation newPhysique);


    //used in circumstances where the physique already exists
    void addForForm(LivingEntity entity,ResourceLocation form);

    void removeForForm(LivingEntity entity, ResourceLocation form);
}
