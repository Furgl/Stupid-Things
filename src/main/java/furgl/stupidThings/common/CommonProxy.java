package furgl.stupidThings.common;

import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.config.Config;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.tileentity.ModTileEntities;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event) {
		registerPackets();
		StupidThings.configFile = event.getSuggestedConfigurationFile();
		StupidThings.logger = event.getModLog();
		ModBlocks.preInit();
		ModTileEntities.preInit();
	}

	public void init(FMLInitializationEvent event) {
		registerEventListeners();
	}

	public void postInit(FMLPostInitializationEvent event) {
		Config.postInit(StupidThings.configFile);
		ModItems.postInit();
		registerRecipes();
	}

	private void registerPackets() {}

	private void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(new Config());
	}

	private void registerRecipes() {}
}
