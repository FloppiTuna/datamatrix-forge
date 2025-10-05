package xyz.meowricles.datamatrix.blocks.devices;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface Device {
    InteractionResult use(BlockState state, Player player, InteractionHand hand);
}
