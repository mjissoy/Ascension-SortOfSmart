package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.events.custom.cultivation.CultivateEvent;

import java.util.List;
import java.util.UUID;

public class CultivationBonusFormation extends FormationNode {


    public final ResourceLocation PATH;
    public final double MULTIPLIER;
    public final double RADIUS;
    public final int QI_DRAIN;
    public AbstractFormationCoreBlockEntity formationCore;
    public CultivationBonusFormation(IFormation formation, UUID uuid, ResourceLocation path, double multiplier, double radius, int qiDrain) {
        super(formation);
        setFormationId(uuid);

        this.PATH = path;
        this.MULTIPLIER = multiplier;
        this.RADIUS = radius;
        this.QI_DRAIN =   qiDrain;
        NeoForge.EVENT_BUS.addListener(this::onCultivate);
    }


    public void onCultivate(CultivateEvent event){
        if(!activeLastTick()) return;
        if(event.path.equals(this.PATH.toString())){
            //range check
            if(event.player.distanceToSqr(formationCore.getBlockPos().getCenter()) > RADIUS*RADIUS) return;

            //adds a multiplier
            event.addMultiplier(MULTIPLIER);
        }
    }

    @Override
    public void tick(AbstractFormationCoreBlockEntity blockEntity, List<ItemStack> jadeSlips) {
        super.tick(blockEntity, jadeSlips);
        formationCore = blockEntity;
    }

    @Override
    public int getEnergyCost() {
        return QI_DRAIN;
    }
}
