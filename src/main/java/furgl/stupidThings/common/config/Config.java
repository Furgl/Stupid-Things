package furgl.stupidThings.common.config;

import java.io.File;

import furgl.stupidThings.common.StupidThings;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
	public static Configuration config;

	public static void preInit(final File file) {
		config = new Configuration(file);
		config.load();
		config.save();
	}

	@SubscribeEvent(receiveCanceled=true)
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(StupidThings.MODID)) 
			config.save();
	}
	
	/**Check in config for this name to see if it's enabled - also populates config*/
	public static boolean isNameEnabled(String name) {
		if (name.contains("Invisible"))
			name = "Invisible Armor";
		else if (name.contains("Improved"))
			name = "Improved Hoes";
		Property prop = config.get(Configuration.CATEGORY_GENERAL, name, true, "Should this be enabled?");
		prop.setRequiresMcRestart(true);
		config.save();
		return prop.getBoolean();
	}
}