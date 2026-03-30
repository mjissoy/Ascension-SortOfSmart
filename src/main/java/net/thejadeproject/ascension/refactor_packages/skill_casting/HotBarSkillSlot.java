package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;

public class HotBarSkillSlot {

    private ResourceLocation skillKey;
    private IPreCastData preCastData;


    public ISkill getSkill(){return AscensionRegistries.Skills.SKILL_REGISTRY.get(skillKey);};

    public void unSlotSKill(IEntityData entityData){
        if(skillKey != null){
            if(getSkill() instanceof ICastableSkill castableSkill){
                castableSkill.onUnEquip(entityData,preCastData);
            }
        }
        skillKey =null;
        preCastData = null;
    }

    public void setSkill(ResourceLocation skill,IEntityData entityData){
        if(AscensionRegistries.Skills.SKILL_REGISTRY.get(skill) instanceof  ICastableSkill castableSkill){
            setSkill(skill,castableSkill.freshPreCastData(),entityData);
        }
    }
    public void setSkill(ResourceLocation skill,IPreCastData preCastData,IEntityData entityData){
        unSlotSKill(entityData);

        skillKey = skill;
        if(getSkill() instanceof ICastableSkill castableSkill){
            castableSkill.onEquip(entityData);
            this.preCastData = preCastData;
        }
    }

    public IPreCastData getPreCastData(){return this.preCastData;}

    public ResourceLocation getSkillKey(){return skillKey;}
}
