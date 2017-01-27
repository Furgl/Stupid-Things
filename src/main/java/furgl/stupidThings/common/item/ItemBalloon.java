package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.client.gui.GuiScreen;
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

public class ItemBalloon extends Item {

	public ItemBalloon() {
		super();
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else {
				tooltip.add(TextFormatting.DARK_RED+"Right click to throw a balloon");
				tooltip.add(TextFormatting.DARK_RED+"Right click the balloon with a lead to attach it");
				tooltip.add("");
				tooltip.add(TextFormatting.DARK_RED+"While the balloon is attached to a lead:");
				tooltip.add(TextFormatting.DARK_RED+"- holding it will pull you up");
				tooltip.add(TextFormatting.DARK_RED+"- sneak to slowly float down again");
				tooltip.add(TextFormatting.DARK_RED+"- right click a fence to attach the balloon to it");
				tooltip.add(TextFormatting.DARK_RED+"- right click the balloon to detach the lead");
			}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.capabilities.isCreativeMode)
            --stack.stackSize;

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 
        		0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            EntityBalloon balloon = new EntityBalloon(world, player);
            balloon.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.3F, 1.0F);
            world.spawnEntityInWorld(balloon);
        }

        return new ActionResult(EnumActionResult.SUCCESS, stack);
    }
}