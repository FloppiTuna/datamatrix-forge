package xyz.meowricles.datamatrix.items.media;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import xyz.meowricles.datamatrix.utils.FileSizePrettier;

import java.util.List;

public class CompactDiscItem extends StorageMediumBase {
    private static final int CAPACITY = 40_000_000;

    public CompactDiscItem(Properties props) {
        super(props, CAPACITY);
    }

    public void burnDisc(ItemStack stack) {
        // you may want to check that there's data written, or that some "burning" process completed
        burn(stack);
    }

    public boolean isDiscBurned(ItemStack stack) {
        return isBurned(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        String label = getLabel(stack);
        return label.isEmpty() ? super.getName(stack) : Component.literal(label).withStyle(ChatFormatting.ITALIC);
    }



    public String getLabel(ItemStack stack) {
        byte[] labelLengthBytes = ((CompactDiscItem) stack.getItem()).read(stack, 10, 1);
        int labelLength = (labelLengthBytes.length > 0) ? labelLengthBytes[0] : 0;

        byte[] labelBytes = ((CompactDiscItem) stack.getItem()).read(stack, 12, labelLength);

        return new String(labelBytes).trim();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        boolean burned = isDiscBurned(stack);

        String label = getLabel(stack);

        if (burned) {
            tooltip.add(
                    Component.literal("Burned").withStyle(ChatFormatting.GREEN)
            );
        } else {
            tooltip.add(
                    Component.literal("Blank").withStyle(ChatFormatting.GRAY)
            );
        }
    }
}
