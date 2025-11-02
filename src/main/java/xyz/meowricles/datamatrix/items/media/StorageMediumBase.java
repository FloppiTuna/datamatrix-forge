package xyz.meowricles.datamatrix.items.media;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

public class StorageMediumBase extends Item implements IStorageMedium {
    private final int capacity;
    private final Path savePath;

    public StorageMediumBase(Properties props, int capacity) {
        super(props);
        this.capacity = capacity;

        this.savePath = Paths.get("datamatrix/storage/" + this.getClass().getName());
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

    public int getUsed(ItemStack stack) {
        return (int) getFile(stack).length();
    }

    public int getCapacity() {
        return capacity;
    }

    /* ---------- Burn/read-only management ---------- */

    /** Check NBT burned flag */
    public boolean isBurned(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("burned") && tag.getBoolean("burned");
    }

    /** Set burned flag in NBT */
    public void setBurned(ItemStack stack, boolean burned) {
        if (stack == null || stack.isEmpty()) return;
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("burned", burned);
    }

    /**
     * Burn/finalize the medium:
     * - mark NBT burned=true
     * - attempt to set the backing file read-only as an OS-level guard
     * After this method completes, further write(...) calls will be rejected by code.
     */
    public void burn(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        if (isBurned(stack)) return; // already burned

        setBurned(stack, true);

        // Best-effort: try to set file read-only on filesystem
        File f = getFile(stack);
        try {
            f.setReadOnly();
        } catch (Exception ignored) {
            // ignore; we still enforce via NBT flags in code
        }
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
        // enforce burned = read-only
        if (isBurned(stack)) {
            // either silently ignore, throw, or log — choose what fits your UX.
            // Throwing will propagate an exception to caller; many mods prefer no throw.
            throw new IllegalStateException("Cannot write to finalized media");
        }

        // enforce capacity guard
        if (offset < 0) throw new IllegalArgumentException("Attempted to access negative offset (" + offset + ")");
        long endPos = (long) offset + data.length;
        if (endPos > capacity) {
            throw new IllegalArgumentException("Write would exceed capacity: " + endPos + " > " + capacity);
        }

        try (RandomAccessFile raf = new RandomAccessFile(getFile(stack), "rw")) {
            raf.seek(offset);
            raf.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
