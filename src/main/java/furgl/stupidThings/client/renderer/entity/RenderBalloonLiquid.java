package furgl.stupidThings.client.renderer.entity;

import furgl.stupidThings.client.model.ModelBalloonLiquid;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBalloonLiquid extends RenderLiving<EntityBalloon> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(StupidThings.MODID+":textures/entity/balloon.png");

	public RenderBalloonLiquid(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelBalloonLiquid(), 0.3f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBalloon entity) {
		return TEXTURE;
	}
}