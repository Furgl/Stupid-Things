package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntityBalloon;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBalloon extends Item implements ICustomTooltip {

	public final static String[] NAMES = new String[] {"White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};
	public final static TextFormatting[] COLORS = new TextFormatting[] {TextFormatting.WHITE, TextFormatting.GOLD, TextFormatting.LIGHT_PURPLE, TextFormatting.BLUE, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.LIGHT_PURPLE, TextFormatting.DARK_GRAY, TextFormatting.GRAY, TextFormatting.AQUA, TextFormatting.DARK_PURPLE, TextFormatting.BLUE, TextFormatting.GOLD, TextFormatting.DARK_GREEN, TextFormatting.DARK_RED, TextFormatting.WHITE};

	public ItemBalloon() {
		super();
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {COLORS[stack.getMetadata()]+"Right click to throw",
						COLORS[stack.getMetadata()]+"Right click the balloon with a lead to attach it",
						"",
						COLORS[stack.getMetadata()]+"While the balloon is attached to a lead:",
						COLORS[stack.getMetadata()]+"- holding multiple balloons will pull you up",
						COLORS[stack.getMetadata()]+"- sneak to slowly float down again",
						COLORS[stack.getMetadata()]+"- right click a fence to attach the balloon to it",
						COLORS[stack.getMetadata()]+"- right click the balloon to detach the lead"}, 
				new String[] {COLORS[stack.getMetadata()]+"Hold right click with a deflated balloon to blow it up"});
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return NAMES[stack.getMetadata()]+" "+super.getItemStackDisplayName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (this.isInCreativeTab(tab))
			for (int i = 0; i < 16; ++i)
				subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!player.capabilities.isCreativeMode)
			player.getHeldItem(hand).shrink(1);

		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 
				0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote) 
			this.throwBalloon(world, player, player.getHeldItem(hand).getMetadata());

		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	protected void throwBalloon(World world, EntityPlayer player, int meta) {
		EntityBalloon balloon = new EntityBalloon(world, player, meta);
		balloon.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.3F, 1.0F);
		world.spawnEntity(balloon);
	}
}