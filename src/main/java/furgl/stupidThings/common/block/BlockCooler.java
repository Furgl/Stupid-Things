package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCooler extends Block implements ICustomTooltip {

	private static final int RADIUS = 10;

	protected BlockCooler() {
		super(Material.IRON);
		this.setHardness(3.5F);
		this.setSoundType(SoundType.STONE);
		this.needsRandomTick = true;
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack iron = new ItemStack(Items.IRON_INGOT);
		ItemStack snow = new ItemStack(Blocks.SNOW);
		return new ItemStack[] {iron, snow, iron, 
				snow, new ItemStack(Blocks.ICE), snow,
				iron, new ItemStack(Blocks.IRON_BARS), iron};
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.world.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.AQUA+"Freezes nearby water and spawns snow"}, new String[0]);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!worldIn.isRemote)
			worldIn.scheduleUpdate(pos, this, 100);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			Iterable<BlockPos> positionsToCheck = BlockPos.getAllInBox(pos.add(-RADIUS, -RADIUS/2, -RADIUS), pos.add(RADIUS, RADIUS/2, RADIUS));
			for (BlockPos pos2 : positionsToCheck) {
				Block block = worldIn.getBlockState(pos2).getBlock();
				if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && rand.nextInt(3) == 0 && worldIn.isAirBlock(pos2.up()) &&
						((Integer)worldIn.getBlockState(pos2).getValue(BlockLiquid.LEVEL)).intValue() == 0) 
					worldIn.setBlockState(pos2, Blocks.ICE.getDefaultState());
				else if (worldIn.isAirBlock(pos2) && Blocks.SNOW_LAYER.canPlaceBlockAt(worldIn, pos2) && rand.nextInt(5) == 0)
					worldIn.setBlockState(pos2, Blocks.SNOW_LAYER.getDefaultState());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, 
				pos.getX()+rand.nextDouble(), pos.getY()+0.5d, pos.getZ()+rand.nextDouble(), 
				(rand.nextDouble()-0.5d)/3d, (rand.nextDouble()-0.5d)/3d, (rand.nextDouble()-0.5d)/3d, new int[0]);
		if (rand.nextInt(5) == 0)
			for (int i=0; i<3; i++)
				world.playSound(Minecraft.getMinecraft().player, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, 
						SoundCategory.BLOCKS, rand.nextFloat()+1.3f, rand.nextFloat()+1.3f);
	}
}