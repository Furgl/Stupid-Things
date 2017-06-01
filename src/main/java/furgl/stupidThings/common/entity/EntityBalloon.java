package furgl.stupidThings.common.entity;

import furgl.stupidThings.common.StupidThings;
import furgl.stupidThings.common.item.ModItems;
import furgl.stupidThings.common.sound.ModSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityBalloon extends EntityThrowable {

	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityBalloon.class, DataSerializers.VARINT);
	private static double balloonGravity = 0.005d;

	public EntityBalloon(World world) {
		super(world);
		this.gravity = 0;
		this.bounceMultiplier = 0.5f;
		this.setSize(0.7F, 1.3F);
	}

	public EntityBalloon(World world, EntityLivingBase thrower, int color) {
		this(world);
		this.setPosition(thrower.posX, thrower.posY + (double)thrower.getEyeHeight() - 0.1D, thrower.posZ);
		this.dataManager.set(COLOR, color);
		this.gravity = 0;
		this.bounceMultiplier = 0.5f;
		this.setSize(0.7F, 1.3F);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		if (target.entityHit instanceof EntityBalloon)
			return new ItemStack(ModItems.balloon, 1, target.entityHit.getDataManager().get(COLOR));
		else
			return null;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(COLOR, 0);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("color", this.getColor());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.dataManager.set(COLOR, nbt.getInteger("color"));
	}

	public int getColor() {
		return this.dataManager.get(COLOR).intValue();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!this.worldObj.isRemote && !source.equals(DamageSource.fall)) {
			this.worldObj.playSound(null, this.getPosition(), ModSoundEvents.balloonPop, SoundCategory.NEUTRAL, 
					0.8f, this.worldObj.rand.nextFloat()+0.3f);
			if (ModItems.balloonDeflated != null && !this.isDead)
				this.entityDropItem(new ItemStack(ModItems.balloonDeflated, 1, this.getColor()), 0);
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
						this.getLeashedToEntity().motionY += 0.08d;
					this.getLeashedToEntity().fallDistance = 0;

					// reset floatingTickCount so player isn't kicked for flying
					try {
						if (this.getLeashedToEntity() instanceof EntityPlayerMP) { 
							ReflectionHelper.setPrivateValue(NetHandlerPlayServer.class, 
									((EntityPlayerMP) this.getLeashedToEntity()).connection, 0, 
									"field_147365_f", "floatingTickCount");
						}
					}
					catch (Exception e) {
						StupidThings.logger.warn("EntityBalloon unable to reset floatingTickCount: ", e);
					}
				}
			}

			if (f > 40.0F)
				this.clearLeashed(true, true);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (ModItems.balloon == null)
			this.setDead();

		if (!this.hasNoGravity())
			this.motionY -= this.getLeashed() ? -0.01d : balloonGravity;

		if (this.onGround) {
			this.motionX *= 0.7D;
			this.motionZ *= 0.7D;
		}

		this.updateLeashedState();
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
}