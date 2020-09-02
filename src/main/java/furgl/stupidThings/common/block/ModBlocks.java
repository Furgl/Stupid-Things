package furgl.stupidThings.common.block;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;


public class ModBlocks {

	public static ArrayList<Block> allBlocks = new ArrayList<Block>();

	public static ArrayList<ItemBlock> itemBlocksToAddToTab = new ArrayList<ItemBlock>();

	public static final Block REVERSE_TNT = new BlockReverseTnt();
	public static final Block EXPLOSIVE_RAIL = new BlockRailExplosive();
	public static final Block COOLER = new BlockCooler();
	public static final Block HEATER = new BlockHeater();
	public static final Block GRAVITY_ACCELERATOR = new BlockGravityAccelerator();
	public static final Block PET_ROCK = new BlockPetRock();
	public static final Block MINE_TURTLE = new BlockMineTurtle();
	public static final Block HIDDEN_LIGHT = new BlockHiddenLight();

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			register(event.getRegistry(), REVERSE_TNT, "reverse_tnt", true, true);
			register(event.getRegistry(), EXPLOSIVE_RAIL, "rail_explosive", true, true);
			register(event.getRegistry(), COOLER, "cooler", true, true);
			register(event.getRegistry(), HEATER, "heater", true, true);
			register(event.getRegistry(), GRAVITY_ACCELERATOR, "gravity_accelerator", true, true);
			register(event.getRegistry(), PET_ROCK, "pet_rock", true, true);
			register(event.getRegistry(), MINE_TURTLE, "mine_turtle", true, true);
			register(event.getRegistry(), HIDDEN_LIGHT, "hidden_light", true, true);
		}

		private static void register(IForgeRegistry<Block> registry, Block block, String blockName, boolean addToTab, boolean checkIfDisabled) {
			if (checkIfDisabled && !Config.isNameEnabled(blockName)) 
				return;

			allBlocks.add(block);
			block.setRegistryName(StupidThings.MODID, blockName);
			block.setTranslationKey(block.getRegistryName().getPath()); 
			registry.register(block);
			if (addToTab) {
				itemBlocksToAddToTab.add(new ItemBlock(block));
				block.setCreativeTab(StupidThings.tab);
			}
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			for (Block block : allBlocks) {
				ItemBlock itemBlock = new ItemBlock(block);
				event.getRegistry().register(itemBlock.setRegistryName(block.getRegistryName()));
			}

			for (ItemBlock itemBlock : ModBlocks.itemBlocksToAddToTab)
				itemBlock.getSubItems(StupidThings.tab, StupidThings.tab.orderedStacks);
		}

	}

	public static void registerRenders() {
		for (Block block : allBlocks)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, 
					new ModelResourceLocation(StupidThings.MODID + ":" + block.getRegistryName().getPath(), "inventory"));	
	}

}