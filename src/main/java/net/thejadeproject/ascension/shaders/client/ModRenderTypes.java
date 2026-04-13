package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL14;

public class ModRenderTypes {

    /** Additive blend: src*srcAlpha + dst*1 — gives a glowing "add light" look. */
    // MOVED HERE: Must be defined BEFORE it is used by RIFT
    public static final RenderType.TransparencyStateShard RIFT_TRANSPARENCY =
            new RenderStateShard.TransparencyStateShard(
                    "rift_transparency",
                    () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(
                                GlStateManager.SourceFactor.SRC_ALPHA,
                                GlStateManager.DestFactor.ONE          // additive
                        );
                    },
                    () -> {
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    });

    /**
     * The rift render type.
     *
     * Texture binding is handled entirely inside the shader (via named samplers
     * uploaded in ModShaders.uploadRiftUniforms), so we use an EmptyTextureStateShard.
     *
     * Additive-ish blending (SRC_ALPHA + ONE) makes the green glow bloom over the
     * world without cutting a hard-edged hole in it.
     */
    public static final RenderType RIFT = RenderType.create(
            "ascension_rift",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            256,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ModShaders.getRiftShader()))
                    .setTextureState(new RenderStateShard.EmptyTextureStateShard(() -> {}, () -> {}))
                    .setTransparencyState(RIFT_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    // ── Ghost render type (unchanged) ────────────────────────────────────────

    private static final RenderType.TransparencyStateShard GHOST_TRANSPARENCY =
            new RenderStateShard.TransparencyStateShard(
                    "ghost_transparency",
                    () -> {
                        RenderSystem.enableBlend();
                        RenderSystem.blendFunc(
                                GlStateManager.SourceFactor.CONSTANT_ALPHA,
                                GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA
                        );
                        GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
                    },
                    () -> {
                        GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.disableBlend();
                        RenderSystem.defaultBlendFunc();
                    });

    public static final RenderType GHOST = RenderType.create(
            "ascension:ghost",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            2097152,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setTextureState(RenderType.BLOCK_SHEET)
                    .setTransparencyState(GHOST_TRANSPARENCY)
                    .createCompositeState(false)
    );
}