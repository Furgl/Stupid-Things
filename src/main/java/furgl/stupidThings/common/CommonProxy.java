package furgl.stupidThings.common;

import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.config.Config;
import furgl.stupidThings.common.entity.ModEntities;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.sound.ModSoundEvents;
import furgl.stupidThings.common.tileentity.ModTileEntities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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
		ModBlocks.preInit();
		ModItems.preInit();
		ModEntities.preInit();
		ModSoundEvents.preInit();
		ModTileEntities.preInit();
	}

	public void init(FMLInitializationEvent event) {
		Config.postInit(StupidThings.configFile);
		registerEventListeners();
		registerRecipes();
	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	private void registerPackets() {}

	private void registerEventListeners() {
		MinecraftForge.EVENT_BUS.register(new Config());
	}

	private void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.reverseTnt), new ItemStack(Blocks.TNT), new ItemStack(Items.ENDER_PEARL));
		GameRegistry.addRecipe(new ItemStack(ModItems.anvilChestplate), "ABA", 'A', new ItemStack(Items.STRING), 'B', new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.explosiveRail), new ItemStack(Blocks.TNT), new ItemStack(Blocks.RAIL));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.balloonDeflated, 16), new ItemStack(Items.LEATHER), new ItemStack(Items.STRING), new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		GameRegistry.addRecipe(new ItemStack(ModItems.balloonWater, 8), "AAA", "ABA", "AAA", 'A', new ItemStack(ModItems.balloonDeflated), 'B', new ItemStack(Items.WATER_BUCKET));
		GameRegistry.addRecipe(new ItemStack(ModItems.balloonLava, 8), "AAA", "ABA", "AAA", 'A', new ItemStack(ModItems.balloonDeflated), 'B', new ItemStack(Items.LAVA_BUCKET));
	}

	public Object getArmorModel(Item item) {
		return null;
	}
}