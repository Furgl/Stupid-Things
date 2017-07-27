package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPropellerHat extends ItemArmor implements ICustomTooltip {

	public ItemPropellerHat() {
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack feather = new ItemStack(Items.FEATHER);
		return new ItemStack[] {feather, new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), feather,
				new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 
				feather, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), feather};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.YELLOW+"Faster ascension and slower descension when jumping"}, new String[0]);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (world.isRemote && player instanceof EntityPlayerSP &&
				((EntityPlayerSP) player).movementInput.jump &&
				player.motionY < 0.38f) {
			if (player.motionY > 0)
				player.motionY *= 1.25f;
			else
				player.motionY *= 0.85f;
			world.playSound(player, player.getPosition(), SoundEvents.BLOCK_GRASS_STEP, SoundCategory.PLAYERS, (float) Math.abs(player.motionY > 0 ? player.motionY : player.motionY / 5f), 1.5f);
		}
		player.fallDistance = 0;

	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
		return (ModelBiped) StupidThings.proxy.getArmorModel(this, entityLiving); 
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":textures/models/armor/propeller_hat.png";
	}
}