package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInvisibleArmor extends ItemArmor implements ICustomTooltip {

	public ItemInvisibleArmor(EntityEquipmentSlot slot) {
		super(ArmorMaterial.IRON, 0, slot);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack glass = new ItemStack(Blocks.GLASS);
		ItemStack armor = null;
		switch (this.getEquipmentSlot()) {
		case HEAD:
			armor = new ItemStack(Items.IRON_HELMET);
			break;
		case CHEST:
			armor = new ItemStack(Items.IRON_CHESTPLATE);
			break;
		case LEGS:
			armor = new ItemStack(Items.IRON_LEGGINGS);
			break;
		case FEET:
			armor = new ItemStack(Items.IRON_BOOTS);
			break;
		}
		return new ItemStack[] {
				glass, glass, glass, 
				glass, armor, glass, 
				glass, glass, glass};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GRAY+"Let your enemies think you're naked"}, new String[0]);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":textures/models/armor/invisible_armor.png";
	}
}