package net.thejadeproject.ascension.cultivation.player.data_attachements;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.IPreCastSkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerSkillData {
    public Player player;


    public PlayerSkillData(Player player){
        this.player = player;
    }

    public PlayerSkillData(Player player, List<SkillMetaData> activeSkills, List<SkillMetaData> passiveSkills){
        this.player = player;
        for(SkillMetaData activeSkill : activeSkills){
            activeSkillHashMap.put(activeSkill.skillId,activeSkill);
        }
        for(SkillMetaData passiveSkill : passiveSkills){
            passiveSkillHashMap.put(passiveSkill.skillId,passiveSkill);
        }
    }

    public boolean hasSkill(String skill_id,String skillType){
        if(skillType.equals("Active")) return activeSkillHashMap.containsKey(skill_id);
        return passiveSkillHashMap.containsKey(skill_id);
    }
    public ActiveSkillContainer activeSkillContainer = new ActiveSkillContainer();
    public static class SkillSlot{
        public ResourceLocation skillId;
        public IPreCastSkillData preCastSkillData;
        public static final SkillSlot EMPTY_SLOT = new SkillSlot(null,null);
        public SkillSlot(ResourceLocation skillId){
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
            if(skill instanceof  AbstractActiveSkill activeSkill){
                preCastSkillData = activeSkill.getPreCastDataInstance();
                this.skillId = skillId;
            }
        }
        public SkillSlot(ResourceLocation skillId,IPreCastSkillData preCastSkillData){
            this.skillId = skillId;
            this.preCastSkillData = preCastSkillData;
        }

    }
    public static class ActiveSkillContainer{
        public List<SkillSlot> skillIdList = new ArrayList<>();

        public final int MAX_SKILL_SLOTS = 4;
        public ActiveSkillContainer(){
            for(int i = 0;i<MAX_SKILL_SLOTS; i++){
                skillIdList.add(SkillSlot.EMPTY_SLOT);
            }
        }
        public List<SkillSlot> getSkillIdList() {
            return skillIdList;
        }
        public void unSlotSkill(int slot){

            skillIdList.set(slot,SkillSlot.EMPTY_SLOT);
        }
        public SkillSlot getSlot(int slot){
            return skillIdList.get(slot);
        }
        public int getSlot(ResourceLocation skillId){
            for(int i = 0; i<skillIdList.size();i++){
                if(skillIdList.get(i).skillId == null) continue;
                if(skillIdList.get(i).skillId.equals(skillId)){
                    return i;
                }
            }
            return -1;
        }
        public void unSlotSkill(ResourceLocation skillId){

            skillIdList.set(getSlot(skillId),SkillSlot.EMPTY_SLOT);
        }
        public boolean hasSkill(ResourceLocation skillId){
            return getSlot(skillId) != -1;
        }
        public void setSlotEmpty(int slot){
            skillIdList.set(slot, SkillSlot.EMPTY_SLOT);
        }
        public void slotSkill(ResourceLocation skillId,int slot){
            slotSkill(skillId,null,slot);

        }
        public void slotSkill(ResourceLocation skillId,IPreCastSkillData preCastSkillData,int slot){
            if(hasSkill(skillId)){
                unSlotSkill(skillId);
            }
            skillIdList.set(slot,new SkillSlot(skillId,preCastSkillData));

        }
    }


    public void modifySkillSlot(int slot,ResourceLocation skillId,boolean slotSkill){
        if(!slotSkill){
            activeSkillContainer.unSlotSkill(slot);
        }else{
            activeSkillContainer.slotSkill(skillId,slot);
        }
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);
    }

    public void writeSkillContainerNBTData(ListTag tag){

        for(int i = 0;i<activeSkillContainer.MAX_SKILL_SLOTS; i++){
            
            CompoundTag skillSlot = new CompoundTag();
            ResourceLocation id = activeSkillContainer.getSkillIdList().get(i).skillId;
            skillSlot.putString("skill_id",id == null? "":id.toString());
            if(activeSkillContainer.getSkillIdList().get(i).preCastSkillData != null)skillSlot.put("pre_cast_data",activeSkillContainer.getSkillIdList().get(i).preCastSkillData.writeData());
            tag.add(i, skillSlot);
        }

    }
    public void loadSkillContainerNBTData(ListTag skillList){
        for(int i = 0;i<skillList.size(); i++){
            CompoundTag tag = skillList.getCompound(i);
            String id = tag.getString("skill_id");
            if(id.isEmpty()) activeSkillContainer.setSlotEmpty(i);
            else activeSkillContainer.slotSkill(ResourceLocation.bySeparator(id,':'),i);

            if(tag.hasUUID("pre_cast_data")){
                ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(id,':'));
                if(skill instanceof AbstractActiveSkill activeSkill){
                    activeSkillContainer.getSlot(i).preCastSkillData = activeSkill.getPreCastDataInstance(tag.getCompound("pre_cast_data"));
                }
            }
        }
    }
    public static class SkillMetaData {
        public String skillId;
        public boolean fixed;
        public IPersistentSkillData data;
        public SkillMetaData(){}
        public SkillMetaData(String skillId, boolean fixed, IPersistentSkillData data){
            this.skillId = skillId;
            this.fixed = fixed;
            this.data = data;
        }
        public CompoundTag writeSkillNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("skill_id",skillId);;
            tag.putBoolean("fixed",fixed);

            if(data != null) tag.put("data",data.writeData());
            return tag;

        }
        public static SkillMetaData loadSkillNBTData(CompoundTag compound){
            SkillMetaData skillMetaData = new SkillMetaData();
            skillMetaData.skillId = compound.getString("skill_id");
            skillMetaData.fixed = compound.getBoolean("fixed");
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillMetaData.skillId,':'));
            if(compound.hasUUID("data")) skillMetaData.data = skill.getPersistentDataInstance(compound.getCompound("data"));
            return skillMetaData;
        }
    }

    private final HashMap<String, SkillMetaData> activeSkillHashMap = new HashMap<>();
    private final HashMap<String, SkillMetaData> passiveSkillHashMap = new HashMap<>();

    private final List<Pair<Boolean, SkillMetaData>> activeSkillBuffer = new ArrayList<>();
    private final List<Pair<Boolean, SkillMetaData>> passiveSkillBuffer = new ArrayList<>();

    public SkillMetaData getActiveSkill(String skillId){
        if(activeSkillHashMap.containsKey(skillId)) return activeSkillHashMap.get(skillId);
        return null;
    }
    public SkillMetaData getPassiveSkill(String skillId){
        if(passiveSkillHashMap.containsKey(skillId)) return passiveSkillHashMap.get(skillId);
        return null;
    }
    public SkillMetaData getSkill(String skillId){
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        if(skill instanceof AbstractActiveSkill) return getActiveSkill(skillId);
        return getPassiveSkill(skillId);
    }

    public boolean hasSkill(String skillId){
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        if(skill instanceof AbstractActiveSkill) return hasActiveSkill(skillId);
        return hasPassiveSkill(skillId);
    }
    public boolean hasActiveSkill(String skillId){
        return activeSkillHashMap.containsKey(skillId);
    }
    public boolean hasPassiveSkill(String skillId){
        return passiveSkillHashMap.containsKey(skillId);
    }


    public List<SkillMetaData> getActiveSkills(){
        return activeSkillHashMap.values().stream().toList();
    }
    public List<SkillMetaData> getPassiveSkills(){
        return passiveSkillHashMap.values().stream().toList();
    }

    public List<SkillMetaData> getSkills(){
        List<SkillMetaData> data = new ArrayList<>(getActiveSkills());
        data.addAll(getPassiveSkills());
        return data;
    }

    public void addSkill(String skillId, String type, boolean fixed, IPersistentSkillData data){
        if(type.equals("Active")) addActiveSkill(skillId,fixed,data);
        else addPassiveSkill(skillId,fixed,data);
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);
    }
    public void addActiveSkill(String skillId, boolean fixed, IPersistentSkillData data){
        if(activeSkillHashMap.containsKey(skillId) && activeSkillHashMap.get(skillId).fixed) return;
        else if (activeSkillHashMap.containsKey(skillId))activeSkillHashMap.get(skillId).fixed = fixed;
        else activeSkillHashMap.put(skillId,new SkillMetaData(skillId,fixed,data));
        activeSkillBuffer.add(new Pair<>(true,activeSkillHashMap.get(skillId)));
    }
    public void addPassiveSkill(String skillId, boolean fixed, IPersistentSkillData data){
        if(passiveSkillHashMap.containsKey(skillId) && passiveSkillHashMap.get(skillId).fixed) return;
        else if (passiveSkillHashMap.containsKey(skillId))passiveSkillHashMap.get(skillId).fixed = fixed;
        else passiveSkillHashMap.put(skillId,new SkillMetaData(skillId,fixed,data));
        passiveSkillBuffer.add(new Pair<>(true,passiveSkillHashMap.get(skillId)));
        
        
    }
    public void removeSkill(String skillId){
        removePassiveSkill(skillId);
        removeActiveSkill(skillId);
        triggerSkillRemoval(skillId);
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);
    }
    //removes even if fixed so make sure to check
    public void removePassiveSkill(String skillId){
        if(!passiveSkillHashMap.containsKey(skillId)) return;
        passiveSkillBuffer.add(new Pair<>(false,passiveSkillHashMap.remove(skillId)));
    }
    //removes even if fixed so make sure to check
    public void removeActiveSkill(String skillId){
        if(!activeSkillHashMap.containsKey(skillId)) return;
        ResourceLocation skillResource = ResourceLocation.bySeparator(skillId,':');
        activeSkillBuffer.add(new Pair<>(false,activeSkillHashMap.remove(skillId)));
        if(activeSkillContainer.hasSkill(skillResource)) activeSkillContainer.unSlotSkill(skillResource);
    }
    public void triggerSkillRemoval(String skillId){
        ResourceLocation id = ResourceLocation.bySeparator(skillId,':');
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(id);
        if(skill == null) return;
        skill.onSkillRemoved(player);
    }
    public void removeAllSkills(){
        for(SkillMetaData data : getActiveSkills()){
            removeActiveSkill(data.skillId);
            triggerSkillRemoval(data.skillId);
        }
        for(SkillMetaData data : getPassiveSkills()){
            removePassiveSkill(data.skillId);
            triggerSkillRemoval(data.skillId);
        }
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

    }
    public List<Pair<Boolean, SkillMetaData>> getPassiveSkillBuffer(){
        return passiveSkillBuffer;
    }
    public List<Pair<Boolean, SkillMetaData>> getActiveSkillBuffer(){
        return activeSkillBuffer;
    }
    public void clearSkillBuffers(){
        activeSkillBuffer.clear();
        passiveSkillBuffer.clear();
    }


    public void writeSkillNBTData(CompoundTag skillTag){
        CompoundTag tag = new CompoundTag();
        for(SkillMetaData dataEntry : activeSkillHashMap.values()){
            tag.put(dataEntry.skillId,dataEntry.writeSkillNBTData());
        }
        CompoundTag tag2 = new CompoundTag();
        for(SkillMetaData dataEntry : passiveSkillHashMap.values()){
            tag2.put(dataEntry.skillId,dataEntry.writeSkillNBTData());
        }
        skillTag.put("Active",tag);
        skillTag.put("Passive",tag2);
    }
    public void loadSkillNBTData(CompoundTag compound){
        for(String key:compound.getCompound("Active").getAllKeys()){
            activeSkillHashMap.put(key, SkillMetaData.loadSkillNBTData(compound.getCompound("Active").getCompound(key)));
        }
        for(String key:compound.getCompound("Passive").getAllKeys()){
            passiveSkillHashMap.put(key, SkillMetaData.loadSkillNBTData(compound.getCompound("Passive").getCompound(key)));
        }
    }
    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){

        loadSkillNBTData(tag.getCompound("skill_data"));
        loadSkillContainerNBTData((ListTag) tag.get("equip_skill_list"));
    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        
        CompoundTag skillTag = new CompoundTag();
        writeSkillNBTData(skillTag);
        tag.put("skill_data",skillTag);
        ListTag activeSkillContainerList = new ListTag();
        writeSkillContainerNBTData(activeSkillContainerList);
        tag.put("equip_skill_list",activeSkillContainerList);

    }
}
