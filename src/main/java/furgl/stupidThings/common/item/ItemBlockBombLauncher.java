package furgl.stupidThings.common.item;

import java.util.ArrayList;
import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.entity.EntityBlockBomb;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBombLauncher extends Item implements ICustomTooltip {

	private static final int BLOCKS_PER_BOMB = 10;

	public ItemBlockBombLauncher() {
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack ironBlock = new ItemStack(Blocks.IRON_BLOCK);
		ItemStack diamond = new ItemStack(Items.DIAMOND);
		ItemStack lever = new ItemStack(Blocks.LEVER);
		return new ItemStack[] {null, null, ironBlock,
				null, ironBlock, diamond, 
				ironBlock, diamond, lever};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		String g = TextFormatting.GRAY+"";
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {g+"Launches block bombs that spawn clusters of blocks", "", 
						g+"Shift + right click to open inventory", 
						g+"Puts up to 10 blocks from the inventory in a bomb",
						g+"Right click to shoot"}, new String[0]);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (playerIn.isSneaking())
			playerIn.openGui(StupidThings.instance, StupidThings.GUI_BLOCK_BOMB_LAUNCHER_ID, worldIn, 0, 0, 0);
		else if (!worldIn.isRemote)
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("items")) {
				NBTTagList list = stack.getTagCompound().getTagList("items", 10);
				if (list != null && !list.isEmpty()) {
					ArrayList<ItemStack> items = new ArrayList<ItemStack>();
					int count = 0;
					for (int i=list.tagCount(); i>=0; --i) {
						ItemStack item = new ItemStack(list.getCompoundTagAt(i));
						if (count < BLOCKS_PER_BOMB && item != null && item.getItem() instanceof ItemBlock)
							// if stack has less than or equal to enough items, add items to bomb and delete from tag
							if (count + item.getCount() <= BLOCKS_PER_BOMB) {
								count += item.getCount();
								items.add(item);
								list.removeTag(i);
							}
							else { // if stack is more items than needed, add enough items to bomb and subtract from tag
								int difference = BLOCKS_PER_BOMB - count;
								count += difference;
								ItemStack itemCopy = item.copy();
								itemCopy.setCount(difference);
								items.add(itemCopy);
								item.setCount(item.getCount() - difference);
								list.set(i, item.writeToNBT(new NBTTagCompound()));
							}
					}
					stack.getTagCompound().setTag("items", list);

					if (count > 0) {
						EntityBlockBomb bomb = new EntityBlockBomb(worldIn, playerIn, items.toArray(new ItemStack[0]));
						bomb.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3F, 1.0F);
						worldIn.spawnEntity(bomb);
						worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.4f, 1f+worldIn.rand.nextFloat()/2f);
					}
					else {
						worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.PLAYERS, 0.4f, 0.9f+worldIn.rand.nextFloat()/2f);
						playerIn.getCooldownTracker().setCooldown(this, 5);
					}
				}
				else {
					worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.PLAYERS, 0.4f, 0.9f+worldIn.rand.nextFloat()/2f);
					playerIn.getCooldownTracker().setCooldown(this, 5);
				}

			}
			else {
				worldIn.playSound(null, playerIn.getPosition(), SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.PLAYERS, 0.4f, 0.9f+worldIn.rand.nextFloat()/2f);
				playerIn.getCooldownTracker().setCooldown(this, 5);
			}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
}