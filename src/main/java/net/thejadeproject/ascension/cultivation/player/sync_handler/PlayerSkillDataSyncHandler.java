package net.thejadeproject.ascension.cultivation.player.sync_handler;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.IPreCastSkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import oshi.util.tuples.Pair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
//add some sort of sync buffer to PlayerSkillData so only new stuff is synced.
public class PlayerSkillDataSyncHandler implements AttachmentSyncHandler<PlayerSkillData> {
    //TODO create a function for reading and writing skill data to reduce bloat
    public static void encodeSkillMetaData(RegistryFriendlyByteBuf buf, PlayerSkillData.SkillMetaData skillMetaData){
        buf.writeInt(skillMetaData.skillId.length());
        buf.writeCharSequence(skillMetaData.skillId, Charset.defaultCharset());
        buf.writeBoolean(skillMetaData.fixed);
        buf.writeBoolean(skillMetaData.permanent);
        if(skillMetaData.data == null) buf.writeBoolean(false);
        else{
            buf.writeBoolean(true);
            skillMetaData.data.encode(buf);
        }
    }
    public static PlayerSkillData.SkillMetaData decodeSkillMetaData(RegistryFriendlyByteBuf buf){
        String skillId = (String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset());
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        boolean fixed = buf.readBoolean();
        boolean permanent = buf.readBoolean();
        IPersistentSkillData skillData= null;
        if(buf.readBoolean()){
            skillData = skill.getPersistentDataInstance(buf);
        }
        return new PlayerSkillData.SkillMetaData(skillId,fixed,permanent,skillData);
    }
    @Override
    public void write(RegistryFriendlyByteBuf buf, PlayerSkillData attachment, boolean initialSync) {
        // Write the attachment data to the buffer
        // If `initialSync` is true, you should write the entire attachment as the client does not have any prior data
        // If `initialSync` is false, you can choose to only write the data you would like to update
        buf.writeBoolean(initialSync);//if it is an initial sync or not(diff behaviour)
        //for non-initial sync there will also be a string header detailing what was changed(potentially)
        if(initialSync){
            
            List<PlayerSkillData.SkillMetaData> activeSkills = attachment.getActiveSkills();
            List<PlayerSkillData.SkillMetaData> passiveSkills = attachment.getPassiveSkills();
            List<PlayerSkillData.SkillSlot> slottedSkills = attachment.activeSkillContainer.getSkillIdList();


            //encode active skills
            buf.writeInt(activeSkills.size());
            for(PlayerSkillData.SkillMetaData activeSkill : activeSkills){
                
                encodeSkillMetaData(buf,activeSkill);
            }

            //encode passive skills
            buf.writeInt(passiveSkills.size());
            for(PlayerSkillData.SkillMetaData passiveSkill : passiveSkills){
                
                encodeSkillMetaData(buf,passiveSkill);
            }


            //encode slottedSkills
            
            

            for (PlayerSkillData.SkillSlot slottedSkill : slottedSkills){
                if(slottedSkill == PlayerSkillData.SkillSlot.EMPTY_SLOT){
                    buf.writeBoolean(false);
                    continue;
                }
                buf.writeBoolean(true);
                buf.writeInt(slottedSkill.skillId.toString().length());
                buf.writeCharSequence(slottedSkill.skillId.toString(),Charset.defaultCharset());
                buf.writeBoolean(slottedSkill.preCastSkillData != null);
                if(slottedSkill.preCastSkillData != null)slottedSkill.preCastSkillData.encode(buf);
            }
        }
        else{
            
            buf.writeBoolean(!attachment.getActiveSkillBuffer().isEmpty());
            if(!attachment.getActiveSkillBuffer().isEmpty()){
                
                buf.writeInt(attachment.getActiveSkillBuffer().size());
                for(Pair<Boolean, PlayerSkillData.SkillMetaData> data : attachment.getActiveSkillBuffer()){
                    buf.writeBoolean(data.getA());//true adding false removing
                    encodeSkillMetaData(buf,data.getB());
                }
            }
            buf.writeBoolean(!attachment.getPassiveSkillBuffer().isEmpty());
            if(!attachment.getPassiveSkillBuffer().isEmpty()){
                
                buf.writeInt(attachment.getPassiveSkillBuffer().size());
                for(Pair<Boolean, PlayerSkillData.SkillMetaData> data : attachment.getPassiveSkillBuffer()){
                    buf.writeBoolean(data.getA());//true adding false removing
                    encodeSkillMetaData(buf,data.getB());
                }
            }
            attachment.clearSkillBuffers();


            

            for (PlayerSkillData.SkillSlot slottedSkill : attachment.activeSkillContainer.getSkillIdList()){
                if(slottedSkill == PlayerSkillData.SkillSlot.EMPTY_SLOT){
                    buf.writeBoolean(false);
                    continue;
                }
                buf.writeBoolean(true);
                buf.writeInt(slottedSkill.skillId.toString().length());
                buf.writeCharSequence(slottedSkill.skillId.toString(),Charset.defaultCharset());
                buf.writeBoolean(slottedSkill.preCastSkillData != null);
                if(slottedSkill.preCastSkillData != null)slottedSkill.preCastSkillData.encode(buf);
            }


        }
       // buf.writeBoolean(true); //add or remove skill true add false remove
    }

    @Override
    public PlayerSkillData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, PlayerSkillData previousValue) {
        // Read the data from the buffer and return the new data attachment
        // `previousValue` is `null` if there was no prior data on the client
        // The result should return `null` if the data attachment should be removed
        
        if(previousValue == null) previousValue = new PlayerSkillData((Player) holder);
        if(buf.readBoolean()){
            //initial sync
            
            int activeSkillNum = buf.readInt();
            List<PlayerSkillData.SkillMetaData> activeSkills = new ArrayList<>();
            for(int i = 0;i<activeSkillNum;i++){

                activeSkills.add(decodeSkillMetaData(buf));
            }

            int passiveSkillNum = buf.readInt();
            List<PlayerSkillData.SkillMetaData> passiveSkills = new ArrayList<>();
            for(int i = 0;i<passiveSkillNum;i++){

                passiveSkills.add(decodeSkillMetaData(buf));
            }

            PlayerSkillData playerSkillData = new PlayerSkillData(previousValue.player,activeSkills,passiveSkills);
            for(int i = 0;i<playerSkillData.activeSkillContainer.MAX_SKILL_SLOTS;i++){

                if(!buf.readBoolean()){
                    //empty slot
                    playerSkillData.activeSkillContainer.setSlotEmpty(i);
                    continue;
                }
                String id = (String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset());
                ResourceLocation skillId = ResourceLocation.bySeparator(id,':');
                IPreCastSkillData preCastSkillData = null;
                if(buf.readBoolean()){
                    //has precastData
                    if(AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId) instanceof AbstractActiveSkill activeSkill){
                        preCastSkillData = activeSkill.getPreCastDataInstance(buf);
                    }
                }
                playerSkillData.activeSkillContainer.slotSkill(skillId,preCastSkillData,i);
            }
            return playerSkillData;
        }else {
            
            if(buf.readBoolean()){
                //active skills
                
                int skills = buf.readInt();
                for(int i = 0;i<skills;i++){
                    boolean action = buf.readBoolean();
                    PlayerSkillData.SkillMetaData skillMetaData = decodeSkillMetaData(buf);
                    if(action) previousValue.addActiveSkill(skillMetaData.skillId, skillMetaData.fixed,skillMetaData.permanent, skillMetaData.data);
                    else previousValue.removeActiveSkill(skillMetaData.skillId);
                }
            }
            if(buf.readBoolean()){
                //passive skills
                
                int skills = buf.readInt();
                
                for(int i = 0;i<skills;i++){

                    boolean action = buf.readBoolean();
                    PlayerSkillData.SkillMetaData skillMetaData = decodeSkillMetaData(buf);
                    
                    if(action) previousValue.addPassiveSkill(skillMetaData.skillId, skillMetaData.fixed,skillMetaData.permanent, skillMetaData.data);
                    else previousValue.removePassiveSkill(skillMetaData.skillId);
                }
            }

            //skill bar
            

            for(int i = 0;i<previousValue.activeSkillContainer.MAX_SKILL_SLOTS;i++){
                if(!buf.readBoolean()){
                    //empty slot
                    previousValue.activeSkillContainer.setSlotEmpty(i);
                    continue;
                }
                String id = (String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset());
                ResourceLocation skillId = ResourceLocation.bySeparator(id,':');
                IPreCastSkillData preCastSkillData = null;
                if(buf.readBoolean()){
                    //has precastData
                    if(AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId) instanceof AbstractActiveSkill activeSkill){
                        preCastSkillData = activeSkill.getPreCastDataInstance(buf);
                    }
                }
                previousValue.activeSkillContainer.slotSkill(skillId,preCastSkillData,i);
            }


            return previousValue;
        }


    }

    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        // Return whether the holder data is synced to the given player client
        // The players checked are different depending on the attachment holder:
        // - Block entities: All players tracking the chunk the block entity is within
        // - Chunk: All players tracking the chunk
        // - Entity: All players tracking the current entity, includes the current player if they are the attachment holder
        // - Level: All players in the current dimension / level

        // Example:
        // Only send the attachment if they are the attachment holder#
        
        return holder == to;
    }
}
