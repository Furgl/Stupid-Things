package furgl.stupidThings.common.item;

import java.util.List;

import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCactusSword extends ItemSword implements ICustomTooltip {

	private static final ToolMaterial MATERIAL = EnumHelper.addToolMaterial("Cactus", 2, 150, 4, 3.5f, 10);

	public ItemCactusSword() {
		super(MATERIAL);
		if (MATERIAL.getRepairItemStack() == null)
			MATERIAL.setRepairItem(new ItemStack(Blocks.CACTUS));
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack stick = new ItemStack(Items.STICK);
		ItemStack cactus = new ItemStack(Blocks.CACTUS);
		return new ItemStack[] {null, cactus, null,
				null, cactus, null, 
				null, stick, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.DARK_GREEN+"Prick your enemies (and yourself) to death!"}, new String[0]);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && isSelected && entityIn.ticksExisted % 40 == 0 && entityIn instanceof EntityLivingBase) {
			if (((EntityLivingBase)entityIn).attackEntityFrom(DamageSource.CACTUS, 1))
				worldIn.playSound(null, entityIn.getPosition(), SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 0.5f, 0.8f+worldIn.rand.nextFloat());
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (attacker.world instanceof WorldServer) {
			((WorldServer)attacker.world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, 
					target.posX, target.posY + (double)target.height * 0.6D, target.posZ, 
					10, 0.5d, 0.5d, 0.5d, 0, new int[] {});
			attacker.world.playSound(null, target.getPosition(), SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 0.9f, 2f);
		}

		return super.hitEntity(stack, target, attacker);
	}
}