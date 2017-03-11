package furgl.stupidThings.common.entity;

import furgl.stupidThings.common.StupidThings;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	private static int id;

	public static void preInit() {
		registerEntity(EntityReverseTntPrimed.class);
		registerEntity(EntityBalloon.class);
		registerEntity(EntityBalloonLiquid.class);
		registerEntity(EntitySmokeBomb.class);
	}

/*	@SuppressWarnings("deprecation") FIXME
	public static void registerRenders() {
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityReverseTntPrimed.class, new RenderReverseTntPrimed(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloon.class, new RenderBalloon(renderManager, new ModelBalloon()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloonLiquid.class, new RenderBalloon(renderManager, new ModelBalloonLiquid()));
		RenderingRegistry.registerEntityRenderingHandler(EntitySmokeBomb.class, new RenderSmokeBomb(renderManager, new ModelSmokeBomb()));
	}*/

	/**Registers entity to unlocalizedName based on entity class (i.e. EntityZombieRunt = zombieRunt)*/
	private static void registerEntity(Class clazz) {
		String unlocalizedName = clazz.getSimpleName().replace("Entity", ""); 
		unlocalizedName = unlocalizedName.substring(0, 1).toLowerCase()+unlocalizedName.substring(1);
		EntityRegistry.registerModEntity(clazz, unlocalizedName, id++, StupidThings.instance, 64, 3, true);
	}
}