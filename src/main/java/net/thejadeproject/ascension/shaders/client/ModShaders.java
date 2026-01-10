package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.thejadeproject.ascension.AscensionCraft;

import java.io.IOException;

public class ModShaders {
    public static ShaderInstance RIFT_SHADER;

    public static void register(RegisterShadersEvent event) throws IOException {
        System.out.println("[ModShaders] Registering rift shader..."); // DEBUG
        event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "rift"),
                        DefaultVertexFormat.POSITION_TEX
                ),
                shader -> {
                    RIFT_SHADER = shader;
                    System.out.println("[ModShaders] RIFT_SHADER loaded successfully!"); // DEBUG
                }
        );
    }
}