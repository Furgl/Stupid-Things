package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
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

public class BlockGravityAccelerator extends Block implements ICustomTooltip {

	private static final int RADIUS = 10;

	protected BlockGravityAccelerator() {
		super(Material.IRON);
		this.setHardness(3.5F);
		this.setSoundType(SoundType.STONE);
		this.needsRandomTick = true;
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack iron = new ItemStack(Items.IRON_INGOT);
		ItemStack obsidian = new ItemStack(Blocks.OBSIDIAN);
		return new ItemStack[] {iron, obsidian, iron, 
				obsidian, new ItemStack(Items.ENDER_EYE), obsidian,
				iron, obsidian, iron};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.DARK_GRAY+"Increases gravity for nearby blocks and causes them to fall"}, new String[0]);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (!worldIn.isRemote)
			worldIn.scheduleUpdate(pos, this, 100);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			Iterable<BlockPos> positionsToCheck = BlockPos.getAllInBox(pos.add(-RADIUS, -RADIUS/2, -RADIUS), pos.add(RADIUS, RADIUS/2, RADIUS));
			boolean empty = true;
			for (BlockPos pos2 : positionsToCheck) {
				IBlockState state2 = worldIn.getBlockState(pos2);
				Block block = state2.getBlock();

				if (worldIn.isAirBlock(pos2.down()) && BlockFalling.canFallThrough(worldIn.getBlockState(pos2.down())) && pos2.getY() >= 0 &&
						!worldIn.isAirBlock(pos2) && !pos2.equals(pos) && block.getBlockHardness(state2, worldIn, pos2) > 0) {
					if (rand.nextInt(3) == 0) {
						EntityFallingBlock fallingEntity = new EntityFallingBlock(worldIn, (double)((float)pos2.getX() + 0.5F), (double)pos2.getY(), (double)((float)pos2.getZ() + 0.5F), state2);
						worldIn.spawnEntity(fallingEntity);
						if (worldIn instanceof WorldServer) {
							worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_BREAK, 
									SoundCategory.BLOCKS, rand.nextFloat(), rand.nextFloat()+0.3f);
							((WorldServer)worldIn).spawnParticle(EnumParticleTypes.FALLING_DUST, pos2.getX()+0.5d, pos2.getY()+0.5d, pos2.getZ()+0.5d, 4, 0.4d, 0.4d, 0.4d, 0, new int[] {Block.getStateId(state2)});
						}
					}
					else
						empty = false;
				}
			}
			if (!empty) // schedule new update if there are more valid positions to do
				worldIn.scheduleUpdate(pos, this, 150);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, 
				pos.getX()+rand.nextDouble()*1.8d, pos.getY()+rand.nextDouble()*1.8d, pos.getZ()+rand.nextDouble()*1.8d, 
				0, 0, 0, new int[0]);
		if (rand.nextInt(5) == 0)
			for (int i=0; i<3; i++)
				world.playSound(Minecraft.getMinecraft().player, pos, SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT, 
						SoundCategory.BLOCKS, rand.nextFloat()/2f, rand.nextFloat()+0.3f);
	}
}