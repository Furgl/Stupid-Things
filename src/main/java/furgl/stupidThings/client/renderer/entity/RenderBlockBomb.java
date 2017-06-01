package furgl.stupidThings.client.renderer.entity;

import furgl.stupidThings.client.model.ModelBlockBomb;
import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.entity.EntityBlockBomb;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBlockBomb extends RenderLiving<EntityBlockBomb> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(StupidThings.MODID+":textures/entity/block_bomb.png");

	public RenderBlockBomb(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelBlockBomb(), 0.4f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBlockBomb entity) {
		return TEXTURE;
	}

}