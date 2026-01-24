package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.FormationArrays;
import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.List;
import java.util.UUID;
//Only increases QI of formation core it is in
//this is because i have no idea how we would balance it otherwise lol
public class QiAbsorptionFormation extends FormationNode {
    public final int QI_REGEN;
    public final double RADIUS;

    public QiAbsorptionFormation(IFormation formation, UUID formationId,double radius,int qiRegen) {
        super(formation);
        setFormationId(formationId);
        QI_REGEN = qiRegen;
        RADIUS = radius;
    }

    @Override
    public void tick(AbstractFormationCoreBlockEntity blockEntity, List<ItemStack> jadeSlips) {
        super.tick(blockEntity, jadeSlips);
        if(blockEntity.getLevel().isClientSide()) return;
        //TODO make sure to update next formation array update
        System.out.println("trying to increase energy");
        System.out.println(blockEntity.getEnergyContainer().getCurrentEnergy());
        blockEntity.getEnergyContainer().increaseEnergy(QI_REGEN);
        System.out.println(blockEntity.getEnergyContainer().getCurrentEnergy());
        blockEntity.setChanged();


    }

    @Override
    public int getEnergyCost() {
        return 0;
    }
}
