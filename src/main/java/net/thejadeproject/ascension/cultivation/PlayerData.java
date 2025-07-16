package net.thejadeproject.ascension.cultivation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.skills.ISkill;
import net.thejadeproject.ascension.skills.data.CastType;
import net.thejadeproject.ascension.skills.data.ICastData;

import java.util.HashMap;
import java.util.List;

public class PlayerData {
    public PlayerData(Player player){

    }
    /********* CULTIVATION PROGRESS *******************************************************/

    private boolean cultivating;
    public boolean isCultivating(){return cultivating;}
    public void setCultivating(boolean state){
        if(state != cultivating){
            cultivating = state;
        }
    }


    public static class PathData{
        public String pathId;
        public int majorRealm;
        public int minorRealm;
        public double pathProgress;

        public PathData(String pathId, int majorRealm, int minorRealm,double pathProgress){
            this.pathId = pathId;
            this.majorRealm = majorRealm;
            this.minorRealm = minorRealm;
            this.pathProgress = pathProgress;

        }
        public PathData(){}
        public CompoundTag writePathNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("path_id",pathId);
            tag.putInt("major_realm",majorRealm);
            tag.putInt("minor_realm",minorRealm);
            tag.putDouble("progress",pathProgress);
            return tag;

        }
        public static PathData loadPathNBTData(CompoundTag compound){
            PathData pathData = new PathData();
            pathData.pathId =  compound.getString("path_id");
            pathData.majorRealm = compound.getInt("major_realm");
            pathData.minorRealm = compound.getInt("minor_realm");
            pathData.pathProgress = compound.getDouble("progress");
            return pathData;
        }
    }


    private final HashMap<String,PathData> pathDataHashMap = new HashMap<>();

    public PathData getPathData(String pathId){

        if(pathDataHashMap.containsKey(pathId)) pathDataHashMap.get(pathId);
        PathData data = new PathData(pathId,0,0,0);
        pathDataHashMap.put(pathId,data);
        return data;
    }

    public void setPathProgress(String pathId,double pathProgress){
        pathDataHashMap.get(pathId).pathProgress = pathProgress;
    }

    public void setPathMajorRealm(String pathId,int majorRealm){
        pathDataHashMap.get(pathId).majorRealm = majorRealm;
    }
    public void setPathMinorRealm(String pathId,int minorRealm){
        pathDataHashMap.get(pathId).minorRealm = minorRealm;
    }


    public CompoundTag writePathNBTData(){
        CompoundTag tag = new CompoundTag();
        for(PathData dataEntry : pathDataHashMap.values()){
            tag.put(dataEntry.pathId,dataEntry.writePathNBTData());
        }
        return tag;
    }
    public void loadPathNBTData(CompoundTag compound){
        for(String key:compound.getAllKeys()){
            pathDataHashMap.put(key,PathData.loadPathNBTData(compound.getCompound(key)));
        }
    }

    /********* Skill Data *******************************************************/

    public static class SkillData{
        public String skillId;
        public boolean fixed;

        public SkillData(){}
        public SkillData(String skillId,boolean fixed){
            this.skillId = skillId;
            this.fixed = fixed;
        }
        public CompoundTag writeSkillNBTData(){
            CompoundTag tag = new CompoundTag();
            tag.putString("skill_id",skillId);;
            tag.putBoolean("fixed",fixed);
            return tag;

        }
        public static SkillData loadSkillNBTData(CompoundTag compound){
            SkillData skillData = new SkillData();
            skillData.skillId = compound.getString("skill_id");
            skillData.fixed = compound.getBoolean("fixed");
            return skillData;
        }
    }

    private final HashMap<String,SkillData> activeSkillHashMap = new HashMap<>();
    private final HashMap<String,SkillData> passiveSkillHashMap = new HashMap<>();



    public SkillData getActiveSkill(String skillId){
        if(activeSkillHashMap.containsKey(skillId)) return activeSkillHashMap.get(skillId);
        SkillData data = new SkillData(skillId,false);
        activeSkillHashMap.put(skillId,data);
        return data;
    }
    public SkillData getPassiveSkill(String skillId){
        if(passiveSkillHashMap.containsKey(skillId)) return passiveSkillHashMap.get(skillId);
        SkillData data = new SkillData(skillId,false);
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

    public void addSkill(String skillId,String type,boolean fixed){
        if(type.equals("Active")) addActiveSkill(skillId,fixed);
        else addPassiveSkill(skillId,fixed);
    }
    public void addActiveSkill(String skillId,boolean fixed){
        activeSkillHashMap.put(skillId,new SkillData(skillId,fixed));
    }
    public void addPassiveSkill(String skillId,boolean fixed){
        passiveSkillHashMap.put(skillId,new SkillData(skillId,fixed));
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

    private double castDuration = 0;
    private double castDurationRemaining = 0;
    private CastType castType;
    private ICastData additionalCastData;
    private boolean casting;
    private ResourceLocation castSkillId;
    public void resetCastingState() {

        this.castDuration = 0;
        this.castDurationRemaining = 0;
        this.castType = CastType.NONE;
        this.casting = false;
        this.castSkillId = null;

    }

    public void castSkill(ISkill skill,int castDuration){
        this.castDuration = castDuration;
        this.castDurationRemaining = castDuration;
        this.castType = skill.getCastType();
        casting = true;
        castSkillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill);
    }
    public ICastData getAdditionalCastData() {
        return additionalCastData;
    }
    public void setAdditionalCastData(ICastData newCastData) {
        additionalCastData = newCastData;
    }
    public void resetAdditionalCastData() {
        if (additionalCastData != null) {
            additionalCastData.reset();
            additionalCastData = null;
        }
    }
    public boolean isCasting() {
        return casting;
    }
    public ResourceLocation getCastSkillId(){
        return castSkillId;
    }
    public CastType getCastType() {
        return castType;
    }
    public double getCastDurationRemaining() {
        return castDurationRemaining;
    }

    public double getCastDuration() {
        return castDuration;
    }
    public void setCastDurationRemaining(double durationRemaining){
        this.castDurationRemaining = durationRemaining;
    }

    /********* Cooldown Data *******************************************************/

    //TODO
    /********* SYSTEM *******************************************************/
    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){
        loadPathNBTData(tag.getCompound("path_data"));
        loadSkillNBTData(tag.getCompound("skill_data"));
    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        tag.put("path_data",writePathNBTData());
        CompoundTag skillTag = new CompoundTag();
        writeSkillNBTData(skillTag);
        tag.put("skill_data",skillTag);
    }
}
