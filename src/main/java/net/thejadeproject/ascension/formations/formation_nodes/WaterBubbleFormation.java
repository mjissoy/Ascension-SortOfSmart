package net.thejadeproject.ascension.formations.formation_nodes;

import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormation;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.lucent.formation_arrays.formations.node.FormationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.thejadeproject.ascension.blocks.ModBlocks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WaterBubbleFormation extends FormationNode {


    //blocks that will be checked every tick
    //basically do distanceToSqr +- 25 ish can be adjusted
    public final double BUBBLE_RADIUS;
    public Set<Vec3> qiMembrane = new HashSet<>();
    private final Set<Vec3> replaceBuffer = new HashSet<>();

    public boolean hasDoneInitialClear;
     public WaterBubbleFormation(IFormation formation, UUID uuid, double bubbleRadius) {
        super(formation);
        BUBBLE_RADIUS = bubbleRadius;
        setFormationId(uuid);
        NeoForge.EVENT_BUS.addListener(this::onBlockRemove);
    }

    public void onBlockRemove(BlockEvent.BreakEvent event){
        if(!activeLastTick()) return;


        if(qiMembrane.contains(event.getPos().getCenter())){
            replaceBuffer.add(event.getPos().getCenter());
        }

    }

    //there are ways to make this more efficient but i will not worry abt that for now
    public void generateDryAir(Level level,BlockPos core){
        //get min x ,min y, min z

        int minX = (int) (core.getX()-BUBBLE_RADIUS);
        int minY = (int) (core.getY()-BUBBLE_RADIUS);
        int minZ = (int) (core.getZ()-BUBBLE_RADIUS);
        int offset = (int) (2*BUBBLE_RADIUS);
        BlockPos.betweenClosedStream(minX,minY,minZ,minX+offset,minY+offset,minZ+offset)
                .forEach(blockPos -> {
                    if(blockPos.distSqr(core)<BUBBLE_RADIUS*BUBBLE_RADIUS
                    && blockPos.distSqr(core)>BUBBLE_RADIUS*BUBBLE_RADIUS-27){
                        boolean waterCleared = clearAtBlock(blockPos,level);
                        if(blockPos.distSqr(core)>BUBBLE_RADIUS*BUBBLE_RADIUS-27){
                            if(waterCleared){
                                level.setBlock(
                                        blockPos,
                                        ModBlocks.QI_MEMBRANE.get().defaultBlockState(),
                                        Block.UPDATE_ALL
                                );
                            }
                            qiMembrane.add(blockPos.getCenter());
                        }

                    }});


    }
    public void clearAllWaterInRange(Level level,BlockPos core){
        //get min x ,min y, min z

        int minX = (int) (core.getX()-BUBBLE_RADIUS);
        int minY = (int) (core.getY()-BUBBLE_RADIUS);
        int minZ = (int) (core.getZ()-BUBBLE_RADIUS);
        int offset = (int) (2*BUBBLE_RADIUS);
        BlockPos.betweenClosedStream(minX,minY,minZ,minX+offset,minY+offset,minZ+offset)
                .filter(blockPos -> blockPos.distSqr(core)<BUBBLE_RADIUS*BUBBLE_RADIUS
                )
                .forEach(blockPos -> {
                    boolean waterCleared = clearAtBlock(blockPos,level);
                    if(blockPos.distSqr(core)>BUBBLE_RADIUS*BUBBLE_RADIUS-27){
                        if(waterCleared){
                            level.setBlock(
                                    blockPos,
                                    ModBlocks.QI_MEMBRANE.get().defaultBlockState(),
                                    Block.UPDATE_ALL
                            );
                        }
                        qiMembrane.add(blockPos.getCenter());
                    }



                });

        hasDoneInitialClear = true;
    }

    public boolean clearAtBlock(BlockPos pos,Level level){
        BlockState state =  level.getBlockState(pos);
        FluidState fluidState = level.getFluidState(pos);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)
                && state.getValue(BlockStateProperties.WATERLOGGED)) {

            level.setBlock(
                    pos,
                    state.setValue(BlockStateProperties.WATERLOGGED, false),
                    Block.UPDATE_ALL
            );
            return true;
        }
        else if (fluidState.is(Tags.Fluids.WATER)) {

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }

    public void clearBuffer(Level level){
        for(Vec3 pos : replaceBuffer){
            System.out.println("replacing destroyed block");
            level.setBlock(
                    BlockPos.containing(pos),
                    ModBlocks.QI_MEMBRANE.get().defaultBlockState(),
                    Block.UPDATE_ALL);
        }
        replaceBuffer.clear();
    }

    @Override
    public void tick(Level level, BlockPos pos, IFormationCore core, List<ItemStack> jadeSlips) {


        if(!hasDoneInitialClear)clearAllWaterInRange(level,pos);
        if(qiMembrane.isEmpty()) generateDryAir(level,pos);

        if(!replaceBuffer.isEmpty()) clearBuffer(level);
    }

    @Override
    public void deactivate(Level level, BlockPos pos, IFormationCore core) {

        for(Vec3 blockPos : qiMembrane){
            if(level.getBlockState(BlockPos.containing(blockPos)).is(ModBlocks.QI_MEMBRANE)){
                level.setBlock(
                        BlockPos.containing(blockPos),
                        Blocks.AIR.defaultBlockState(),
                        3
                );
            }

        }
        qiMembrane.clear();
        hasDoneInitialClear = false;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        super.encode(buf);
        buf.writeBoolean(hasDoneInitialClear);
    }

    @Override
    public void decode(RegistryFriendlyByteBuf buf) {
        super.decode(buf);
        hasDoneInitialClear = buf.readBoolean();
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries) {
        super.read(tag, registries);
        hasDoneInitialClear = tag.getBoolean("has_done_initial_clear");
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("has_done_initial_clear",hasDoneInitialClear);
        return tag;
    }
}
