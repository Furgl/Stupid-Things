package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.entity.EntityBalloon;
import furgl.stupidThings.common.entity.EntityBalloonLiquid;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBalloonLiquid extends ItemBalloon {

	private BlockLiquid liquid;
	
	private ItemBalloonLiquid(BlockLiquid liquid) {
		super();
		this.liquid = liquid;
	}
	
	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack deflatedBalloon = new ItemStack(ModItems.balloonDeflated, 1, stack.getMetadata());
		ItemStack liquidBucket = new ItemStack(liquid == Blocks.WATER ? Items.WATER_BUCKET : Items.LAVA_BUCKET);
		return new ItemStack[] {deflatedBalloon, deflatedBalloon, deflatedBalloon, 
				deflatedBalloon, liquidBucket, deflatedBalloon, 
				deflatedBalloon, deflatedBalloon, deflatedBalloon};
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {COLORS[stack.getMetadata()]+"Right click to throw",
							COLORS[stack.getMetadata()]+"Spawns "+liquid.getLocalizedName().toLowerCase()+" on impact"}, 
					new String[0]);
	}
	
	@Override
	protected void throwBalloon(World world, EntityPlayer player, int meta) {
		EntityBalloon balloon = new EntityBalloonLiquid(liquid, world, player, meta);
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