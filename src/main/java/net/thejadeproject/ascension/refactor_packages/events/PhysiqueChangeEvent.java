package net.thejadeproject.ascension.refactor_packages.events;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;


//the physique removed method is called BEFORE this event. same with physique added
public class PhysiqueChangeEvent extends Event {
    public final ResourceLocation oldPhysique;
    public final IPhysiqueData oldPhysiqueData;

    public final ResourceLocation newPhysique;
    public final IPhysiqueData newPhysiqueData;


    public final IEntityData entityData;


    public PhysiqueChangeEvent(ResourceLocation oldPhysique, IPhysiqueData oldPhysiqueData, ResourceLocation newPhysique, IPhysiqueData newPhysiqueData, IEntityData entityData) {
        this.oldPhysique = oldPhysique;
        this.oldPhysiqueData = oldPhysiqueData;
        this.newPhysique = newPhysique;
        this.newPhysiqueData = newPhysiqueData;
        this.entityData = entityData;
    }

    public IPhysique getOldPhysique(){
        return AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(oldPhysique);

    }
    public IPhysique getNewPhysique(){
        return AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(newPhysique);
    }
}
