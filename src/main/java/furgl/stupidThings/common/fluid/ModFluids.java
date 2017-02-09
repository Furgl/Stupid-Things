package furgl.stupidThings.common.fluid;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.BlockFluidAcid;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.UniversalBucket;

@SuppressWarnings("deprecation")
public class ModFluids {

	public static ArrayList<Fluid> allFluids = new ArrayList<Fluid>();
	public static ArrayList<IFluidBlock> allFluidBlocks = new ArrayList<IFluidBlock>();

	public static Fluid acid;
	public static IFluidBlock acidBlock;

	public static void preInit() {
		acid = registerFluid(new Fluid("acid", new ResourceLocation(StupidThings.MODID, "blocks/fluid_acid_still"), 
				new ResourceLocation(StupidThings.MODID, "blocks/fluid_acid_flow")), "acid", true);
		if (acid != null) {
			acid.setDensity(100).setViscosity(1000);
			acidBlock = registerFluidBlock(acid, new BlockFluidAcid(acid, Material.WATER), "acid");
		}

		registerModels();
	}

	private static void registerModels() {
		for (IFluidBlock fluidBlock : allFluidBlocks) {
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

	private static Fluid registerFluid(Fluid fluid, String unlocalizedName, boolean checkIfDisabled) {
		fluid.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(I18n.translateToLocal("fluid."+unlocalizedName).replace("White ", "")))
			return null;
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		StupidThings.tab.orderedStacks.add(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid));
		allFluids.add(fluid);
		return fluid;
	}

	private static IFluidBlock registerFluidBlock(Fluid fluid, IFluidBlock fluidBlock, String unlocalizedName) {
		ModBlocks.registerBlock((Block) fluidBlock, unlocalizedName, true, false, false);
		allFluidBlocks.add(fluidBlock);
		return fluidBlock;
	}
}