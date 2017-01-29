package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBalloon extends Item {

	protected final String[] NAMES = new String[] {"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
	protected final TextFormatting[] COLORS = new TextFormatting[] {TextFormatting.WHITE, TextFormatting.GOLD, TextFormatting.LIGHT_PURPLE, TextFormatting.BLUE, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.LIGHT_PURPLE, TextFormatting.DARK_GRAY, TextFormatting.GRAY, TextFormatting.AQUA, TextFormatting.DARK_PURPLE, TextFormatting.BLUE, TextFormatting.GOLD, TextFormatting.DARK_GREEN, TextFormatting.DARK_RED, TextFormatting.WHITE};
	
	public ItemBalloon() {
		super();
		this.setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote) {
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else {
				tooltip.add(COLORS[stack.getMetadata()]+"Right click to throw");
				tooltip.add(COLORS[stack.getMetadata()]+"Right click the balloon with a lead to attach it");
				tooltip.add("");
				tooltip.add(COLORS[stack.getMetadata()]+"While the balloon is attached to a lead:");
				tooltip.add(COLORS[stack.getMetadata()]+"- holding it will pull you up");
				tooltip.add(COLORS[stack.getMetadata()]+"- sneak to slowly float down again");
				tooltip.add(COLORS[stack.getMetadata()]+"- right click a fence to attach the balloon to it");
				tooltip.add(COLORS[stack.getMetadata()]+"- right click the balloon to detach the lead");
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return NAMES[stack.getMetadata()]+" "+super.getItemStackDisplayName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < 16; ++i)
			subItems.add(new ItemStack(itemIn, 1, i));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (!player.capabilities.isCreativeMode)
			--stack.stackSize;

		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 
				0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote) 
			this.throwBalloon(world, player, stack.getMetadata());

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	protected void throwBalloon(World world, EntityPlayer player, int meta) {
		EntityBalloon balloon = new EntityBalloon(world, player, meta);
		balloon.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.3F, 1.0F);
		world.spawnEntityInWorld(balloon);
	}
}