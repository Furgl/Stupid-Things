package furgl.stupidThings.client.gui;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Maps;

import furgl.stupidThings.client.ClientProxy;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.item.ItemImprovedHoe;
import furgl.stupidThings.common.item.ItemInvisibleArmor;
import furgl.stupidThings.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({"all"})
@SideOnly(Side.CLIENT)
public class GuiDisplay extends GuiScreen
{
	/**Should display be opened on chat event?*/
	public static boolean display_gui = false;
	public static final int GUI_MODE = 1;

	private final ResourceLocation backgroundTooltipColor = new ResourceLocation(StupidThings.MODID+":textures/gui/tooltip_color.png");
	private LinkedHashMap<ItemStack, List<String>> stacks = Maps.newLinkedHashMap();

	public GuiDisplay() {
		for (Item item : ModItems.allItems)
			if ((!(item instanceof ItemImprovedHoe) || item == ModItems.IMPROVED_WOOD_HOE) &&
					(!(item instanceof ItemInvisibleArmor) || item == ModItems.INVISIBLE_HELMET))
				stacks.put(new ItemStack(item), new ItemStack(item).getTooltip(Minecraft.getMinecraft().player, TooltipFlags.NORMAL));				
		for (Block block : ModBlocks.allBlocks)
			stacks.put(new ItemStack(block), new ItemStack(block).getTooltip(Minecraft.getMinecraft().player, TooltipFlags.NORMAL));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//background
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(backgroundTooltipColor);
		GlStateManager.pushMatrix();
		float scale = 1f;
		this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height);

		int perRow = 3;
		int count = 0;
		int column = 0;
		int columnHeight = 0;
		int page = 1;
		for (ItemStack stack : stacks.keySet()) {
			// length of longest string
			int length = 0;
			for (String string : stacks.get(stack))
				if (this.fontRenderer.getStringWidth(string) > length)
					length = this.fontRenderer.getStringWidth(string);

			// calculate x and y
			RenderHelper.enableGUIStandardItemLighting();
			int x = -10 - length/2;
			int y = 42 + columnHeight;
			if (column == page*3)
				x += width/6;
			else if (column == page*3+1)
				x += width/2;
			else if (column == page*3+2)
				x += width*5/6;
			else
				x += 10000;

			// render items
			if (ClientProxy.COLORED_ITEMS.contains(stack.getItem())) {
				stacks.get(stack).set(0, stacks.get(stack).get(0).replace("White ", ""));
				if (GUI_MODE == 0)
					for (int meta=0; meta<16; ++meta)
						this.itemRender.renderItemIntoGUI(new ItemStack(stack.getItem(), 1, meta), 
								x+length/2+(meta-8)*12+10, y-35);
			}
			else if (stack.getItem() instanceof ItemImprovedHoe) {
				stacks.get(stack).set(0, "Improved Hoes");
				stacks.get(stack).set(1, TextFormatting.GREEN+"Range: 1x1 to 7x7");
				Item[] items = new Item[] {ModItems.IMPROVED_WOOD_HOE, ModItems.IMPROVED_STONE_HOE, ModItems.IMPROVED_IRON_HOE, ModItems.IMPROVED_GOLD_HOE, ModItems.IMPROVED_DIAMOND_HOE};
				if (GUI_MODE == 0)
					for (int i=0; i<items.length; ++i)
						this.itemRender.renderItemIntoGUI(new ItemStack(items[i]), 
								(int) (x+length/2+(i-(items.length-1)/2d)*18), y-35);
			}
			else if (stack.getItem() instanceof ItemInvisibleArmor) {
				stacks.get(stack).set(0, "Invisible Armor");
				Item[] items = new Item[] {ModItems.INVISIBLE_HELMET, ModItems.INVISIBLE_CHESTPLATE, ModItems.INVISIBLE_LEGGINGS, ModItems.INVISIBLE_BOOTS};
				if (GUI_MODE == 0)
					for (int i=0; i<4; ++i)
						this.itemRender.renderItemIntoGUI(new ItemStack(items[i]), 
								(int) (x+length/2+(i-(items.length-1)/2d)*18), y-35);
			}
			else
				if (GUI_MODE == 0)
					this.itemRender.renderItemIntoGUI(stack, x+length/2, y-35);

			if (GUI_MODE == 1) {
				// render tooltips
				net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
				this.drawHoveringText(stacks.get(stack), x, y);
			}

			columnHeight += stacks.get(stack).size()*11+30;

			if (columnHeight > (column >= 3 ? 430 : 391)) {
				column++;
				columnHeight = 0;
			}
			count++;
		}
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class OpenGuiEvent {

		@SubscribeEvent
		public static void openGui(ClientChatReceivedEvent event) {
			if (GuiDisplay.display_gui) 
				Minecraft.getMinecraft().displayGuiScreen(new GuiDisplay());
		}

	}

}
