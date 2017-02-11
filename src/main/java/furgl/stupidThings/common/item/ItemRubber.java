package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemRubber extends Item implements ICustomTooltip {

	public ItemRubber() {
		super();
	}
	
	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return null;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.GRAY+"Used for crafting"}, new String[] {TextFormatting.GRAY+"Smelt Raw Rubber"});
	}
}