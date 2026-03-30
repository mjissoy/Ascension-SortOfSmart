package net.thejadeproject.ascension.refactor_packages.skills;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class HeldSkills {




    private final HashMap<ResourceLocation,HeldSkill> skills = new HashMap<>();


    private final ArrayList<HeldSkill> modifiedSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> additionSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> removalSyncBuffer = new ArrayList<>();


    /*
        if a skill is fixed it just means if a technique is removed the skill stays,
        ideally this should be set up such that when loading the data the technique history is able to add the skill
        so it should not be a situation where a player can keep a skill after LOSING cultivation since lost realms are not tracked

        this is a limitation of the system that we will have to deal with

        should i even handle it like this though? or should i instead removed the idea of fixed and offload removal checks?

        like an on remove event that can be cancelled? so if for example i try to remove a skill that another source gives it can cancel it
        
     */

    public void addSkill(ResourceLocation skill,IPersistentSkillData skillData){
        HeldSkill heldSkill = new HeldSkill(skill);
        heldSkill.setPersistentData(skillData);
        skills.put(skill,heldSkill);
        additionSyncBuffer.add(heldSkill);
    }


    //does not mark as dirty since this is only used client side
    private void updateModifiedSkill(HeldSkill skill){
        HeldSkill heldSkill =  skills.get(skill.getKey());
        heldSkill.setPersistentData(skill.getPersistentData());

    }


    public HeldSkill getHeldSkill(ResourceLocation skillKey){
        return skills.get(skillKey);
    }

    public void setPersistentData(ResourceLocation skillKey,IPersistentSkillData persistentData){
        if(!skills.containsKey(skillKey)) return;
        skills.get(skillKey).setPersistentData(persistentData);
        markDirty(skillKey);
    }


    public void markDirty(ResourceLocation skillKey){
        if(!skills.containsKey(skillKey)) return;
        modifiedSyncBuffer.add(skills.get(skillKey));
    }

    public IPersistentSkillData removeSkill(ResourceLocation skillKey){
        if(!skills.containsKey(skillKey))return null;
        HeldSkill heldSkill = skills.remove(skillKey);
        removalSyncBuffer.add(heldSkill);
        return heldSkill.getPersistentData();

    }

    public boolean hasSkill(ResourceLocation skillKey){
        return skills.containsKey(skillKey);
    }
    public Collection<HeldSkill> getSkills(){
        return skills.values();
    }
    private void clearBuffers(){
        additionSyncBuffer.clear();
        modifiedSyncBuffer.clear();
        removalSyncBuffer.clear();
    }




    //============================== NETWORK =================================
    public static HeldSkills decodeFull(RegistryFriendlyByteBuf buf){
        HeldSkills heldSkills = new HeldSkills();
        for(int i =0;i<buf.readInt();i++){
            HeldSkill heldSkill = HeldSkill.decode(buf);
            heldSkills.skills.put(heldSkill.getKey(),heldSkill);
        }
        return heldSkills;
    }
    public static void encodeFull(RegistryFriendlyByteBuf buf,HeldSkills heldSkills){

        buf.writeInt(heldSkills.skills.size());
        for(HeldSkill skill : heldSkills.skills.values()){
            skill.encode(buf);
        }
    }


    public void decode(RegistryFriendlyByteBuf buf){

        if(buf.readBoolean())decodeChanges(buf);
        else decodeFull(buf);
    }

    private void decodeChanges(RegistryFriendlyByteBuf buf){
        //added
        for(int i =0;i<buf.readInt();i++){
            HeldSkill skill = HeldSkill.decode(buf);
            skills.put(skill.getKey(),skill);
        }
        //removed
        for(int i =0;i<buf.readInt();i++){
            skills.remove(ByteBufHelper.readResourceLocation(buf));
        }

        //modified skills
        for(int i =0;i<buf.readInt();i++){
            updateModifiedSkill(HeldSkill.decode(buf));
        }


        if(FMLLoader.getDist() != Dist.CLIENT) clearBuffers(); //should prevent accidental memory leak
    }


    public void encode(RegistryFriendlyByteBuf buf,boolean onlyChanges){

        buf.writeBoolean(onlyChanges);
        if(onlyChanges) encodeChanges(buf);

    }
    private void encodeChanges(RegistryFriendlyByteBuf buf){
        //write added

        buf.writeInt(additionSyncBuffer.size());
        for(HeldSkill heldSkill : additionSyncBuffer){
            heldSkill.encode(buf);
        }
        //write removed
        buf.writeInt(removalSyncBuffer.size());
        for(HeldSkill heldSkill : removalSyncBuffer){
            ByteBufHelper.encodeString(buf,heldSkill.getKey().toString());
        }
        //write modified
        buf.writeInt(modifiedSyncBuffer.size());
        for(HeldSkill heldSkill : modifiedSyncBuffer){
            heldSkill.encode(buf);
        }
        clearBuffers();

    }


}
