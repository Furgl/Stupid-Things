package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTargetChestplate extends ItemArmor implements ICustomTooltip {

	public ItemTargetChestplate() {
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, null, null, 
				new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), 
				new ItemStack(Items.IRON_CHESTPLATE), 
				new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), 
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.RED+"Causes nearby mobs to target you"}, new String[0]);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":textures/models/armor/target_chestplate_layer_1.png";
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote && player.ticksExisted % 150 == 0) {
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(30d));
			for (Entity entity : entities) 
				if (entity instanceof EntityLiving) 
					((EntityLiving)entity).setAttackTarget(player);
		}
	}
}