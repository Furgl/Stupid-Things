package furgl.stupidThings.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

/**
 * Upside Down Goggles - Furgl
 * Created using Tabula 5.1.0
 */
public class ModelUpsideDownGoggles extends ModelBiped {
	
	private ModelRenderer backstrap;
	private ModelRenderer lefteye;
	private ModelRenderer righteye;
	private ModelRenderer rightstrap;
	private ModelRenderer leftstrap;
	private ModelRenderer bigfront;
	private ModelRenderer leftglass;
	private ModelRenderer rightglass;
	private ModelRenderer rightslant;
	private ModelRenderer leftslant2;
	private ModelRenderer leftslant;
	private ModelRenderer rightslant2;

	public ModelUpsideDownGoggles() {
		this.textureWidth = 64;
        this.textureHeight = 32;
        this.bigfront = new ModelRenderer(this, 4, 11);
        this.bigfront.setRotationPoint(-4.0F, -5.5F, -6.0F);
        this.bigfront.addBox(0.0F, 0.0F, 0.0F, 8, 3, 2, 0.0F);
        this.leftslant = new ModelRenderer(this, 0, 29);
        this.leftslant.setRotationPoint(1.0F, -5.0F, -8.0F);
        this.leftslant.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(leftslant, -0.9250245035569946F, 0.0F, 0.0F);
        this.rightstrap = new ModelRenderer(this, 41, 0);
        this.rightstrap.setRotationPoint(-5.0F, -5.0F, -5.0F);
        this.rightstrap.addBox(0.0F, 0.0F, 0.0F, 1, 2, 10, 0.0F);
        this.rightglass = new ModelRenderer(this, 0, 28);
        this.rightglass.setRotationPoint(-3.0F, -5.0F, -8.0F);
        this.rightglass.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
        this.rightslant = new ModelRenderer(this, 0, 29);
        this.rightslant.setRotationPoint(-3.0F, -5.0F, -8.0F);
        this.rightslant.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1, 0.0F);
        this.setRotateAngle(rightslant, -0.9250245035569946F, 0.0F, 0.0F);
        this.lefteye = new ModelRenderer(this, 4, 3);
        this.lefteye.setRotationPoint(0.5F, -6.0F, -6.0F);
        this.lefteye.addBox(0.0F, 0.0F, 0.0F, 3, 4, 2, 0.0F);
        this.rightslant2 = new ModelRenderer(this, 0, 30);
        this.rightslant2.setRotationPoint(-3.0F, -4.0F, -9.0F);
        this.rightslant2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
        this.leftglass = new ModelRenderer(this, 0, 28);
        this.leftglass.setRotationPoint(1.0F, -5.0F, -8.0F);
        this.leftglass.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
        this.backstrap = new ModelRenderer(this, 44, 0);
        this.backstrap.setRotationPoint(-4.0F, -5.0F, 4.0F);
        this.backstrap.addBox(0.0F, 0.0F, 0.0F, 8, 2, 1, 0.0F);
        this.leftstrap = new ModelRenderer(this, 41, 0);
        this.leftstrap.setRotationPoint(4.0F, -5.0F, -5.0F);
        this.leftstrap.addBox(0.0F, 0.0F, 0.0F, 1, 2, 10, 0.0F);
        this.righteye = new ModelRenderer(this, 4, 3);
        this.righteye.setRotationPoint(-3.5F, -6.0F, -6.0F);
        this.righteye.addBox(0.0F, 0.0F, 0.0F, 3, 4, 2, 0.0F);
        this.leftslant2 = new ModelRenderer(this, 0, 30);
        this.leftslant2.setRotationPoint(1.0F, -4.0F, -9.0F);
        this.leftslant2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);

		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addChild(backstrap);
		this.bipedHead.addChild(lefteye);
		this.bipedHead.addChild(righteye);
		this.bipedHead.addChild(rightstrap);
		this.bipedHead.addChild(leftstrap);
		this.bipedHead.addChild(bigfront);
		this.bipedHead.addChild(leftglass);
		this.bipedHead.addChild(rightglass);
		this.bipedHead.addChild(rightslant);
		this.bipedHead.addChild(leftslant2);
		this.bipedHead.addChild(leftslant);
		this.bipedHead.addChild(rightslant2);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entityIn instanceof EntityArmorStand) 
			netHeadYaw = 0;
		
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();
        
		GlStateManager.enableBlend(); //enables transparency
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        if (this.isChild) {
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
        }
        else {
            if (entityIn.isSneaking())
                GlStateManager.translate(0.0F, 0.2F, 0.0F);

            this.bipedHead.render(scale);
        }

		GlStateManager.disableBlend(); //disable transparency
        
        GlStateManager.popMatrix();
    }
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
