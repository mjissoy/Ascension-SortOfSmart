package net.thejadeproject.ascension.capabilities.implementation;

import net.lucent.formation_arrays.api.capability.IFormationHolder;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.api.registries.FormationRegistry;
import net.lucent.formation_arrays.data_components.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FormationHolder implements IFormationHolder {
    @Override
    public ResourceLocation getFormationResourceLocation(ItemStack stack) {
        if(!stack.has(ModDataComponents.FORMATION_PLATE_COMPONENT)) return null;
        return ResourceLocation.bySeparator(stack.get(ModDataComponents.FORMATION_PLATE_COMPONENT),':');
    }
    @Override
    public IFormation getFormation(ItemStack itemStack){
        ResourceLocation location = getFormationResourceLocation(itemStack);
        if(location == null) return null;
        return FormationRegistry.FORMATION_REGISTRY.get(getFormationResourceLocation(itemStack));
    }
}
