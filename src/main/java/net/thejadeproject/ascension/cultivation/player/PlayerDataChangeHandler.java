package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.util.ModAttachments;

public class PlayerDataChangeHandler {

    public static void resetData(Player player){
        PlayerSkillData skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        skillData.removeAllSkills();
        String oldPhysique = player.getData(ModAttachments.PHYSIQUE);
        player.setData(ModAttachments.PHYSIQUE,"ascension:empty_vessel");
        NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player,oldPhysique,"ascension:empty_vessel"));

        player.setData(ModAttachments.PLAYER_SKILL_DATA,new PlayerSkillData(player));
        player.setData(ModAttachments.MOVEMENT_SPEED,0.1);
        player.setData(ModAttachments.ATTACK_DAMAGE, 1.0);
        player.setData(ModAttachments.JUMP_HEIGHT, 0.42);
        player.setData(ModAttachments.MAX_HEALTH, 20.0);
        player.setData(ModAttachments.SAFE_FALL_DISTANCE, 3.0);
        player.setData(ModAttachments.ARMOR, 0.0);
        player.setData(ModAttachments.OXYGEN_BONUS, 0.0);
        player.setData(ModAttachments.WATER_MOVEMENT_EFFICIENCY, 0.0);

        AttributeSupplier playerSupplier = Player.createAttributes().build();
        for(Holder<Attribute> attributeHolder :player.getAttributes().attributes.keySet()){
            if(playerSupplier.hasAttribute(attributeHolder)){
                AttributeInstance instance = player.getAttributes().attributes.get(attributeHolder);
                instance.setBaseValue(playerSupplier.getBaseValue(attributeHolder));
                instance.removeModifiers();
            }else{
                AttributeInstance instance = player.getAttributes().attributes.get(attributeHolder);
                instance.setBaseValue(attributeHolder.value().getDefaultValue());
                instance.removeModifiers();
            }
        }
        
        //PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPlayerPhysique("ascension:empty_vessel"));
        for(CultivationData.PathData pathData : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()){
            
            
            PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPathDataPayload(
                    pathData.pathId,
                    0,
                    0,
                    0,
                    "ascension:none",0
            ));

        }
        player.setData(ModAttachments.PLAYER_DATA ,new PlayerData(player));

        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncAttackDamageAttribute(player.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        //PacketDistributor.sendToPlayer((ServerPlayer) player,new Sync(player.getAttributeValue(Attributes.ATTACK_DAMAGE)));

    }
}
