package furgl.stupidThings.common.item;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class ModItems {
	public static ArrayList<Item> allItems;

	public static Item anvilBackpack;
	public static Item balloonDeflated;
	public static Item balloon;
	public static Item balloonWater;
	public static Item balloonLava;
	public static Item paperBagHat;
	public static Item rawRubber;
	public static Item rubber;
	public static Item smokeBomb;
	public static Item targetChestplate;
	public static Item rubberChicken;
	public static Item ball;
	public static Item propellerHat;

	public static void preInit() {		
		allItems = new ArrayList<Item>();

		anvilBackpack = registerItem(new ItemAnvilBackpack(), "anvil_backpack", true, true);
		paperBagHat = registerItem(new ItemPaperBagHat(), "paper_bag_hat", true, true);
		rubber = registerItem(new ItemRubber(), "rubber", true, true);
		if (rubber != null)
			rawRubber = registerItem(new ItemRubberRaw(), "rubber_raw", true, false);
		targetChestplate = registerItem(new ItemTargetChestplate(), "target_chestplate", true, true);
		rubberChicken = registerItem(new ItemRubberChicken(), "rubber_chicken", true, true);
		propellerHat = registerItem(new ItemPropellerHat(), "propeller_hat", true, true);
		
		smokeBomb = registerItem(new ItemSmokeBomb(), "smoke_bomb", true, true);
		balloon = registerItem(new ItemBalloon(), "balloon", true, true);
		balloonDeflated = registerItem(new ItemBalloonDeflated(), "balloon_deflated", true, true);
		balloonWater = registerItem(new ItemBalloonLiquid.ItemBalloonWater(), "balloon_water", true, true);
		balloonLava = registerItem(new ItemBalloonLiquid.ItemBalloonLava(), "balloon_lava", true, true);
	}

	public static Item registerItem(Item item, String unlocalizedName, boolean addToTab, boolean checkIfDisabled) {
		item.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(I18n.translateToLocal("item."+unlocalizedName+".name").replace("White ", "")))
			return null;
		item.setRegistryName(StupidThings.MODID, unlocalizedName);
		if (addToTab) 
			StupidThings.proxy.addToTab(item, StupidThings.tab, StupidThings.tab.orderedStacks);
		GameRegistry.register(item);
		allItems.add(item);
		return item;
	}

	public static void registerRender(Item item, int meta) {
		if (item != null)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, 
					new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}