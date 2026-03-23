package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufHelper;
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


    // technique -> remove skill1  but physique also had skill1
    // technique -> remove skill1 skill1 checks if sources i empty. can cancel

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

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putString("skill_key",getKey().toString());
        tag.putString("skill_version",getSkillVersion());
        tag.putBoolean("fixed",isFixed());
        tag.putBoolean("permanent",isPermanent());
        if(getPersistentData() != null) tag.put("skill_data",getPersistentData().write());
        ListTag sourcesTag = new ListTag();
        for(ResourceLocation source : sources){
            sourcesTag.add(StringTag.valueOf(source.toString()));
        }
        tag.put("sources",sourcesTag);
        return tag;
    }
    public static HeldSkill read(CompoundTag tag,IEntityData heldEntity){
        HeldSkill heldSkill = new HeldSkill(ResourceLocation.bySeparator(tag.getString("skill_key"),':'));
        //TODO RUN VERSION VERIFICATION HERE
        String version = tag.getString("skill_version");

        heldSkill.setSkillVersion(version);
        heldSkill.setFixed(tag.getBoolean("fixed"));
        heldSkill.setPermanent(tag.getBoolean("permanent"));

        if(tag.contains("skill_data")){
            heldSkill.setPersistentData(heldSkill.getSkill().fromCompound(tag,heldEntity));
        }

        ListTag sources = tag.getList("sources", Tag.TAG_STRING);
        for(int i =0;i<sources.size();i++){
            heldSkill.addSource(ResourceLocation.bySeparator(sources.getString(i),':'));
        }
        return heldSkill;
    }

    public void encode(RegistryFriendlyByteBuf buf){
        ByteBufHelper.encodeString(buf,getKey().toString());
        ByteBufHelper.encodeString(buf,getSkillVersion());
        buf.writeBoolean(isFixed());
        buf.writeBoolean(isPermanent());
        persistentData.encode(buf);
        buf.writeInt(sources.size());
        for(ResourceLocation key : sources){
            ByteBufHelper.encodeString(buf,key.toString());
        }
    }
    public static HeldSkill decode(RegistryFriendlyByteBuf buf, IEntityData heldEntity){
        ResourceLocation skillKey = ByteBufHelper.readResourceLocation(buf);
        HeldSkill skill = new HeldSkill(skillKey);
        String version = ByteBufHelper.readString(buf);

        skill.setSkillVersion(version);
        boolean fixed = buf.readBoolean();
        boolean permanent = buf.readBoolean();
        skill.setFixed(fixed);
        skill.setPermanent(permanent);

        IPersistentSkillData data = skill.getSkill().fromNetwork(buf,heldEntity);

        skill.setPersistentData(data);

        for(int i = 0;i<buf.readInt();i++){
            skill.addSource(ByteBufHelper.readResourceLocation(buf));
        }
        return skill;
    }
}
