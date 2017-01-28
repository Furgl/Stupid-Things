package furgl.stupidThings.common.entity;

import java.util.List;

import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.sound.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBalloon extends EntityLiving implements IProjectile {

	protected double gravity;

	public EntityBalloon(World world) {
		super(world);
		this.gravity = 0.005D;
		this.setSize(0.7F, 1.3F);
	}

	public EntityBalloon(World world, EntityLivingBase thrower) {
		this(world);
		this.setPosition(thrower.posX, thrower.posY + (double)thrower.getEyeHeight() - 0.1D, thrower.posZ);
	}
	
	@Override
    protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!this.worldObj.isRemote && !source.equals(DamageSource.fall)) {
			this.worldObj.playSound(null, this.getPosition(), ModSoundEvents.balloonPop, SoundCategory.NEUTRAL, 
					0.8f, this.worldObj.rand.nextFloat()+0.3f);
			this.entityDropItem(new ItemStack(ModItems.balloonDeflated), 0);
			this.setDead();
		}

		return true;
	}

	@Override
	protected void updateLeashedState() {
		super.updateLeashedState();

		if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj) {
			Entity entity = this.getLeashedToEntity();
			float f = this.getDistanceToEntity(entity);

			if (f > 2.0F)
				this.getNavigator().tryMoveToEntityLiving(entity, 1.0D);

			if (f > 4.0F) {
				double d0 = (entity.posX - this.posX) / (double)f;
				double d1 = (entity.posY - this.posY) / (double)f;
				double d2 = (entity.posZ - this.posZ) / (double)f;
				this.motionX += d0 * Math.abs(d0) * 0.4D;
				this.motionY += d1 * Math.abs(d1) * 0.03D;
				this.motionZ += d2 * Math.abs(d2) * 0.4D;

				if (this.getLeashedToEntity() != null && this.posY > this.getLeashedToEntity().posY+3 &&
						!(this.getLeashedToEntity() instanceof EntityPlayer && ((EntityPlayer)this.getLeashedToEntity()).capabilities.isFlying)) {
					if (this.getLeashedToEntity().isSneaking() && this.getLeashedToEntity().motionY < 0d && !this.getLeashedToEntity().onGround) 
						this.getLeashedToEntity().motionY += 0.01d;
					else if (!this.getLeashedToEntity().isSneaking())
						this.getLeashedToEntity().motionY += 0.05d;
					this.getLeashedToEntity().fallDistance = 0;
				}
			}

			if (f > 40.0F)
				this.clearLeashed(true, true);
		}
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (!this.hasNoGravity())
			this.motionY -= this.getLeashed() ? -0.01d : gravity;

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98D;
		this.motionY *= 0.98D;
		this.motionZ *= 0.98D;

		if (this.onGround) {
			this.motionX *= 0.7D;
			this.motionZ *= 0.7D;
			this.motionY *= -0.5D;
		}

		this.handleWaterMovement();

		List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.<Entity>getTeamCollisionPredicate(this));

		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); ++i) {
				Entity entity = (Entity)list.get(i);
				this.applyEntityCollision(entity);
			}
		}

		this.updateLeashedState();
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
	public void applyEntityCollision(Entity entityIn) {
        if (!this.isRidingSameEntity(entityIn) && !entityIn.noClip && !this.noClip) {
        	this.motionY += 0.02d;
        	this.rotationYaw += (entityIn.worldObj.rand.nextFloat()-0.5f)*100f;
        	this.prevRotationYaw = this.rotationYaw;
        }
		
		super.applyEntityCollision(entityIn);
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
