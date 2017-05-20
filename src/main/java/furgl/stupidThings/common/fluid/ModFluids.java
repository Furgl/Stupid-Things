package furgl.stupidThings.common.fluid;

import java.util.ArrayList;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.block.BlockFluidAcid;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
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
	}

	private static Fluid registerFluid(Fluid fluid, String unlocalizedName, boolean checkIfDisabled) {
		fluid.setUnlocalizedName(unlocalizedName);
		if (checkIfDisabled && !Config.isNameEnabled(I18n.translateToLocal("fluid."+unlocalizedName).replace("White ", "")))
			return null;
		FluidRegistry.registerFluid(fluid);
		FluidRegistry.addBucketForFluid(fluid);
		if (StupidThings.tab.orderedStacks.size() > 1)
			StupidThings.tab.orderedStacks.add(1, UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid));
		else
			StupidThings.tab.orderedStacks.add(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid));		allFluids.add(fluid);
		return fluid;
	}

	private static IFluidBlock registerFluidBlock(Fluid fluid, IFluidBlock fluidBlock, String unlocalizedName) {
		ModBlocks.registerBlock((Block) fluidBlock, unlocalizedName, true, false, false);
		allFluidBlocks.add(fluidBlock);
		return fluidBlock;
	}
}