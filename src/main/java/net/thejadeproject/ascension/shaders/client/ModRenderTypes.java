package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class ModRenderTypes {
    public static final RenderType RIFT = RenderType.create(
            "ascension_rift",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            256,
            RenderType.CompositeState.builder()
                    // CRITICAL: Use lambda supplier to prevent null at init
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ModShaders.getRiftShader()))
                    .setTextureState(new RenderStateShard.EmptyTextureStateShard(() -> {}, () -> {}))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false)
    );
}