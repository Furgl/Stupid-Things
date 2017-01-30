package furgl.stupidThings.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelPaperBagHat extends ModelBiped {

	public ModelPaperBagHat() {
		super();
		this.textureHeight = 32;
		this.textureWidth = 64;

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-5, -9, -5, 10, 12, 10); 
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entityIn instanceof EntityArmorStand) 
			netHeadYaw = 0;
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}
}