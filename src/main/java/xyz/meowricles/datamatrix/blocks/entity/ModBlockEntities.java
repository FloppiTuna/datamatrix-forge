package xyz.meowricles.datamatrix.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.meowricles.datamatrix.Datamatrix;
import xyz.meowricles.datamatrix.blocks.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<? >> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Datamatrix.MODID);

    public static final RegistryObject<BlockEntityType<CDPlayerEntity>> CD_PLAYER_ENTITY =
            BLOCK_ENTITIES.register("cd_player_entity", () ->
                    BlockEntityType.Builder.of(CDPlayerEntity::new, ModBlocks.CD_PLAYER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
