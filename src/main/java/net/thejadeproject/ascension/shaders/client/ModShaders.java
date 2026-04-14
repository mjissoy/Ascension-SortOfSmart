package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.thejadeproject.ascension.AscensionCraft;

import java.io.IOException;

public class ModShaders {
    private static ShaderInstance riftShader;

    // Texture ResourceLocations used by the rift shader
    public static final ResourceLocation TEX_VORTEX    = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/vortex.png");
    public static final ResourceLocation TEX_CRACK_1   = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rift_crack_bloom_1.png");
    public static final ResourceLocation TEX_CRACK_2   = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rift_crack_bloom_2.png");
    public static final ResourceLocation TEX_CRACK_3   = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rift_crack_bloom_3.png");
    public static final ResourceLocation TEX_CRACK_4   = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rift_crack_bloom_4.png");
    public static final ResourceLocation TEX_CRACK_5   = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rift_crack_bloom_5.png");
    public static final ResourceLocation TEX_RUNE_GLOW = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/rune_glow.png");

    public static ShaderInstance getRiftShader() {
        return riftShader;
    }

    public static void register(RegisterShadersEvent event) throws IOException {
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "rift"),
                        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
                ),
                shader -> riftShader = shader
        );
    }

    /**
     * Call once per frame before rendering the rift.
     * Sets the GameTime uniform and binds textures to their sampler slots.
     *
     * NOTE: Unit 2 is reserved for the lightmap (Sampler2), so we skip it and
     * use higher units to avoid conflicts with Minecraft's internal bindings.
     */
    public static void uploadRiftUniforms(float partialTick) {
        ShaderInstance s = riftShader;
        if (s == null) return;

        // Upload GameTime uniform
        float ticks = (Minecraft.getInstance().level != null
                ? Minecraft.getInstance().level.getGameTime()
                : 0L) + partialTick;

        if (s.getUniform("GameTime") != null) {
            s.getUniform("GameTime").set(ticks);
        }

        // Bind sampler uniforms to texture units and bind textures
        // Unit 2 is reserved for Sampler2 (lightmap), so we start at 3
        bindSampler(s, "SamplerVortex",   TEX_VORTEX,    3);
        bindSampler(s, "SamplerCrack1",   TEX_CRACK_1,   4);
        bindSampler(s, "SamplerCrack2",   TEX_CRACK_2,   5);
        bindSampler(s, "SamplerCrack3",   TEX_CRACK_3,   6);
        bindSampler(s, "SamplerCrack4",   TEX_CRACK_4,   7);
        bindSampler(s, "SamplerCrack5",   TEX_CRACK_5,   8);
        bindSampler(s, "SamplerRuneGlow", TEX_RUNE_GLOW, 9);

        // Note: Sampler2 (lightmap) is automatically bound by Minecraft
        // because we setLightmapState(LIGHTMAP) in the RenderType
    }

    private static void bindSampler(ShaderInstance shader, String uniformName, ResourceLocation loc, int unit) {
        // Set the uniform to the texture unit index
        var uniform = shader.getUniform(uniformName);
        if (uniform != null) {
            uniform.set(unit);
        }

        // Bind the actual texture to that unit
        var tex = Minecraft.getInstance().getTextureManager().getTexture(loc, null);
        if (tex != null) {
            RenderSystem.setShaderTexture(unit, tex.getId());
        }
    }
}