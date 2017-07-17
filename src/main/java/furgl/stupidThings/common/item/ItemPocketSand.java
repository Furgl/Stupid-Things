package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPocketSand extends Item implements ICustomTooltip {

	public ItemPocketSand() {
		super();
		this.setMaxStackSize(1);
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.GOLD+"Right click to throw", TextFormatting.GOLD+"Blinds nearby enemies", "", TextFormatting.GRAY+"(King of the Hill reference)"}, new String[0]);
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world instanceof WorldServer) {
			// spawn particles
			double d0 = (double)(-MathHelper.sin(player.rotationYaw * 0.017453292F));
			double d1 = (double)MathHelper.cos(player.rotationYaw * 0.017453292F);
			double d2 = (double)(-MathHelper.sin(player.rotationPitch * 0.017453292F));
			
			double variation = 0.5d;
			((WorldServer)world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, 
					player.posX + d0*2d, player.posY + (double)player.height * 0.6D + d2 * 2d, player.posZ + d1*2d, 
					40, variation, variation/2d, variation, 0,
					new int[] {Block.getStateId(Blocks.SAND.getDefaultState())});
			((WorldServer)world).spawnParticle(EnumParticleTypes.FALLING_DUST, 
					player.posX + d0*2d, player.posY + (double)player.height * 0.6D + d2 * 2d, player.posZ + d1*2d, 
					40, variation, variation/2d, variation, 0, 
					new int[] {Block.getStateId(Blocks.SAND.getDefaultState())});
			
			// blind entities in front 
			AxisAlignedBB aabb = player.getEntityBoundingBox().grow(1);
			aabb = aabb.offset(new BlockPos(player.getLookVec().scale(2)));
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, aabb);
			for (Entity entity : entities)
				if (entity instanceof EntityLivingBase)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, false, true));
			
			// play sound
			world.playSound(null, player.getPosition(), SoundEvents.BLOCK_SAND_BREAK, SoundCategory.PLAYERS, 1.0f, 1.3f + world.rand.nextFloat());
		}
		
		player.getCooldownTracker().setCooldown(this, 20);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}