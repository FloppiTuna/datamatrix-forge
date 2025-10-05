package xyz.meowricles.datamatrix.items.media;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;

public class CompactDiscItem extends Item implements IStorageMedium {
    private final int capacity;
    private final Path savePath;

    public CompactDiscItem(Properties props, int capacity) {
        super(props);
        this.capacity = capacity;

        // Save path for all CDs of this type
        this.savePath = Paths.get("datamatrix/storage/compact_disc");
        try {
            Files.createDirectories(savePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage folder", e);
        }
    }

    /** Ensures this ItemStack has a storage_id in its NBT */
    private void ensureStorageId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("storage_id")) {
            tag.putString("storage_id", UUID.randomUUID().toString());
            // no need for stack.setTag(tag); getOrCreateTag() already mutates the real NBT
        }
    }

    private File getFile(ItemStack stack) {
        ensureStorageId(stack);
        String id = stack.getOrCreateTag().getString("storage_id");

        File f = savePath.resolve(id + ".bin").toFile();
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Could not create storage file " + f, e);
            }
        }
        return f;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getUsed(ItemStack stack) {
        return (int) getFile(stack).length();
    }

    @Override
    public byte[] read(ItemStack stack, int offset, int length) {
        try (RandomAccessFile raf = new RandomAccessFile(getFile(stack), "r")) {
            raf.seek(offset);
            byte[] buf = new byte[length];
            raf.read(buf);
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    public void write(ItemStack stack, int offset, byte[] data) {
        try (RandomAccessFile raf = new RandomAccessFile(getFile(stack), "rw")) {
            raf.seek(offset);
            raf.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ensureStorageId(stack);

            player.sendSystemMessage(Component.literal(
                    "Storage ID: " + stack.getOrCreateTag().getString("storage_id")));

            // write random data
            byte[] data = new byte[capacity];
            new Random().nextBytes(data);
            write(stack, 0, data);

            player.sendSystemMessage(Component.literal(
                    "Wrote random data (" + data.length + " bytes)"));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
