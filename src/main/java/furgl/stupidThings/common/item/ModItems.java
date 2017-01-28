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
	
	public static Item anvilChestplate;
	public static Item balloonDeflated;
	public static Item balloon;
	public static Item balloonWater;
	public static Item balloonLava;

	public static void preInit() {
		allItems = new ArrayList<Item>();
		anvilChestplate = registerItem(new ItemAnvilChestplate(), true);
		balloonDeflated = registerItem(new ItemBalloonDeflated(), true);
		balloon = registerItem(new ItemBalloon(), true);
		balloonWater = registerItem(new ItemBalloonLiquid.ItemBalloonWater(), true);
		balloonLava = registerItem(new ItemBalloonLiquid.ItemBalloonLava(), true);
	}

	public static void registerRenders() {
		for (Item item : allItems)
			registerRender(item);
	}

	private static Item registerItem(Item item, boolean addToTab) {
		String unlocalizedName = item.getClass().getSimpleName().replace("Item", "");   
    	unlocalizedName = unlocalizedName.substring(0, 1).toLowerCase()+unlocalizedName.substring(1);
    	return registerItem(item, unlocalizedName, addToTab);
	}
	
	private static Item registerItem(Item item, String unlocalizedName, boolean addToTab) {
		item.setUnlocalizedName(unlocalizedName);
		item.setRegistryName(StupidThings.MODID, unlocalizedName);
		if (addToTab) {
			StupidThings.tab.orderedStacks.add(new ItemStack(item));
			item.setCreativeTab(StupidThings.tab);
		}
		GameRegistry.register(item);
		allItems.add(item);
		return item;
	}

	private static void registerRender(Item item) {		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, 
				new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}