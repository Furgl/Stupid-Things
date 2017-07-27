package furgl.stupidThings.common.block;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;


@SuppressWarnings("deprecation")
public class ModBlocks {

	public static ArrayList<Block> allBlocks = new ArrayList<Block>();
	
	public static Block reverseTnt;
	public static Block explosiveRail;
	public static Block cooler;
	public static Block heater;
	public static Block gravityAccelerator;
	public static Block petRock;
	public static Block mineTurtle;
	public static Block hiddenLight;
	//public static Block customizableLight;

	public static void preInit() {
		allBlocks = new ArrayList<Block>();
		
		reverseTnt = registerBlock(new BlockReverseTnt(), "reverse_tnt", true, true, true);
		explosiveRail = registerBlock(new BlockRailExplosive(), "rail_explosive", true, true, true);
		cooler = registerBlock(new BlockCooler(), "cooler", true, true, true);
		heater = registerBlock(new BlockHeater(), "heater", true, true, true);
		gravityAccelerator = registerBlock(new BlockGravityAccelerator(), "gravity_accelerator", true, true, true);
		petRock = registerBlock(new BlockPetRock(), "pet_rock", true, true, true);
		mineTurtle = registerBlock(new BlockMineTurtle(), "mine_turtle", true, true, true);
		hiddenLight = registerBlock(new BlockHiddenLight(), "hidden_light", true, true, true); // TODO remove
		//customizableLight = registerBlock(new BlockCustomizableLight(), "customizable_light", true, true, true);
	}

	public static void registerRenders() {
		for (Block block : allBlocks)
			registerRender(block);
	}

	public static Block registerBlock(Block block, String unlocalizedName, boolean isItemBlock, boolean addToTab, boolean checkIfDisabled) {
		block.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(I18n.translateToLocal("tile."+unlocalizedName+".name").replace("White ", "")))
			return null;
		GameRegistry.register(block.setRegistryName(unlocalizedName));
		if (isItemBlock) {
			Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
			GameRegistry.register(item);
			if (addToTab) 
				StupidThings.proxy.addToTab(item, StupidThings.tab, StupidThings.tab.orderedStacks);
		}
		allBlocks.add(block);
		return block;
	}

	public static void registerRender(Block block) {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, 
				new ModelResourceLocation(StupidThings.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}