package net.thejadeproject.ascension.cultivation.player.sync_handler;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import oshi.util.tuples.Pair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
//add some sort of sync buffer to PlayerSkillData so only new stuff is synced.
public class PlayerSkillDataSyncHandler implements AttachmentSyncHandler<PlayerSkillData> {
    //TODO create a function for reading and writing skill data to reduce bloat
    public static void encodeSkillData(RegistryFriendlyByteBuf buf, PlayerSkillData.SkillData skillData){
        buf.writeInt(skillData.skillId.length());
        buf.writeCharSequence(skillData.skillId, Charset.defaultCharset());
        buf.writeBoolean(skillData.fixed);
        if(skillData.data == null) buf.writeBoolean(false);
        else{
            buf.writeBoolean(true);
            skillData.data.encode(buf);
        }
    }
    public static PlayerSkillData.SkillData decodeSkillData(RegistryFriendlyByteBuf buf){
        String skillId = (String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset());
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        boolean fixed = buf.readBoolean();
        ISkillData skillData= null;
        if(buf.readBoolean()){
            skillData = skill.decode(buf);
        }
        return new PlayerSkillData.SkillData(skillId,fixed,skillData);
    }
    @Override
    public void write(RegistryFriendlyByteBuf buf, PlayerSkillData attachment, boolean initialSync) {
        // Write the attachment data to the buffer
        // If `initialSync` is true, you should write the entire attachment as the client does not have any prior data
        // If `initialSync` is false, you can choose to only write the data you would like to update
        buf.writeBoolean(initialSync);//if it is an initial sync or not(diff behaviour)
        //for non-initial sync there will also be a string header detailing what was changed(potentially)
        if(initialSync){
            System.out.println("initial sync");
            List<PlayerSkillData.SkillData> activeSkills = attachment.getActiveSkills();
            List<PlayerSkillData.SkillData> passiveSkills = attachment.getPassiveSkills();
            List<String> slottedSkills = attachment.activeSkillContainer.getSkillIdList();


            //encode active skills
            buf.writeInt(activeSkills.size());
            for(PlayerSkillData.SkillData activeSkill : activeSkills){
                System.out.println("active skills");
                encodeSkillData(buf,activeSkill);
            }

            //encode passive skills
            buf.writeInt(passiveSkills.size());
            for(PlayerSkillData.SkillData passiveSkill : passiveSkills){
                System.out.println("passive skills");
                encodeSkillData(buf,passiveSkill);
            }


            //encode slottedSkills
            buf.writeInt(slottedSkills.size());
            for (String slottedSkill : slottedSkills){
                buf.writeInt(slottedSkill.length());
                buf.writeCharSequence(slottedSkill,Charset.defaultCharset());
            }
        }
        else{
            System.out.println("normal sync");
            buf.writeBoolean(!attachment.getActiveSkillBuffer().isEmpty());
            if(!attachment.getActiveSkillBuffer().isEmpty()){
                System.out.println("active skills");
                buf.writeInt(attachment.getActiveSkillBuffer().size());
                for(Pair<Boolean, PlayerSkillData.SkillData> data : attachment.getActiveSkillBuffer()){
                    buf.writeBoolean(data.getA());//true adding false removing
                    encodeSkillData(buf,data.getB());
                }
            }
            buf.writeBoolean(!attachment.getPassiveSkillBuffer().isEmpty());
            if(!attachment.getPassiveSkillBuffer().isEmpty()){
                System.out.println("passive skills");
                buf.writeInt(attachment.getPassiveSkillBuffer().size());
                for(Pair<Boolean, PlayerSkillData.SkillData> data : attachment.getPassiveSkillBuffer()){
                    buf.writeBoolean(data.getA());//true adding false removing
                    encodeSkillData(buf,data.getB());
                }
            }
            attachment.clearSkillBuffers();
            buf.writeBoolean(attachment.activeSkillContainer.changed);
            if(attachment.activeSkillContainer.changed){
                System.out.println("skill bar");
                System.out.println(attachment.activeSkillContainer.changed);
                buf.writeInt(attachment.activeSkillContainer.getSkillIdList().size());
                for (String slottedSkill : attachment.activeSkillContainer.getSkillIdList()){
                    buf.writeInt(slottedSkill.length());
                    buf.writeCharSequence(slottedSkill,Charset.defaultCharset());
                }
            }
            attachment.activeSkillContainer.changed = false;
        }
       // buf.writeBoolean(true); //add or remove skill true add false remove
    }

    @Override
    public PlayerSkillData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, PlayerSkillData previousValue) {
        // Read the data from the buffer and return the new data attachment
        // `previousValue` is `null` if there was no prior data on the client
        // The result should return `null` if the data attachment should be removed
        System.out.println("reading data");
        if(previousValue == null) previousValue = new PlayerSkillData(Minecraft.getInstance().player);
        if(buf.readBoolean()){
            //initial sync
            System.out.println("initial sync");
            int activeSkillNum = buf.readInt();
            List<PlayerSkillData.SkillData> activeSkills = new ArrayList<>();
            for(int i = 0;i<activeSkillNum;i++){

                activeSkills.add(decodeSkillData(buf));
            }

            int passiveSkillNum = buf.readInt();
            List<PlayerSkillData.SkillData> passiveSkills = new ArrayList<>();
            for(int i = 0;i<passiveSkillNum;i++){

                passiveSkills.add(decodeSkillData(buf));
            }
            int skillSlots = buf.readInt();
            System.out.println("skill slots "+ skillSlots);
            List<String> slottedSkills = new ArrayList<>();
            for(int i = 0;i<skillSlots;i++){
                slottedSkills.add((String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset()));
            }
            return new PlayerSkillData(previousValue.player,activeSkills,passiveSkills,slottedSkills);
        }else {
            System.out.println("normal sync");
            if(buf.readBoolean()){
                //active skills
                System.out.println("active skills");
                int skills = buf.readInt();
                for(int i = 0;i<skills;i++){
                    boolean action = buf.readBoolean();
                    PlayerSkillData.SkillData skillData = decodeSkillData(buf);
                    if(action) previousValue.addActiveSkill(skillData.skillId, skillData.fixed,skillData.data);
                    else previousValue.removeActiveSkill(skillData.skillId);
                }
            }
            if(buf.readBoolean()){
                //passive skills
                System.out.println("passive skills");
                int skills = buf.readInt();
                System.out.println(skills);
                for(int i = 0;i<skills;i++){

                    boolean action = buf.readBoolean();
                    PlayerSkillData.SkillData skillData = decodeSkillData(buf);
                    System.out.println(skillData.toString());
                    if(action) previousValue.addPassiveSkill(skillData.skillId, skillData.fixed,skillData.data);
                    else previousValue.removePassiveSkill(skillData.skillId);
                }
            }
            if(buf.readBoolean()){
                //skill bar
                System.out.println("skill bar");
                int skillSlots = buf.readInt();
                System.out.println(skillSlots);
                List<String> slottedSkills = new ArrayList<>();
                for(int i = 0;i<skillSlots;i++){
                    slottedSkills.add((String) buf.readCharSequence(buf.readInt(),Charset.defaultCharset()));
                }
                previousValue.activeSkillContainer.skillIdList = slottedSkills;
                previousValue.activeSkillContainer.changed = true;//so the ui knows to refresh for hot-bar
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
        System.out.println("SYNCING SKILL DATA WITH CLIENT");
        return holder == to;
    }
}
