package furgl.stupidThings.common.item;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	public static ArrayList<Item> allItems;

	public static void postInit() {
		allItems = new ArrayList<Item>();
	}

	public static void registerRenders() {
		for (Item item : allItems)
			registerRender(item);
	}

	private static Item registerItem(Item item, String unlocalizedName, boolean addToTab, boolean isFromModdedBlock) {
		item.setUnlocalizedName(unlocalizedName);
		item.setRegistryName(StupidThings.MODID, unlocalizedName);
		if (addToTab) {
			StupidThings.tab.orderedStacks.add(new ItemStack(item));
			item.setCreativeTab(StupidThings.tab);
		}
		GameRegistry.register(item);
		return item;
	}

	private static void registerRender(Item item) {		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, 
				new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}