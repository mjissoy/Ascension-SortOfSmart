package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastingInstance;
import net.thejadeproject.ascension.refactor_packages.skill_casting.persistent_casting.PersistentCastingInstance;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPersistentSkillInstance;

import java.util.ArrayList;
import java.util.List;

//Do not worry about cast logic yet
public class SkillCastHandler {

    private final Player player;

    private final CastingInstance castingInstance;

    private final ArrayList<PersistentCastingInstance> persistentCastingInstances = new ArrayList<>();

    private final CooldownHandler cooldownHandler = new CooldownHandler();


    public SkillCastHandler(Player player) {
        this.player = player;
        this.castingInstance = new CastingInstance(player);
    }

    public void addPersistentCastingInstance(ResourceLocation skill, IPersistentSkillInstance skillInstance){
        persistentCastingInstances.add(new PersistentCastingInstance(skillInstance,skill));
    }
    public void removePersistentCastingInstances(int instanceIndex){
        persistentCastingInstances.remove(instanceIndex);
    }

    public void tick(){
        getCooldownHandler().tick(player);
        CastEndData castEndData =  castingInstance.castTick();
        if(castEndData != null){
            int cooldownTime = castEndData.getSkill().getCooldown(castEndData);
            getCooldownHandler().putSkillOnCooldown(castEndData.skillId(),cooldownTime);
        }
        ArrayList<PersistentCastingInstance> toRemove = new ArrayList<>();
        for(PersistentCastingInstance instance : getPersistentInstances()){
            if(!instance.tick(player)) toRemove.add(instance);
        }
        for(PersistentCastingInstance instance: toRemove){
            persistentCastingInstances.remove(instance);
        }

    }

    public CooldownHandler getCooldownHandler(){
        return cooldownHandler;
    }
    public CastingInstance getCastingInstance(){
        return castingInstance;
    }
    public List<PersistentCastingInstance> getPersistentInstances(){
        return persistentCastingInstances;
    }

    public boolean isCasting(){
        return castingInstance.isCasting();
    }

}
