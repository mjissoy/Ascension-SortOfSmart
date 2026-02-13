package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.api.capability.Capabilities;
import net.lucent.formation_arrays.api.capability.IAccessControlToken;
import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.thejadeproject.ascension.capabilities.AscensionCapabilities;
import net.thejadeproject.ascension.capabilities.IPlayerFilter;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AreaProtectionFormation extends FormationNode {

    public final int QI_DRAIN_PER_PREVENTION;
    public final double RADIUS;
    public BlockPos corePos;
    public IFormationCore core;
    public AreaProtectionFormation(IFormation formation, UUID uuid, int qiDrainPerPrevention, double radius) {
        super(formation);
        QI_DRAIN_PER_PREVENTION = qiDrainPerPrevention;
        this.RADIUS = radius;
        setFormationId(uuid);
        NeoForge.EVENT_BUS.addListener(this::onBlockBreak);
        NeoForge.EVENT_BUS.addListener(this::onMobGrief);
        NeoForge.EVENT_BUS.addListener(this::onBlockPlace);
        NeoForge.EVENT_BUS.addListener(this::onDetonation);
    }

    public boolean doesPlayerHavePerms(Player player){
        List<Pair<IPlayerFilter,ItemStack>> filters = new ArrayList<>();
        for(ItemStack stack : core.getFormationJadeSlips(getFormationId())){
            IPlayerFilter filter = stack.getCapability(AscensionCapabilities.PLAYER_FILTER_CAPABILITY);
            if(filter != null) filters.add(new Pair<>(filter,stack));
        }

        ItemStack controlToken =  ((AbstractFormationCoreBlockEntity) core).getFormationItemStackHandler().getControlToken();
        IAccessControlToken token = controlToken.getCapability(Capabilities.ACCESS_TOKEN_CAPABILITY);
        if(token != null && token.hasPermission(player,controlToken)) return true;
        for(Pair<IPlayerFilter,ItemStack> filter : filters){
            if(filter.getA().filterPlayer(player,filter.getB())) return true;
        }
        return false;
    }

    public boolean tryProtectBlock(BlockPos pos){
        //first check if block is in range
        if(pos.getCenter().distanceToSqr(corePos.getCenter()) > RADIUS*RADIUS) return false;

        //try burn qi to protect it
        return tryBurnEnergy(core,QI_DRAIN_PER_PREVENTION);
    }

    public void onDetonation(ExplosionEvent.Detonate event){
        if(!activeLastTick()) return;

        event.getAffectedBlocks().removeIf(this::tryProtectBlock);

    }

    public void onBlockBreak(BlockEvent.BreakEvent event){
        if(!activeLastTick()) return;
        if(doesPlayerHavePerms(event.getPlayer())) return;
        if(!tryProtectBlock(event.getPos())) return;
        event.setCanceled(true);
    }

    public void onBlockPlace(BlockEvent.EntityPlaceEvent event){
        if(!activeLastTick()) return; // barrier is off
        if(event.getEntity() == null) return;
        if(!(event.getEntity() instanceof Player player)) return;
        if(doesPlayerHavePerms(player)) return;
        if(!tryProtectBlock(event.getPos())) return;
        event.setCanceled(true);
    }
    public void onMobGrief(EntityMobGriefingEvent event){
        if(!activeLastTick()) return;
        if(event.getEntity() instanceof Player player && doesPlayerHavePerms(player)) return;
        if(event.getEntity().position().distanceToSqr(corePos.getCenter()) < RADIUS*RADIUS){
            event.setCanGrief(false);
        }
    }
    @Override
    public void tick(Level level,BlockPos pos, IFormationCore core, List<ItemStack> jadeSlips) {

        corePos = pos;
        this.core = core;
    }

    @Override
    public int getEnergyCost() {
        return 2;
    }
}
