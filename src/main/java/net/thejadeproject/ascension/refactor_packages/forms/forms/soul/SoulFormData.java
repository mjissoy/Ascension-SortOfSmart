package net.thejadeproject.ascension.refactor_packages.forms.forms.soul;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.forms.GenericFormData;

import java.util.UUID;

public class SoulFormData extends GenericFormData {

    private UUID tetheredEntity;    //the "body" the soul is currently tethered to

    public SoulFormData(LivingEntity attachedEntity, ResourceLocation entityForm) {
        super(attachedEntity, entityForm);
    }

    //TODO setup some stuff
    public void setTetheredEntity(LivingEntity entity){
        tetheredEntity = entity.getUUID();
    }
    public boolean isTetheredTo(LivingEntity entity){
        return tetheredEntity.equals(entity.getUUID());
    }

    @Override
    public void changeFormTo(IEntityForm form) {

    }
}
