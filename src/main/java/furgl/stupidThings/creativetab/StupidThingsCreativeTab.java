package furgl.stupidThings.creativetab;

import furgl.stupidThings.common.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StupidThingsCreativeTab extends CreativeTabs 
{
	public NonNullList<ItemStack> orderedStacks = NonNullList.func_191196_a();

	public StupidThingsCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.itemCatalog);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list) {
		list.clear();
		list.addAll(orderedStacks);
	}
}