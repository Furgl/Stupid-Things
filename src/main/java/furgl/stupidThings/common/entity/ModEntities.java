package furgl.stupidThings.common.entity;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	private static int id;

	public static void preInit() {
		registerEntity(EntityReverseTntPrimed.class);
		registerEntity(EntityBalloon.class);
		registerEntity(EntityBalloonLiquid.class);
		registerEntity(EntitySmokeBomb.class);
		registerEntity(EntityBlockBomb.class);
	}

	/**Registers entity to unlocalizedName based on entity class (i.e. EntityZombieRunt = zombieRunt)*/
	private static void registerEntity(Class clazz) {
		String unlocalizedName = clazz.getSimpleName().replace("Entity", ""); 
		unlocalizedName = unlocalizedName.substring(0, 1).toLowerCase()+unlocalizedName.substring(1);
		ResourceLocation registryName = new ResourceLocation(StupidThings.MODID, unlocalizedName);
		EntityRegistry.registerModEntity(registryName, clazz, unlocalizedName, id++, StupidThings.instance, 64, 3, true);
	}
}