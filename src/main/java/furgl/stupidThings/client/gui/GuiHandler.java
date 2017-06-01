package furgl.stupidThings.client.gui;

import furgl.stupidThings.client.gui.blockBombLauncher.ContainerBlockBombLauncher;
import furgl.stupidThings.client.gui.blockBombLauncher.GuiBlockBombLauncher;
import furgl.stupidThings.client.gui.blockBombLauncher.InventoryBlockBombLauncher;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.item.ItemBlockBombLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
		if (guiId == StupidThings.GUI_BLOCK_BOMB_LAUNCHER_ID)
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemBlockBombLauncher)
				return new ContainerBlockBombLauncher(player.inventory, new InventoryBlockBombLauncher(player.getHeldItemMainhand()), player);
			else if (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof ItemBlockBombLauncher)
				return new ContainerBlockBombLauncher(player.inventory, new InventoryBlockBombLauncher(player.getHeldItemOffhand()), player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
		if (guiId == StupidThings.GUI_BLOCK_BOMB_LAUNCHER_ID)
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemBlockBombLauncher)
				return new GuiBlockBombLauncher(new ContainerBlockBombLauncher(player.inventory, new InventoryBlockBombLauncher(player.getHeldItemMainhand()), player), player.inventory);
			else if (player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() instanceof ItemBlockBombLauncher)
				return new GuiBlockBombLauncher(new ContainerBlockBombLauncher(player.inventory, new InventoryBlockBombLauncher(player.getHeldItemOffhand()), player), player.inventory);
		return null;
	}
	
}