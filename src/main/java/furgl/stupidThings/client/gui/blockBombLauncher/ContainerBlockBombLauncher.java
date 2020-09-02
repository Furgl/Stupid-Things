package furgl.stupidThings.client.gui.blockBombLauncher;

import javax.annotation.Nullable;

import furgl.stupidThings.common.item.ItemBlockBombLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ContainerBlockBombLauncher extends Container {

	private InventoryBlockBombLauncher launcherInv;

	public ContainerBlockBombLauncher(IInventory playerInventory, InventoryBlockBombLauncher launcherInventory, EntityPlayer player) {		
		this.launcherInv = launcherInventory;

		// add custom slots, Slot 0-9
		for (int y = 0; y < 3; ++y) 
			for (int x = 0; x < 3; ++x) 
				this.addSlotToContainer(new SlotBlockBombLauncher(launcherInventory, x + y * 3, 62 + x * 18, 17 + y * 18));

		// player inventory, Slot 9-35, Slot IDs 9-35
		for (int y = 0; y < 3; ++y)
			for (int x = 0; x < 9; ++x)
				this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));

		// player inventory, Slot 0-8, Slot IDs 36-44
		for (int x = 0; x < 9; ++x) 
			this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 142));

		// read inventory from launcher item
		if(launcherInv.launcher != null && launcherInv.launcher.getItem() instanceof ItemBlockBombLauncher) {
			if (launcherInv.launcher.hasTagCompound()) {
				NBTTagCompound nbt = launcherInv.launcher.getTagCompound();
				NBTTagList list = nbt.getTagList("items", 10);
				if (list != null && !list.isEmpty()) 
					for (int i=0; i<launcherInv.getSizeInventory(); ++i)
						if (list.get(i) instanceof NBTTagCompound)
							launcherInv.setInventorySlotContents(i, new ItemStack((NBTTagCompound) list.get(i)));
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		// write inventory to launcher item
		if(launcherInv.launcher != null && launcherInv.launcher.getItem() instanceof ItemBlockBombLauncher) {
			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for (int i=0; i<launcherInv.getSizeInventory(); ++i)
				if (launcherInv.getStackInSlot(i) != null)
					list.appendTag(launcherInv.getStackInSlot(i).writeToNBT(new NBTTagCompound()));
			if (!list.isEmpty()) {
				nbt.setTag("items", list);
				launcherInv.launcher.setTagCompound(nbt);
			}
			else
				launcherInv.launcher.setTagCompound(null);
		}
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {		
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);

		// used for shift-clicking (crashes without this)
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 45 && index > 8) {
				if (!this.mergeItemStack(itemstack1, 0, 9, false))
					return ItemStack.EMPTY;
			}
			else if (!this.mergeItemStack(itemstack1, 9, 45, true))
				return ItemStack.EMPTY;

			if (itemstack1.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}