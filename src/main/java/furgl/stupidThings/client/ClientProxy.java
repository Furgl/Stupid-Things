package furgl.stupidThings.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.collect.Maps;

import furgl.stupidThings.client.gui.GuiCatalog;
import furgl.stupidThings.client.model.ModelAnvilBackpack;
import furgl.stupidThings.client.model.ModelPaperBagHat;
import furgl.stupidThings.client.model.ModelPropellerHat;
import furgl.stupidThings.client.model.ModelUpsideDownGoggles;
import furgl.stupidThings.client.particle.ParticleSmokeCloud;
import furgl.stupidThings.client.renderer.entity.RenderBalloon;
import furgl.stupidThings.client.renderer.entity.RenderBalloonLiquid;
import furgl.stupidThings.client.renderer.entity.RenderBlockBomb;
import furgl.stupidThings.client.renderer.entity.RenderReverseTntPrimed;
import furgl.stupidThings.client.renderer.entity.RenderSmokeBomb;
import furgl.stupidThings.common.CommonProxy;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.entity.EntityBalloon;
import furgl.stupidThings.common.entity.EntityBalloonLiquid;
import furgl.stupidThings.common.entity.EntityBlockBomb;
import furgl.stupidThings.common.entity.EntityReverseTntPrimed;
import furgl.stupidThings.common.entity.EntitySmokeBomb;
import furgl.stupidThings.common.item.ItemCatalog;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.sound.SoundWorldsSmallestViolin;
import furgl.stupidThings.util.TooltipHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
	private static final ModelBiped MODEL_ANVIL_BACKPACK = new ModelAnvilBackpack();
	private static final ModelBiped MODEL_PAPER_BAG_HAT = new ModelPaperBagHat();
	private static final ModelBiped MODEL_UPSIDE_DOWN_GOGGLES = new ModelUpsideDownGoggles();
	// used for models that are unique per entity
	private HashMap<EntityLivingBase, ModelBiped> modelMap = Maps.newHashMap();
	public static final ArrayList<Item> COLORED_ITEMS = new ArrayList<Item>(Arrays.asList(new Item[] {ModItems.BALLOON, ModItems.BALLOON_DEFLATED, ModItems.BALLOON_WATER, ModItems.BALLOON_LAVA, ModItems.SMOKE_BOMB}));

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		StupidThings.util = TooltipHelper.INSTANCE;
		OBJLoader.INSTANCE.addDomain(StupidThings.MODID);
		registerItemObjModels();
		registerEntityRenders();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ModBlocks.registerRenders();
		registerItemRenders();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	private static void registerItemObjModels() {
		for (Item item : ModItems.objModelItems)
			ModItems.registerObjRender(item, 0);
	}

	private static void registerItemRenders() {
		for (Item item : ModItems.allItems)
			if (!ModItems.objModelItems.contains(item))
				ModItems.registerRender(item, 0);

		for (Item item : COLORED_ITEMS) {
			if (item.getRegistryName() != null) {			
				for (int i=1; i<16; i++) 
					ModItems.registerRender(item, i);

				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {
					@Override
					public int colorMultiplier(ItemStack stack, int tintIndex) {
						return tintIndex > 0 ? -1 : MapColor.getBlockColor(EnumDyeColor.byMetadata(stack.getMetadata())).colorValue;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityBlockBomb.class, RenderBlockBomb::new);
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
	public Object getArmorModel(Item item, EntityLivingBase entity) {
		if (item == ModItems.ANVIL_BACKPACK) 
			return MODEL_ANVIL_BACKPACK;
		else if (item == ModItems.PAPER_BAG_HAT) 
			return MODEL_PAPER_BAG_HAT;
		else if (item == ModItems.UPSIDE_DOWN_GOGGLES)
			return MODEL_UPSIDE_DOWN_GOGGLES;
		else if (item == ModItems.PROPELLER_HAT) {
			if (!modelMap.containsKey(entity) || !(modelMap.get(entity) instanceof ModelPropellerHat)) 
				modelMap.put(entity, new ModelPropellerHat());		
			return modelMap.get(entity);
		}

		return null;
	}

	@Override
	public void spawnParticlesSmokeCloud(World worldIn, int color, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		ParticleSmokeCloud particle = new ParticleSmokeCloud(worldIn, color, x, y, z, motionX, motionY, motionZ, scale);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	/**Add item (and sub-items in clientproxy) to creative tab*/
	@Override
	public void addToTab(Item item, CreativeTabs tab, NonNullList<ItemStack> stacks) {
		item.setCreativeTab(tab);
		if (item instanceof ItemCatalog)
			stacks.add(0, new ItemStack(item));
		else
			item.getSubItems(tab, stacks);
	}

	@Override
	public void openCatalogGui() { 
		Minecraft.getMinecraft().displayGuiScreen(new GuiCatalog());
	}

	@Override
	public void playWorldsSmallestViolinSound(EntityPlayer player) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new SoundWorldsSmallestViolin(player));
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

}