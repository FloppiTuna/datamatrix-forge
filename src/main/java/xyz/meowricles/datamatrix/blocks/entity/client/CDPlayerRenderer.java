package xyz.meowricles.datamatrix.blocks.entity.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import xyz.meowricles.datamatrix.blocks.entity.CDPlayerEntity;

public class CDPlayerRenderer extends GeoBlockRenderer<CDPlayerEntity> {
    public CDPlayerRenderer(BlockEntityRendererProvider.Context context) {
        super(new CDPlayerModel());
    }
}
