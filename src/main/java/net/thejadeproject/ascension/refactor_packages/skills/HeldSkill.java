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

    public IPersistentSkillData getPersistentData(){return persistentData;}
    public void setPersistentData(IPersistentSkillData persistentData){this.persistentData = persistentData;}

    public CompoundTag write(){
        CompoundTag tag = new CompoundTag();
        tag.putString("skill_key",getKey().toString());
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


        if(tag.contains("skill_data")){
            heldSkill.setPersistentData(heldSkill.getSkill().fromCompound(tag,heldEntity));
        }

        return heldSkill;
    }

    public void encode(RegistryFriendlyByteBuf buf){
        ByteBufHelper.encodeString(buf,getKey().toString());

        persistentData.encode(buf);
        buf.writeInt(sources.size());

    }
    public static HeldSkill decode(RegistryFriendlyByteBuf buf, IEntityData heldEntity){
        ResourceLocation skillKey = ByteBufHelper.readResourceLocation(buf);
        HeldSkill skill = new HeldSkill(skillKey);


        IPersistentSkillData data = skill.getSkill().fromNetwork(buf,heldEntity);

        skill.setPersistentData(data);

        return skill;
    }
}
