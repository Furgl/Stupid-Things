package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.sound.ModSoundEvents;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemRubberChicken extends Item implements ICustomTooltip {

	public ItemRubberChicken() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		if (!OreDictionary.getOres("itemRubber").isEmpty()) {
			ItemStack rubber = OreDictionary.getOres("itemRubber").get(0);
			return new ItemStack[] {null, rubber, null,
					rubber, new ItemStack(Items.CHICKEN), rubber,
					null, rubber, null};
		}
		else 
			return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GOLD+"Squeak"}, new String[0]);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		player.world.playSound(player, entity.getPosition(), ModSoundEvents.RUBBER_CHICKEN, SoundCategory.PLAYERS, 
				player.world.rand.nextFloat()+0.5f, player.world.rand.nextFloat()*0.5f+0.75f);
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.world.playSound(player, player.getPosition(), ModSoundEvents.RUBBER_CHICKEN, SoundCategory.PLAYERS, 
				player.world.rand.nextFloat()+0.5f, player.world.rand.nextFloat()*0.5f+0.75f);
		return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}