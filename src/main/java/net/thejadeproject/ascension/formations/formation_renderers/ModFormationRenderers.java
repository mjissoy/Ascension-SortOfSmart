package net.thejadeproject.ascension.formations.formation_renderers;

import net.lucent.formation_arrays.api.formations.IFormationRenderer;
import net.lucent.formation_arrays.api.registries.FormationRegistry;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.formations.formation_nodes.BarrierFormation;

public class ModFormationRenderers {
    public static final DeferredRegister<IFormationRenderer> FORMATION_RENDERERS = DeferredRegister.create(FormationRegistry.FormationRenderers.RENDERERS_REGISTRY, AscensionCraft.MOD_ID);

    public static final DeferredHolder<IFormationRenderer,  ? extends BarrierRenderer> BARRIER_RENDERER =
            FORMATION_RENDERERS.register("barrier_renderer",
                    BarrierRenderer::new

            );
    public static final DeferredHolder<IFormationRenderer, ? extends AmbientParticleFormationRenderer> CULTIVATION_BONUS_RENDERER =
            FORMATION_RENDERERS.register("cultivation_bonus_renderer",
                    ()->new AmbientParticleFormationRenderer(-11096362,0.2));

    public static final DeferredHolder<IFormationRenderer, ? extends AmbientParticleFormationRenderer> GROWTH_FORMATION_RENDERER =
            FORMATION_RENDERERS.register("growth_formation_renderer",
                    ()->new AmbientParticleFormationRenderer(-15546045,0.4));
    public static void register(IEventBus eventBus){
        FORMATION_RENDERERS.register(eventBus);
    }
}
