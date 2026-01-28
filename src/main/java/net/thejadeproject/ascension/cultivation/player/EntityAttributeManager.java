package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class EntityAttributeManager {

    public static void increaseAttribute(LivingEntity entity, Double value, Holder<Attribute> attributeHolder){

            entity.getAttribute(attributeHolder).setBaseValue(entity.getAttributeBaseValue(attributeHolder) + value);
            if(!(entity instanceof  Player)) return;
            if(attributeHolder == Attributes.ATTACK_DAMAGE){
                PacketDistributor.sendToPlayer((ServerPlayer) entity,new SyncAttackDamageAttribute(entity.getAttribute(attributeHolder).getBaseValue()));
            }
            if(attributeHolder == Attributes.MOVEMENT_SPEED){
                entity.setData(ModAttachments.MOVEMENT_SPEED,entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
            }



    }
    public static void decreaseAttribute(LivingEntity entity, Double value, Holder<Attribute> attributeHolder){

            entity.getAttribute(attributeHolder).setBaseValue(entity.getAttributeBaseValue(attributeHolder) - value);
            if(!(entity instanceof  Player)) return;
            if(attributeHolder == Attributes.ATTACK_DAMAGE){
                PacketDistributor.sendToPlayer((ServerPlayer) entity,new SyncAttackDamageAttribute(entity.getAttribute(attributeHolder).getBaseValue()));
            }
            if(attributeHolder == Attributes.MOVEMENT_SPEED){
                entity.setData(ModAttachments.MOVEMENT_SPEED,entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
        }

    }
    public static void changeAttributeRange(double min, double max, RangedAttribute attribute){
        attribute.minValue = min;
        attribute.maxValue = max;
    }
    public static void changeDefaultValue(double defaultValue,Attribute attribute){
        attribute.defaultValue = defaultValue;
    }
}
