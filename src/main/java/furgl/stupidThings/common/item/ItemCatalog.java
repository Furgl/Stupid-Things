package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCatalog extends Item implements ICustomTooltip {

	public ItemCatalog() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, null, null,
				new ItemStack(Blocks.DIRT), new ItemStack(Items.BOOK), null, 
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GOLD+"Shows all items in the mod"}, new String[0]);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		StupidThings.proxy.openCatalogGui();
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}