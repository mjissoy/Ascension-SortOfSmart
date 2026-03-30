package net.thejadeproject.ascension.refactor_packages.skills.castable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndReason;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;

public interface ICastableSkill extends ISkill {

    void onEquip(IEntityData entityData);
    void onUnEquip(IEntityData entityData,IPreCastData preCastData);

    //called after continue and initial
    void finalCast(CastEndData reason, Entity caster,ICastData castData);
    //called before continue casting
    void initialCast(Entity caster,IPreCastData preCastData);

    /*
        done on the server, this is where you do things like make sure there are valid targets,
        and that the amount of qi is valid(should reduce qi in this method
     */
    CastResult canCast(Entity caster, IPreCastData preCastData);

    boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData);

    int getCooldown(CastEndData castEndData);//measured in ticks


    /*
        these are called on both the client and server so make sure to check before handling
        should be used to handle stuff like extra input handlers or UI elements
     */
    void selected(IEntityData entityData);
    void unselected(IEntityData entityData);

    IPreCastData freshPreCastData();
    IPreCastData preCastDataFromCompound(CompoundTag tag);
    IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf);

    ICastData freshCastData();
    ICastData castDataFromCompound(CompoundTag tag);
    ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf);

    IPersistentSkillData freshPersistentInstance();
    IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag);
    IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf);


    CastType getCastType();
}
