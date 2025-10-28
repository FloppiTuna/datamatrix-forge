package xyz.meowricles.datamatrix.items;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.meowricles.datamatrix.Datamatrix;
import xyz.meowricles.datamatrix.items.ingredients.PolycarbonateBlob;
import xyz.meowricles.datamatrix.items.media.CompactDiscItem;
import xyz.meowricles.datamatrix.items.media.MysteryNandItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Datamatrix.MODID);

    // Ingredients
    public static final RegistryObject<Item> POLYCARBONATE_BLOB =
            ITEMS.register("polycarbonate_blob", () -> new PolycarbonateBlob(new Item.Properties()));


    // Storage Mediums
    public static final RegistryObject<Item> COMPACT_DISC =
            ITEMS.register("compact_disc", () -> new CompactDiscItem(new Item.Properties()));

    public static final RegistryObject<Item> MYSTERY_NAND =
            ITEMS.register("mystery_nand", () -> new MysteryNandItem(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
