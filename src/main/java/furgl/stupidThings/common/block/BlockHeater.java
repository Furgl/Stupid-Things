package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHeater extends Block implements ICustomTooltip {

	private static final int RADIUS = 10;

	protected BlockHeater() {
		super(Material.IRON);
		this.setHardness(3.5F);
		this.setSoundType(SoundType.STONE);
		this.lightValue = 15;
		this.needsRandomTick = true;
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack iron = new ItemStack(Items.IRON_INGOT);
		ItemStack brick = new ItemStack(Blocks.RED_NETHER_BRICK);
		return new ItemStack[] {iron, brick, iron, 
				brick, new ItemStack(Items.LAVA_BUCKET), brick,
				iron, new ItemStack(Blocks.IRON_BARS), iron};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.RED+"Melts nearby snow and ice"}, new String[0]);
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
			boolean empty = true;
			for (BlockPos pos2 : positionsToCheck) {
				Block block = worldIn.getBlockState(pos2).getBlock();
				if (block == Blocks.ICE) 
					if (rand.nextInt(3) == 0) {
						worldIn.setBlockState(pos2, Blocks.WATER.getDefaultState());
						worldIn.updateObservingBlocksAt(pos, Blocks.WATER);
						if (worldIn instanceof WorldServer) {
							worldIn.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, 
									SoundCategory.BLOCKS, rand.nextFloat()/3f, rand.nextFloat());
							((WorldServer)worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos2.getX()+0.5d, pos2.getY()+0.5d, pos2.getZ()+0.5d, 4, 0.4d, 0.4d, 0.4d, 0, new int[0]);
						}
					}
					else
						empty = false;
				else if (block == Blocks.SNOW || block == Blocks.SNOW_LAYER)
					if (rand.nextInt(3) == 0) {
						worldIn.setBlockToAir(pos2);
						if (worldIn instanceof WorldServer) {
							worldIn.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, 
									SoundCategory.BLOCKS, rand.nextFloat()/3f, rand.nextFloat());
							((WorldServer)worldIn).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos2.getX()+0.5d, pos2.getY()+0.5d, pos2.getZ()+0.5d, 4, 0.4d, 0.4d, 0.4d, 0, new int[0]);
						}
					}
					else 
						empty = false;

				if (!empty) // schedule new update if there are more valid positions to do
					worldIn.scheduleUpdate(pos, this, 150);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		world.spawnParticle(rand.nextBoolean() ? EnumParticleTypes.SMOKE_LARGE : EnumParticleTypes.SMOKE_NORMAL, 
				pos.getX()+rand.nextDouble(), pos.getY()+1.0d, pos.getZ()+rand.nextDouble(), 
				0, 0, 0, new int[0]);
		if (rand.nextInt(10) == 0)
			world.playSound(Minecraft.getMinecraft().player, pos, SoundEvents.BLOCK_FIRE_AMBIENT, 
					SoundCategory.BLOCKS, rand.nextFloat(), rand.nextFloat());
	}
}