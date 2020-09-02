package furgl.stupidThings.common.fluid;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.BlockFluidAcid;
import furgl.stupidThings.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

public class ModFluids {

	public static ArrayList<Fluid> allFluids = new ArrayList<Fluid>();
	public static ArrayList<IFluidBlock> allFluidBlocks = new ArrayList<IFluidBlock>();

	public static final Fluid ACID = registerFluid(new Fluid("acid", new ResourceLocation(StupidThings.MODID, "blocks/fluid_acid_still"), 
			new ResourceLocation(StupidThings.MODID, "blocks/fluid_acid_flow")), "acid", true);
	public static IFluidBlock acidBlock;

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			if (!Config.isNameEnabled("acid"))
				return;
			
			ACID.setDensity(100).setViscosity(1000);
			acidBlock = new BlockFluidAcid(ACID, Material.WATER);
			final Block block = (Block) acidBlock;
			allFluidBlocks.add((IFluidBlock) block);
			block.setRegistryName(StupidThings.MODID, "acid");
			block.setTranslationKey(block.getRegistryName().getPath());
			registry.register(block);
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			if (ACID != null) {
				final Block block = (Block) acidBlock;
				final ItemBlock itemBlock = new ItemBlock(block);
				itemBlock.setRegistryName(block.getRegistryName());
				registry.register(itemBlock);

				FluidRegistry.addBucketForFluid(ACID);

				if (StupidThings.tab.orderedStacks.size() > 1)
					StupidThings.tab.orderedStacks.add(1, FluidUtil.getFilledBucket(new FluidStack(ACID, Fluid.BUCKET_VOLUME)));
				else
					StupidThings.tab.orderedStacks.add(FluidUtil.getFilledBucket(new FluidStack(ACID, Fluid.BUCKET_VOLUME)));
			}
		}
	}
	
	@Mod.EventBusSubscriber(Side.CLIENT)
	public static class ModelRegisterer {

		@SubscribeEvent
		public static void registerModel(ModelRegistryEvent event) {
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
		
	}

	private static Fluid registerFluid(Fluid fluid, String unlocalizedName, boolean checkIfDisabled) {
		fluid.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(unlocalizedName))
			return null;
		FluidRegistry.registerFluid(fluid);
		allFluids.add(fluid);
		return fluid;
	}

}