package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

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
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.world.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.GRAY+"Can be smelted into Rubber"}, new String[0]);
	}
}