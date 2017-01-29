package furgl.stupidThings.common.item;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
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
		balloon = registerItem(new ItemBalloon(), true);
		balloonDeflated = registerItem(new ItemBalloonDeflated(), true);
		balloonWater = registerItem(new ItemBalloonLiquid.ItemBalloonWater(), true);
		balloonLava = registerItem(new ItemBalloonLiquid.ItemBalloonLava(), true);
	}

	public static void registerRenders() {
		for (Item item : allItems)
			registerRender(item, 0);
		
		for (int i=1; i<16; i++) {
			registerRender(balloon, i);
			registerRender(balloonDeflated, i);
			registerRender(balloonWater, i);
			registerRender(balloonLava, i);
		}

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				//System.out.println(EnumDyeColor.byMetadata(stack.getMetadata()).getMapColor().colorValue);
				return EnumDyeColor.byMetadata(stack.getMetadata()).getMapColor().colorValue;
			}
		}, balloon, balloonDeflated, balloonWater, balloonLava);
	}

	private static Item registerItem(Item item, boolean addToTab) {
		String unlocalizedName = getUnlocalizedName(item);
		item.setUnlocalizedName(unlocalizedName);
		item.setRegistryName(StupidThings.MODID, unlocalizedName);
		if (addToTab) {
			item.getSubItems(item, StupidThings.tab, StupidThings.tab.orderedStacks);
			item.setCreativeTab(StupidThings.tab);
		}
		GameRegistry.register(item);
		allItems.add(item);
		return item;
	}

	/**Derives unlocalized name from class (ex: ItemAnvilChestplate -> anvil_chestplate)*/
	private static String getUnlocalizedName(Item item) {
		String unlocalizedName = "";
		String tmp = item.getClass().getSimpleName().replace("Item", "");   
		tmp = tmp.substring(0, 1).toLowerCase()+tmp.substring(1);
		for (int i=0; i<tmp.length(); i++) 
			if (Character.isUpperCase(tmp.charAt(i)))
				unlocalizedName += "_"+Character.toLowerCase(tmp.charAt(i));
			else
				unlocalizedName += tmp.charAt(i);
		return unlocalizedName;
	}

	private static void registerRender(Item item, int meta) {		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, 
				new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}