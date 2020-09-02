package furgl.stupidThings.common.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Maps;

import furgl.stupidThings.common.sound.ModSoundEvents;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMineTurtle extends BlockDirectional implements ICustomTooltip {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.6d, 1);
	private static HashMap<BlockPos, Integer> activeTurtles = Maps.newHashMap();

	protected BlockMineTurtle() {
		super(Material.CLOTH);
		this.setHardness(1.5F);
		this.setResistance(10.0f);
		this.setSoundType(SoundType.CLOTH);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if (!worldIn.isRemote && !activeTurtles.containsKey(pos)) {
			activeTurtles.put(pos, 30);
			worldIn.playSound(null, pos, ModSoundEvents.MINE_TURTLE, SoundCategory.BLOCKS, 1.0F, 1.05F+worldIn.rand.nextFloat()/3f);
		}
	}

	@SubscribeEvent
	public void onEvent(WorldTickEvent event) {
		if (!activeTurtles.isEmpty() && event.phase == Phase.START && !event.world.isRemote) {
			ArrayList<BlockPos> posToRemove = new ArrayList<BlockPos>();

			for (BlockPos pos : activeTurtles.keySet()) {
				// if timer up, delete mine turtle and explode
				if (activeTurtles.get(pos) - 1 <= 0 && event.world.getBlockState(pos).getBlock() == this) {
					posToRemove.add(pos);
					event.world.setBlockToAir(pos);
					event.world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4.0F, true);
				}
				else 
					activeTurtles.put(pos, activeTurtles.get(pos) - 1);
			}

			for (BlockPos pos : posToRemove)
				activeTurtles.remove(pos);
		}
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack wool = new ItemStack(Blocks.WOOL, 1, EnumDyeColor.GREEN.getMetadata());
		return new ItemStack[] {wool, new ItemStack(Blocks.STONE_PRESSURE_PLATE), wool, 
				wool, new ItemStack(Blocks.TNT), wool,
				wool, wool, wool};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GREEN+"Hello!", "", TextFormatting.GRAY+"(asdfmovie reference)"}, new String[0]);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
}