package furgl.stupidThings.client;

import java.util.ArrayList;

import furgl.stupidThings.client.model.ModelAnvilBackpack;
import furgl.stupidThings.client.model.ModelPaperBagHat;
import furgl.stupidThings.client.particle.ParticleSmokeCloud;
import furgl.stupidThings.client.renderer.entity.RenderBalloon;
import furgl.stupidThings.client.renderer.entity.RenderBalloonLiquid;
import furgl.stupidThings.client.renderer.entity.RenderReverseTntPrimed;
import furgl.stupidThings.client.renderer.entity.RenderSmokeBomb;
import furgl.stupidThings.common.CommonProxy;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.entity.EntityBalloon;
import furgl.stupidThings.common.entity.EntityBalloonLiquid;
import furgl.stupidThings.common.entity.EntityReverseTntPrimed;
import furgl.stupidThings.common.entity.EntitySmokeBomb;
import furgl.stupidThings.common.fluid.ModFluids;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
	private static final ModelAnvilBackpack MODEL_ANVIL_BACKPACK = new ModelAnvilBackpack();
	private static final ModelPaperBagHat MODEL_PAPER_BAG_HAT = new ModelPaperBagHat();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		StupidThings.util = TooltipHelper.INSTANCE;
		registerFluidModels();
		registerEntityRenders();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ModBlocks.registerRenders();
		registerColoredItemRenders();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	private static void registerColoredItemRenders() {
		for (Item item : ModItems.allItems)
			ModItems.registerRender(item, 0);

		Item[] coloredItems = new Item[] {ModItems.balloon, ModItems.balloonDeflated, ModItems.balloonWater, ModItems.balloonLava, ModItems.smokeBomb};

		for (Item item : coloredItems) {
			if (item != null) {			
				for (int i=1; i<16; i++) 
					ModItems.registerRender(item, i);

				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
					@Override
					public int getColorFromItemstack(ItemStack stack, int tintIndex) {
						return EnumDyeColor.byMetadata(stack.getMetadata()).getMapColor().colorValue;
					}
				}, item);
			}
		}
	}

	private static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityReverseTntPrimed.class, RenderReverseTntPrimed::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloon.class, RenderBalloon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloonLiquid.class, RenderBalloonLiquid::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySmokeBomb.class, RenderSmokeBomb::new);
	}

	private static void registerFluidModels() {
		for (IFluidBlock fluidBlock : ModFluids.allFluidBlocks) {
			Item item = Item.getItemFromBlock((Block) fluidBlock);

			final ModelResourceLocation modelLocation = new ModelResourceLocation(StupidThings.MODID+":acid", "fluid");

			ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
				public ModelResourceLocation getModelLocation(ItemStack stack) {
					return modelLocation;
				}
			});

			ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return modelLocation;
				}
			});
		}
	}

	@Override
	protected void registerEventListeners() {
		super.registerEventListeners();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void stitcherEventPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(ParticleSmokeCloud.TEXTURE);
	}

	@Override
	public Object getArmorModel(Item item) {
		if (item == null)
			return null;
		else if (item == ModItems.anvilBackpack)
			return MODEL_ANVIL_BACKPACK;
		else if (item == ModItems.paperBagHat)
			return MODEL_PAPER_BAG_HAT;

		return null;
	}

	@Override
	public void spawnParticlesSmokeCloud(World worldIn, int color, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		ParticleSmokeCloud particle = new ParticleSmokeCloud(worldIn, color, x, y, z, motionX, motionY, motionZ, scale);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	/**Add item (and sub-items in clientproxy) to creative tab*/
	@Override
	public void addToTab(Item item, CreativeTabs tab, ArrayList<ItemStack> stacks) {
		item.getSubItems(item, tab, stacks);
		item.setCreativeTab(StupidThings.tab);
	}
}