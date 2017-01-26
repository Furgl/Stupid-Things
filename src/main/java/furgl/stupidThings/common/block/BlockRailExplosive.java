package furgl.stupidThings.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRailExplosive extends BlockRailPowered {

	protected BlockRailExplosive() {
		super();
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else {
				tooltip.add(TextFormatting.DARK_RED+"Explodes when a minecart passes");
				tooltip.add(TextFormatting.DARK_RED+"over while this is powered");
			}
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