package net.thejadeproject.ascension.refactor_packages.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

public class AscensionModifier implements IDataInstance {

    private final Operator operator;
    private final ResourceLocation modifierId;
    private final ResourceLocation groupId;
    private final double value;
    public AscensionModifier(Operator operator, ResourceLocation modifierId, double value){
        this(operator,modifierId,ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"default"),value);
    }
    public AscensionModifier(Operator operator, ResourceLocation modifierId, ResourceLocation groupId, double value){
        this.operator = operator;
        this.modifierId = modifierId;
        this.groupId = groupId;
        this.value = value;
    }


    public enum Operator{
       TRUE_ADD_BASE,
       ADD_BASE,
       MULTIPLY_BASE,
       MULTIPLY_FINAL,
   }

    public Operator getOperator(){return operator;}
    public ResourceLocation getModifierId(){return modifierId;}
    public ResourceLocation getGroupId(){
        return groupId == null ? ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"default") : groupId;
    }
    public double getValue(){return value;}


    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id",modifierId.toString());
        tag.putString("group_id",groupId.toString());
        tag.putString("operator",operator.name());
        tag.putDouble("value",getValue());
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        ByteBufHelper.encodeString(buf,modifierId.toString());
        ByteBufHelper.encodeString(buf,groupId.toString());
        ByteBufHelper.encodeString(buf,operator.name());
        buf.writeDouble(getValue());
    }

    public static AscensionModifier read(CompoundTag tag){
        ResourceLocation id = ResourceLocation.bySeparator(tag.getString("id"),':');
        ResourceLocation groupId = ResourceLocation.bySeparator(tag.getString("groupId"),':');
        Operator operator = Operator.valueOf(tag.getString("operator"));
        double val = tag.getDouble("value");
        return new AscensionModifier(operator,id,groupId,val);
    }
    public static AscensionModifier decode(RegistryFriendlyByteBuf buf){
        ResourceLocation id = ByteBufHelper.readResourceLocation(buf);
        ResourceLocation groupId = ByteBufHelper.readResourceLocation(buf);
        Operator operator = Operator.valueOf(ByteBufHelper.readString(buf));
        double val = buf.readDouble();
        return new AscensionModifier(operator,id,groupId,val);
    }
}
