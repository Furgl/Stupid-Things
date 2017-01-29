package furgl.stupidThings.common.entity;

import furgl.stupidThings.client.model.ModelBalloon;
import furgl.stupidThings.client.model.ModelBalloonLiquid;
import furgl.stupidThings.client.renderer.entity.RenderBalloon;
import furgl.stupidThings.client.renderer.entity.RenderReverseTntPrimed;
import furgl.stupidThings.common.StupidThings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	private static int id;

	public static void preInit() {
		registerEntity(EntityReverseTntPrimed.class);
		registerEntity(EntityBalloon.class);
		registerEntity(EntityBalloonLiquid.class);
	}

	@SuppressWarnings("deprecation")
	public static void registerRenders() {
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityReverseTntPrimed.class, new RenderReverseTntPrimed(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloon.class, new RenderBalloon(renderManager, new ModelBalloon()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBalloonLiquid.class, new RenderBalloon(renderManager, new ModelBalloonLiquid()));
	}

	/**Registers entity to unlocalizedName based on entity class (i.e. EntityZombieRunt = zombieRunt)*/
	private static void registerEntity(Class clazz) {
		String unlocalizedName = clazz.getSimpleName().replace("Entity", ""); 
		unlocalizedName = unlocalizedName.substring(0, 1).toLowerCase()+unlocalizedName.substring(1);
		EntityRegistry.registerModEntity(clazz, unlocalizedName, id++, StupidThings.instance, 64, 3, true);
	}
}