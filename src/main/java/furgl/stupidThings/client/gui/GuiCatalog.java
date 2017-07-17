package furgl.stupidThings.client.gui;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiCatalog extends GuiScreen {

	private final int textureHeight = 180/2;
	private final int textureWidth = 292/2;
	private final ResourceLocation catalogTexture = new ResourceLocation(StupidThings.MODID, "textures/gui/item_catalog.png");

	public GuiCatalog() {
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false; // false so recipes can change in tooltips
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(catalogTexture);
		float w = (this.width - this.textureWidth) / 2;
		float h = (this.height - this.textureHeight) / 2;

		GlStateManager.pushMatrix();

		// background
		GlStateManager.translate(w - 100, h, 0);
		float scale = 2.5f;
		GlStateManager.scale(scale, scale, scale);
		this.drawTexturedModalRect(-3, -28, 0, 0, textureWidth, textureHeight);

		// title
		GlStateManager.scale(1/scale, 1/scale, 1/scale);
		GlStateManager.translate(-w + 100, -h, 0);
		String title = TextFormatting.BOLD+""+TextFormatting.UNDERLINE+"Current Items in "+StupidThings.MODNAME;
		this.mc.fontRenderer.drawString(title, width/2-this.fontRenderer.getStringWidth(title)/2, height/2-95, 16763904, true);

		// items
		GlStateManager.translate(w, h, 0);
		int itemsPerRow = 16;
		for (int i=0; i<StupidThings.tab.orderedStacks.size(); ++i) {	
			int row = i / itemsPerRow;
			int spaceBetween = 335/(Math.min(StupidThings.tab.orderedStacks.size(), itemsPerRow));
			int xPos = -89 + i*spaceBetween-row*itemsPerRow*spaceBetween;
			int yPos = -28 + row*(spaceBetween+3);
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemAndEffectIntoGUI(StupidThings.tab.orderedStacks.get(i), xPos, yPos);
		}
		// tooltips
		for (int i=0; i<StupidThings.tab.orderedStacks.size(); ++i) {
			int row = i / itemsPerRow;
			int spaceBetween = 335/(Math.min(StupidThings.tab.orderedStacks.size(), itemsPerRow));
			int xPos = -89 + i*spaceBetween-row*itemsPerRow*spaceBetween;
			int yPos = -28 + row*(spaceBetween+3);
			int mX = (int) ((mouseX-w));
			int mY = (int) ((mouseY-h));
			if (mX >= xPos && mY >= yPos && mX < xPos + 16 && mY < yPos + 16) {
				List<String> tooltip = StupidThings.tab.orderedStacks.get(i).getTooltip(mc.player, TooltipFlags.NORMAL);
				GuiUtils.drawHoveringText(StupidThings.tab.orderedStacks.get(i), tooltip, mX, mY, width/2, height, -1, mc.fontRenderer);
				RenderHelper.disableStandardItemLighting();
			}
		}

		GlStateManager.popMatrix();
	}

}
