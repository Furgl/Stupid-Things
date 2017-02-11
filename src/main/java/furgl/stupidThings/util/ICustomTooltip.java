package furgl.stupidThings.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICustomTooltip {

	/**Recipe to be displayed in tooltip when ctrl is held down*/
	@SideOnly(Side.CLIENT)
	public ItemStack[] getTooltipRecipe(ItemStack stack);
		
}
