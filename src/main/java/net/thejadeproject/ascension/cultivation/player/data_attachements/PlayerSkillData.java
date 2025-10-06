package net.thejadeproject.ascension.cultivation.player.data_attachements;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerSkillData {
    public Player player;


    public PlayerSkillData(Player player){
        this.player = player;
    }

    public PlayerSkillData(Player player,List<SkillData> activeSkills,List<SkillData> passiveSkills,List<String> slottedSkills){
        this.player = player;
        for(SkillData activeSkill : activeSkills){
            activeSkillHashMap.put(activeSkill.skillId,activeSkill);
        }
        for(SkillData passiveSkill : passiveSkills){
            passiveSkillHashMap.put(passiveSkill.skillId,passiveSkill);
        }
        activeSkillContainer.skillIdList = slottedSkills;
    }

    public boolean hasSkill(String skill_id,String skillType){
        if(skillType.equals("Active")) return activeSkillHashMap.containsKey(skill_id);
        return passiveSkillHashMap.containsKey(skill_id);
    }
    public ActiveSkillContainer activeSkillContainer = new ActiveSkillContainer();

    public static class ActiveSkillContainer{
        public List<String> skillIdList = new ArrayList<>();
        public boolean changed = false;
        public List<String> getSkillIdList() {
            return skillIdList;
        }
        public void unSlotSkill(String id){
            changed = true;
            skillIdList.remove(id);
        }
    }


    public ListTag writeSkillContainerNBTData(ListTag skillTag){
        ListTag tag = new ListTag();
        for(int i = 0;i<activeSkillContainer.getSkillIdList().size(); i++){
            tag.add(i, StringTag.valueOf(activeSkillContainer.getSkillIdList().get(i)));
        }
        return tag;
    }
    public void loadSkillContainerNBTData(ListTag skillList){
        for(int i = 0;i<skillList.size(); i++){
            activeSkillContainer.getSkillIdList().add(skillList.getString(i));
        }
    }
    public static class SkillData{
        public String skillId;
        public boolean fixed;
        public ISkillData data;
        public SkillData(){}
        public SkillData(String skillId,boolean fixed,ISkillData data){
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
        public static SkillData loadSkillNBTData(CompoundTag compound){
            SkillData skillData = new SkillData();
            skillData.skillId = compound.getString("skill_id");
            skillData.fixed = compound.getBoolean("fixed");
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.skillId,':'));
            skillData.data = skill.getSkillData(compound.getCompound("data"));
            return skillData;
        }
    }

    private final HashMap<String, SkillData> activeSkillHashMap = new HashMap<>();
    private final HashMap<String, SkillData> passiveSkillHashMap = new HashMap<>();

    private final List<Pair<Boolean, SkillData>> activeSkillBuffer = new ArrayList<>();
    private final List<Pair<Boolean, SkillData>> passiveSkillBuffer = new ArrayList<>();

    public SkillData getActiveSkill(String skillId){
        if(activeSkillHashMap.containsKey(skillId)) return activeSkillHashMap.get(skillId);
        return null;
    }
    public SkillData getPassiveSkill(String skillId){
        if(passiveSkillHashMap.containsKey(skillId)) return passiveSkillHashMap.get(skillId);
        return null;
    }
    public SkillData getSkill(String skillId){
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


    public List<SkillData> getActiveSkills(){
        return activeSkillHashMap.values().stream().toList();
    }
    public List<SkillData> getPassiveSkills(){
        return passiveSkillHashMap.values().stream().toList();
    }

    public void addSkill(String skillId,String type,boolean fixed,ISkillData data){
        if(type.equals("Active")) addActiveSkill(skillId,fixed,data);
        else addPassiveSkill(skillId,fixed,data);
    }
    public void addActiveSkill(String skillId,boolean fixed,ISkillData data){
        if(activeSkillHashMap.containsKey(skillId) && activeSkillHashMap.get(skillId).fixed) return;
        activeSkillHashMap.put(skillId,new SkillData(skillId,fixed,data));
        activeSkillBuffer.add(new Pair<>(true,activeSkillHashMap.get(skillId)));
    }
    public void addPassiveSkill(String skillId,boolean fixed,ISkillData data){
        if(passiveSkillHashMap.containsKey(skillId) && passiveSkillHashMap.get(skillId).fixed) return;
        passiveSkillHashMap.put(skillId,new SkillData(skillId,fixed,data));
        passiveSkillBuffer.add(new Pair<>(true,passiveSkillHashMap.get(skillId)));
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
    public List<Pair<Boolean,SkillData>> getPassiveSkillBuffer(){
        return passiveSkillBuffer;
    }
    public List<Pair<Boolean,SkillData>> getActiveSkillBuffer(){
        return activeSkillBuffer;
    }
    public void clearSkillBuffers(){
        activeSkillBuffer.clear();
        passiveSkillBuffer.clear();
    }


    public void writeSkillNBTData(CompoundTag skillTag){
        CompoundTag tag = new CompoundTag();
        for(SkillData dataEntry : activeSkillHashMap.values()){
            tag.put(dataEntry.skillId,dataEntry.writeSkillNBTData());
        }
        CompoundTag tag2 = new CompoundTag();
        for(SkillData dataEntry : passiveSkillHashMap.values()){
            tag2.put(dataEntry.skillId,dataEntry.writeSkillNBTData());
        }
        skillTag.put("Active",tag);
        skillTag.put("Passive",tag2);
    }
    public void loadSkillNBTData(CompoundTag compound){
        for(String key:compound.getCompound("Active").getAllKeys()){
            activeSkillHashMap.put(key, SkillData.loadSkillNBTData(compound.getCompound("Active").getCompound(key)));
        }
        for(String key:compound.getCompound("Passive").getAllKeys()){
            passiveSkillHashMap.put(key, SkillData.loadSkillNBTData(compound.getCompound("Passive").getCompound(key)));
        }
    }
}
