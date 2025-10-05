package xyz.meowricles.datamatrix.blocks.entity.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import xyz.meowricles.datamatrix.Datamatrix;
import xyz.meowricles.datamatrix.blocks.entity.CDPlayerEntity;

import javax.xml.crypto.Data;

public class CDPlayerModel extends GeoModel<CDPlayerEntity> {
    @Override
    public ResourceLocation getModelResource(CDPlayerEntity cdPlayerEntity) {
        return new ResourceLocation(Datamatrix.MODID, "geo/cd_player.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CDPlayerEntity cdPlayerEntity) {
        return new ResourceLocation(Datamatrix.MODID, "textures/block/cd_player.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CDPlayerEntity cdPlayerEntity) {
        return new ResourceLocation(Datamatrix.MODID, "animations/cd_player.animation.json");
    }
}
