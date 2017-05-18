package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPaperBagHat extends ItemArmor implements ICustomTooltip {

	public ItemPaperBagHat() {
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);
	}
	
	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack paper = new ItemStack(Items.PAPER);
		return new ItemStack[] {paper, paper, paper,
				paper, null, paper, 
				null, null, null};
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			TooltipHelper.addTooltipText(tooltip, 
					new String[] {TextFormatting.GOLD+"For when makeup isn't enough"}, new String[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
		return (ModelBiped) StupidThings.proxy.getArmorModel(this, entityLiving); 
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":/textures/models/armor/paper_bag_hat.png";
	}
}