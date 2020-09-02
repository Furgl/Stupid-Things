package furgl.stupidThings.common.recipe;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.fluid.ModFluids;
import furgl.stupidThings.common.item.ItemImprovedHoe;
import furgl.stupidThings.common.item.ModItems;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModRecipes {

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<IRecipe> event) {	
			NonNullList<Ingredient> ingredients = null;

			if (ModItems.ITEM_CATALOG.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Blocks.DIRT)), 
						Ingredient.fromStacks(new ItemStack(Items.BOOK)));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 2, 1, ingredients, new ItemStack(ModItems.ITEM_CATALOG)).setRegistryName(ModItems.ITEM_CATALOG.getRegistryName()));
			}
			if (ModBlocks.REVERSE_TNT.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Blocks.TNT)), 
						Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL)));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 2, 1, ingredients, new ItemStack(ModBlocks.REVERSE_TNT)).setRegistryName(ModBlocks.REVERSE_TNT.getRegistryName()));
			}
			if (ModItems.ANVIL_BACKPACK.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Items.STRING)), 
						Ingredient.fromStacks(new ItemStack(Blocks.ANVIL, 1, OreDictionary.WILDCARD_VALUE)),
						Ingredient.fromStacks(new ItemStack(Items.STRING)));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 1, ingredients, new ItemStack(ModItems.ANVIL_BACKPACK)).setRegistryName(ModItems.ANVIL_BACKPACK.getRegistryName()));
			}
			if (ModItems.RUBBER.getRegistryName() != null) {
				OreDictionary.registerOre("itemRubber", ModItems.RUBBER);
				OreDictionary.registerOre("itemRawRubber", ModItems.RAW_RUBBER);
				for (ItemStack rawRubber : OreDictionary.getOres("itemRawRubber"))
					GameRegistry.addSmelting(rawRubber, new ItemStack(ModItems.RUBBER), 0.2f);
				Ingredient A = Ingredient.fromStacks(ArrayUtils.add(
						OreDictionary.getOres("treeSapling").toArray(new ItemStack[0]), 
						new ItemStack(Blocks.TALLGRASS, 1, EnumType.GRASS.getMeta())));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						A, A, A,
						A, A, A,
						A, A, A);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.RAW_RUBBER)).setRegistryName(ModItems.RAW_RUBBER.getRegistryName()));
			}
			if (ModBlocks.EXPLOSIVE_RAIL.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Blocks.TNT)), 
						Ingredient.fromStacks(new ItemStack(Blocks.RAIL)));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 2, 1, ingredients, new ItemStack(ModBlocks.EXPLOSIVE_RAIL)).setRegistryName(ModBlocks.EXPLOSIVE_RAIL.getRegistryName()));
			}
			for (EnumDyeColor color : EnumDyeColor.values()) {
				List<ItemStack> dyes = OreDictionary.getOres("dye"+
						color.getName().substring(0, 1).toUpperCase()+color.getName().substring(1));
				if (color.equals(EnumDyeColor.SILVER))
					dyes = OreDictionary.getOres("dyeLightGray");
				for (ItemStack dye : dyes) {
					if (ModItems.BALLOON_DEFLATED.getRegistryName() != null) {
						ingredients = NonNullList.from(Ingredient.EMPTY,
								Ingredient.fromStacks(OreDictionary.getOres("itemRubber").toArray(new ItemStack[0])), 
								Ingredient.fromStacks(new ItemStack(Items.STRING)), Ingredient.fromStacks(dye));
						ForgeRegistries.RECIPES.register(new ShapelessMatchingRecipes(StupidThings.MODNAME, new ItemStack(ModItems.BALLOON_DEFLATED, 8, color.getMetadata()), ingredients).setRegistryName(ModItems.BALLOON_DEFLATED.getRegistryName()+"_"+color.getMetadata()));
					}
					if (ModItems.BALLOON_WATER.getRegistryName() != null) {
						Ingredient D = Ingredient.fromStacks(new ItemStack(ModItems.BALLOON_DEFLATED, 1, color.getMetadata()));
						Ingredient W = Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET));
						ingredients = NonNullList.from(Ingredient.EMPTY,
								D, D, D,
								D, W, D,
								D, D, D);
						ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.BALLOON_WATER, 8, color.getMetadata())).setRegistryName(ModItems.BALLOON_WATER.getRegistryName()+"_"+color.getMetadata()));
					}
					if (ModItems.BALLOON_LAVA.getRegistryName() != null) {
						Ingredient D = Ingredient.fromStacks(new ItemStack(ModItems.BALLOON_DEFLATED, 1, color.getMetadata()));
						Ingredient L = Ingredient.fromStacks(new ItemStack(Items.LAVA_BUCKET));
						ingredients = NonNullList.from(Ingredient.EMPTY,
								D, D, D,
								D, L, D,
								D, D, D);
						ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.BALLOON_LAVA, 8, color.getMetadata())).setRegistryName(ModItems.BALLOON_LAVA.getRegistryName()+"_"+color.getMetadata()));				
					}
					if (ModItems.SMOKE_BOMB.getRegistryName() != null) {
						ingredients = NonNullList.from(Ingredient.EMPTY,
								Ingredient.fromStacks(OreDictionary.getOres("itemRubber").toArray(new ItemStack[0])), Ingredient.fromStacks(new ItemStack(Items.GUNPOWDER)), Ingredient.fromStacks(dye));
						ForgeRegistries.RECIPES.register(new ShapelessMatchingRecipes(StupidThings.MODNAME, new ItemStack(ModItems.SMOKE_BOMB, 1, color.getMetadata()), ingredients).setRegistryName(ModItems.SMOKE_BOMB.getRegistryName()+"_"+color.getMetadata()));
					}
				}
			}
			if (ModItems.PAPER_BAG_HAT.getRegistryName() != null) {
				Ingredient P = Ingredient.fromStacks(new ItemStack(Items.PAPER));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						P, P, P,
						P, Ingredient.EMPTY, P);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 2, ingredients, new ItemStack(ModItems.PAPER_BAG_HAT)).setRegistryName(ModItems.PAPER_BAG_HAT.getRegistryName()));
			}
			if (ModFluids.ACID != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Items.ROTTEN_FLESH)), 
						Ingredient.fromStacks(new ItemStack(Items.GUNPOWDER)), 
						Ingredient.fromStacks(new ItemStack(Items.SPIDER_EYE)), 
						Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)));
				ForgeRegistries.RECIPES.register(new ShapelessMatchingRecipes(StupidThings.MODNAME, FluidUtil.getFilledBucket(new FluidStack(ModFluids.ACID, 0)), ingredients).setRegistryName(ModFluids.ACID.getUnlocalizedName()));
			}
			if (ModBlocks.HEATER.getRegistryName() != null) {
				Ingredient A = Ingredient.fromItem(Items.IRON_INGOT);
				Ingredient B = Ingredient.fromItem(Item.getItemFromBlock(Blocks.RED_NETHER_BRICK));
				Ingredient C = Ingredient.fromItem(Items.LAVA_BUCKET);
				Ingredient D = Ingredient.fromItem(Item.getItemFromBlock(Blocks.IRON_BARS));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						A, B, A,
						B, C, B,
						A, D, A);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModBlocks.HEATER)).setRegistryName(ModBlocks.HEATER.getRegistryName()));
			}
			if (ModBlocks.COOLER.getRegistryName() != null) {
				Ingredient A = Ingredient.fromItem(Items.IRON_INGOT);
				Ingredient B = Ingredient.fromItem(Item.getItemFromBlock(Blocks.SNOW));
				Ingredient C = Ingredient.fromItem(Item.getItemFromBlock(Blocks.ICE));
				Ingredient D = Ingredient.fromItem(Item.getItemFromBlock(Blocks.IRON_BARS));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						A, B, A,
						B, C, B,
						A, D, A);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModBlocks.COOLER)).setRegistryName(ModBlocks.COOLER.getRegistryName()));
			}
			if (ModBlocks.GRAVITY_ACCELERATOR.getRegistryName() != null) {
				Ingredient A = Ingredient.fromItem(Items.IRON_INGOT);
				Ingredient B = Ingredient.fromItem(Item.getItemFromBlock(Blocks.OBSIDIAN));
				Ingredient C = Ingredient.fromItem(Items.ENDER_EYE);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						A, B, A,
						B, C, B,
						A, B, A);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModBlocks.GRAVITY_ACCELERATOR)).setRegistryName(ModBlocks.GRAVITY_ACCELERATOR.getRegistryName()));
			}
			if (ModBlocks.PET_ROCK.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(Blocks.STONE)), 
						Ingredient.fromStacks(new ItemStack(Items.DYE)));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 2, 1, ingredients, new ItemStack(ModBlocks.PET_ROCK)).setRegistryName(ModBlocks.PET_ROCK.getRegistryName()));
			}
			if (ModItems.TARGET_CHESTPLATE.getRegistryName() != null) {
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(OreDictionary.getOres("dyeRed").toArray(new ItemStack[0])), 
						Ingredient.fromStacks(new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)),
						Ingredient.fromStacks(OreDictionary.getOres("dyeWhite").toArray(new ItemStack[0])));
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 1, ingredients, new ItemStack(ModItems.TARGET_CHESTPLATE)).setRegistryName(ModItems.TARGET_CHESTPLATE.getRegistryName()));
			}
			if (ModItems.RUBBER_CHICKEN.getRegistryName() != null) {
				Ingredient A = Ingredient.fromItem(Items.CHICKEN);
				Ingredient B = Ingredient.fromStacks(OreDictionary.getOres("itemRubber").toArray(new ItemStack[0]));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, B, Ingredient.EMPTY,
						B, A, B,
						Ingredient.EMPTY, B, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.RUBBER_CHICKEN)).setRegistryName(ModItems.RUBBER_CHICKEN.getRegistryName()));
			}
			if (ModItems.PROPELLER_HAT.getRegistryName() != null) {
				Ingredient A = Ingredient.fromItem(Items.FEATHER);
				Ingredient B = Ingredient.fromStacks(OreDictionary.getOres("dyeRed").toArray(new ItemStack[0]));
				Ingredient C = Ingredient.fromStacks(OreDictionary.getOres("dyeYellow").toArray(new ItemStack[0]));
				Ingredient D = Ingredient.fromStacks(OreDictionary.getOres("dyeGreen").toArray(new ItemStack[0]));
				Ingredient E = Ingredient.fromStacks(OreDictionary.getOres("dyeBlue").toArray(new ItemStack[0]));
				Ingredient F = Ingredient.fromItem(Items.LEATHER_HELMET);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						A, B, A,
						C, F, D,
						A, E, A);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.PROPELLER_HAT)).setRegistryName(ModItems.PROPELLER_HAT.getRegistryName()));
			}
			if (ModItems.POCKET_SAND.getRegistryName() != null) {
				Ingredient L = Ingredient.fromItem(Items.LEATHER);
				Ingredient S = Ingredient.fromItem(Item.getItemFromBlock(Blocks.SAND));
				Ingredient T = Ingredient.fromItem(Items.STRING);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, T, Ingredient.EMPTY,
						L, S, L,
						Ingredient.EMPTY, L, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.POCKET_SAND)).setRegistryName(ModItems.POCKET_SAND.getRegistryName()));
			}
			if (ModItems.UPSIDE_DOWN_GOGGLES.getRegistryName() != null) {
				Ingredient I = Ingredient.fromItem(Items.IRON_INGOT);
				Ingredient G = Ingredient.fromItem(Item.getItemFromBlock(Blocks.GLASS));
				Ingredient R = Ingredient.fromItem(Items.REDSTONE);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						I, I, I,
						I, Ingredient.EMPTY, I,
						G, R, G);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.UPSIDE_DOWN_GOGGLES)).setRegistryName(ModItems.UPSIDE_DOWN_GOGGLES.getRegistryName()));
			}
			if (ModItems.INVISIBLE_HELMET.getRegistryName() != null) {
				Ingredient G = Ingredient.fromItem(Item.getItemFromBlock(Blocks.GLASS));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						G, G, G,
						G, Ingredient.fromStacks(new ItemStack(Items.IRON_HELMET, 1, OreDictionary.WILDCARD_VALUE)), G,
						G, G, G);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.INVISIBLE_HELMET)).setRegistryName(ModItems.INVISIBLE_HELMET.getRegistryName()));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						G, G, G,
						G, Ingredient.fromStacks(new ItemStack(Items.IRON_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)), G,
						G, G, G);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.INVISIBLE_CHESTPLATE)).setRegistryName(ModItems.INVISIBLE_CHESTPLATE.getRegistryName()));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						G, G, G,
						G, Ingredient.fromStacks(new ItemStack(Items.IRON_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)), G,
						G, G, G);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.INVISIBLE_LEGGINGS)).setRegistryName(ModItems.INVISIBLE_LEGGINGS.getRegistryName()));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						G, G, G,
						G, Ingredient.fromStacks(new ItemStack(Items.IRON_BOOTS, 1, OreDictionary.WILDCARD_VALUE)), G,
						G, G, G);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.INVISIBLE_BOOTS)).setRegistryName(ModItems.INVISIBLE_BOOTS.getRegistryName()));
			}
			if (ModBlocks.MINE_TURTLE.getRegistryName() != null) {
				Ingredient P = Ingredient.fromItem(Item.getItemFromBlock(Blocks.STONE_PRESSURE_PLATE));
				Ingredient W = Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 1, EnumDyeColor.GREEN.getMetadata()));
				Ingredient T = Ingredient.fromItem(Item.getItemFromBlock(Blocks.TNT));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						W, P, W,
						W, T, W,
						W, W, W);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModBlocks.MINE_TURTLE)).setRegistryName(ModBlocks.MINE_TURTLE.getRegistryName()));
			}
			if (ModItems.IMPROVED_WOOD_HOE.getRegistryName() != null) {
				for (Item improvedHoe : new Item[] {ModItems.IMPROVED_WOOD_HOE, ModItems.IMPROVED_STONE_HOE, ModItems.IMPROVED_IRON_HOE, ModItems.IMPROVED_GOLD_HOE, ModItems.IMPROVED_DIAMOND_HOE}) {
					Ingredient H = Ingredient.fromStacks(((ItemImprovedHoe)improvedHoe).hoe);
					Ingredient S = Ingredient.fromItem(Items.STRING);
					ingredients = NonNullList.from(Ingredient.EMPTY,
							H, H, H,
							S);
					ForgeRegistries.RECIPES.register(new ShapelessMatchingRecipes(StupidThings.MODNAME, new ItemStack(improvedHoe), ingredients).setRegistryName(improvedHoe.getRegistryName()));
				}
			}
			if (ModItems.FIRING_CAN.getRegistryName() != null) {
				Ingredient I = Ingredient.fromItem(Items.IRON_INGOT);
				Ingredient L = Ingredient.fromStacks(new ItemStack(Items.LAVA_BUCKET.setContainerItem(Items.BUCKET)));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						I, Ingredient.EMPTY, Ingredient.EMPTY,
						I, L, I,
						Ingredient.EMPTY, I, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.FIRING_CAN)).setRegistryName(ModItems.FIRING_CAN.getRegistryName()));
			}
			if (ModItems.CACTUS_SWORD.getRegistryName() != null) {
				Ingredient C = Ingredient.fromItem(Item.getItemFromBlock(Blocks.CACTUS));
				Ingredient S = Ingredient.fromItem(Items.STICK);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, C, Ingredient.EMPTY,
						Ingredient.EMPTY, C, Ingredient.EMPTY,
						Ingredient.EMPTY, S, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.CACTUS_SWORD)).setRegistryName(ModItems.CACTUS_SWORD.getRegistryName()));
			}
			if (ModItems.WORLDS_SMALLEST_VIOLIN.getRegistryName() != null) {
				Ingredient P = Ingredient.fromStacks(OreDictionary.getOres("plankWood").toArray(new ItemStack[0]));
				Ingredient S = Ingredient.fromItem(Items.STRING);
				Ingredient T = Ingredient.fromItem(Items.STICK);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, P, T,
						P, S, P,
						P, P, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.WORLDS_SMALLEST_VIOLIN)).setRegistryName(ModItems.WORLDS_SMALLEST_VIOLIN.getRegistryName()));
			}
			if (ModBlocks.HIDDEN_LIGHT.getRegistryName() != null) {
				Ingredient G = Ingredient.fromItem(Item.getItemFromBlock(Blocks.GLASS));
				Ingredient L = Ingredient.fromItem(Items.GLOWSTONE_DUST);
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, G, Ingredient.EMPTY,
						G, L, G,
						Ingredient.EMPTY, G, Ingredient.EMPTY);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModBlocks.HIDDEN_LIGHT)).setRegistryName(ModBlocks.HIDDEN_LIGHT.getRegistryName()));
			}
			if (ModItems.BLOCK_BOMB_LAUNCHER.getRegistryName() != null) {
				Ingredient I = Ingredient.fromItem(Item.getItemFromBlock(Blocks.IRON_BLOCK));
				Ingredient D = Ingredient.fromItem(Items.DIAMOND);
				Ingredient L = Ingredient.fromItem(Item.getItemFromBlock(Blocks.LEVER));
				ingredients = NonNullList.from(Ingredient.EMPTY,
						Ingredient.EMPTY, Ingredient.EMPTY, I,
						Ingredient.EMPTY, I, D,
						I, D, L);
				ForgeRegistries.RECIPES.register(new ShapedMatchingRecipes(StupidThings.MODNAME, 3, 3, ingredients, new ItemStack(ModItems.BLOCK_BOMB_LAUNCHER)).setRegistryName(ModItems.BLOCK_BOMB_LAUNCHER.getRegistryName()));
			}
			
		}
		
	}


}
