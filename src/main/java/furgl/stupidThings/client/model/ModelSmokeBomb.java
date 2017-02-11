package furgl.stupidThings.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelSmokeBomb extends ModelBase {

	public ModelRenderer bomb;
	
	public ModelSmokeBomb() {
		this.bomb = new ModelRenderer(this, 0, 0);
		this.bomb.addBox(-2.5f, -2.5f, -2.5f, 5, 5, 5);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1.35d, 0);
		float motion = (float) (Math.abs(entityIn.motionX)+Math.abs(entityIn.motionY)+Math.abs(entityIn.motionZ)) * 10f;
		bomb.rotateAngleX += motion + Math.sin(ageInTicks/50f)/3f;
		bomb.rotateAngleY += motion + ageInTicks/2f;
		bomb.rotateAngleZ += motion + Math.sin(ageInTicks/50f)/3f;
		bomb.render(scale);
		GlStateManager.popMatrix();
	}
}