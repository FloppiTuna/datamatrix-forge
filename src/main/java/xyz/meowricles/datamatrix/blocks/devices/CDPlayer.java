package xyz.meowricles.datamatrix.blocks.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import xyz.meowricles.datamatrix.blocks.entity.CDPlayerEntity;
import xyz.meowricles.datamatrix.items.ModItems;

import javax.annotation.Nullable;

public class CDPlayer extends BaseEntityBlock {
    public CDPlayer(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        CDPlayerEntity blockEntity = (CDPlayerEntity) level.getBlockEntity(pos);
        if (blockEntity == null) return InteractionResult.PASS;

        ItemStack heldStack = player.getItemInHand(hand);

        // SNEAK = toggle tray
        if (player.isShiftKeyDown()) {
            blockEntity.setTrayOpen(!blockEntity.isTrayOpen());
            player.displayClientMessage(
                    Component.literal("Tray is now " + (blockEntity.isTrayOpen() ? "open" : "closed")),
                    true
            );
            return InteractionResult.SUCCESS;
        }

        // --- Normal right click ---
        if (blockEntity.isTrayOpen()) {
            if (!heldStack.isEmpty() && heldStack.getItem() == ModItems.COMPACT_DISC.get()) {
                // insert disc if empty
                if (!blockEntity.hasDisc()) {
                    blockEntity.setDisc(heldStack.split(1)); // remove one from hand
                    player.displayClientMessage(Component.literal("Inserted Compact Disc"), true);
                } else {
                    player.displayClientMessage(Component.literal("There's already a disc inside!"), true);
                }
            } else {
                // eject disc if present
                if (blockEntity.hasDisc()) {
                    ItemStack disc = blockEntity.getDisc();
                    blockEntity.ejectDisc();

                    if (!player.addItem(disc)) {
                        player.drop(disc, false); // drop on ground if inventory full
                    }

                    player.displayClientMessage(Component.literal("Ejected Compact Disc"), true);
                } else {
                    player.displayClientMessage(Component.literal("Tray is empty"), true);
                }
            }
        } else {
            // tray closed → do something else (play, show msg, etc)
            blockEntity.playPressed();
            player.displayClientMessage(Component.literal("CD Player: Play pressed"), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CDPlayerEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
