package furgl.stupidThings.client.model;

import java.awt.Color;

import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;

public class ModelBalloonLiquid extends ModelBase {

	public ModelRenderer balloon;

	public ModelBalloonLiquid() {
		this.textureHeight = 32;
		this.textureWidth = 32;
		this.balloon = new ModelRenderer(this, 0, 0);
		this.balloon.addBox(-1.5f, 5.5f, -1.5f, 3, 1, 3);
		this.balloon.addBox(-2.5f, 4.5f, -2.5f, 5, 1, 5);
		this.balloon.addBox(-3, -1.5f, -3, 6, 6, 6);
		this.balloon.addBox(-2.5f, -3.5f, -2.5f, 5, 2, 5);
		this.balloon.addBox(-1.5f, -4.5f, -1.5f, 3, 1, 3);
		this.balloon.addBox(-0.5f, -5.5f, -0.5f, 1, 1, 1);
		this.balloon.addBox(-1, -6.5f, -1, 2, 1, 2);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		
		GlStateManager.enableBlend(); //enables transparency
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		if (entity instanceof EntityBalloon) {
			Color color = new Color(MapColor.getBlockColor(EnumDyeColor.byMetadata(((EntityBalloon)entity).getColor())).colorValue);
			GlStateManager.color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
		}
		GlStateManager.translate(0, 1.2d, 0);
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * 1 + 180f, 0.0F, 0.0F, 1.0F);//angle
		balloon.rotateAngleY = (float) (entity.rotationYaw + Math.sin(Math.abs(entity.motionX)+
				Math.abs(entity.motionY*2f)+Math.abs(entity.motionZ))*10f);
		balloon.render(scale);
		
		GlStateManager.disableBlend(); //disable transparency

		GlStateManager.popMatrix(); 
	}
}