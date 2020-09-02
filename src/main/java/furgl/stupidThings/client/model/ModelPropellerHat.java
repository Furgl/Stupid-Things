package furgl.stupidThings.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

/**
 * Propeller Hat - Furgl
 * Created using Tabula 5.1.0
 */
public class ModelPropellerHat extends ModelBiped {
    public ModelRenderer propeller;
    public ModelRenderer right;
    public ModelRenderer top;
    public ModelRenderer rod;
    public ModelRenderer left;
    public ModelRenderer front;
    public ModelRenderer back;

    public ModelPropellerHat() { 	
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.left = new ModelRenderer(this, 0, 8);
        this.left.setRotationPoint(4.0F, -10.0F, -4.0F);
        this.left.addBox(0.0F, 0.0F, 0.0F, 1, 3, 8, 0.0F);
        this.propeller = new ModelRenderer(this, 0, 30);
        this.propeller.addBox(-5.0F, -14.0F, -0.5F, 10, 1, 1);
        this.back = new ModelRenderer(this, 0, 4);
        this.back.setRotationPoint(-5.0F, -10.0F, 4.0F);
        this.back.addBox(1.0F, 0.0F, 0.0F, 8, 3, 1, 0.0F);
        this.rod = new ModelRenderer(this, 42, 0);
        this.rod.setRotationPoint(3.5F, -5.0F, 3.5F);
        this.rod.addBox(-4.0F, -8.0F, -4.0F, 1, 2, 1, 0.0F);
        this.front = new ModelRenderer(this, 0, 0);
        this.front.setRotationPoint(-4.0F, -10.0F, -5.0F);
        this.front.addBox(0.0F, 0.0F, 0.0F, 8, 3, 1, 0.0F);
        this.right = new ModelRenderer(this, 0, 19);
        this.right.setRotationPoint(-5.0F, -10.0F, -4.0F);
        this.right.addBox(0.0F, 0.0F, 0.0F, 1, 3, 8, 0.0F);
        this.top = new ModelRenderer(this, 18, 0);
        this.top.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.top.addBox(-4.0F, -8.0F, -4.0F, 8, 1, 8, 0.0F);
        
        this.bipedHeadwear = new ModelRenderer(this, 0, 0);
        this.bipedHead = new ModelRenderer(this, 0, 0);
        this.bipedHead.addChild(left);
        this.bipedHead.addChild(propeller);
        this.bipedHead.addChild(back);
        this.bipedHead.addChild(rod);
        this.bipedHead.addChild(front);
        this.bipedHead.addChild(right);
        this.bipedHead.addChild(top);
    }

    @Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entityIn instanceof EntityArmorStand) 
			netHeadYaw = 0;
		// no idea how to fix 'double propeller' when rotating
		double x = Math.abs(entityIn.posX-entityIn.lastTickPosX);
		double y = Math.abs(entityIn.posY-entityIn.lastTickPosY);
		double z = Math.abs(entityIn.posZ-entityIn.lastTickPosZ);
		float motion = (float) (x*1.5f + Math.abs(y + (y == 0 || !entityIn.onGround ? 0.01f : 0.07f))*5f + z*1.5f);
		this.propeller.rotateAngleY += motion / 6f;
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
}
