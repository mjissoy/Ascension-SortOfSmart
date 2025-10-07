package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.events.custom.skills.SkillPreCastEvent;
import net.thejadeproject.ascension.network.clientBound.SkillCastSyncPayload;
import net.thejadeproject.ascension.progression.skills.data.CastResult;
import net.thejadeproject.ascension.progression.skills.data.CastSource;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public abstract class AbstractActiveSkill implements ISkill{
    public String path;
    public double qiCost;
    public String title;
    public ITextureData skillIcon;
    public AbstractActiveSkill(String title){
        this.title = title;
    }
    @Override
    public String getSkillTitle() {
        return title;
    }
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
    public AbstractActiveSkill setSkillIcon(ITextureData textureData){
        this.skillIcon = textureData;
        return this;
    }
    @Override
    public ITextureData skillIcon() {
        return skillIcon;
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
    public abstract CastType castType();
    @Override
    public ISkillData getSkillData(CompoundTag tag) {
        return null;
    }

    /**
     * could also include things like if they are not holding a sword etc
     * also can include things like required key presses. although i only recommend that for primary skills
     * as secondary skills are able to be multi cast
     * so this will be called on both client and server so key presses can be handled
     * if it returns false on the client sends a packet to the server to cancel
     * @param castingTicksElapsed how many ticks have elapsed since initial cast
     * @return
     */
    public boolean continueCasting(int castingTicksElapsed,Level level){
        return false;
    }

    /**
     *
     * @param ticksElapsed how many ticks since initial cast. mainly used for charge skills
     */
    public abstract void cast(int ticksElapsed);
    //for any server stuff like sound events etc
    public abstract void onPreCast();
    //TODO add some invalid cast conditions
    public CastResult validateSkillCast(Player player,CastSource castSource){
        return new CastResult(CastResult.Type.SUCCESS);
    }
    //For stuff like ensuring valid targets etc
    public boolean checkPreCast(Player player,CastSource castSource){
        return true;
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
        PacketDistributor.sendToPlayer((ServerPlayer) player,new SkillCastSyncPayload(AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this).toString()));

        return true;

    }


}
