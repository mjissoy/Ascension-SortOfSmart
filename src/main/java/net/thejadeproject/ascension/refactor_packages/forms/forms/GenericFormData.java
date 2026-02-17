package net.thejadeproject.ascension.refactor_packages.forms.forms;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttributesContainer;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.stats.StatSheet;

import java.util.HashMap;

public abstract class GenericFormData implements IEntityFormData {

    private LivingEntity attachedEntity;
    private ResourceLocation entityForm;
    private HashMap<ResourceLocation,PathData> pathData = new HashMap<>();
    private final HeldSkills heldSkills;
    private IPhysiqueData physiqueData;
    private IBloodlineData bloodlineData;
    private final AscensionAttributesContainer attributesContainer = new AscensionAttributesContainer(this);
    private final StatSheet statSheet = new StatSheet();
    public GenericFormData(LivingEntity attachedEntity,ResourceLocation entityForm){
        this.attachedEntity = attachedEntity;
        this.entityForm = entityForm;
        this.heldSkills = new HeldSkills(attachedEntity);
    }


    @Override
    public LivingEntity getAttachedEntity() {
        return attachedEntity;
    }

    @Override
    public void setAttachedEntity(LivingEntity attachedEntity) {
        this.attachedEntity = attachedEntity;
    }

    @Override
    public IEntityForm getEntityForm() {
        return AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(entityForm);
    }

    @Override
    public ResourceLocation getEntityFormId() {
        return entityForm;
    }

    @Override
    public PathData getPathData(ResourceLocation path) {
        return pathData.get(path);
    }

    @Override
    public boolean hasPathData(ResourceLocation path) {
        return pathData.containsKey(path);
    }

    @Override
    public HeldSkills getHeldSkills() {
        return heldSkills;
    }

    @Override
    public IPhysiqueData getPhysiqueData() {
        return physiqueData;
    }

    @Override
    public void setPhysiqueData(IPhysiqueData data) {
        physiqueData = data;
    }

    @Override
    public IBloodlineData getBloodlineData() {
        return bloodlineData;
    }

    @Override
    public void setBloodlineData(IBloodlineData data) {
        bloodlineData = data;
    }

    @Override
    public AscensionAttributesContainer getAttributeContainer() {
        return attributesContainer;
    }

    @Override
    public StatSheet getStatSheet() {
        return statSheet;
    }

    //TODO =====================
    @Override
    public void syncHeldSkills() {

    }

    @Override
    public void syncPhysiqueData() {

    }

    @Override
    public void syncBloodlineData() {

    }

    @Override
    public void syncStatSheet() {

    }

    @Override
    public void syncAllPathData() {

    }

    @Override
    public void syncPathData(ResourceLocation path) {

    }

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {

    }
    //TODO END =====================
}
