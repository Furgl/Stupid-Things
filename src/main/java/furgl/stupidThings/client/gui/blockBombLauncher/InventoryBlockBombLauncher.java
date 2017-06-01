package furgl.stupidThings.client.gui.blockBombLauncher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IInteractionObject;

public class InventoryBlockBombLauncher extends InventoryBasic implements IInteractionObject {

	public ItemStack launcher;

	public InventoryBlockBombLauncher(ItemStack stack) {
		super(stack.getDisplayName(), true, 9);
		this.launcher = stack;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerBlockBombLauncher(playerInventory, this, playerIn);
	}

	@Override
	public String getGuiID() {
		return "stupidthings:blockBombLauncher";
	}

}