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
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
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
			for (BlockPos pos2 : positionsToCheck) {
				Block block = worldIn.getBlockState(pos2).getBlock();
				if (block == Blocks.ICE && rand.nextInt(3) == 0) {
					worldIn.setBlockState(pos2, Blocks.WATER.getDefaultState());
					worldIn.notifyBlockOfStateChange(pos, Blocks.WATER);
				}
				else if ((block == Blocks.SNOW || block == Blocks.SNOW_LAYER) && rand.nextInt(3) == 0)
					worldIn.setBlockToAir(pos2);
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
			world.playSound(Minecraft.getMinecraft().thePlayer, pos, SoundEvents.BLOCK_FIRE_AMBIENT, 
					SoundCategory.BLOCKS, rand.nextFloat(), rand.nextFloat());
    }
}