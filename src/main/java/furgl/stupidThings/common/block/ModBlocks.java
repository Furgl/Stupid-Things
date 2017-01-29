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

	public static ArrayList<Block> allBlocks;
	
	public static Block reverseTnt;
	public static Block explosiveRail;

	public static void preInit() {
		allBlocks = new ArrayList<Block>();

		reverseTnt = registerBlock(new BlockReverseTnt(), true, true);
		explosiveRail = registerBlock(new BlockRailExplosive(), true, true);
	}

	public static void registerRenders() {
		for (Block block : allBlocks)
			registerRender(block);
	}

	private static Block registerBlock(Block block, boolean isItemBlock, boolean addToTab) {
		String unlocalizedName = getUnlocalizedName(block);
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
	
	/**Derives unlocalized name from class (ex: BlockReverseTNT -> reverse_tnt)*/
	private static String getUnlocalizedName(Block block) {
		String unlocalizedName = "";
		String tmp = block.getClass().getSimpleName().replace("Block", ""); 
		tmp = tmp.substring(0, 1).toLowerCase()+tmp.substring(1);
		for (int i=0; i<tmp.length(); i++) 
			if (Character.isUpperCase(tmp.charAt(i)))
				unlocalizedName += "_"+Character.toLowerCase(tmp.charAt(i));
			else
				unlocalizedName += tmp.charAt(i);
		return unlocalizedName;
	}

	private static void registerRender(Block block) {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, 
				new ModelResourceLocation(StupidThings.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}