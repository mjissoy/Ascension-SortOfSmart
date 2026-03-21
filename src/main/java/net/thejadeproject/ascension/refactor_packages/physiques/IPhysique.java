package net.thejadeproject.ascension.refactor_packages.physiques;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;

import java.util.UUID;

public interface IPhysique {


    IPhysiqueData onPhysiqueAdded(IEntityData heldEntity,ResourceLocation oldPhysique);
    /*
        called when physique is replaced with a new one.
        if it is removed due to entity tether being broken it is called after all that removal logic
     */
    void onPhysiqueRemoved(IEntityData heldEntity,IPhysiqueData physiqueData,ResourceLocation newPhysique);

    /*
        onEntityTethered is called when an entities data is tethered to another existing entity.
        not called when,for example, a fresh tethered vessel is created. Since in that instance data is only being
        moved around. nothing new is being added or removed

        same with Untethered. called when there is some sort of data being added or removed rather than just moved around
     */
    void onEntityTethered(IEntityData heldEntity,IEntityData tetheredEntity,IPhysiqueData physiqueData);
    void onEntityUntethered(IEntityData heldEntity,IEntityData oldTetheredEntity,IPhysiqueData physiqueData);



    /*
        handles when forms are added and removed. is also called if an untethered entity held forms
        used when you want to apply data to specific forms
     */
    void onFormAdded(IEntityData heldEntity, ResourceLocation form,IPhysiqueData physiqueData);
    void onFormRemoved(IEntityData heldEntity,ResourceLocation form,IPhysiqueData physiqueData);


    IPhysiqueData freshPhysique(IEntityData heldEntity);
    IPhysiqueData fromCompound(CompoundTag tag,IEntityData heldEntity);
    IPhysiqueData fromNetwork(RegistryFriendlyByteBuf buf,IEntityData heldEntity);
}
