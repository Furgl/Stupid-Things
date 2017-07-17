package furgl.stupidThings.client.gui.blockBombLauncher;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.item.ModItems;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBlockBombLauncher extends GuiContainer {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(StupidThings.MODID, "textures/gui/block_bomb_launcher_background.png");
	private InventoryPlayer playerInventory;

	public GuiBlockBombLauncher(ContainerBlockBombLauncher container, InventoryPlayer playerInv) {
		super(container);
        this.playerInventory = playerInv;
	}

	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = new ItemStack(ModItems.BLOCK_BOMB_LAUNCHER).getDisplayName();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
	}
	
}