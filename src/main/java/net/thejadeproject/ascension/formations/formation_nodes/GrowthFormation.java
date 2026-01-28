package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.neoforged.neoforge.common.Tags;
import net.thejadeproject.ascension.util.ModTags;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

//ONLY TICKS CROPS, or at least i tried to program it like that
//FOR TRUE RANDOM TICK INCREASE USE THE YET IMPLEMENTED TIME DILATION FORMATION
public class GrowthFormation extends FormationNode {

    public final double RADIUS;
    public final int RANDOM_TICKS;
    public final int MIN_INTERVAL; //min ticks before random ticks are run
    public final int MAX_INTERVAL; //max ticks before random ticks are run
    public final int QI_DRAIN;
    public int ticksLeft;
    public GrowthFormation(IFormation formation, UUID formationID,double radius, int randomTicks,int minInterval,int maxInterval,int qiDrain) {
        super(formation);
        setFormationId(formationID);
        RADIUS = radius;
        RANDOM_TICKS = randomTicks;
        MIN_INTERVAL = minInterval;
        MAX_INTERVAL = maxInterval;
        QI_DRAIN = qiDrain;
    }


    public void randomTickNearbyBlocks(Level level,BlockPos core){
        int minX = (int) (core.getX()-RADIUS);
        int minY = (int) (core.getY()-RADIUS);
        int minZ = (int) (core.getZ()-RADIUS);
        int offset = (int) (2*RADIUS);
        List<BlockPos> tickableBlocks =BlockPos.betweenClosedStream(minX,minY,minZ,minX+offset,minY+offset,minZ+offset)
                .filter(pos -> {
                    //make sure in range
                    if(pos.distSqr(core) > RADIUS*RADIUS)return false;
                    if(!level.getBlockState(pos).isRandomlyTicking())return false;
                    Block block = level.getBlockState(pos).getBlock();
                    //System.out.println("checking block :"+block.getName().getString() + " "+((block instanceof BonemealableBlock) || level.getBlockState(pos).is(ModTags.Blocks.HERB)) );
                    return  (block instanceof BonemealableBlock) || level.getBlockState(pos).is(ModTags.Blocks.HERB) ;
                }).map(BlockPos::immutable).toList();

        if(tickableBlocks.isEmpty()) return;
        for(int i = 0; i<RANDOM_TICKS;i++){
            System.out.println("RANDOM TICKING BLOCK");
            int tickedBlock = ThreadLocalRandom.current().nextInt(0,tickableBlocks.size());
            System.out.println("ticking : "+level.getBlockState(tickableBlocks.get(tickedBlock)).getBlock().getName().getString());
            level.getBlockState(tickableBlocks.get(tickedBlock)).randomTick((ServerLevel) level,tickableBlocks.get(tickedBlock),level.random);

        }
    }

    @Override
    public int getEnergyCost() {
        return QI_DRAIN;
    }

    @Override
    public void tick(Level level, BlockPos pos, IFormationCore blockEntity, List<ItemStack> jadeSlips) {

        if(level.isClientSide()) return;
        if(ticksLeft <= 0){
            randomTickNearbyBlocks(level,pos);
            ticksLeft = ThreadLocalRandom.current().nextInt(MIN_INTERVAL,MAX_INTERVAL);
        }
        ticksLeft -= 1;

    }

}
