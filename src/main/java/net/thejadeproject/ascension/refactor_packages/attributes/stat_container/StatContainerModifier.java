package net.thejadeproject.ascension.refactor_packages.attributes.stat_container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

public class StatContainerModifier implements IDataInstance {
    private final Operator operator;
    private final ResourceLocation id;
    private final double value;

    public StatContainerModifier(Operator operator, ResourceLocation id, double value) {
        this.operator = operator;
        this.id = id;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public ResourceLocation getId() {
        return id;
    }

    public enum Operator{
        PERCENTAGE,//add to the scaling percentage
        EFFECTIVENESS,//add to the effectiveness
    }

    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("modifier_id",id.toString());
        tag.putString("operator",operator.name());
        tag.putDouble("value",value);
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        ByteBufHelper.encodeString(buf,id.toString());
        ByteBufHelper.encodeString(buf,operator.name());
        buf.writeDouble(value);
    }
    public static StatContainerModifier read(CompoundTag tag){
        ResourceLocation id = ResourceLocation.bySeparator(tag.getString("modifier_id"),':');
        Operator operator = Operator.valueOf(tag.getString("operator"));
        double val = tag.getDouble("value");
        return new StatContainerModifier(operator,id,val);
    }
    public static StatContainerModifier decode(RegistryFriendlyByteBuf buf){
        ResourceLocation id = ByteBufHelper.readResourceLocation(buf);
        Operator operator = Operator.valueOf(ByteBufHelper.readString(buf));
        double val = buf.readDouble();
        return new StatContainerModifier(operator,id,val);
    }
}
