package furgl.stupidThings.creativetab;

import furgl.stupidThings.common.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StupidThingsCreativeTab extends CreativeTabs 
{
	public NonNullList<ItemStack> orderedStacks = NonNullList.create();

	public StupidThingsCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.ITEM_CATALOG);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list) {
		list.clear();
		list.addAll(orderedStacks);
	}
	
}