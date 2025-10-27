package xyz.meowricles.datamatrix.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

public class CDPlayerEntity extends BlockEntity implements GeoBlockEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private ItemStack discStack = ItemStack.EMPTY;
    private boolean trayOpen = false;

    public CDPlayerEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.CD_PLAYER_ENTITY.get(), p_155229_, p_155230_);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    // logic crap
    public boolean hasDisc() {
        return !discStack.isEmpty();
    }

    public ItemStack getDisc() {
        return discStack;
    }

    public void setDisc(ItemStack disc) {
        this.discStack = disc.copy();
        setChanged();
    }

    public void ejectDisc() {
        this.discStack = ItemStack.EMPTY;
        setChanged();
    }

    public boolean isTrayOpen() {
        return trayOpen;
    }

    public void setTrayOpen(boolean trayOpen) {
        this.trayOpen = trayOpen;
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }

        System.out.println("trayOpen=" + trayOpen + " hasDisc=" + hasDisc());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!discStack.isEmpty()) {
            tag.put("disc", discStack.save(new CompoundTag()));
        }
        tag.putBoolean("trayOpen", trayOpen);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("disc")) {
            discStack = ItemStack.of(tag.getCompound("disc"));
        } else {
            discStack = ItemStack.EMPTY;
        }
        trayOpen = tag.getBoolean("trayOpen");
    }



    // animation crap
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "controller", 0, this::predicate)
        );
    }


    private PlayState predicate(AnimationState<CDPlayerEntity> state) {
        if (isTrayOpen()) {
            if (hasDisc()) {
                state.setAnimation(RawAnimation.begin().then("open_disk", Animation.LoopType.HOLD_ON_LAST_FRAME));
            } else {
                state.setAnimation(RawAnimation.begin().then("open", Animation.LoopType.HOLD_ON_LAST_FRAME));
            }
            return PlayState.CONTINUE;
        } else {
            if (hasDisc()) {
                state.setAnimation(RawAnimation.begin().then("close_disk", Animation.LoopType.HOLD_ON_LAST_FRAME));
            } else {
                state.setAnimation(RawAnimation.begin().then("close", Animation.LoopType.HOLD_ON_LAST_FRAME));
            }
            return PlayState.CONTINUE;
        }
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }


}
