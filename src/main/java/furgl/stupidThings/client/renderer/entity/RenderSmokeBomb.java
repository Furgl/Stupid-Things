package furgl.stupidThings.client.renderer.entity;

import furgl.stupidThings.client.model.ModelSmokeBomb;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.entity.EntitySmokeBomb;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSmokeBomb extends RenderLiving<EntitySmokeBomb> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(StupidThings.MODID+":textures/entity/smoke_bomb.png");

	public RenderSmokeBomb(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelSmokeBomb(), 0.2f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySmokeBomb entity) {
		return TEXTURE;
	}
}