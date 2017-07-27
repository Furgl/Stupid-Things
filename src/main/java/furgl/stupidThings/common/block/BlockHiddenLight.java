package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHiddenLight extends BlockDirectional implements ICustomTooltip {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	protected BlockHiddenLight() {
		super(Material.CIRCUITS);
		this.lightValue = 15;
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.YELLOW+"The easiest way to hide lighting", "", "Can be broken while shifting", "Other placed Hidden Lights can be seen while holding this"}, new String[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		EntityPlayer player = StupidThings.proxy.getPlayer();
		if (player != null && ((player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(this))) || 
				(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == Item.getItemFromBlock(this)))
			worldIn.spawnParticle(EnumParticleTypes.END_ROD, pos.getX()+rand.nextDouble(), pos.getY()+rand.nextDouble(), pos.getZ()+rand.nextDouble(), 0, 0.00d, 0, new int[0]);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
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
		return (StupidThings.proxy.getPlayer() != null && StupidThings.proxy.getPlayer().isSneaking()) ? Block.FULL_BLOCK_AABB : AABB;
	}
}