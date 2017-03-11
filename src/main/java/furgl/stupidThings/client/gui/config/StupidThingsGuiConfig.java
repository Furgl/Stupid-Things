package furgl.stupidThings.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class StupidThingsGuiConfig extends GuiConfig {
	
	public StupidThingsGuiConfig(GuiScreen parent) {
		super(parent, getConfigElements(), StupidThings.MODID, false, false, StupidThings.MODNAME+" Configuration");
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.addAll(new ConfigElement(Config.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
		return list;
	}
}