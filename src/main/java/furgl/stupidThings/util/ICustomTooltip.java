package furgl.stupidThings.util;

import net.minecraft.item.ItemStack;

public interface ICustomTooltip {

	/**Recipe to be displayed in tooltip when ctrl is held down*/
	public ItemStack[] getTooltipRecipe(ItemStack stack);
		
}
