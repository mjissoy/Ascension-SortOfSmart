package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.util.ModAttachments;

public class PlayerAttributeManager {

    public static void increaseAttribute(Player player, Double value, Holder<Attribute> attributeHolder){
        player.getAttribute(attributeHolder).setBaseValue(player.getAttributeBaseValue(attributeHolder) + value);
        if(attributeHolder == Attributes.ATTACK_DAMAGE){
            PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncAttackDamageAttribute(player.getAttribute(attributeHolder).getBaseValue()));
        }
        if(attributeHolder == Attributes.MOVEMENT_SPEED){
            player.setData(ModAttachments.MOVEMENT_SPEED,player.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
        }
    }
    public static void decreaseAttribute(Player player, Double value, Holder<Attribute> attributeHolder){
        player.getAttribute(attributeHolder).setBaseValue(Math.max(player.getAttributeBaseValue(attributeHolder) - value,0));
        if(attributeHolder == Attributes.ATTACK_DAMAGE){
            PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncAttackDamageAttribute(player.getAttribute(attributeHolder).getBaseValue()));
        }
        if(attributeHolder == Attributes.MOVEMENT_SPEED){
            player.setData(ModAttachments.MOVEMENT_SPEED,player.getAttributeBaseValue(Attributes.MOVEMENT_SPEED));
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
