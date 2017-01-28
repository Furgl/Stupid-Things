package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntityBalloon;
import furgl.stupidThings.common.entity.EntityBalloonLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemBalloonLiquid extends ItemBalloon {

	private BlockLiquid liquid;
	
	private ItemBalloonLiquid(BlockLiquid liquid) {
		super();
		this.liquid = liquid;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else {
				tooltip.add(TextFormatting.DARK_RED+"Right click to throw");
				tooltip.add(TextFormatting.DARK_RED+"Spawns "+liquid.getLocalizedName().toLowerCase()+" on impact");
			}
	}
	
	@Override
	protected void throwBalloon(World world, EntityPlayer player) {
		EntityBalloon balloon = new EntityBalloonLiquid(liquid, world, player);
        balloon.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2F, 1.0F);
        world.spawnEntityInWorld(balloon);
	}
	
	public static class ItemBalloonWater extends ItemBalloonLiquid {
		public ItemBalloonWater() {
			super(Blocks.WATER);
		}
	}
	public static class ItemBalloonLava extends ItemBalloonLiquid {
		public ItemBalloonLava() {
			super(Blocks.LAVA);
		}
	}
}