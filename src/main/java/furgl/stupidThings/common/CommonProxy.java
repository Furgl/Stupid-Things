package furgl.stupidThings.common;

import furgl.stupidThings.common.config.Config;
import furgl.stupidThings.common.entity.ModEntities;
import furgl.stupidThings.common.event.BlindnessClearTargetEvent;
import furgl.stupidThings.common.tileentity.ModTileEntities;
import furgl.stupidThings.packet.PacketWorldsSmallestViolinSound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		StupidThings.configFile = event.getSuggestedConfigurationFile();
		StupidThings.logger = event.getModLog();
		registerPackets();
		Config.preInit(StupidThings.configFile);
		ModEntities.preInit();
		ModTileEntities.preInit();
		registerEventListeners();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(StupidThings.instance, StupidThings.guiHandler);
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	private void registerPackets() { // Side is where the packet goes TO
		int id = 0;
		StupidThings.network.registerMessage(PacketWorldsSmallestViolinSound.Handler.class, PacketWorldsSmallestViolinSound.class, id++, Side.CLIENT);
	}

	protected void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(new Config());
		MinecraftForge.EVENT_BUS.register(new BlindnessClearTargetEvent());
	}

	public Object getArmorModel(Item item, EntityLivingBase entity) {
		return null;
	}

	public void spawnParticlesSmokeCloud(World worldIn, int color, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {}

	/**Add item (and sub-items in clientproxy) to creative tab*/
	public void addToTab(Item item, CreativeTabs tab, NonNullList<ItemStack> orderedStacks) {
		item.setCreativeTab(StupidThings.tab);
	}

	public void openCatalogGui() {}

	public void playWorldsSmallestViolinSound(EntityPlayer player) {}

	public EntityPlayer getPlayer() {
		return null;
	}
}