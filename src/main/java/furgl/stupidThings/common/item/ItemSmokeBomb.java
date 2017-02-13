package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntitySmokeBomb;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemSmokeBomb extends Item implements ICustomTooltip {

	public ItemSmokeBomb() {
		super();
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		if (!OreDictionary.getOres("itemRubber").isEmpty())
			return new ItemStack[] {null, null, null,
					OreDictionary.getOres("itemRubber").get(0), new ItemStack(Items.GUNPOWDER), 
					new ItemStack(Items.DYE, 1, EnumDyeColor.byMetadata(stack.getMetadata()).getDyeDamage()),
					null, null, null};
		else 
			return null;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {ItemBalloon.COLORS[stack.getMetadata()]+"Right click to throw", 
							ItemBalloon.COLORS[stack.getMetadata()]+"Releases smoke to blind opponents"}, 
					OreDictionary.getOres("itemRubber").isEmpty() ? null : new String[0]);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ItemBalloon.NAMES[stack.getMetadata()]+" "+super.getItemStackDisplayName(stack);
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

		if (!world.isRemote) {
			EntitySmokeBomb bomb = new EntitySmokeBomb(world, player, stack.getMetadata());
			bomb.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.3F, 1.0F);
			world.spawnEntityInWorld(bomb);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
}