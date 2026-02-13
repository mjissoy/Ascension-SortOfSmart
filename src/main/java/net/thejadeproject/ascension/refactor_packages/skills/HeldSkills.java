package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class HeldSkills {

    private final LivingEntity attachedEntity;

    private final HashMap<ResourceLocation,HeldSkill> skills = new HashMap<>();


    private final ArrayList<HeldSkill> modifiedSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> additionSyncBuffer = new ArrayList<>();
    private final ArrayList<HeldSkill> removalSyncBuffer = new ArrayList<>();


    public HeldSkills(LivingEntity attachedEntity){
        this.attachedEntity = attachedEntity;
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
            if(attachedEntity instanceof Player player) heldSkill.getSkill().onAdded(player);
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
        if(attachedEntity instanceof Player player) heldSkill.getSkill().onRemoved(player,heldSkill.getPersistentData());
        removalSyncBuffer.add(heldSkill);

    }

    private void clearBuffers(){
        additionSyncBuffer.clear();
        modifiedSyncBuffer.clear();
        removalSyncBuffer.clear();
    }

    public CompoundTag write(){return null;}
    public void read(CompoundTag tag){}


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


        if(attachedEntity.level().isClientSide()) clearBuffers(); //should prevent accidental memory leak
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
