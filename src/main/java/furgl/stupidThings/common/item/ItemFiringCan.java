package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemFiringCan extends Item implements ICustomTooltip {

	public ItemFiringCan() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack lava = new ItemStack(Items.LAVA_BUCKET);
		ItemStack iron = new ItemStack(Items.IRON_INGOT);
		return new ItemStack[] {
				iron, null, null,
				iron, lava, iron, 
				null, iron, null};
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.DARK_RED+"\"Remember: only YOU can start forest fires.\"", TextFormatting.RED+" - Smokey Bear's evil twin brother"}, new String[0]);
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 999999;
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.FAIL, itemStackIn);
    }
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		if (player.worldObj instanceof WorldServer) {
			Vec3d vec = player.getLookVec().scale(3).add(player.getPositionVector());
			
			// spawn particles
			((WorldServer)player.worldObj).spawnParticle(EnumParticleTypes.LAVA, 
					vec.xCoord, vec.yCoord + (double)player.height, vec.zCoord, 
					2, 0.8d, 0.1d, 0.8d, 0,	new int[] {});
			((WorldServer)player.worldObj).spawnParticle(EnumParticleTypes.FLAME, 
					vec.xCoord, vec.yCoord + (double)player.height, vec.zCoord, 
					2, 0.8d, 0.1d, 0.8d, 0,	new int[] {});
			
			if ((count+1) % 10 == 0) {
				AxisAlignedBB aabb = player.getEntityBoundingBox().expandXyz(1.5d).offset(new BlockPos(player.getLookVec().scale(4)));
				
				// set enemies on fire  
				List<Entity> entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, aabb);
				for (Entity entity : entities)
						entity.setFire(5);
				
				// spawn fire
				Iterable<BlockPos> positions = BlockPos.getAllInBox(new BlockPos(aabb.minX, aabb.minY, aabb.minZ), new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ));
				for (BlockPos pos : positions)
					if (!(pos.getX() == player.getPosition().getX() && pos.getZ() == player.getPosition().getZ()) && player.worldObj.rand.nextInt(5) == 0 && player.worldObj.isAirBlock(pos) && Blocks.FIRE.canPlaceBlockAt(player.worldObj, pos))
						player.worldObj.setBlockState(pos, Blocks.FIRE.getDefaultState());
			}

			// play sound
			if ((count+1) % 2 == 0)
			player.worldObj.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.PLAYERS, 0.9f, 0f + player.worldObj.rand.nextFloat());
		}
	}

}