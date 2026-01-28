package net.thejadeproject.ascension.entity.custom.formation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.thejadeproject.ascension.formations.IDummyListenerNode;

import java.util.List;

public class DummyEntity extends LivingEntity {
    public IDummyListenerNode node;
    public DummyEntity(EntityType<? extends LivingEntity> entityType, Level level, IDummyListenerNode node) {
        super(entityType, level);
        this.node = node;
    }


    @Override
    public boolean shouldRender(double x, double y, double z) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createLivingAttributes().add(Attributes.STEP_HEIGHT, 0.0);
    }

    public DummyEntity(EntityType<? extends  LivingEntity> entityType, Level level) {
        super(entityType, level);
    }



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }




    @Override
    public boolean canBeHitByProjectile() {
        return true;
    }


    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(super.hurt(source, amount) && node != null){
            node.onHurt(source,amount);
            return true;
        }
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }
}
