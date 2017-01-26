package furgl.stupidThings.client.renderer.entity;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderBalloon extends Render<EntityBalloon> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(StupidThings.MODID, "textures/models/armor/anvil_chestplate.png");

	public RenderBalloon(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
	public void doRender(EntityBalloon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
        
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-7.0D, -2.0D, -2.0D).tex(0.0D, 0.15625D).endVertex();
        vertexbuffer.pos(-7.0D, -2.0D, 2.0D).tex(0.15625D, 0.15625D).endVertex();
        vertexbuffer.pos(-7.0D, 2.0D, 2.0D).tex(0.15625D, 0.3125D).endVertex();
        vertexbuffer.pos(-7.0D, 2.0D, -2.0D).tex(0.0D, 0.3125D).endVertex();
        tessellator.draw();
        
        GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBalloon entity) {
		return TEXTURE;
	}
}