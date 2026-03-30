package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndReason;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastingInstance;
import net.thejadeproject.ascension.refactor_packages.skill_casting.persistent_casting.PersistentCastingInstance;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPersistentSkillInstance;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;

import java.util.ArrayList;
import java.util.List;

//Do not worry about cast logic yet
public class SkillCastHandler {




    private final CastingInstance castingInstance = new CastingInstance();;

    private final ArrayList<PersistentCastingInstance> persistentCastingInstances = new ArrayList<>();

    private final SkillCooldownHandler cooldownHandler = new SkillCooldownHandler();

    //TODO update to use config with max 14
    private final SkillHotBar hotBar = new SkillHotBar(7);

    public void addPersistentCastingInstance(ResourceLocation skill, IPersistentSkillInstance skillInstance){
        persistentCastingInstances.add(new PersistentCastingInstance(skillInstance,skill));
    }
    public void removePersistentCastingInstances(int instanceIndex){
        persistentCastingInstances.remove(instanceIndex);
    }

    public void tick(Entity entity){
        IEntityData entityData = entity.getData(ModAttachments.ENTITY_DATA);
        getCooldownHandler().tick(entityData);
        CastEndData castEndData =  castingInstance.castTick(entity);
        if(castEndData != null){
            int cooldownTime = castEndData.getSkill().getCooldown(castEndData);
            getCooldownHandler().addCooldown(castEndData.skillId(),cooldownTime);
        }
        ArrayList<PersistentCastingInstance> toRemove = new ArrayList<>();
        for(PersistentCastingInstance instance : getPersistentInstances()){
            if(!instance.tick(entity)) toRemove.add(instance);
        }
        for(PersistentCastingInstance instance: toRemove){
            persistentCastingInstances.remove(instance);
        }

    }

    public SkillCooldownHandler getCooldownHandler(){
        return cooldownHandler;
    }
    public CastingInstance getCastingInstance(){
        return castingInstance;
    }
    public List<PersistentCastingInstance> getPersistentInstances(){
        return persistentCastingInstances;
    }

    public void setSelectedSkill(IEntityData entityData,int selectedSkill){
        hotBar.setActiveSlot(entityData,selectedSkill);
    }

    public boolean isCasting(){
        return castingInstance.isCasting();
    }

    //server side
    //TODO get it to send message to client on fail
    public void tryCast(Entity entity){
        //get selected skill
        if(hotBar.getActiveSkill() == null) return;
        if(!(hotBar.getActiveSkill() instanceof ICastableSkill castableSkill))return;
        IPreCastData preCastData = hotBar.getPreCastData(hotBar.getActiveSlot());
        //call try cast
        CastResult result = castableSkill.canCast(entity,preCastData);
        if(!result.isSuccess()){
            //TODO send message to client
            System.out.println(result.message.getString());
            return;
        }

        //cancel any existing cast

        if(castingInstance.isCasting()){
            CastEndData endData = castingInstance.endCast(entity, CastEndReason.CANCELLED);
            if(endData != null){
                int cooldownTime = endData.getSkill().getCooldown(endData);
                getCooldownHandler().addCooldown(endData.skillId(),cooldownTime);
            }
        }

        //call initial cast
        castableSkill.initialCast(entity,preCastData);
        //if long cast setup cast instance
        if(castableSkill.getCastType() == CastType.LONG){
            castingInstance.startCast(entity,hotBar.getActiveSkillKey());
        }

        //trigger sync
        //TODO trigger sync
    }
    public int getMaxSlots(){
        return hotBar.MAX_SLOTS;
    }
}
