package net.thejadeproject.ascension.refactor_packages.skills;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HeldSkills {

    private IEntityData attachedEntityData;

    private final HashMap<ResourceLocation,HeldSkill> skills = new HashMap<>();


    private final ArrayList<HeldSkill> modifiedSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> additionSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> removalSyncBuffer = new ArrayList<>();


    public HeldSkills(IEntityData attachedEntityData){
        this.attachedEntityData = attachedEntityData;
    }
    public IEntityData getAttachedEntity(){
        return attachedEntityData;
    }
    public void setAttachedEntity(IEntityData entityData){
        this.attachedEntityData = entityData;
    }
    public void addSkill(ISkill skill,boolean fixed,boolean permanent){
        //TODO IMPLEMENT
    }
    public void addSkill(ResourceLocation skillKey,boolean fixed,boolean permanent){
        addSkill(skillKey,fixed,permanent,null);
    }
    public void addSkill(ResourceLocation skillKey,boolean fixed,boolean permanent,ResourceLocation source){
        if(skills.containsKey(skillKey)){
            HeldSkill heldSkill = skills.get(skillKey);
            if(!heldSkill.isFixed() && fixed) heldSkill.setFixed(true);
            if(!heldSkill.isPermanent() && permanent) heldSkill.setPermanent(true);
            if(source != null)heldSkill.addSource(source);
            markDirty(skillKey);
        }else {
            HeldSkill heldSkill = new HeldSkill(skillKey);
            heldSkill.setPermanent(permanent);
            heldSkill.setFixed(fixed);
            if(source != null) heldSkill.addSource(source);
            skills.put(skillKey,heldSkill);
            additionSyncBuffer.add(heldSkill);
            heldSkill.getSkill().onAdded(attachedEntityData);
        }
    }


    //does not mark as dirty since this is only used client side
    private void updateModifiedSkill(HeldSkill skill){
        HeldSkill heldSkill =  skills.get(skill.getKey());
        heldSkill.setPersistentData(skill.getPersistentData());
        heldSkill.setFixed(skill.isFixed());
        heldSkill.setPermanent(skill.isPermanent());
        heldSkill.setSkillVersion(skill.getSkillVersion());
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

    public void removeSkill(ResourceLocation skillKey){
        if(!skills.containsKey(skillKey))return;
        HeldSkill heldSkill = skills.remove(skillKey);
        heldSkill.getSkill().onRemoved(attachedEntityData,heldSkill.getPersistentData());
        removalSyncBuffer.add(heldSkill);

    }
    /*
        used when skill data is stored on a different tethered entity data and they are unlinked
     */
    public void removeAllSkillsFromEntityData(IEntityData targetEntityData){
        for(HeldSkill heldSkill : skills.values()){
            heldSkill.getSkill().removeForTetheredEntity(attachedEntityData,targetEntityData,heldSkill.getPersistentData());
        }
    }

    public boolean hasSkill(ResourceLocation skillKey){
        return skills.containsKey(skillKey);
    }

    private void clearBuffers(){
        additionSyncBuffer.clear();
        modifiedSyncBuffer.clear();
        removalSyncBuffer.clear();
    }

    public ListTag write(){
        ListTag skills = new ListTag();
        for (HeldSkill heldSkill :this.skills.values()){
            skills.add(heldSkill.write());
        }
        return skills;
    }
    public void read(ListTag tag){
        for(int i =0;i<tag.size();i++){
            HeldSkill heldSkill = HeldSkill.read(tag.getCompound(i));
            skills.put(heldSkill.getKey(),heldSkill);
        }
    }


    //============================== NETWORK =================================


    public void decode(RegistryFriendlyByteBuf buf){
        if(buf.readBoolean())decodeChanges(buf);
        else decodeFull(buf);
    }

    private void decodeFull(RegistryFriendlyByteBuf buf){
        for(int i = 0;i<buf.readInt();i++){
            HeldSkill skill = HeldSkill.decode(buf);
            skills.put(skill.getKey(),skill);
        }
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
        else encodeFull(buf);
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
    private void encodeFull(RegistryFriendlyByteBuf buf){
        buf.writeInt(skills.size());
        for(HeldSkill skill : skills.values()){
            skill.encode(buf);
        }
    }

}
