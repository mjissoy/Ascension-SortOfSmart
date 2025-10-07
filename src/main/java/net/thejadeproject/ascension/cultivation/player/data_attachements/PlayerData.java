package net.thejadeproject.ascension.cultivation.player.data_attachements;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.util.ModAttributes;

import java.util.*;

public class PlayerData {
    public Player player;
    public PlayerData(Player player){
        this.player =player;
        cultivationData = new CultivationData(player);
    }
    /********* CULTIVATION PROGRESS *******************************************************/
    private final CultivationData cultivationData;

    public CultivationData getCultivationData(){ return cultivationData;}

    /********* Casting Data *******************************************************/
    //using indexes could cause sync issues cus of index shuffling
    private CastingInstance primarySkillCastingInstance = null;
    private final List<UUID> castingThreadQueue = new ArrayList<>();
    private final HashMap<UUID,CastingInstance> castingThreads = new HashMap<>();

    //move this over to a server only thing? since it does need to be synced with the client
    //nah should be fine
    public void tryCast(ISkill skill){
        UUID uuid = UUID.randomUUID();
        CastingInstance castingInstance = new CastingInstance(skill,uuid);
        if(!(skill instanceof AbstractActiveSkill activeSkill)) return;
        //use primary skill thread if skill is primary or user has 0 max casting instances
        if(activeSkill.isPrimarySkill() || player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue() == 0){
            if(primarySkillCastingInstance != null) primarySkillCastingInstance.cancelCast();;
            primarySkillCastingInstance = castingInstance;
            return;
        }
        //attempted to add secondary skill casting instance
        if(castingThreads.size() >= player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue()){
            //cancel the cast of the first available secondary skill
            UUID toCancel = castingThreadQueue.removeFirst();
            castingThreads.remove(toCancel).cancelCast();
        }
        castingThreads.put(castingInstance.uuid,castingInstance);
        castingThreadQueue.add(castingInstance.uuid);
    }

    public void removeCastingInstance(UUID uuid){
        castingThreads.remove(uuid);
        castingThreadQueue.remove(uuid);
    }



    /********* Cooldown Data *******************************************************/

    //TODO
    /********* SYSTEM *******************************************************/
    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){
        getCultivationData().loadNBTData(tag.getCompound("path_data"));

    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        tag.put("path_data",getCultivationData().writeNBTData());
    }
}
