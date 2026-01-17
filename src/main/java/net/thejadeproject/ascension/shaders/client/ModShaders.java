package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.thejadeproject.ascension.AscensionCraft;

import java.io.IOException;

public class ModShaders {
    private static ShaderInstance riftShader;

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
}