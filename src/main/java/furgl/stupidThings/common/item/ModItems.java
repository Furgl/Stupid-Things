package furgl.stupidThings.common.item;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class ModItems {
	public static ArrayList<Item> allItems;
	public static ArrayList<Item> objModelItems;

	public static Item itemCatalog;
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
	public static Item pocketSand;
	public static Item upsideDownGoggles;
	public static Item invisibleHelmet;
	public static Item invisibleChestplate;
	public static Item invisibleLeggings;
	public static Item invisibleBoots;
	public static Item improvedWoodHoe;
	public static Item improvedStoneHoe;
	public static Item improvedIronHoe;
	public static Item improvedGoldHoe;
	public static Item improvedDiamondHoe;
	public static Item firingCan;
	public static Item cactusSword;
	public static Item worldsSmallestViolin;
	public static Item blockBombLauncher;

	public static void preInit() {		
		allItems = new ArrayList<Item>();
		objModelItems = new ArrayList<Item>();

		itemCatalog = registerItem(new ItemCatalog(), "item_catalog", true, false, false);
		anvilBackpack = registerItem(new ItemAnvilBackpack(), "anvil_backpack", true, true, false);
		paperBagHat = registerItem(new ItemPaperBagHat(), "paper_bag_hat", true, true, false);
		rubber = registerItem(new ItemRubber(), "rubber", true, true, false);
		if (rubber != null)
			rawRubber = registerItem(new ItemRubberRaw(), "rubber_raw", true, false, false);
		targetChestplate = registerItem(new ItemTargetChestplate(), "target_chestplate", true, true, false);
		rubberChicken = registerItem(new ItemRubberChicken(), "rubber_chicken", true, true, false);
		propellerHat = registerItem(new ItemPropellerHat(), "propeller_hat", true, true, false);
		pocketSand = registerItem(new ItemPocketSand(), "pocket_sand", true, true, false);
		upsideDownGoggles = registerItem(new ItemUpsideDownGoggles(), "upside_down_goggles", true, true, false);
		invisibleHelmet = registerItem(new ItemInvisibleArmor(EntityEquipmentSlot.HEAD), "invisible_helmet", true, true, false);
		invisibleChestplate = registerItem(new ItemInvisibleArmor(EntityEquipmentSlot.CHEST), "invisible_chestplate", true, true, false);
		invisibleLeggings = registerItem(new ItemInvisibleArmor(EntityEquipmentSlot.LEGS), "invisible_leggings", true, true, false);
		invisibleBoots = registerItem(new ItemInvisibleArmor(EntityEquipmentSlot.FEET), "invisible_boots", true, true, false);
		improvedWoodHoe = registerItem(new ItemImprovedHoe(ToolMaterial.WOOD), "improved_wood_hoe", true, true, false);
		improvedStoneHoe = registerItem(new ItemImprovedHoe(ToolMaterial.STONE), "improved_stone_hoe", true, true, false);
		improvedIronHoe = registerItem(new ItemImprovedHoe(ToolMaterial.IRON), "improved_iron_hoe", true, true, false);
		improvedGoldHoe = registerItem(new ItemImprovedHoe(ToolMaterial.GOLD), "improved_gold_hoe", true, true, false);
		improvedDiamondHoe = registerItem(new ItemImprovedHoe(ToolMaterial.DIAMOND), "improved_diamond_hoe", true, true, false);
		firingCan = registerItem(new ItemFiringCan(), "firing_can", true, true, false);
		cactusSword = registerItem(new ItemCactusSword(), "cactus_sword", true, true, false);
		worldsSmallestViolin = registerItem(new ItemWorldsSmallestViolin(), "worlds_smallest_violin", true, true, false);
		blockBombLauncher = registerItem(new ItemBlockBombLauncher(), "block_bomb_launcher", true, true, false);

		smokeBomb = registerItem(new ItemSmokeBomb(), "smoke_bomb", true, true, false);
		balloonDeflated = registerItem(new ItemBalloonDeflated(), "balloon_deflated", true, true, false);
		balloon = registerItem(new ItemBalloon(), "balloon", true, true, false);
		balloonWater = registerItem(new ItemBalloonLiquid.ItemBalloonWater(), "balloon_water", true, true, false);
		balloonLava = registerItem(new ItemBalloonLiquid.ItemBalloonLava(), "balloon_lava", true, true, false);
	}

	public static Item registerItem(Item item, String unlocalizedName, boolean addToTab, boolean checkIfDisabled, boolean objModel) {
		item.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(I18n.translateToLocal("item."+unlocalizedName+".name").replace("White ", "")))
			return null;
		item.setRegistryName(StupidThings.MODID, unlocalizedName);
		if (addToTab) 
			StupidThings.proxy.addToTab(item, StupidThings.tab, StupidThings.tab.orderedStacks);
		GameRegistry.register(item);
		allItems.add(item);
		if (objModel)
			objModelItems.add(item);
		return item;
	}

	public static void registerObjRender(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta, 
				new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}

	public static void registerRender(Item item, int meta) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, 
				new ModelResourceLocation(StupidThings.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}