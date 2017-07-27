package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.sound.ModSoundEvents;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBalloonDeflated extends ItemBalloon {

	public ItemBalloonDeflated() {
		super();
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		if (!OreDictionary.getOres("itemRubber").isEmpty())
			return new ItemStack[] {null, null, null,
					new ItemStack(Items.STRING), OreDictionary.getOres("itemRubber").get(0),
					new ItemStack(Items.DYE, 1, EnumDyeColor.byMetadata(stack.getMetadata()).getDyeDamage()),
					null, null, null};
		else 
			return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {COLORS[stack.getMetadata()]+"Hold right click to blow up"}, 
				OreDictionary.getOres("itemRubber").isEmpty() ? null : new String[0]);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			world.playSound(null, player.getPosition(), ModSoundEvents.BALLOON_INFLATE, SoundCategory.PLAYERS, 0.5f, 1.0f);
			player.setActiveHand(hand);
		}

		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (!world.isRemote) {
			if (ModItems.BALLOON.getRegistryName() != null) {
				ItemStack balloon = new ItemStack(ModItems.BALLOON, 1, stack.getMetadata());
				if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).inventory.addItemStackToInventory(balloon))
					entity.entityDropItem(balloon, 0);
			}

			stack.shrink(1);
		}

		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}
}