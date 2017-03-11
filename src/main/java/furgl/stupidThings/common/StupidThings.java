package furgl.stupidThings.common;

import java.io.File;

import org.apache.logging.log4j.Logger;

import furgl.stupidThings.creativetab.StupidThingsCreativeTab;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = StupidThings.MODID, version = StupidThings.VERSION, name = StupidThings.MODNAME, guiFactory = "furgl.stupidThings.client.gui.config.StupidThingsGuiFactory", updateJSON = "https://raw.githubusercontent.com/Furgl/Stupid-Things/1.10.2/update.json")
public class StupidThings {
	public static final String MODNAME = "Stupid Things"; 
	public static final String MODID = "stupidthings";
	public static final String VERSION = "1.0";
	public static StupidThingsCreativeTab tab = new StupidThingsCreativeTab("tabStupidThings");
	@SidedProxy(clientSide = "furgl.stupidThings.client.ClientProxy", serverSide = "furgl.stupidThings.common.CommonProxy")
	public static CommonProxy proxy;
	@Mod.Instance(MODID)
	public static StupidThings instance;
	public static Logger logger;
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	protected static File configFile;
	@SideOnly(Side.CLIENT)
	public static TooltipHelper util;

	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}