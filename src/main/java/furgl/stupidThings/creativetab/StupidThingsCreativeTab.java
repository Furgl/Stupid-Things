package furgl.stupidThings.creativetab;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StupidThingsCreativeTab extends CreativeTabs 
{
	public ArrayList<ItemStack> orderedStacks = new ArrayList<ItemStack>();

	public StupidThingsCreativeTab(String label) {
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return orderedStacks.isEmpty() ? Item.getItemFromBlock(Blocks.BARRIER) : orderedStacks.get(0).getItem();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(List<ItemStack> list) {
		list.clear();
		list.addAll(orderedStacks);
	}
}