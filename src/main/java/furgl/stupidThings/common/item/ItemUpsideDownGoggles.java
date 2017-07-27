package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpsideDownGoggles extends ItemArmor implements ICustomTooltip {

	public ItemUpsideDownGoggles() {
		super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.HEAD);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void fovEvent(EntityViewRenderEvent.FOVModifier event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			ItemStack helm = ((EntityLivingBase) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if (helm != null && helm.getItem() == this)
				event.setFOV(1000);
		}
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack glass = new ItemStack(Blocks.GLASS);
		ItemStack redstone = new ItemStack(Items.REDSTONE);
		ItemStack iron = new ItemStack(Items.IRON_INGOT);
		return new ItemStack[] {iron, iron, iron,
				iron, null, iron, 
				glass, redstone, glass};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.RED+"See the world upside down!"}, new String[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
		return (ModelBiped) StupidThings.proxy.getArmorModel(this, entityLiving); 
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":textures/models/armor/upside_down_goggles.png";
	}
}