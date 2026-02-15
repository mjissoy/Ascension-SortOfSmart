package net.thejadeproject.ascension.refactor_packages.skills.castable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndReason;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;

public interface ICastableSkill extends ISkill {

    void onEquip(Player player);
    void onUnEquip(Player player,IPreCastData preCastData);


    void finalCast(CastEndData reason,Player player,ICastData castData);
    boolean continueCasting(int ticksElapsed, Player player,ICastData castData);
    int getCooldown(CastEndData castEndData);//measured in ticks

    void initialCast(Player player,IPreCastData preCastData);



    IPreCastData freshPreCastData();
    IPreCastData preCastDataFromCompound(CompoundTag tag);
    IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf);

    ICastData freshCastData();
    ICastData castDataFromCompound(CompoundTag tag);
    ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf);

    IPersistentSkillData freshPersistentInstance();
    IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag);
    IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf);

}
