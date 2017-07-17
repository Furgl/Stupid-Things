package furgl.stupidThings.client.model;

import java.awt.Color;

import com.google.common.base.Optional;

import furgl.stupidThings.common.entity.EntityBlockBomb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBlockBomb extends ModelBase {

	public ModelRenderer bomb;
	
	public ModelBlockBomb() {
		this.textureHeight = 8;
		this.textureWidth = 8;
		
		this.bomb = new ModelRenderer(this, 0, 0);
		this.bomb.addBox(-4f, -4f, -4f, 8, 8, 8);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 1.2f, 0);
		
		GlStateManager.enableBlend(); //enables transparency
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		if (entityIn instanceof EntityBlockBomb) {
			Optional<IBlockState> state = ((EntityBlockBomb)entityIn).getDataManager().get(EntityBlockBomb.STATE);
			if (state.isPresent()) {
				Color color = new Color(state.get().getMapColor(entityIn.world, entityIn.getPosition()).colorValue);
				GlStateManager.color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f);
			}
		}
		
		float motion = (float) (Math.abs(entityIn.motionX)+Math.abs(entityIn.motionY)+Math.abs(entityIn.motionZ)) / 5f;
		bomb.rotateAngleX += motion;
		bomb.rotateAngleY += motion;
		bomb.rotateAngleZ += motion;		
		bomb.render(scale);
		
		GlStateManager.disableBlend(); //disable transparency
		GlStateManager.popMatrix();
	}
}