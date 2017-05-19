package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemPocketSand extends Item implements ICustomTooltip {

	public ItemPocketSand() {
		super();
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack string = new ItemStack(Items.STRING);
		ItemStack sand = new ItemStack(Blocks.SAND);
		ItemStack leather = new ItemStack(Items.LEATHER);
		return new ItemStack[] {null, string, null,
				leather, sand, leather, 
				null, leather, null};
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.GOLD+"Right click to throw", TextFormatting.GOLD+"Blinds nearby enemies"}, new String[0]);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (worldIn instanceof WorldServer) {
			// spawn particles
			double d0 = (double)(-MathHelper.sin(playerIn.rotationYaw * 0.017453292F));
			double d1 = (double)MathHelper.cos(playerIn.rotationYaw * 0.017453292F);
			double d2 = (double)(-MathHelper.sin(playerIn.rotationPitch * 0.017453292F));
			
			double variation = 0.5d;
			((WorldServer)worldIn).spawnParticle(EnumParticleTypes.BLOCK_CRACK, 
					playerIn.posX + d0*2d, playerIn.posY + (double)playerIn.height * 0.6D + d2 * 2d, playerIn.posZ + d1*2d, 
					40, variation, variation/2d, variation, 0,
					new int[] {Block.getStateId(Blocks.SAND.getDefaultState())});
			((WorldServer)worldIn).spawnParticle(EnumParticleTypes.FALLING_DUST, 
					playerIn.posX + d0*2d, playerIn.posY + (double)playerIn.height * 0.6D + d2 * 2d, playerIn.posZ + d1*2d, 
					40, variation, variation/2d, variation, 0, 
					new int[] {Block.getStateId(Blocks.SAND.getDefaultState())});
			
			// blind entities in front 
			// TODO test that this works properly on server w/ other players
			AxisAlignedBB aabb = playerIn.getEntityBoundingBox().expandXyz(1);
			aabb = aabb.offset(new BlockPos(playerIn.getLookVec().scale(2)));
			List<Entity> entities = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, aabb);
			for (Entity entity : entities)
				if (entity instanceof EntityLivingBase)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, false, false));
			
			// play sound
			worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_SAND_BREAK, SoundCategory.PLAYERS, 1.0f, 1.3f + worldIn.rand.nextFloat());
		}
		
		playerIn.getCooldownTracker().setCooldown(this, 20);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}