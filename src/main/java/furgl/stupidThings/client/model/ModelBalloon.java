package furgl.stupidThings.client.model;

import java.awt.Color;

import furgl.stupidThings.common.entity.EntityBalloon;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;

public class ModelBalloon extends ModelBase {

	public ModelRenderer balloon;
	
	public ModelBalloon() {
		this.textureHeight = 32;
		this.textureWidth = 32;
		this.balloon = new ModelRenderer(this, 0, 0);
		this.balloon.setRotationPoint(5, 0, -5);
		this.balloon.addBox(-2f, 20, -2f, 5, 1, 5);
		this.balloon.addBox(-3f, 19, -3f, 7, 1, 7);
		this.balloon.addBox(-4f, 18, -4f, 9, 1, 9);
		this.balloon.addBox(-5f, 11, -5f, 11, 7, 11);
		this.balloon.addBox(-4.5f, 8, -4.5f, 10, 3, 10);
		this.balloon.addBox(-4f, 6, -4f, 9, 2, 9);
		this.balloon.addBox(-3f, 4, -3f, 7, 2, 7);
		this.balloon.addBox(-2f, 3, -2f, 5, 1, 5);
		this.balloon.addBox(-0.5f, 1, -0.5f, 2, 3, 2);
		this.balloon.addBox(-1f, 0, -1f, 3, 1, 3);
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
		GlStateManager.translate(-0.35d, 1.35d+Math.sin(entity.ticksExisted/20d)/15f, 0.35d);
		balloon.rotateAngleX = (float) Math.PI;
		float modifier = (entity instanceof EntityBalloon && ((EntityBalloon)entity).getLeashed()) ? 2f : 10f;
		balloon.rotateAngleY = (float) (entity.rotationYaw + Math.sin(entity.rotationYaw + Math.sin(Math.abs(entity.motionX)+
				Math.abs(entity.motionY*2f)+Math.abs(entity.motionZ)))*modifier);
		balloon.render(scale);
		
		GlStateManager.disableBlend(); //disable transparency

		GlStateManager.popMatrix();
	}
}