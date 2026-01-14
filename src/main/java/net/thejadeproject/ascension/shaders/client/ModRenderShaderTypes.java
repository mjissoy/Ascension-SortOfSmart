package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModRenderShaderTypes {

    public static final RenderType RIFT = RenderType.create(
            "ascension_rift",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ModShaders.RIFT_SHADER))
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            ResourceLocation.withDefaultNamespace("textures/misc/white.png"),
                            false,
                            false
                    ))

                    // Transparency & visibility
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderType.NO_CULL)
                    .setLightmapState(RenderType.NO_LIGHTMAP)
                    .setOverlayState(RenderStateShard.NO_OVERLAY)

                    // Depth handling
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .createCompositeState(false)
    );
}
