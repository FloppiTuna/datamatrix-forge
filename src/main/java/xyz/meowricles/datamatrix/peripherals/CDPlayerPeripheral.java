package xyz.meowricles.datamatrix.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import xyz.meowricles.datamatrix.Datamatrix;
import xyz.meowricles.datamatrix.blocks.entity.CDPlayerEntity;
import xyz.meowricles.datamatrix.items.media.CompactDiscItem;

import java.nio.charset.StandardCharsets;


public class CDPlayerPeripheral implements GenericPeripheral {
    @Override
    public String id() {
        return ResourceLocation.fromNamespaceAndPath(Datamatrix.MODID, "cd_player").toString();
    }

    @Override
    public PeripheralType getType() {
        return PeripheralType.ofType("cd_player");
    }

    @LuaFunction(mainThread = true)
    public boolean hasDisc(CDPlayerEntity player) {
        return player.hasDisc();
    }

    @LuaFunction(mainThread = true)
    public boolean isFinalized(CDPlayerEntity player) {
        ItemStack stack = player.getDisc();
        CompactDiscItem disc = (CompactDiscItem) stack.getItem();

        return disc.isDiscBurned(stack);
    }

    @LuaFunction(mainThread = true)
    public void finalizeDisc(CDPlayerEntity player) {
        ItemStack stack = player.getDisc();
        CompactDiscItem disc = (CompactDiscItem) stack.getItem();

        disc.burnDisc(stack);
    }

    @LuaFunction(mainThread = true)
    public boolean isTrayOpen(CDPlayerEntity player) {
        return player.isTrayOpen();
    }

    @LuaFunction(mainThread = true)
    public void toggleTray(CDPlayerEntity player) {
        player.setTrayOpen(!player.isTrayOpen());
    }

    @LuaFunction(mainThread = true)
    public byte[] read(CDPlayerEntity player, int offset, int size) throws LuaException {
        if (!player.hasDisc()) {
            throw new LuaException("No disc");
        }

        ItemStack stack = player.getDisc();
        CompactDiscItem disc = (CompactDiscItem) stack.getItem();

        return disc.read(stack, offset, size);
    }

    @LuaFunction(mainThread = true)
    public void write(CDPlayerEntity player, int offset, String data) throws LuaException {
        if (!player.hasDisc()) {
            throw new LuaException("No disc");
        }

        ItemStack stack = player.getDisc();
        CompactDiscItem disc = (CompactDiscItem) stack.getItem();

        byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);
        disc.write(stack, offset, bytes);
    }

}
