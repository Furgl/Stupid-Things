package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidAcid extends BlockFluidClassic implements ICustomTooltip {

	private static final BlockPos[] DISSOLVE_POSITIONS = new BlockPos[] {new BlockPos(1,0,0), new BlockPos(-1,0,0), new BlockPos(0,0,1), 
			new BlockPos(0,0,-1), new BlockPos(0,-1,0)};

	public BlockFluidAcid(Fluid fluid, Material material) {
		super(fluid, material);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, new ItemStack(Items.ROTTEN_FLESH), null,
				new ItemStack(Items.GUNPOWDER), new ItemStack(Items.WATER_BUCKET),new ItemStack(Items.SPIDER_EYE),
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+"Warning: Extremely corrosive",
						TextFormatting.GREEN+"Rapidly dissolves soft blocks on contact"}, new String[0]);
	}

	@Override
	public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
		if (entity instanceof EntityLivingBase && !entity.world.isRemote && ((EntityLivingBase)entity).attackEntityFrom(DamageSource.LAVA, 2.0f)) {
			entity.world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 
					entity.world.rand.nextFloat()+1f, entity.world.rand.nextFloat()+1.5f);
			((WorldServer)entity.world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX, entity.posY, 
					entity.posZ, 10, 0.5d, 0.5d, 0.5d, 0, new int[0]);
		}
		return null;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);

		//turn source blocks into non-source
		if (!world.isRemote && state.getValue(LEVEL) == 0)
			world.setBlockState(pos, getDefaultState().withProperty(LEVEL, 1));

		//dissolve blocks with low hardness
		if (!world.isRemote) {			
			BlockPos pos2 = pos.add(DISSOLVE_POSITIONS[rand.nextInt(DISSOLVE_POSITIONS.length)]);

			IBlockState state2 = world.getBlockState(pos2);
			float hardness = state2.getBlockHardness(world, pos2);
			if (!world.isAirBlock(pos2) && hardness < 2 && hardness >= 0 && rand.nextInt((int) (hardness*10f+1f)) <= 5 &&
					!(state2.getBlock() instanceof BlockLiquid || state2.getBlock() instanceof IFluidBlock) && 
					(state.getValue(BlockLiquid.LEVEL) != 15 || pos2 == pos.down())) {
				world.setBlockToAir(pos2);
				world.playSound(null, pos2, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 
						rand.nextFloat()+1f, rand.nextFloat()+1.5f);
				((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX(), pos.getY(), pos.getZ(), 
						10, 0.5d, 0.5d, 0.5d, 0, new int[0]);
			}
		}
	}

	@Override
	public boolean isSourceBlock(IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (worldIn.isRemote && rand.nextInt(30) == 0)
			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX()+rand.nextDouble(), pos.getY()+rand.nextDouble(), pos.getZ()+rand.nextDouble(), 0, 0, 0, new int[0]);
	}
}