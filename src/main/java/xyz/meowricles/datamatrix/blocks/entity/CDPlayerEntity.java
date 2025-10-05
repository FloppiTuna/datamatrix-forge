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
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

public class CDPlayerEntity extends BlockEntity implements GeoBlockEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private ItemStack discStack;
    private boolean trayOpen = false;

    public CDPlayerEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.CD_PLAYER_ENTITY.get(), p_155229_, p_155230_);
    }

    // logic crap
    public boolean hasDisc() {
        return discStack != null;
    }

    public ItemStack getDisc() {
        return discStack;
    }

    public void setDisc(ItemStack disc) {
        this.discStack = disc.copy();
        setChanged();
    }

    public void ejectDisc() {
        this.discStack = null;
        setChanged();
    }

    public boolean isTrayOpen() {
        return trayOpen;
    }

    public void setTrayOpen(boolean trayOpen) {
        this.trayOpen = trayOpen;
        setChanged();

        System.out.println("Tray set to: " + trayOpen + " hasDisc=" + hasDisc());

        if (level != null) {
            System.out.println("running animations");
            if (trayOpen) {
                if (hasDisc()) {
                    this.triggerAnim("controller", "open_disk");
                } else {
                    this.triggerAnim("controller", "open");
                }
            } else {
                if (hasDisc()) {
                    this.triggerAnim("controller", "close_disk");
                } else {
                    this.triggerAnim("controller", "close");
                }
            }
        }
    }

    public void playPressed() {
        this.triggerAnim("controller", "play");
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (hasDisc()) {
            tag.put("disc", discStack.save(new CompoundTag()));
        }
        tag.putBoolean("trayOpen", trayOpen);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("disc")) {
            discStack = ItemStack.of(tag.getCompound("disc"));
        }
        trayOpen = tag.getBoolean("trayOpen");
    }


    // animation crap
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "controller", 0, this::predicate)
                        .triggerableAnim("open", RawAnimation.begin().thenPlay("open"))
                        .triggerableAnim("close", RawAnimation.begin().thenPlay("close"))
                        .triggerableAnim("open_disk", RawAnimation.begin().thenPlay("open_disk"))
                        .triggerableAnim("close_disk", RawAnimation.begin().thenPlay("close_disk"))
                        .triggerableAnim("play", RawAnimation.begin().thenPlay("play"))
        );
    }


    private PlayState predicate(AnimationState<CDPlayerEntity> state) {
        if (state.getController().getCurrentRawAnimation() == null) {
            System.out.println("setting up");
            state.setAnimation(RawAnimation.begin().thenPlay("idle"));
        }
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }


}
