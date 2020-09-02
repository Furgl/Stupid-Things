package furgl.stupidThings.common.item;

import java.lang.reflect.Method;
import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemImprovedHoe extends ItemHoe implements ICustomTooltip {

	public ItemStack hoe;
	private int radius;

	public ItemImprovedHoe(ToolMaterial material) {
		super(material);
		switch (this.toolMaterial) {
		case WOOD:
			hoe = new ItemStack(Items.WOODEN_HOE);
			radius = 0;
			break;
		case STONE:
			hoe = new ItemStack(Items.STONE_HOE);
			radius = 1;
			break;
		case IRON:
			hoe = new ItemStack(Items.IRON_HOE);
			radius = 2;
			break;
		case GOLD:
			hoe = new ItemStack(Items.GOLDEN_HOE);
			radius = 2;
			break;
		case DIAMOND:
			hoe = new ItemStack(Items.DIAMOND_HOE);
			radius = 3;
			break;
		}
		this.setMaxDamage(this.getMaxDamage(new ItemStack(this))*3);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, new ItemStack(Items.STRING), null,
				hoe, hoe, hoe, 
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		int range = radius * 2 + 1;
		String g = TextFormatting.GREEN+"";
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {g+"Range: "+range+"x"+range, "", g+"Actions: ", 
						g+"- Left click crops -> harvest within range",
						g+"- Right click crops -> harvest and replant within range",
						g+"- Right click ground -> till within range",
						g+"- Shift -> Minecraft default"}, new String[0]);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (!entityLiving.isSneaking() && entityLiving instanceof EntityPlayer) {
			for (int x=-radius; x<=radius; x++)
				for (int z=-radius; z<=radius; z++)
					if (((EntityPlayer) entityLiving).canPlayerEdit(pos.add(x, 0, z).offset(EnumFacing.UP), EnumFacing.UP, stack) &&
							worldIn.getBlockState(pos.add(x, 0, z)).getBlock() instanceof BlockCrops && 
							!((BlockCrops) worldIn.getBlockState(pos.add(x, 0, z)).getBlock()).canGrow(worldIn, pos.add(x, 0, z), worldIn.getBlockState(pos.add(x, 0, z)), false)) {
						BlockCrops crop = (BlockCrops) worldIn.getBlockState(pos.add(x, 0, z)).getBlock();
						crop.harvestBlock(worldIn, (EntityPlayer) entityLiving, pos.add(x, 0, z), worldIn.getBlockState(pos.add(x, 0, z)), (TileEntity)null, stack);
						worldIn.setBlockToAir(pos.add(x, 0, z));
						worldIn.scheduleBlockUpdate(pos.add(x, 0, z), worldIn.getBlockState(pos.add(x, 0, z)).getBlock(), 0, 1);
					}
			return true;
		}

		return false;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);

		boolean ret = false;
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockCrops) {
			for (int x=-radius; x<=radius; x++)
				for (int z=-radius; z<=radius; z++)
					if (playerIn.canPlayerEdit(pos.add(x, 0, z).offset(facing), facing, playerIn.getHeldItem(hand)))
						ret = rightClickCrop(playerIn, worldIn, pos.add(x, 0, z), playerIn.getHeldItem(hand)) || ret;
		}
		else {
			for (int x=-radius; x<=radius; x++)
				for (int z=-radius; z<=radius; z++)
					if (playerIn.canPlayerEdit(pos.add(x, 0, z).offset(facing), facing, playerIn.getHeldItem(hand)))
						ret = rightClickDirt(playerIn.getHeldItem(hand), playerIn, worldIn, pos.add(x, 0, z), facing) || ret;
		}

		return ret ? EnumActionResult.SUCCESS : EnumActionResult.PASS; // PASS or FAIL?
	}

	private boolean rightClickDirt(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumFacing facing) {
		int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos);
		if (hook != 0) return hook > 0 ? true : false;

		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
			if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
				this.setBlock(stack, playerIn, worldIn, pos, Blocks.FARMLAND.getDefaultState());
				return true;
			}

			if (block == Blocks.DIRT) {
				switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT)) {
				case DIRT:
					this.setBlock(stack, playerIn, worldIn, pos, Blocks.FARMLAND.getDefaultState());
					return true;
				case COARSE_DIRT:
					this.setBlock(stack, playerIn, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
					return true;
				}
			}
		}

		return false;
	}

	private boolean rightClickCrop(EntityPlayer playerIn, World worldIn, BlockPos pos, ItemStack stack) {
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockCrops && !((BlockCrops) worldIn.getBlockState(pos).getBlock()).canGrow(worldIn, pos, worldIn.getBlockState(pos), false)) {
			BlockCrops crop = (BlockCrops) worldIn.getBlockState(pos).getBlock();
			crop.harvestBlock(worldIn, playerIn, pos, worldIn.getBlockState(pos), null, stack);
			try {
				Method method = crop.getClass().getDeclaredMethod("getSeed");
				method.setAccessible(true);
				Item seed = (Item) method.invoke(crop);
				if (playerIn.capabilities.isCreativeMode || playerIn.inventory.clearMatchingItems(seed, -1, 1, null) == 1) {
					playerIn.inventoryContainer.detectAndSendChanges();
					worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), crop.getStateFromMeta(0), 0);
					worldIn.setBlockState(pos, crop.getStateFromMeta(0), 0);
				}
				else
					worldIn.setBlockToAir(pos);
			}
			catch(Exception e) {
				try {
					Method method = crop.getClass().getDeclaredMethod("func_149866_i");
					method.setAccessible(true);
					Item seed = (Item) method.invoke(crop);
					if (playerIn.capabilities.isCreativeMode || playerIn.inventory.clearMatchingItems(seed, -1, 1, null) == 1) {
						playerIn.inventoryContainer.detectAndSendChanges();
						worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), crop.getStateFromMeta(0), 0);
						worldIn.setBlockState(pos, crop.getStateFromMeta(0), 0);
					}
					else
						worldIn.setBlockToAir(pos);
				}
				catch(Exception ex) {
					StupidThings.logger.warn("Error during onPlayerInteractEvent(): ", ex);
				}
			}
			return true;
		}
		else
			return false;
	}
}