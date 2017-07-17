package furgl.stupidThings.client.particle;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleSimpleAnimated;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSmokeCloud extends ParticleSimpleAnimated {

	public static final ResourceLocation TEXTURE = new ResourceLocation(StupidThings.MODID, "entity/particle/smoke_cloud");

	public ParticleSmokeCloud(World worldIn, int color, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
		super(worldIn, x, y, z, 0, 0, 0);
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.particleGravity = 0.2f;
		this.particleMaxAge = rand.nextInt(100)+50;
		this.particleScale = scale;
		this.particleAlpha = 0.7f;
		this.setColorFade(0x666666);
		this.setColor(MapColor.getBlockColor(EnumDyeColor.byMetadata(color)).colorValue);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TEXTURE.toString());
		this.setParticleTexture(sprite);  
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		return 0xf000f0;
	}

	@Override
	public boolean shouldDisableDepth() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.multipleParticleScaleBy(0.08f-this.particleAge/1000f+1f);
	}
	
	@Override
	public void setParticleTextureIndex(int particleTextureIndex) {}
}
