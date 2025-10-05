package xyz.meowricles.datamatrix.items.media;

import net.minecraft.world.item.ItemStack;

public interface IStorageMedium {
    int getCapacity();
    int getUsed(ItemStack stack);

    byte[] read(ItemStack stack, int offset, int length);
    void write(ItemStack stack, int offset, byte[] data);
}

