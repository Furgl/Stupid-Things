package furgl.stupidThings.common.entity;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySmokeBomb extends EntityLiving implements IProjectile {

	private float bounce;

	public EntitySmokeBomb(World world) {
		super(world);
		this.setSize(0.3F, 0.3F);
	}

	public EntitySmokeBomb(World world, EntityLivingBase thrower) {
		this(world);
		this.setPosition(thrower.posX, thrower.posY + (double)thrower.getEyeHeight() - 0.1D, thrower.posZ);
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return source != DamageSource.outOfWorld;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);

		if (distance > 1) 
			bounce = (float) Math.abs(motionY);
	}

	@Override
	public void onUpdate() {
		if (!this.worldObj.isRemote && this.ticksExisted > 250)
			this.setDead();

		if (this.ticksExisted > 40 && this.ticksExisted % 4 == 0)
			this.worldObj.playSound(null, this.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 
					0.2f, 1);

		if (this.worldObj.isRemote) {
			double speedModifier = Math.min(this.ticksExisted/90f, 1.5d);
			for (int i=0; i<2; i++)
				StupidThings.proxy.spawnParticlesSmokeCloud(worldObj, posX, posY, posZ, 
						(this.worldObj.rand.nextDouble()-0.5d)*speedModifier, (this.worldObj.rand.nextDouble()-0.5d)*speedModifier, 
						(this.worldObj.rand.nextDouble()-0.5d)*speedModifier, (float) Math.min(this.ticksExisted/100f, 1d));
			if (this.ticksExisted < 40)
				for (int i=0; i<this.ticksExisted/2; i++)
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 
							(this.worldObj.rand.nextDouble()-0.5d)*0.3d, (this.worldObj.rand.nextDouble()-0.5d)*0.3d, 
							(this.worldObj.rand.nextDouble()-0.5d)*0.3d, new int[0]);
			else {
				for (int i=0; i<3; i++)
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 
							(this.worldObj.rand.nextDouble()-0.5d)*0.3d, (this.worldObj.rand.nextDouble()-0.5d)*0.3d, 
							(this.worldObj.rand.nextDouble()-0.5d)*0.3d, new int[0]);
			}
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (!this.hasNoGravity())
			this.motionY -= 0.05d;

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98D;
		this.motionY *= 0.98D;
		this.motionZ *= 0.98D;

		if (this.onGround) {
			this.motionX *= 0.9D;
			this.motionZ *= 0.9D;
			if (bounce != 0) {
				this.motionY = bounce*0.5f;
				bounce = 0;
			}				
		}

		this.handleWaterMovement();

		if (this.ticksExisted > 40) {
			double radius = Math.min(this.ticksExisted/30f, 5d);
			List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expandXyz(radius), EntitySelectors.<Entity>getTeamCollisionPredicate(this));

			for (Entity entity : list)
				if (entity instanceof EntityLivingBase)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, false));
		}
	}

	public void setHeadingFromThrower(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.setThrowableHeading((double)f, (double)f1, (double)f2, velocity, inaccuracy);
		this.motionX += entityThrower.motionX;
		this.motionZ += entityThrower.motionZ;

		if (!entityThrower.onGround)
			this.motionY += entityThrower.motionY;
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt_double(x * x + y * y + z * z);
		x = x / (double)f;
		y = y / (double)f;
		z = z / (double)f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double)inaccuracy;
		x = x * (double)velocity;
		y = y * (double)velocity;
		z = z * (double)velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt_double(x * x + z * z);
		this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	protected SoundEvent getFallSound(int heightIn) {
		return null;
	}
}