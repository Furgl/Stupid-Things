package furgl.stupidThings.common.block;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static void preInit() {
		
	}

	public static void registerRenders() {

	}

	private static Block registerBlock(final Block block, final String unlocalizedName) {
		block.setUnlocalizedName(unlocalizedName);
		GameRegistry.register(block.setRegistryName(unlocalizedName));
		return block;
	}

	private static void registerRender(Block block) {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, 
				new ModelResourceLocation(StupidThings.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}