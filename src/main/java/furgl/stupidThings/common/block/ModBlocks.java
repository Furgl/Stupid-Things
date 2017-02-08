package furgl.stupidThings.common.block;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static ArrayList<Block> allBlocks = new ArrayList<Block>();
	
	public static Block reverseTnt;
	public static Block explosiveRail;

	public static void preInit() {
		allBlocks = new ArrayList<Block>();
		
		reverseTnt = registerBlock(new BlockReverseTnt(), "reverse_tnt", true, true);
		explosiveRail = registerBlock(new BlockRailExplosive(), "rail_explosive", true, true);
	}

	public static void registerRenders() {
		for (Block block : allBlocks)
			registerRender(block);
	}

	public static Block registerBlock(Block block, String unlocalizedName, boolean isItemBlock, boolean addToTab) {
		block.setUnlocalizedName(unlocalizedName);
		GameRegistry.register(block.setRegistryName(unlocalizedName));
		if (isItemBlock) {
			Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
			GameRegistry.register(item);
			if (addToTab) {
				block.getSubBlocks(item, StupidThings.tab, StupidThings.tab.orderedStacks);
				block.setCreativeTab(StupidThings.tab);
			}
		}
		allBlocks.add(block);
		return block;
	}

	public static void registerRender(Block block) {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, 
				new ModelResourceLocation(StupidThings.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}