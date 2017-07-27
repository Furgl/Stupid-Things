package furgl.stupidThings.common.item;

import java.util.List;

import javax.annotation.Nullable;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.packet.PacketWorldsSmallestViolinSound;
import furgl.stupidThings.util.ICustomTooltip;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWorldsSmallestViolin extends Item implements ICustomTooltip {

	public ItemWorldsSmallestViolin() {
		super();
		this.setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("playing"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				ItemStack itemstack = entityIn == null ? null : entityIn.getActiveItemStack();
				if (entityIn == null || itemstack == null || itemstack.getItem() != ModItems.WORLDS_SMALLEST_VIOLIN || itemstack != stack) 
					return 0.0F;
				else 
					return (MathHelper.sin(entityIn.getItemInUseCount()/7.5f)+1f)/2f+0.1f;
			}
		});
	}

	@Override
	public ItemStack[] getTooltipRecipe(ItemStack stack) {
		ItemStack stick = new ItemStack(Items.STICK);
		ItemStack plank = new ItemStack(Blocks.PLANKS);
		ItemStack string = new ItemStack(Items.STRING);
		return new ItemStack[] {
				null, plank, stick,
				plank, string, plank, 
				plank, plank, null};
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.addTooltipText(tooltip, 
				new String[] {TextFormatting.GOLD+"Let me play you a sad song on the world's smallest violin", "", "(Spongebob reference)"}, new String[0]);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 768;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.getActiveItemStack() == null || playerIn.getActiveItemStack().getItem() != this)
			if (worldIn.isRemote) {
				StupidThings.proxy.playWorldsSmallestViolinSound(playerIn);
				playerIn.setActiveHand(hand);
			}
			else {
				StupidThings.network.sendToAll(new PacketWorldsSmallestViolinSound(playerIn.getPersistentID()));
				playerIn.setActiveHand(hand);
			}

		return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(hand));
	}

}