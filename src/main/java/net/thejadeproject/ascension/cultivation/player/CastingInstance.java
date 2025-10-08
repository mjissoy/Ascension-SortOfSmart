package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.network.serverBound.ServerCastSkillPayload;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ICastData;
import net.thejadeproject.ascension.registries.AscensionRegistries;

import java.util.UUID;

//built like this so i can have multiple casting instances
public class CastingInstance {
    public CastType castType;
    public ICastData castData;// additional info the spell might need
    public int castTickElapsed = 0;
    public ResourceLocation skillId;
    public UUID uuid; //for casting threads

    public CastingInstance(ISkill skill,UUID uuid){
        castType = skill.getCastType();
        this.uuid = uuid;
        skillId = AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill);
    }
    public CastingInstance(UUID uuid,ResourceLocation skillId,CastType castType){
        this.castType = castType;
        this.uuid = uuid;
        this.skillId = skillId;
    }
    public void cancelCast(){
        //TODO
    }
    public static CastingInstance fromPayload(ServerCastSkillPayload payload){
        CastType type = CastType.valueOf(payload.castType());
        UUID uuid = UUID.fromString(payload.uuid());
        ResourceLocation id = ResourceLocation.bySeparator(payload.skillID(),':');
        return new CastingInstance(uuid,id,type);
    }

}
