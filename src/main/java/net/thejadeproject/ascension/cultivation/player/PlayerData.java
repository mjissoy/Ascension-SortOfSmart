package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ICastData;
import net.thejadeproject.ascension.util.ModAttributes;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class PlayerData {
    public Player player;
    public PlayerData(Player player){
        this.player =player;
    }
    /********* CULTIVATION PROGRESS *******************************************************/
    private final CultivationData cultivationData = new CultivationData();

    public CultivationData getCultivationData(){ return cultivationData;}
    /********* Skill Data *******************************************************/

    public boolean hasSkill(String skill_id,String skillType){
        if(skillType.equals("Active")) return activeSkillHashMap.containsKey(skill_id);
        return passiveSkillHashMap.containsKey(skill_id);
    }
    public ActiveSkillContainer activeSkillContainer = new ActiveSkillContainer();

    public static class ActiveSkillContainer{
        private final List<String> skillIdList = List.of();

        public List<String> getSkillIdList() {
            return skillIdList;
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

    private final HashMap<String,SkillData> activeSkillHashMap = new HashMap<>();
    private final HashMap<String,SkillData> passiveSkillHashMap = new HashMap<>();



    public SkillData getActiveSkill(String skillId){
        if(activeSkillHashMap.containsKey(skillId)) return activeSkillHashMap.get(skillId);
        SkillData data = new SkillData(skillId,false,null);
        activeSkillHashMap.put(skillId,data);
        return data;
    }
    public SkillData getPassiveSkill(String skillId){
        if(passiveSkillHashMap.containsKey(skillId)) return passiveSkillHashMap.get(skillId);
        SkillData data = new SkillData(skillId,false,null);
        passiveSkillHashMap.put(skillId,data);
        return data;
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
    }
    public void addPassiveSkill(String skillId,boolean fixed,ISkillData data){
        if(passiveSkillHashMap.containsKey(skillId) && passiveSkillHashMap.get(skillId).fixed) return;
        passiveSkillHashMap.put(skillId,new SkillData(skillId,fixed,data));
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
            activeSkillHashMap.put(key,SkillData.loadSkillNBTData(compound.getCompound("Active").getCompound(key)));
        }
        for(String key:compound.getCompound("Passive").getAllKeys()){
            passiveSkillHashMap.put(key,SkillData.loadSkillNBTData(compound.getCompound("Passive").getCompound(key)));
        }
    }


    /********* Casting Data *******************************************************/
    //using indexes could cause sync issues cus of index shuffling
    private final List<UUID> castingThreadQueue = new ArrayList<>();
    private final HashMap<UUID,CastingInstance> castingThreads = new HashMap<>();

    public void tryCast(ISkill skill){
        if(castingThreads.size() == player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue()) return; // TODO replace casting of 1st slot skill
        UUID uuid = UUID.randomUUID();
        castingThreadQueue.add(uuid);
        castingThreads.put(uuid,new CastingInstance(skill,uuid));
    }

    public void removeCastingInstance(UUID uuid){
        castingThreads.remove(uuid);
        castingThreadQueue.remove(uuid);
    }
    public void addCastingInstance(CastingInstance castingInstance){
        if(castingThreads.size() == player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue()) return; // TODO replace casting of 1st slot skill
        castingThreads.put(castingInstance.uuid,castingInstance);
        castingThreadQueue.add(castingInstance.uuid);
    }

    /********* Cooldown Data *******************************************************/

    //TODO
    /********* SYSTEM *******************************************************/
    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){
        getCultivationData().loadNBTData(tag.getCompound("path_data"));
        loadSkillNBTData(tag.getCompound("skill_data"));
        loadSkillContainerNBTData((ListTag) tag.get("equip_skill_list"));
    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        tag.put("path_data",getCultivationData().writeNBTData());
        CompoundTag skillTag = new CompoundTag();
        writeSkillNBTData(skillTag);
        tag.put("skill_data",skillTag);
        ListTag activeSkillContainerList = new ListTag();
        writeSkillContainerNBTData(activeSkillContainerList);
        tag.put("equip_skill_list",activeSkillContainerList);

    }
}
