package furgl.stupidThings.client;

import furgl.stupidThings.client.model.ModelAnvilBackpack;
import furgl.stupidThings.client.model.ModelPaperBagHat;
import furgl.stupidThings.common.CommonProxy;
import furgl.stupidThings.common.block.ModBlocks;
import furgl.stupidThings.common.entity.ModEntities;
import furgl.stupidThings.common.item.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	private static final ModelAnvilBackpack MODEL_ANVIL_BACKPACK = new ModelAnvilBackpack();
	private static final ModelPaperBagHat MODEL_PAPER_BAG_HAT = new ModelPaperBagHat();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ModBlocks.registerRenders();
		ModItems.registerRenders();
		ModEntities.registerRenders();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	@Override
	public Object getArmorModel(Item item) {
		if (item == ModItems.anvilBackpack)
			return MODEL_ANVIL_BACKPACK;
		else if (item == ModItems.paperBagHat)
			return MODEL_PAPER_BAG_HAT;

		return null;
	}
}