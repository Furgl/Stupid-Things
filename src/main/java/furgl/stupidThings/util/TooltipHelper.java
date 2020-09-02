package furgl.stupidThings.util;

import java.util.List;

import furgl.stupidThings.client.gui.GuiDisplay;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.fluid.ModFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TooltipHelper {

	public static final TooltipHelper INSTANCE = new TooltipHelper();

	private static final ResourceLocation TOOLTIP_RECIPE_BACKGROUND = 
			new ResourceLocation(StupidThings.MODID+":textures/gui/tooltip_recipe_background.png");

	private TooltipHelper() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void addTooltip(ItemTooltipEvent event) {
		if (event.getItemStack() != null && 
				event.getItemStack().getItem() instanceof UniversalBucket && 
				((UniversalBucket)event.getItemStack().getItem()).getFluid(event.getItemStack()) != null &&
				((UniversalBucket)event.getItemStack().getItem()).getFluid(event.getItemStack()).getFluid() == ModFluids.ACID) 
			TooltipHelper.addTooltipText(event.getToolTip(), 
					new String[] {TextFormatting.DARK_GREEN+""+TextFormatting.BOLD+"Warning: Extremely corrosive",
							TextFormatting.GREEN+"Rapidly dissolves soft blocks on contact"}, new String[0]);
	}
	
	/**Adds shiftText/ctrlText to tooltip appropriately;
	 * displays prompt to hold keys for more info/recipe if shiftText/ctrlText are not null*/
	@SideOnly(Side.CLIENT)
	public static void addTooltipText(List<String> tooltip, String[] shiftText, String[] ctrlText) {
		if (GuiScreen.isShiftKeyDown() || GuiDisplay.display_gui)
			for (String string : shiftText == null ? new String[0] : shiftText)
				tooltip.add(string);
		else if (shiftText != null)
			tooltip.add(TextFormatting.AQUA+""+TextFormatting.ITALIC+"SHIFT "+TextFormatting.DARK_GRAY+""+TextFormatting.ITALIC+"for info");
		if (GuiScreen.isCtrlKeyDown() || GuiDisplay.display_gui) {
			if (GuiScreen.isShiftKeyDown() && ctrlText != null && ctrlText.length > 0)
				tooltip.add("");
			for (String string : ctrlText == null ? new String[0] : ctrlText) 
				tooltip.add(string);
		}
		else if (ctrlText != null)
			tooltip.add(TextFormatting.AQUA+""+TextFormatting.ITALIC+"CTRL "+TextFormatting.DARK_GRAY+""+TextFormatting.ITALIC+"for recipe");
	}

	@SubscribeEvent
	public void addTooltipRecipe(RenderTooltipEvent.PostText event) {
		if (event.getStack() != null) {
			ItemStack[] recipe = null;

			if (event.getStack().getItem() instanceof ICustomTooltip) 
				recipe = ((ICustomTooltip) event.getStack().getItem()).getTooltipRecipe(event.getStack());
			else if (event.getStack().getItem() instanceof ItemBlock && ((ItemBlock) event.getStack().getItem()).getBlock() instanceof ICustomTooltip)
				recipe = ((ICustomTooltip) ((ItemBlock) event.getStack().getItem()).getBlock()).getTooltipRecipe(event.getStack());
			else if (event.getStack().getItem() instanceof UniversalBucket && ModFluids.ACID != null && 
					((UniversalBucket)event.getStack().getItem()).getFluid(event.getStack()) != null &&
					((UniversalBucket)event.getStack().getItem()).getFluid(event.getStack()).getFluid() == ModFluids.ACID) 
				recipe = ((ICustomTooltip) ModFluids.acidBlock).getTooltipRecipe(event.getStack());

			if (recipe != null && recipe.length >= 9 && GuiScreen.isCtrlKeyDown()) {				
				GlStateManager.pushMatrix();
				GlStateManager.color(1, 1, 1);
				GlStateManager.translate(event.getX()+event.getWidth()/2-33, event.getY()+event.getHeight(), 0);
				Minecraft.getMinecraft().getTextureManager().bindTexture(TOOLTIP_RECIPE_BACKGROUND);

				GlStateManager.enableBlend();
				GuiUtils.drawTexturedModalRect(0, 1, 0, 0, 66, 66, 1);
		        GlStateManager.disableBlend();

				RenderHelper.enableGUIStandardItemLighting();
				for (int i=0; i<9; i++)
					if (recipe[i] != null)
						Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(recipe[i], 
								7+18*(i%3), 8+18*(i/3));

				GlStateManager.popMatrix();
			}
		}
	}
	
}