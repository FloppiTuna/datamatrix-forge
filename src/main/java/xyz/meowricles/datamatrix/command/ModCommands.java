package xyz.meowricles.datamatrix.command;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.meowricles.datamatrix.Datamatrix;

public class ModCommands {
    public static final DeferredRegister<Item> COMMANDS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Datamatrix.MODID);

    public static void register(IEventBus eventBus) {
        COMMANDS.register(eventBus);
    }
}
