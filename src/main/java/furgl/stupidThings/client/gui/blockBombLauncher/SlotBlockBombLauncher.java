package furgl.stupidThings.client.gui.blockBombLauncher;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotBlockBombLauncher extends Slot {

	public SlotBlockBombLauncher(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemBlock;
    }
	
}