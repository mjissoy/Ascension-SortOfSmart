package net.thejadeproject.ascension.cultivation.player.data_attachements;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
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

    public static class ActiveSkillContainer{
        public List<String> skillIdList = new ArrayList<>();

        public final int MAX_SKILL_SLOTS = 4;
        public ActiveSkillContainer(){
            for(int i = 0;i<MAX_SKILL_SLOTS; i++){
                skillIdList.add("");
            }
        }
        public List<String> getSkillIdList() {
            return skillIdList;
        }
        public void unSlotSkill(int slot){

            skillIdList.set(slot,"");
        }
        public void unSlotSkill(String skillId){

            skillIdList.set(skillIdList.indexOf(skillId),"");
        }
        public void slotSkill(String skillId,int slot){
            if(skillIdList.contains(skillId)){
                unSlotSkill(skillId);
            }
            skillIdList.set(slot,skillId);

        }
    }


    public void modifySkillSlot(int slot,String skillId,boolean slotSkill){
        if(!slotSkill){
            activeSkillContainer.unSlotSkill(slot);
        }else{
            activeSkillContainer.slotSkill(skillId,slot);
        }
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);
    }

    public void writeSkillContainerNBTData(ListTag tag){

        for(int i = 0;i<activeSkillContainer.MAX_SKILL_SLOTS; i++){
            System.out.println("writing skill slot "+i );

            tag.add(i, StringTag.valueOf(activeSkillContainer.getSkillIdList().get(i)));
        }

    }
    public void loadSkillContainerNBTData(ListTag skillList){
        for(int i = 0;i<skillList.size(); i++){
            System.out.println("skill slot : " +i);
            activeSkillContainer.slotSkill(skillList.getString(i),i);
        }
    }
    public static class SkillMetaData {
        public String skillId;
        public boolean fixed;
        public ISkillData data;
        public SkillMetaData(){}
        public SkillMetaData(String skillId, boolean fixed, ISkillData data){
            this.skillId = skillId;
            this.fixed = fixed;
            this.data = data;
        }
        public CompoundTag writeSkillNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("skill_id",skillId);;
            tag.putBoolean("fixed",fixed);
            CompoundTag dataTag = new CompoundTag();
            if(data != null) data.writeData(dataTag);
            tag.put("data",dataTag);
            return tag;

        }
        public static SkillMetaData loadSkillNBTData(CompoundTag compound){
            SkillMetaData skillMetaData = new SkillMetaData();
            skillMetaData.skillId = compound.getString("skill_id");
            skillMetaData.fixed = compound.getBoolean("fixed");
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillMetaData.skillId,':'));
            skillMetaData.data = skill.getSkillData(compound.getCompound("data"));
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

    public void addSkill(String skillId,String type,boolean fixed,ISkillData data){
        if(type.equals("Active")) addActiveSkill(skillId,fixed,data);
        else addPassiveSkill(skillId,fixed,data);
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);
    }
    public void addActiveSkill(String skillId,boolean fixed,ISkillData data){
        if(activeSkillHashMap.containsKey(skillId) && activeSkillHashMap.get(skillId).fixed) return;
        else if (activeSkillHashMap.containsKey(skillId))activeSkillHashMap.get(skillId).fixed = fixed;
        else activeSkillHashMap.put(skillId,new SkillMetaData(skillId,fixed,data));
        activeSkillBuffer.add(new Pair<>(true,activeSkillHashMap.get(skillId)));
    }
    public void addPassiveSkill(String skillId,boolean fixed,ISkillData data){
        if(passiveSkillHashMap.containsKey(skillId) && passiveSkillHashMap.get(skillId).fixed) return;
        else if (passiveSkillHashMap.containsKey(skillId))passiveSkillHashMap.get(skillId).fixed = fixed;
        else passiveSkillHashMap.put(skillId,new SkillMetaData(skillId,fixed,data));
        passiveSkillBuffer.add(new Pair<>(true,passiveSkillHashMap.get(skillId)));
        System.out.println("passive skill added");
        System.out.println(passiveSkillBuffer.size());
    }
    //removes even if fixed so make sure to check
    public void removePassiveSkill(String skillId){
        if(!passiveSkillHashMap.containsKey(skillId)) return;
        passiveSkillBuffer.add(new Pair<>(false,passiveSkillHashMap.remove(skillId)));
    }
    //removes even if fixed so make sure to check
    public void removeActiveSkill(String skillId){
        if(!activeSkillHashMap.containsKey(skillId)) return;

        activeSkillBuffer.add(new Pair<>(false,activeSkillHashMap.remove(skillId)));
        if(activeSkillContainer.skillIdList.contains(skillId)) activeSkillContainer.unSlotSkill(skillId);
    }
    public void triggerSkillRemoval(String skillId){
        ResourceLocation id = ResourceLocation.bySeparator(skillId,':');
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(id);
        if(skill == null) return;
        skill.onSkillRemoved(player);
    }
    public void removeAllSkills(){
        for(SkillMetaData data : getActiveSkills()){
            triggerSkillRemoval(activeSkillHashMap.remove(data.skillId).skillId);
        }
        for(SkillMetaData data : getPassiveSkills()){
            triggerSkillRemoval(passiveSkillHashMap.remove(data.skillId).skillId);
        }

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
        System.out.println("writing skill data");
        CompoundTag skillTag = new CompoundTag();
        writeSkillNBTData(skillTag);
        tag.put("skill_data",skillTag);
        ListTag activeSkillContainerList = new ListTag();
        writeSkillContainerNBTData(activeSkillContainerList);
        tag.put("equip_skill_list",activeSkillContainerList);

    }
}
