package furgl.stupidThings.common.item;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
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

public class ItemAnvilBackpack extends ItemArmor implements ICustomTooltip {

	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("308e4fee-a300-48b6-9b56-05e53e35eb8f");

	public ItemAnvilBackpack() {
		super(ArmorMaterial.IRON, 0, EntityEquipmentSlot.CHEST);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		return new ItemStack[] {null, null, null, 
				new ItemStack(Items.STRING), new ItemStack(Item.getItemFromBlock(Blocks.ANVIL)), new ItemStack(Items.STRING), 
				null, null, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GRAY+"Weighs down the wearer and",
						TextFormatting.GRAY+"damages entities that are fallen on"}, new String[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
		return (ModelBiped) StupidThings.proxy.getArmorModel(this, entityLiving);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return StupidThings.MODID+":textures/models/armor/anvil_backpack.png";
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.CHEST)
			map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(MOVEMENT_SPEED_UUID, 
					SharedMonsterAttributes.MOVEMENT_SPEED.getName(), -0.02d, 0));
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
			List<Entity> list = Lists.newArrayList(world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(3)));

			for (Entity entity : list) 
				entity.attackEntityFrom(DamageSource.ANVIL, Math.min(player.fallDistance, 20));
		}
	}

	@SubscribeEvent
	public void onEvent(LivingFallEvent event) {
		//play anvil sound when landing
		if (event.getEntityLiving() != null && !event.getEntityLiving().world.isRemote &&
				event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null &&
				event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == this) 
			event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.BLOCK_ANVIL_LAND, 
					SoundCategory.PLAYERS, event.getDistance()/30f, event.getEntityLiving().world.rand.nextFloat());
	}
}