package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

import java.util.UUID;

public interface ISkill {


    void onAdded(IEntityData attachedEntityData);
    void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData);

    void removeForTetheredEntity(IEntityData attachedEntityData,IEntityData tetheredEntityData,IPersistentSkillData persistentSkillData);

    void finishedCooldown(IEntityData attachedEntityData);

    IPersistentSkillData freshPersistentData(IEntityData heldEntity);
    IPersistentSkillData fromCompound(CompoundTag tag,IEntityData heldEntity);
    IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf,IEntityData heldEntity);
}
