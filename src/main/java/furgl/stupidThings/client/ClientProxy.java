package furgl.stupidThings.client;

import furgl.stupidThings.client.model.ModelAnvilChestplate;
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
	private static final ModelAnvilChestplate anvilChestplateModel = new ModelAnvilChestplate();
	
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
		if (item == ModItems.anvilChestplate)
			return anvilChestplateModel;

		return null;
	}
}