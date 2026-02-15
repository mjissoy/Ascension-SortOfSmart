package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;

public class HotBarSkillSlot {
    private final Player player;
    private ResourceLocation skillKey;
    private IPreCastData preCastData;

    public HotBarSkillSlot(Player player){
        this.player = player;
    }

    public ISkill getSkill(){return null;};

    public void unSlotSKill(){
        if(skillKey != null){
            if(getSkill() instanceof ICastableSkill castableSkill){
                castableSkill.onUnEquip(player,preCastData);
            }
        }
        skillKey =null;
        preCastData = null;
    }

    public void setSkill(ResourceLocation skill){
        unSlotSKill();

        skillKey = skill;
        if(getSkill() instanceof ICastableSkill castableSkill){
            castableSkill.onEquip(player);
            preCastData = castableSkill.freshPreCastData();
        }
    }
    public ResourceLocation getSkillKey(){return skillKey;}
}
