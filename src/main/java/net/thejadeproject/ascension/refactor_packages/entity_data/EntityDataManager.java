package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;

import java.util.HashMap;
import java.util.UUID;

/*

    in order to realise if an entities data needs to be reatached i am going to do 1 of 2 things
    either find out how to modify the data component of unloaded entites directly, or wait till it is loaded,
    and check if there are any tethered entitites left
 */
public class EntityDataManager {



    private static final HashMap<UUID, IEntityData> secondaryTetheredEntities = new HashMap<>();


    private static final HashMap<UUID, IEntityData> primaryTetheredEntities = new HashMap<>();


    private static void addPrimaryTetheredEntity(LivingEntity primaryEntity){

    }

    public static void addTetheredEntity(LivingEntity primaryEntity,LivingEntity secondaryEntity,IEntityData secondaryEntityData){

    }

    public static IEntityData getEntityData(UUID uuid){
        if(secondaryTetheredEntities.containsKey(uuid)) return secondaryTetheredEntities.get(uuid);
        return primaryTetheredEntities.get(uuid);
    }
    public static IEntityFormData getEntityFormData(UUID uuid, ResourceLocation form){
        if(secondaryTetheredEntities.containsKey(uuid)) return secondaryTetheredEntities.get(uuid).getEntityFormData(form);
        return primaryTetheredEntities.get(uuid).getEntityFormData(form);
    }

    public static IEntityData removeTetheredEntity(UUID uuid){
        if(secondaryTetheredEntities.containsKey(uuid)) return secondaryTetheredEntities.remove(uuid);
        return primaryTetheredEntities.remove(uuid);
    }


}
