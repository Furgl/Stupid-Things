package furgl.stupidThings.common.block;

import java.util.List;

import furgl.stupidThings.common.entity.EntityReverseTntPrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockReverseTnt extends BlockTNT {

	public BlockReverseTnt() {
		super();
		this.setHardness(0.0F);
		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else 
				tooltip.add(TextFormatting.AQUA+"Pulls in nearby blocks and entities");
	}

	@Override
	/**Copied from BlockTNT with EntityTNTPrimed replaced by EntityReverseTNTPrimed*/
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			EntityTNTPrimed entitytntprimed = new EntityReverseTntPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy());
			entitytntprimed.setFuse((short)(worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
			worldIn.spawnEntityInWorld(entitytntprimed);
		}
	}

	@Override
	/**Copied from BlockTNT with EntityTNTPrimed replaced by EntityReverseTNTPrimed*/
	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote && ((Boolean)state.getValue(EXPLODE)).booleanValue()) {
			EntityTNTPrimed entitytntprimed = new EntityReverseTntPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter);
			worldIn.spawnEntityInWorld(entitytntprimed);
			worldIn.playSound((EntityPlayer)null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}
}