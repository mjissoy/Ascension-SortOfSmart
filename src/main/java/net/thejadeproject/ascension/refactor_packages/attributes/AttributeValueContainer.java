package net.thejadeproject.ascension.refactor_packages.attributes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.StatInstance;
import net.thejadeproject.ascension.refactor_packages.stats.StatSheet;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainer;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

import java.util.HashMap;

/**
 * need to be careful when handling remote entity data. this should only be held on Loaded entities
 * so when i store the entity data remotely ensure i remove this and add it to the remote instance
 *<br>
 * the remote entity data should listen to stat changed event and then "refresh"
 *<br>
 * TODO remember to save the suppression values
 */
public class AttributeValueContainer extends ValueContainer {

    private static final ResourceLocation CULTIVATION_APPLIED_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "cultivation_applied");

    private static final ResourceLocation SUPPRESSION_APPLIED_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "suppression_applied");

    private final Holder<Attribute> attributeHolder;
    private  LivingEntity attachedEntity;
    public AttributeValueContainer(LivingEntity attachedEntity,Holder<Attribute> attributeHolder, Component valueName) {
        super(attributeHolder.getKey().location(), valueName, 0);
        this.attributeHolder = attributeHolder;
        this.attachedEntity =attachedEntity;
        calculateCachedVal();
    }
    private final HashMap<Stat,ValueContainer> statMultipliers = new HashMap<>();
    double cachedBaseStat;
    double cachedAttributeValue;
    double suppressedValue;
    boolean suppressed;
    private double suppressionPercent;

    @Override
    public void calculateCachedVal() {
        if(cachedAttributeValue+cachedBaseStat != getBaseValue()) setBaseValue(cachedAttributeValue+cachedBaseStat);
        super.calculateCachedVal();
    }
    public Holder<Attribute> getAttributeHolder(){return attributeHolder;}
    public void setAttachedEntity(LivingEntity entity){this.attachedEntity = entity;}
    public void addStatScaling(Stat stat, double scaling){
        if(!statMultipliers.containsKey(stat)) statMultipliers.put(stat,new ValueContainer(
                AscensionRegistries.Stats.STATS_REGISTRY.getKey(stat),
                stat.getDisplayName(),
                0
                ));
        statMultipliers.get(stat).setBaseValue(Math.max(0,statMultipliers.get(stat).getBaseValue()+scaling));
    }
    public void removeStatScaling(Stat stat,double scaling){
        if(!statMultipliers.containsKey(stat)) return;

        addStatScaling(stat,-scaling);
    }
    public void addStatScalingModifier(Stat stat,ValueContainerModifier modifier){
        if(!statMultipliers.containsKey(stat)) statMultipliers.put(stat,new ValueContainer(
                AscensionRegistries.Stats.STATS_REGISTRY.getKey(stat),
                stat.getDisplayName(),
                0
        ));
        statMultipliers.get(stat).addModifier(modifier);

    }

    public void updateValue(IEntityData entityData){
        StatSheet statSheet = entityData.getActiveFormData().getStatSheet();

        double baseVal = 0;
        for (Stat stat : statMultipliers.keySet()){
            StatInstance instance = statSheet.getStatInstance(stat);
            if (instance != null) {
                baseVal += instance.getValue() * statMultipliers.get(stat).getValue();
            }
        }
        cachedBaseStat = baseVal;

        calculateCachedVal();
        applyLiveModifiers();
    }

    private void applyLiveModifiers() {
        if (attachedEntity == null) return;

        AttributeInstance inst = attachedEntity.getAttribute(attributeHolder);
        if (inst == null) return;

        inst.removeModifier(CULTIVATION_APPLIED_ID);
        inst.removeModifier(SUPPRESSION_APPLIED_ID);

        double base = cachedAttributeValue;
        double full = getUnsuppressedValue();
        double cultivationBonus = full - base;

        if (cultivationBonus != 0.0) {
            inst.addTransientModifier(new AttributeModifier(
                    CULTIVATION_APPLIED_ID,
                    cultivationBonus,
                    AttributeModifier.Operation.ADD_VALUE
            ));
        }

        if (isSuppressed()) {
            double shown = getSuppressedValue();
            double suppressionDelta = shown - full;

            if (suppressionDelta != 0.0) {
                inst.addTransientModifier(new AttributeModifier(
                        SUPPRESSION_APPLIED_ID,
                        suppressionDelta,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            }
        }
    }


    public void validateAttributeValue() {
        if (attachedEntity == null) return;

        AttributeInstance inst = attachedEntity.getAttribute(attributeHolder);
        if (inst == null) return;

        double base = inst.getBaseValue();

        if (base != cachedAttributeValue) {
            cachedAttributeValue = base;
            calculateCachedVal();
        }
    }

    public double getSuppressionPercent() {
        return suppressionPercent;
    }

    public void setSuppressionPercent(double suppressionPercent) {
        this.suppressionPercent = Math.max(0.0, Math.min(1.0, suppressionPercent));
        this.suppressed = this.suppressionPercent > 0.0;
    }

    @Override
    public double getValue() {
        return isSuppressed() ? getSuppressedValue() : getUnsuppressedValue();
    }

    public double getUnsuppressedValue() {
        validateAttributeValue();
        return super.getValue();
    }

    @Override
    public double getBaseValue() {
        validateAttributeValue();
        return super.getBaseValue();
    }

    public double getSuppressedValue() {
        double full = getUnsuppressedValue();
        return full * (1.0 - suppressionPercent);
    }

    public void setSuppressed(boolean suppressed){
        this.suppressed = suppressed;
    }

    public boolean isSuppressed() {
        return suppressionPercent > 0.0;
    }

    public static void encode(RegistryFriendlyByteBuf buf, AttributeValueContainer container){
        ValueContainer.encode(buf, container);
        buf.writeInt(container.statMultipliers.size());
        for (ValueContainer statContainer : container.statMultipliers.values()) {
            ValueContainer.encode(buf, statContainer);
        }
        buf.writeDouble(container.suppressionPercent);
    }

    public static AttributeValueContainer decode(RegistryFriendlyByteBuf buf){
        ResourceLocation identifier = ByteBufHelper.readResourceLocation(buf);
        Component displayName = ComponentSerialization.STREAM_CODEC.decode(buf);
        double base = buf.readDouble();
        int modifierNumber = buf.readInt();

        AttributeValueContainer container = new AttributeValueContainer(
                null,
                BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(identifier)),
                displayName
        );
        container.setBaseValue(base);

        for (int i = 0; i < modifierNumber; i++) {
            container.addModifier(ValueContainerModifier.decode(buf));
        }

        int statContainers = buf.readInt();
        for (int i = 0; i < statContainers; i++) {
            ValueContainer statContainer = ValueContainer.decode(buf);
            container.statMultipliers.put(
                    AscensionRegistries.Stats.STATS_REGISTRY.get(statContainer.getIdentifier()),
                    statContainer
            );
        }

        container.suppressionPercent = buf.readDouble();
        container.suppressed = container.suppressionPercent > 0.0;
        return container;
    }

    public void log() {
        System.out.print(getDisplayName().getString() + " : ");
        System.out.print(getValue());
        if (isSuppressed()) System.out.print(" (" + getUnsuppressedValue() + ")");
        System.out.println(" base : " + getBaseValue());
    }


}
