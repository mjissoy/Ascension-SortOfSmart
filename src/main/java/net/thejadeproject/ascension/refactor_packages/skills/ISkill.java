package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;

import java.util.UUID;

public interface ISkill {


    void onAdded(IEntityData attachedEntityData);
    void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData);

    /*
     handles when forms are added and removed. is also called if an untethered entity held forms
     used when you want to apply data to specific forms
  */
    void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData);
    void onFormRemoved(IEntityData heldEntity,ResourceLocation form,IPhysiqueData physiqueData);


    void finishedCooldown(IEntityData attachedEntityData);

    IPersistentSkillData freshPersistentData(IEntityData heldEntity);
    IPersistentSkillData fromCompound(CompoundTag tag,IEntityData heldEntity);
    IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf,IEntityData heldEntity);
}
