package furgl.stupidThings.common.item;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	public static ArrayList<Item> allItems = new ArrayList<Item>();
	public static ArrayList<Item> objModelItems = new ArrayList<Item>();

	public static final Item ITEM_CATALOG = new ItemCatalog();
	public static final Item ANVIL_BACKPACK = new ItemAnvilBackpack();
	public static final Item BALLOON_DEFLATED = new ItemBalloonDeflated();
	public static final Item BALLOON = new ItemBalloon();
	public static final Item BALLOON_WATER = new ItemBalloonLiquid.ItemBalloonWater();
	public static final Item BALLOON_LAVA = new ItemBalloonLiquid.ItemBalloonLava();
	public static final Item PAPER_BAG_HAT = new ItemPaperBagHat();
	public static final Item RAW_RUBBER = new ItemRubberRaw();
	public static final Item RUBBER = new ItemRubber();
	public static final Item SMOKE_BOMB = new ItemSmokeBomb();
	public static final Item TARGET_CHESTPLATE = new ItemTargetChestplate();
	public static final Item RUBBER_CHICKEN = new ItemRubberChicken();
	public static final Item PROPELLER_HAT = new ItemPropellerHat();
	public static final Item POCKET_SAND = new ItemPocketSand();
	public static final Item UPSIDE_DOWN_GOGGLES = new ItemUpsideDownGoggles();
	public static final Item INVISIBLE_HELMET = new ItemInvisibleArmor(EntityEquipmentSlot.HEAD);
	public static final Item INVISIBLE_CHESTPLATE = new ItemInvisibleArmor(EntityEquipmentSlot.CHEST);
	public static final Item INVISIBLE_LEGGINGS = new ItemInvisibleArmor(EntityEquipmentSlot.LEGS);
	public static final Item INVISIBLE_BOOTS = new ItemInvisibleArmor(EntityEquipmentSlot.FEET);
	public static final Item IMPROVED_WOOD_HOE = new ItemImprovedHoe(ToolMaterial.WOOD);
	public static final Item IMPROVED_STONE_HOE = new ItemImprovedHoe(ToolMaterial.STONE);
	public static final Item IMPROVED_IRON_HOE = new ItemImprovedHoe(ToolMaterial.IRON);
	public static final Item IMPROVED_GOLD_HOE = new ItemImprovedHoe(ToolMaterial.GOLD);
	public static final Item IMPROVED_DIAMOND_HOE = new ItemImprovedHoe(ToolMaterial.DIAMOND);
	public static final Item FIRING_CAN = new ItemFiringCan();
	public static final Item CACTUS_SWORD = new ItemCactusSword();
	public static final Item WORLDS_SMALLEST_VIOLIN = new ItemWorldsSmallestViolin();
	public static final Item BLOCK_BOMB_LAUNCHER = new ItemBlockBombLauncher();

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent(priority=EventPriority.LOW)
		public static void registerItems(final RegistryEvent.Register<Item> event) {			
			register(event.getRegistry(), ITEM_CATALOG, "item_catalog", true, false, false);
			register(event.getRegistry(), ANVIL_BACKPACK, "anvil_backpack", true, true, false);
			register(event.getRegistry(), PAPER_BAG_HAT, "paper_bag_hat", true, true, false);
			register(event.getRegistry(), RUBBER, "rubber", true, true, false);
			register(event.getRegistry(), RAW_RUBBER, "rubber_raw", true, false, false);
			register(event.getRegistry(), TARGET_CHESTPLATE, "target_chestplate", true, true, false);
			register(event.getRegistry(), RUBBER_CHICKEN, "rubber_chicken", true, true, false);
			register(event.getRegistry(), PROPELLER_HAT, "propeller_hat", true, true, false);
			register(event.getRegistry(), POCKET_SAND, "pocket_sand", true, true, false);
			register(event.getRegistry(), UPSIDE_DOWN_GOGGLES, "upside_down_goggles", true, true, false);
			register(event.getRegistry(), INVISIBLE_HELMET, "invisible_helmet", true, true, false);
			register(event.getRegistry(), INVISIBLE_CHESTPLATE, "invisible_chestplate", true, true, false);
			register(event.getRegistry(), INVISIBLE_LEGGINGS, "invisible_leggings", true, true, false);
			register(event.getRegistry(), INVISIBLE_BOOTS, "invisible_boots", true, true, false);
			register(event.getRegistry(), IMPROVED_WOOD_HOE, "improved_wood_hoe", true, true, false);
			register(event.getRegistry(), IMPROVED_STONE_HOE, "improved_stone_hoe", true, true, false);
			register(event.getRegistry(), IMPROVED_IRON_HOE, "improved_iron_hoe", true, true, false);
			register(event.getRegistry(), IMPROVED_GOLD_HOE, "improved_gold_hoe", true, true, false);
			register(event.getRegistry(), IMPROVED_DIAMOND_HOE, "improved_diamond_hoe", true, true, false);
			register(event.getRegistry(), FIRING_CAN, "firing_can", true, true, false);
			register(event.getRegistry(), CACTUS_SWORD, "cactus_sword", true, true, false);
			register(event.getRegistry(), WORLDS_SMALLEST_VIOLIN, "worlds_smallest_violin", true, true, false);
			register(event.getRegistry(), BLOCK_BOMB_LAUNCHER, "block_bomb_launcher", true, true, false);

			register(event.getRegistry(), SMOKE_BOMB, "smoke_bomb", true, true, false);
			register(event.getRegistry(), BALLOON_DEFLATED, "balloon_deflated", true, true, false);
			register(event.getRegistry(), BALLOON, "balloon", true, true, false);
			register(event.getRegistry(), BALLOON_WATER, "balloon_water", true, true, false);
			register(event.getRegistry(), BALLOON_LAVA, "balloon_lava", true, true, false);
		}

		private static void register(IForgeRegistry<Item> registry, Item item, String itemName, boolean addToTab, boolean checkIfDisabled, boolean objModel) {
			if (checkIfDisabled && !Config.isNameEnabled(itemName))
				return;

			allItems.add(item);
			if (objModel)
				objModelItems.add(item);
			item.setRegistryName(StupidThings.MODID, itemName);
			item.setTranslationKey(item.getRegistryName().getPath()); 
			if (addToTab) {
				item.setCreativeTab(StupidThings.tab);
				if (item instanceof ItemCatalog)
					StupidThings.tab.orderedStacks.add(0, new ItemStack(item));
				else
					item.getSubItems(StupidThings.tab, StupidThings.tab.orderedStacks);
			}
			registry.register(item);
		}

	}

	public static void registerObjRender(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(StupidThings.MODID+":" + item.getRegistryName().getPath(), "inventory"));
	}

	public static void registerRender(Item item, int meta) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta,
				new ModelResourceLocation(StupidThings.MODID+":" + item.getRegistryName().getPath(), "inventory"));
	}
}