package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.sound.ModSoundEvents;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBalloonDeflated extends Item {

	public ItemBalloonDeflated() {
		super();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else 
				tooltip.add(TextFormatting.DARK_RED+"Hold right click to blow up");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			world.playSound(null, player.getPosition(), ModSoundEvents.balloonInflate, SoundCategory.PLAYERS, 0.5f, 1.0f);
			player.setActiveHand(hand);
		}
		
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
		if (!world.isRemote) {
			--stack.stackSize;
			
			ItemStack balloon = new ItemStack(ModItems.balloon);
			if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).inventory.addItemStackToInventory(balloon))
				entity.entityDropItem(balloon, 0);
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