package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public interface ISkill {


    void onAdded(Player player);
    void onRemoved(Player player,IPersistentSkillData persistentData);

    void finishedCooldown(Player player);

    IPersistentSkillData freshPersistentData();
    IPersistentSkillData fromCompound(CompoundTag tag);
    IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf);
}
