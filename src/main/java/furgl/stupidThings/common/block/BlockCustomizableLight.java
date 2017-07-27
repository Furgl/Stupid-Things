/*package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomizableLight extends Block implements ICustomTooltip {

	public static final PropertyBool VARIABLE = PropertyBool.create("variable");
	public static final PropertyBool HIDDEN = PropertyBool.create("hidden");
	public static final PropertyBool FADE = PropertyBool.create("fade");

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	protected BlockCustomizableLight() {
		super(Material.REDSTONE_LIGHT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HIDDEN, Boolean.valueOf(false)).withProperty(FADE, Boolean.valueOf(false)).withProperty(VARIABLE, Boolean.valueOf(false)));
		//this.lightValue = 15;
		this.translucent = true;
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack glass = new ItemStack(Blocks.GLASS);
		ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST);
		return new ItemStack[] {
				null, glass, null, 
				glass, glowstone, glass,
				null, glass, null};
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.YELLOW+"The easiest way to hide lighting", "", "Can be broken while shifting", "Other placed Hidden Lights can be seen while holding this"}, new String[0]);
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		System.out.println("activated");
		if (worldIn.getBlockState(pos).getBlock() == this) 
			worldIn.setBlockState(pos, this.getDefaultState()
					.withProperty(FADE, true).withProperty(HIDDEN, true).withProperty(VARIABLE, true));
		return true;
    }

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		System.out.println(worldIn.isRemote + ": " + this.lightValue); //TODO remove
		if (state.getValue(VARIABLE)) {
			this.lightValue = worldIn.isBlockIndirectlyGettingPowered(pos);
			//this.setLightLevel(lightValue);
			worldIn.scheduleUpdate(pos, blockIn, 0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
		if (state.getValue(HIDDEN)) {
			EntityPlayer player = StupidThings.proxy.getPlayer();
			if (player != null && ((player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(this))) || 
					(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(this)))
				worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+rand.nextDouble(), pos.getY()+rand.nextDouble(), pos.getZ()+rand.nextDouble(), 0, 0.00d, 0, new int[0]);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		if (state.getValue(HIDDEN)) 
			return EnumBlockRenderType.INVISIBLE;
		else 
			return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		if (state.getValue(HIDDEN)) 
			return false;
		else
			return true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		if (state.getValue(HIDDEN)) 
			return false;
		else
			return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(HIDDEN) && (StupidThings.proxy.getPlayer() == null || !StupidThings.proxy.getPlayer().isSneaking())) 
			return AABB;
		else
			return Block.FULL_BLOCK_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		if (state.getValue(HIDDEN)) 
			return NULL_AABB;
		else
			return Block.FULL_BLOCK_AABB;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean hidden = (meta & 1) != 0;
		boolean variable = (meta & 2) != 0;
		boolean fade = (meta & 4) != 0;
		return this.getDefaultState()
				.withProperty(FADE, fade).withProperty(HIDDEN, hidden).withProperty(VARIABLE, variable);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
	    int meta = 0;
	    for (boolean b : new boolean[] {state.getValue(HIDDEN), state.getValue(VARIABLE), state.getValue(FADE)})
	        meta = (meta << 1) | (b ? 1 : 0);
	    StupidThings.logger.info(state);
	    StupidThings.logger.info(meta); //TODO remove
		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {HIDDEN, VARIABLE, FADE});
	}
}*/