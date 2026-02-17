package net.thejadeproject.ascension.refactor_packages.forms.forms.soul;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.mortal_vessel.MortalVesselFormData;
import net.thejadeproject.ascension.refactor_packages.player_data.EntityData;

public class SoulForm implements IEntityForm {
    @Override
    public void enterForm(LivingEntity entity, IEntityFormData previousEntityFormData) {
        if(previousEntityFormData.getEntityFormId().equals(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"mortal_vessel")) && previousEntityFormData instanceof MortalVesselFormData mortalVesselData){
            createEntityVessel(entity,mortalVesselData);
            ((SoulFormData) EntityData.getEntityData(entity).getEntityFormData(this)).setTetheredEntity(mortalVesselData.getAttachedEntity());
        }
    }

    @Override
    public void leaveForm(LivingEntity entity) {
        //DO NOTHING
    }

    @Override
    public void newFormAddedPre(LivingEntity entity, ResourceLocation entityForm, IEntityFormData entityFormData) {

        if(entityForm.equals(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"mortal_vessel"))&&
                entityFormData instanceof MortalVesselFormData mortalVesselData){
            // the vessel being added is new
            //check if it is our tethered body
            SoulFormData soulFormData = (SoulFormData) EntityData.getEntityData(entity).getEntityFormData(this);
            if(!soulFormData.isTetheredTo(mortalVesselData.getAttachedEntity())){
                //the new vessel was not tethered to this soul
                //begin process of assimilating a new body
                assimilateNewVessel(entity,mortalVesselData);
            }

            //reset tether
            soulFormData.setTetheredEntity(entity);
        }
    }

    @Override
    public void newFormAddedPost(LivingEntity entity, ResourceLocation entityForm) {

    }


    //TODO create entity and attach the form data to it, and modify the form data attached entity
    private void createEntityVessel(LivingEntity entity,MortalVesselFormData previousFormData){

    }
    //TODO
    private void assimilateNewVessel(LivingEntity entity,MortalVesselFormData newVesselData){}



    @Override
    public IEntityFormData freshEntityFormData(LivingEntity entity) {
        return null;
    }

    @Override
    public IEntityFormData fromCompound(CompoundTag tag, LivingEntity entity) {
        return null;
    }

    @Override
    public IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf, LivingEntity entity) {
        return null;
    }
}
