package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRailExplosive extends BlockRailPowered implements ICustomTooltip {

	protected BlockRailExplosive() {
		super();
		this.setHardness(0.7F);
		this.setSoundType(SoundType.METAL);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, null, null, 
				new ItemStack(Blocks.TNT), new ItemStack(Blocks.RAIL), null,
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.DARK_RED+"Explodes when a minecart passes",
						TextFormatting.DARK_RED+"over while this is powered"}, new String[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (world.isRemote && world.getBlockState(pos).getBlock() == this && 
				world.getBlockState(pos).getValue(BlockRailPowered.POWERED).booleanValue())
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, 
					pos.getX()+rand.nextDouble(), pos.getY(), pos.getZ()+rand.nextDouble(), 0, 0, 0, new int[0]);
	}

	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos) {
		if (!world.isRemote && world.getBlockState(pos).getBlock() == this && 
				world.getBlockState(pos).getValue(BlockRailPowered.POWERED).booleanValue()) {
			world.isAirBlock(pos);
			world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0f, true);
		}
	}
}