package furgl.stupidThings.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAnvilBackpack extends ItemArmor {

	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("308e4fee-a300-48b6-9b56-05e53e35eb8f");

	public ItemAnvilBackpack() {
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (player.worldObj.isRemote)
			if (!GuiScreen.isShiftKeyDown())
				tooltip.add(TextFormatting.DARK_GRAY+"Hold shift for more info");
			else { 
				tooltip.add(TextFormatting.GRAY+"Weighs down the wearer and");
				tooltip.add(TextFormatting.GRAY+"damages entities that are fallen on");
			}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
		return (ModelBiped) StupidThings.proxy.getArmorModel(this);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":/textures/models/armor/anvil_backpack.png";
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.CHEST)
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(MOVEMENT_SPEED_UUID, 
					SharedMonsterAttributes.MOVEMENT_SPEED.getAttributeUnlocalizedName(), -0.02d, 0));
		return map;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		//increase fall speed
		if (world.isRemote && !player.capabilities.isFlying && player.motionY > -3.5d)
			if (player.motionY > 0)
				player.motionY *= 0.8d;
			else
				player.motionY *= 1.3d;
		//hurt entities while falling
		if (!world.isRemote && player.fallDistance > 1) {
			List<Entity> list = Lists.newArrayList(world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox()));

			for (Entity entity : list) 
				entity.attackEntityFrom(DamageSource.anvil, Math.min(player.fallDistance, 20));
		}
	}

	@SubscribeEvent
	public void onEvent(LivingFallEvent event) {
		//play anvil sound when landing
		if (event.getEntityLiving() != null && !event.getEntityLiving().worldObj.isRemote &&
				event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null &&
				event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == this) 
			event.getEntityLiving().worldObj.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.BLOCK_ANVIL_LAND, 
					SoundCategory.PLAYERS, event.getDistance()/15f, event.getEntityLiving().worldObj.rand.nextFloat());
	}
}