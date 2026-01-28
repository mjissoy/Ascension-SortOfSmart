package net.thejadeproject.ascension.formations.formation_renderers;

import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.IFormationRenderer;
import net.lucent.formation_arrays.api.formations.node.IFormationNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public class AmbientParticleFormationRenderer implements IFormationRenderer {

    public final int particleColour;
    public final double particleDensity;
    public AmbientParticleFormationRenderer(int particleColour,double particleDensity){
        this.particleColour =particleColour;
        this.particleDensity =particleDensity;
    }


    @Override
    public void tick(Level level, BlockPos pos, IFormationCore core, IFormationNode node) {

    }

    @Override
    public void render(RenderLevelStageEvent event, BlockPos core, IFormationNode node) {}
}
