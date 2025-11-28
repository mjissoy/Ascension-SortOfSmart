package net.thejadeproject.ascension.cultivation.player.data_attachements;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.network.clientBound.SyncCastingInstance;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerQi;
import net.thejadeproject.ascension.progression.skills.data.CastSource;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.util.ModAttributes;
import org.apache.logging.log4j.util.Cast;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class PlayerData {
    //TODO add a packet for syncing castingThread data like ticks elapsed

    public Player player;
    public PlayerData(Player player){
        this.player =player;
        cultivationData = new CultivationData(player);
    }
    /********* CULTIVATION PROGRESS *******************************************************/
    private final CultivationData cultivationData;

    public CultivationData getCultivationData(){ return cultivationData;}

    /********* Player Qi *******************************************************/

    private double currentQi =  0;
    private int ticksSinceRegen = 0;
    private final int TICKS_FOR_REGEN = 60;
    public void increaseQi(double qi){
        double max = player.getAttribute(ModAttributes.PLAYER_QI_INSTANCE).getValue();
        setQi(Math.min(currentQi + qi,max));
    }
    public void setQiNoSync(double qi){
        currentQi = qi;
    }
    public void setQi(double qi){
        currentQi = qi;
        syncQi();
    }
    public double getCurrentQi(){
        return  currentQi;
    }
    public double getPlayerMaxQi(){
        return player.getAttribute(ModAttributes.PLAYER_QI_INSTANCE).getValue();
    }
    public boolean tryConsumeQi(double amount){
        if(currentQi < amount) return false;
        setQi(currentQi-amount);
        return true;

    }
    public void syncQi(){
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncPlayerQi(getCurrentQi()));
    }

    public void regenQi(){
        ticksSinceRegen += 1;

        if(ticksSinceRegen >= TICKS_FOR_REGEN){
            increaseQi(player.getAttribute(ModAttributes.PLAYER_QI_REGEN_RATE).getValue());
            ticksSinceRegen = 0;
        }
    }
    /********* Casting Data *******************************************************/
    //using indexes could cause sync issues cus of index shuffling

    private ResourceLocation selectedSkillId;
    private CastingInstance primarySkillCastingInstance = null;
    private final List<UUID> castingThreadQueue = new ArrayList<>();
    private final HashMap<UUID,CastingInstance> castingThreads = new HashMap<>();

    public void setSelectedSkillId(ResourceLocation skillId){
        selectedSkillId = skillId;
    }
    public ResourceLocation getSelectedSkillId(){
        return selectedSkillId;
    }
    public int getSkillCooldown(String skillId){
        AbstractActiveSkill activeSkill = (AbstractActiveSkill) AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        return activeSkill.getCooldown();
    }
    public double getQiCost(String skillId){
        AbstractActiveSkill activeSkill = (AbstractActiveSkill) AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillId,':'));
        return activeSkill.getQiCost();
    }

    public void castSelectedSkill(){
        if(selectedSkillId == null) return;
        ((AbstractActiveSkill)AscensionRegistries.Skills.SKILL_REGISTRY.get(selectedSkillId)).attemptInitialCast(
                player,
                player.level(),
                CastSource.PLAYER
        );
    }

    public CastingInstance getSkillFromSlot(int slot){
        if(slot == 0) return primarySkillCastingInstance;
        if(slot >= castingThreadQueue.size()+1) return null;
        return castingThreads.get(castingThreadQueue.get(slot-1));
    }

    public void tryAddSecondarySkillCastingInstance(CastingInstance castingInstance,boolean insertAtEnd){
        //attempted to add secondary skill casting instance
        System.out.println("trying to cast secondary skill");
        //if casting threads are full remove one and replace it
        if(castingThreads.size() >= player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue()){
            //cancel the cast of the first available secondary skill
            UUID toCancel = null;
            if(insertAtEnd) toCancel= castingThreadQueue.removeFirst();
            else toCancel = castingThreadQueue.removeLast();
            putSkillOnCooldown(
                    castingThreads.get(toCancel).skillId,
                    getSkillCooldown(castingThreads.get(toCancel).skillId.toString())
            );
            castingThreads.remove(toCancel).cancelCast();
        }

        if(insertAtEnd) castingThreadQueue.add(castingInstance.uuid);
        else castingThreadQueue.addFirst(castingInstance.uuid);
        castingThreads.put(castingInstance.uuid,castingInstance);

    }

    //TODO do a QI check
    //TODO if cast type is instant, cast without creating thread. but only if thread is available
    //move this over to a server only thing? since it does need to be synced with the client
    //nah should be fine
    public void tryCast(ISkill skill){
        //make sure it is not on cooldown
        System.out.println("trying to cast");
        if(isSkillOnCooldown(AscensionRegistries.Skills.SKILL_REGISTRY.getKey(skill))) return;

        UUID uuid = UUID.randomUUID();
        CastingInstance castingInstance = new CastingInstance(skill,uuid);
        if(!(skill instanceof AbstractActiveSkill activeSkill)) return;

        //consumeQi
        if(!tryConsumeQi(((AbstractActiveSkill) skill).getQiCost())){
            player.displayClientMessage(Component.literal("not enough qi").withColor(ChatFormatting.RED.getColor()),true);
            return;
        }

        //use primary skill thread if skill is primary or user has 0 max casting instances
        if(activeSkill.isPrimarySkill() || player.getAttribute(ModAttributes.MAX_CASTING_INSTANCES).getValue() == 0 || primarySkillCastingInstance == null){
            System.out.println("casting primary skill or secondary as primary");
            if(primarySkillCastingInstance != null) {
                System.out.println("canceled "+primarySkillCastingInstance.skillId.toString()+ " skill cast");
                AbstractActiveSkill skill2 =(AbstractActiveSkill) AscensionRegistries.Skills.SKILL_REGISTRY.get(primarySkillCastingInstance.skillId);
                if(!skill2.isPrimarySkill())  tryAddSecondarySkillCastingInstance(castingInstance,false);
                else {
                    putSkillOnCooldown(
                            primarySkillCastingInstance.skillId,
                            getSkillCooldown(primarySkillCastingInstance.skillId.toString())
                    );
                    primarySkillCastingInstance.cancelCast();

                }
            }
            if(activeSkill.getCastType() == CastType.INSTANT){
                System.out.println("casting instant spell");
                activeSkill.cast(0,player.level(),player);
                return;
            }
            System.out.println("setting cast instance");
            primarySkillCastingInstance = castingInstance;
            System.out.println(primarySkillCastingInstance.skillId);
            return;
        }
        tryAddSecondarySkillCastingInstance(castingInstance,true);
    }


    public void setPrimarySkillCastingInstance(CastingInstance castingInstance){
        this.primarySkillCastingInstance = castingInstance;
    }

    public void removeCastingInstanceThread(UUID uuid){
        putSkillOnCooldown(
                castingThreads.get(uuid).skillId,
                getSkillCooldown(castingThreads.get(uuid).skillId.toString())
        );
        castingThreads.remove(uuid);
        castingThreadQueue.remove(uuid);
    }

    public void tickAllCastingThreads(){

        if(primarySkillCastingInstance != null){

            if(!primarySkillCastingInstance.tick(player.level(),player)){
                System.out.println("cast: "+ primarySkillCastingInstance.skillId.toString());
                putSkillOnCooldown(
                        primarySkillCastingInstance.skillId,
                        getSkillCooldown(primarySkillCastingInstance.skillId.toString())
                );
                primarySkillCastingInstance = null;
                System.out.println("skill cast finished");

            }
        }

        for(CastingInstance instance : castingThreads.values()){
            if(!instance.tick(player.level(),player)){
                System.out.println("cast : "+instance.skillId.toString());
                removeCastingInstanceThread(instance.uuid);

            }
        }
    }

    /********* Cooldown Data *******************************************************/
    private final HashMap<ResourceLocation,Integer> cooldowns = new HashMap<>();

    public boolean isSkillOnCooldown(ResourceLocation skillId){
        if(!cooldowns.containsKey(skillId)) return false;
        NumberFormat format = new DecimalFormat("#0.00");
        player.displayClientMessage(Component.literal("On Cooldown : "+format.format(cooldowns.get(skillId)/20.0))+"s".withColor(ChatFormatting.RED.getColor()),true);


        return true;
    }

    public void tickAllCooldowns(){
        for(ResourceLocation skill : cooldowns.keySet()){
            cooldowns.put(skill,cooldowns.get(skill)-1);
            if(cooldowns.get(skill) <= 0){
                cooldowns.remove(skill);
            }
        }
    }

    public void putSkillOnCooldown(ResourceLocation skillId,int duration){
        cooldowns.put(skillId,duration);
    }
    /********* SYSTEM *******************************************************/
    public void loadNBTData(CompoundTag tag, HolderLookup.Provider provider){
        getCultivationData().loadNBTData(tag.getCompound("path_data"));

        currentQi = tag.getDouble("qi");
    }
    public void saveNBTData(CompoundTag tag,HolderLookup.Provider provider){
        tag.put("path_data",getCultivationData().writeNBTData());
        tag.putDouble("qi",currentQi);
    }



    public void onServerTick(ServerTickEvent.Post event){
        tickAllCooldowns();
        tickAllCastingThreads();
        syncCastingData();
        regenQi();

    }

    public void syncCastingData(){
        CastingInstance primaryInstance = primarySkillCastingInstance;
        if(primarySkillCastingInstance == null) {
            primaryInstance = new CastingInstance(null,null,null);
            primaryInstance.isReal = false;
        }
        List<CastingInstance> threads = new ArrayList<>();
        for(UUID id:castingThreadQueue){
            threads.add(castingThreads.get(id));
        }
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SyncCastingInstance(primaryInstance,threads));
    }

    public void setCastingInstances(List<CastingInstance> instances){
        castingThreads.clear();
        castingThreadQueue.clear();
        for(CastingInstance instance : instances){
            castingThreadQueue.add(instance.uuid);
            castingThreads.put(instance.uuid,instance);
        }
    }
}
