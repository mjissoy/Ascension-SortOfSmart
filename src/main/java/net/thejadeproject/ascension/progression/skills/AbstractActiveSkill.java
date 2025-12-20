package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.events.custom.skills.SkillPreCastEvent;
import net.thejadeproject.ascension.progression.skills.data.CastResult;
import net.thejadeproject.ascension.progression.skills.data.CastSource;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActiveSkill implements ISkill{
    public String path;
    public double qiCost;
    public Component title;
    public ITextureData skillIcon;
    public List<MutableComponent> skillDescription = new ArrayList<>(); //for static descriptions

    public AbstractActiveSkill(Component title){
        this.title = title;
    }

    /******SKILL DESCRIPTION STUFF*****/

    @Override
    public Component getSkillTitle() {
        return title;
    }


    //used for static skills
    public AbstractActiveSkill setSkillDescription(List<MutableComponent> components){
        skillDescription = components;
        return this;
    }

    @Override
    public List<MutableComponent> getSkillDescription(Player player) {
        return skillDescription;
    }

    /******SKILL ICON STUFF*****/

    public AbstractActiveSkill setSkillIcon(ITextureData textureData){
        this.skillIcon = textureData;
        return this;
    }
    @Override
    public ITextureData skillIcon() {
        return skillIcon;
    }

    /******SKILL DATA STUFF*****/

    @Override
    public String getSkillPath() {
        return path;
    }

    @Override
    public boolean isFixedSkill() {
        //todo uses nbt
        return false;
    }
    @Override
    public void setFixedSkill(boolean fixedSkill) {
        //todo uses nbt data.
    }


    /**
     *
     * @return time in ticks
     */
    public int getCooldown(){
        return 0;
    }
    public double getQiCost(){
        return qiCost;
    };

    public abstract boolean isPrimarySkill();
    public abstract CastType getCastType();
    @Override
    public ISkillData getSkillData(CompoundTag tag) {
        return null;
    }

    public int maxCastingTicks(){return 0;};

    /**
     * could also include things like if they are not holding a sword etc
     * also for stuff like channels
     * @param castingTicksElapsed how many ticks have elapsed since initial cast
     * @return
     */
    public boolean continueCasting(int castingTicksElapsed, Level level,Player player){
        if(castingTicksElapsed >= maxCastingTicks()) cast(castingTicksElapsed,level,player);
        return castingTicksElapsed <maxCastingTicks();
    }

    /**
     *
     * @param castingTicksElapsed how many ticks since initial cast. mainly used for charge skills
     */
    public abstract void cast(int castingTicksElapsed, Level level,Player player);
    //for any server stuff like sound events etc
    public abstract void onPreCast();
    //TODO add some invalid cast conditions
    public CastResult validateSkillCast(Player player,CastSource castSource){
        return new CastResult(CastResult.Type.SUCCESS);
    }
    //For stuff like ensuring valid targets etc
    public boolean checkPreCast(Player player,CastSource castSource){
        return false;
    }
    public boolean attemptInitialCast(Player player, Level level, CastSource castSource){
        
        //this is a server side cast
        //client stuff is done later
        if(level.isClientSide()) return false;
        CastResult castResult =validateSkillCast(player,castSource);
        if(!castResult.isSuccess() || checkPreCast(player,castSource) || NeoForge.EVENT_BUS.post(new SkillPreCastEvent(player, AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this).toString(),castSource)).isCanceled()){
            //handle result here
            return false;
        }
        //make sure to cancel any current actions the player might be doing if this is a primary skill
        if(player.isUsingItem() && isPrimarySkill()){
            player.stopUsingItem();
        }
        player.getData(ModAttachments.PLAYER_DATA).tryCast(this);
        onPreCast();
        player.getData(ModAttachments.PLAYER_DATA).syncCastingData(); //syncs with player
        return true;

    }


}
