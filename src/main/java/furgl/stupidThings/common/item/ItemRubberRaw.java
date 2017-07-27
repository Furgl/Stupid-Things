package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRubberRaw extends Item implements ICustomTooltip {

	public ItemRubberRaw() {
		super();
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack item = new ItemStack(Blocks.SAPLING);
		if (Math.sin(Minecraft.getMinecraft().world.getTotalWorldTime()/6.5f) > 0)
			item = new ItemStack(Blocks.TALLGRASS, 1, EnumType.GRASS.getMeta());
		return new ItemStack[] {item, item, item,
				item, item, item,
				item, item, item};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GRAY+"Can be smelted into Rubber"}, new String[0]);
	}
}