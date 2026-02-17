package net.thejadeproject.ascension.refactor_packages.forms.forms.mortal_vessel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.forms.GenericFormData;

public class MortalVesselFormData extends GenericFormData {
    public MortalVesselFormData(LivingEntity attachedEntity, ResourceLocation entityForm) {
        super(attachedEntity, entityForm);
    }

    @Override
    public void changeFormTo(IEntityForm form) {

    }
}
