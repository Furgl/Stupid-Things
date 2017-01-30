package furgl.stupidThings.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelAnvilBackpack extends ModelBiped {

	public ModelAnvilBackpack() {
		super();
		this.textureHeight = 32;
		this.textureWidth = 32;
		
		this.bipedRightArm = new ModelRenderer(this, 0, 0);
		this.bipedLeftArm = new ModelRenderer(this, 0, 0);
		this.bipedBody = new ModelRenderer(this, 0, 0);
		//anvil		
		this.bipedBody.addBox(-8, 0, 4, 16, 6, 10);
		this.bipedBody.addBox(-4, 6, 7, 8, 5, 4);
		this.bipedBody.addBox(-5, 11, 5, 10, 1, 8);
		this.bipedBody.addBox(-6, 12, 3, 12, 4, 12);
		//top straps
		ModelRenderer model = new ModelRenderer(this, 0, 26);
		model.addBox(-14, -1, 4, 16, 1, 2);
		model.addBox(-14, -1, -6, 16, 1, 2);
		model.rotateAngleY = (float) Math.PI/2;
		this.bipedBody.addChild(model);
		//front straps
		model = new ModelRenderer(this, 0, 26);
		model.addBox(-1, -6, -3, 7, 2, 1);
		model.addBox(-1, 4, -3, 7, 2, 1);
		model.rotateAngleZ = (float) Math.PI/2;
		this.bipedBody.addChild(model);
		//back straps
		model = new ModelRenderer(this, 0, 26);
		model.addBox(-1, -6, 14, 7, 2, 1);
		model.addBox(-1, 4, 14, 7, 2, 1);
		model.rotateAngleZ = (float) Math.PI/2;
		this.bipedBody.addChild(model);
		//bottom straps
		model = new ModelRenderer(this, 0, 26);
		model.addBox(-15, 6, 4, 18, 1, 2);
		model.addBox(-15, 6, -6, 18, 1, 2);
		model.rotateAngleY = (float) Math.PI/2;
		this.bipedBody.addChild(model);
	}
}