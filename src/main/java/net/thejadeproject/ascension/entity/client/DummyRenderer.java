package net.thejadeproject.ascension.entity.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.entity.custom.CushionEntity;
import net.thejadeproject.ascension.entity.custom.formation.DummyEntity;

public class DummyRenderer extends EntityRenderer<DummyEntity> {
    public DummyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(DummyEntity dummyEntity) {
        return null;
    }


    @Override
    public boolean shouldRender(DummyEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }
}
