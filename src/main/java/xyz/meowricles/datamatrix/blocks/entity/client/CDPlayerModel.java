package xyz.meowricles.datamatrix.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import xyz.meowricles.datamatrix.Datamatrix;
import xyz.meowricles.datamatrix.blocks.entity.CDPlayerEntity;

public class CDPlayerModel extends GeoModel<CDPlayerEntity> {
    @Override
    public ResourceLocation getModelResource(CDPlayerEntity cdPlayerEntity) {
        return ResourceLocation.fromNamespaceAndPath(Datamatrix.MODID, "geo/cd_player.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CDPlayerEntity cdPlayerEntity) {
        return ResourceLocation.fromNamespaceAndPath(Datamatrix.MODID, "textures/block/cd_player.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CDPlayerEntity cdPlayerEntity) {
        return ResourceLocation.fromNamespaceAndPath(Datamatrix.MODID, "animations/cd_player.animation.json");
    }
}
