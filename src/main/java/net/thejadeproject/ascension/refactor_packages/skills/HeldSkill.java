package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.util.IDataInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HeldSkill  {



    private final ResourceLocation skillKey;
    private IPersistentSkillData persistentData;
    private String skillVersion;
    private boolean fixed;
    private boolean permanent;

    /*
        stores the key of anything that gave the player this skill
        this includes techniques,physiques,bloodlines etc
        useful for balancing stuff
     */
    private HashSet<ResourceLocation> sources = new HashSet<>();

    public HeldSkill(ResourceLocation skillKey){
        this.skillKey = skillKey;

    }

    public ISkill getSkill(){return null;}
    public ResourceLocation getKey(){return skillKey;}

    public String getSkillVersion(){return skillVersion;}
    public void setSkillVersion(String version){skillVersion = version;}

    public IPersistentSkillData getPersistentData(){return persistentData;}
    public void setPersistentData(IPersistentSkillData persistentData){this.persistentData = persistentData;}

    public boolean isFixed(){return fixed;}
    public boolean isPermanent(){return permanent;}

    public void setFixed(boolean fixed){this.fixed =fixed;}
    public void setPermanent(boolean permanent) {this.permanent = permanent;}

    public void addSource(ResourceLocation source){sources.add(source);}
    public Set<ResourceLocation> getSources(){return sources;}
    public boolean hasSource(ResourceLocation source){return sources.contains(source);}

    public CompoundTag write(){return null;}
    public void read(CompoundTag tag){}

    public void encode(RegistryFriendlyByteBuf buf){

    }
    public static HeldSkill decode(RegistryFriendlyByteBuf buf){return null;}
}
