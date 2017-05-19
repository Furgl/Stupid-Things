package furgl.stupidThings.common;

import java.util.ArrayList;
import java.util.List;

import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.config.Config;
import furgl.stupidThings.common.entity.ModEntities;
import furgl.stupidThings.common.fluid.ModFluids;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.sound.ModSoundEvents;
import furgl.stupidThings.common.tileentity.ModTileEntities;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		StupidThings.configFile = event.getSuggestedConfigurationFile();
		StupidThings.logger = event.getModLog();
		registerPackets();
		Config.preInit(StupidThings.configFile);
		ModBlocks.preInit();
		ModItems.preInit();
		ModFluids.preInit();
		ModEntities.preInit();
		ModSoundEvents.preInit();
		ModTileEntities.preInit();
	}

	public void init(FMLInitializationEvent event) {
		registerEventListeners();
		registerRecipes();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	private void registerPackets() {}

	protected void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(new Config());
	}

	private void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.itemCatalog), new ItemStack(Items.BOOK), new ItemStack(Blocks.DIRT));
		if (ModBlocks.reverseTnt != null)
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.reverseTnt), new ItemStack(Blocks.TNT), new ItemStack(Items.ENDER_PEARL));
		if (ModItems.anvilBackpack != null)
			GameRegistry.addRecipe(new ItemStack(ModItems.anvilBackpack), "ABA", 'A', new ItemStack(Items.STRING), 'B', new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE));
		if (ModItems.rubber != null) {
			OreDictionary.registerOre("itemRubber", ModItems.rubber);
			OreDictionary.registerOre("itemRawRubber", ModItems.rawRubber);
			for (ItemStack sapling : OreDictionary.getOres("treeSapling"))
				GameRegistry.addRecipe(new ItemStack(ModItems.rawRubber), "AAA", "AAA", "AAA", 'A', sapling);
			GameRegistry.addRecipe(new ItemStack(ModItems.rawRubber), "AAA", "AAA", "AAA", 'A', new ItemStack(Blocks.TALLGRASS, 1, EnumType.GRASS.getMeta()));
			for (ItemStack rawRubber : OreDictionary.getOres("itemRawRubber"))
				GameRegistry.addSmelting(rawRubber, new ItemStack(ModItems.rubber), 0.2f);
		}
		if (ModBlocks.explosiveRail != null)
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.explosiveRail), new ItemStack(Blocks.TNT), new ItemStack(Blocks.RAIL));
		for (EnumDyeColor color : EnumDyeColor.values()) {
			List<ItemStack> dyes = OreDictionary.getOres("dye"+
					color.getUnlocalizedName().substring(0, 1).toUpperCase()+color.getUnlocalizedName().substring(1));
			if (color.equals(EnumDyeColor.SILVER))
				dyes = OreDictionary.getOres("dyeLightGray");
			for (ItemStack dye : dyes) {
				if (ModItems.balloonDeflated != null)
					for (ItemStack rubber : OreDictionary.getOres("itemRubber")) 
						GameRegistry.addShapelessRecipe(new ItemStack(ModItems.balloonDeflated, 8, color.getMetadata()), rubber, new ItemStack(Items.STRING), dye);
				if (ModItems.balloonWater != null)
					GameRegistry.addRecipe(new ItemStack(ModItems.balloonWater, 8, color.getMetadata()), "AAA", "ABA", "AAA", 'A', new ItemStack(ModItems.balloonDeflated, 1, color.getMetadata()), 'B', new ItemStack(Items.WATER_BUCKET.setContainerItem(Items.BUCKET)));
				if (ModItems.balloonLava != null)
					GameRegistry.addRecipe(new ItemStack(ModItems.balloonLava, 8, color.getMetadata()), "AAA", "ABA", "AAA", 'A', new ItemStack(ModItems.balloonDeflated, 1, color.getMetadata()), 'B', new ItemStack(Items.LAVA_BUCKET.setContainerItem(Items.BUCKET)));
				if (ModItems.smokeBomb != null)
					for (ItemStack rubber : OreDictionary.getOres("itemRubber"))
						GameRegistry.addShapelessRecipe(new ItemStack(ModItems.smokeBomb, 1, color.getMetadata()), new ItemStack(Items.GUNPOWDER), dye, rubber);
			}
		}
		if (ModItems.paperBagHat != null)
			GameRegistry.addRecipe(new ItemStack(ModItems.paperBagHat), "AAA", "A A", 'A', new ItemStack(Items.PAPER));
		if (ModFluids.acid != null)
			GameRegistry.addShapelessRecipe(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, ModFluids.acid), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.WATER_BUCKET.setContainerItem(Items.BUCKET)));
		if (ModBlocks.heater != null)
			GameRegistry.addRecipe(new ItemStack(ModBlocks.heater), "ABA", "BCB", "ADA", 'A', new ItemStack(Items.IRON_INGOT),'B', new ItemStack(Blocks.RED_NETHER_BRICK), 'C', new ItemStack(Items.LAVA_BUCKET), 'D', new ItemStack(Blocks.IRON_BARS));
		if (ModBlocks.cooler != null)
			GameRegistry.addRecipe(new ItemStack(ModBlocks.cooler), "ABA", "BCB", "ADA", 'A', new ItemStack(Items.IRON_INGOT),'B', new ItemStack(Blocks.SNOW), 'C', new ItemStack(Blocks.ICE), 'D', new ItemStack(Blocks.IRON_BARS));
		if (ModBlocks.petRock != null)
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.petRock), new ItemStack(Blocks.STONE), new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		if (ModItems.targetChestplate != null)
			for (ItemStack redDye : OreDictionary.getOres("dyeRed")) 
				for (ItemStack whiteDye : OreDictionary.getOres("dyeWhite"))
					GameRegistry.addShapelessRecipe(new ItemStack(ModItems.targetChestplate), redDye, new ItemStack(Items.LEATHER_CHESTPLATE), whiteDye);
		if (ModItems.rubberChicken != null)
			for (ItemStack rubber : OreDictionary.getOres("itemRubber")) 
				GameRegistry.addRecipe(new ItemStack(ModItems.rubberChicken), " A ", "ABA", " A ", 'A', rubber, 'B', new ItemStack(Items.CHICKEN));
		if (ModItems.propellerHat != null) 
			GameRegistry.addRecipe(new ItemStack(ModItems.propellerHat), "FRF", "YLG", "FBF", 'F', new ItemStack(Items.FEATHER), 'L', new ItemStack(Items.LEATHER_HELMET), 'R', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()), 'B', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		if (ModItems.pocketSand != null)
			GameRegistry.addRecipe(new ItemStack(ModItems.pocketSand), " T ", "LSL", " L ", 'L', new ItemStack(Items.LEATHER), 'S', new ItemStack(Blocks.SAND), 'T', new ItemStack(Items.STRING));
		if (ModItems.upsideDownGoggles != null)
			GameRegistry.addRecipe(new ItemStack(ModItems.upsideDownGoggles), "III", "I I", "GRG", 'I', new ItemStack(Items.IRON_INGOT), 'G', new ItemStack(Blocks.GLASS), 'R', new ItemStack(Items.REDSTONE));
	}

	public Object getArmorModel(Item item, EntityLivingBase entity) {
		return null;
	}

	public void spawnParticlesSmokeCloud(World worldIn, int color, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {}

	/**Add item (and sub-items in clientproxy) to creative tab*/
	public void addToTab(Item item, CreativeTabs tab, ArrayList<ItemStack> stacks) {
		item.setCreativeTab(StupidThings.tab);
	}

	public void openCatalogGui() {}
}