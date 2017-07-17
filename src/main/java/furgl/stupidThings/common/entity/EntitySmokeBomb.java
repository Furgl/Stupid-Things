package furgl.stupidThings.common.entity;

import java.util.List;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntitySmokeBomb extends EntityThrowable {

	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntitySmokeBomb.class, DataSerializers.VARINT);

	public EntitySmokeBomb(World world) {
		super(world);
		this.setSize(0.3F, 0.3F);
		this.bounceMultiplier = 0.5f;
		this.gravity = 0.05f;
	}

	public EntitySmokeBomb(World world, EntityLivingBase thrower, int color) {
		super(world, thrower);
		this.setSize(0.3F, 0.3F);
		this.bounceMultiplier = 0.5f;
		this.gravity = 0.05f;
		this.dataManager.set(COLOR, color);
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
	public boolean isEntityInvulnerable(DamageSource source) {
		return source != DamageSource.OUT_OF_WORLD;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (this.onGround) {
			this.motionX *= 0.9D;
			this.motionZ *= 0.9D;
		}
		
		//kill after certain amount of time
		if (!this.world.isRemote && this.ticksExisted > 250)
			this.setDead();

		//play sound
		if (this.ticksExisted > 40 && this.ticksExisted % 4 == 0)
			this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 
					0.2f, 1);

		//spawn particles
		if (this.world.isRemote) {
			double speedModifier = Math.min(this.ticksExisted/90f, 1.5d);
			for (int i=0; i<2; i++)
				StupidThings.proxy.spawnParticlesSmokeCloud(world, this.getColor(), posX, posY, posZ, 
						(this.world.rand.nextDouble()-0.5d)*speedModifier, (this.world.rand.nextDouble()-0.5d)*speedModifier, 
						(this.world.rand.nextDouble()-0.5d)*speedModifier, (float) Math.min(this.ticksExisted/100f, 1d));
			if (this.ticksExisted < 40)
				for (int i=0; i<this.ticksExisted/2; i++)
					this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 
							(this.world.rand.nextDouble()-0.5d)*0.3d, (this.world.rand.nextDouble()-0.5d)*0.3d, 
							(this.world.rand.nextDouble()-0.5d)*0.3d, new int[0]);
			else {
				for (int i=0; i<3; i++)
					this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 
							(this.world.rand.nextDouble()-0.5d)*0.3d, (this.world.rand.nextDouble()-0.5d)*0.3d, 
							(this.world.rand.nextDouble()-0.5d)*0.3d, new int[0]);
			}
		}

		//give nearby entities blindness
		if (!this.world.isRemote && this.ticksExisted > 40) {
			double radius = Math.min(this.ticksExisted/30f, 6d);
			List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(radius), EntitySelectors.<Entity>getTeamCollisionPredicate(this));
			for (Entity entity : list)
				if (entity instanceof EntityLivingBase)
					((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
		}
	}
}