package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;

import java.util.HashMap;
import java.util.UUID;

public class CooldownHandler {
    private final HashMap<ResourceLocation,Integer> cooldowns = new HashMap<>();


    public void putSkillOnCooldown(ResourceLocation skill,int time){
        cooldowns.put(skill,time);
    }

    public int getCooldown(ResourceLocation skill){
        return cooldowns.get(skill);
    }
    public boolean isOnCooldown(ResourceLocation skill){
        return cooldowns.containsKey(skill);
    }
    public ISkill getSkill(ResourceLocation skill){return null;}//TODO

    public void tick(UUID entity){
        for (ResourceLocation key : cooldowns.keySet()){
            cooldowns.put(key,cooldowns.get(key)-1);
            if(cooldowns.get(key) <=0) {
                cooldowns.remove(key);
                getSkill(key).finishedCooldown(entity);
            }
        }
    }
}
