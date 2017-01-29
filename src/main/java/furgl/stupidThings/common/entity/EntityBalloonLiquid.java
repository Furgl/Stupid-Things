package furgl.stupidThings.common.entity;

import java.util.List;

import furgl.stupidThings.common.sound.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBalloonLiquid extends EntityBalloon {

	private static final DataParameter<Integer> LIQUID = EntityDataManager.<Integer>createKey(EntityBalloonLiquid.class, DataSerializers.VARINT);
	private BlockLiquid liquid;

	public EntityBalloonLiquid(World world) {
		super(world);
		this.gravity = 0.05d;
		this.setSize(0.6F, 0.6F);
	}

	public EntityBalloonLiquid(BlockLiquid liquid, World world, EntityPlayer player, int color) {
		super(world, player, color);
		this.liquid = liquid;
		this.dataManager.set(LIQUID, Block.getIdFromBlock(liquid));
		this.gravity = 0.05d;
		this.setSize(0.6F, 0.6F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(LIQUID, 0);
	}

	@Override
	public void setLeashedToEntity(Entity entityIn, boolean sendAttachNotification) { }

	@Override
	public void onUpdate() {
		super.onUpdate();

		//angle down over time
		this.prevRotationYaw = this.rotationYaw;
		if (MathHelper.abs_max(motionX, motionZ)+motionY < 0.5D) 
			if (Math.abs(180-this.rotationYaw) <= Math.abs(-180-this.rotationYaw)) { //closer to 180
				if (this.rotationYaw > 180) 
					this.rotationYaw = Math.max(this.rotationYaw-10, 180);
				else if (this.rotationYaw < 180)
					this.rotationYaw = Math.min(this.rotationYaw+10, 180);
			}
			else //closer to -180
				if (this.rotationYaw > -180) 
					this.rotationYaw = Math.max(this.rotationYaw-10, -180);
				else if (this.rotationYaw < -180)
					this.rotationYaw = Math.min(this.rotationYaw+10, -180);

		//set liquid if null
		if (liquid == null) {
			Block block = Block.getBlockById(this.dataManager.get(LIQUID).intValue());
			if (block instanceof BlockLiquid)
				this.liquid = (BlockLiquid) block;
		}

		//spawn particles
		if (this.worldObj.isRemote && this.liquid != null) {
			if (this.liquid == Blocks.WATER)
				this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX+(worldObj.rand.nextDouble()-0.5d)*0.4d, 
						this.posY+worldObj.rand.nextDouble()*0.4d, this.posZ+(worldObj.rand.nextDouble()-0.5d)*0.4d, 
						0, 0, 0, new int[0]);
			else if (this.liquid == Blocks.LAVA)
				this.worldObj.spawnParticle(EnumParticleTypes.FLAME, this.posX+(worldObj.rand.nextDouble()-0.5d)*0.4d, 
						this.posY+worldObj.rand.nextDouble()*0.4d, this.posZ+(worldObj.rand.nextDouble()-0.5d)*0.4d, 
						0, 0, 0, new int[0]);
		}

		//raytrace for entity collision
		Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null)
			vec3d1 = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);

		Entity entity = null;
		List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0D));
		double d0 = 0.0D;
		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity)list.get(i);

			if (entity1.canBeCollidedWith()) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.30000001192092896D);
				RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

				if (raytraceresult1 != null) {
					double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		if (entity != null) 
			raytraceresult = new RayTraceResult(entity);

		if (raytraceresult != null) {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.worldObj.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL)
				this.setPortal(raytraceresult.getBlockPos());
			else if (this.ticksExisted > 2)
				this.attackEntityFrom(DamageSource.generic, 1);
		}

		//pop if hitting block
		if (this.onGround || this.isCollidedHorizontally) 
			this.attackEntityFrom(DamageSource.generic, 1);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!this.worldObj.isRemote && !source.equals(DamageSource.fall) && 
				!source.equals(DamageSource.inFire) && !source.equals(DamageSource.onFire)) {
			this.worldObj.playSound(null, this.getPosition(), ModSoundEvents.balloonPop, SoundCategory.NEUTRAL, 
					0.8f, this.worldObj.rand.nextFloat()+0.3f);
			this.setDead();
			if (this.liquid != null) {
				BlockPos[] positions = new BlockPos[] {this.getPosition(), this.getPosition().up(), this.getPosition().down()};
				for (BlockPos pos : positions)	
					if (this.worldObj.isAirBlock(pos) || 
							this.worldObj.getBlockState(pos).getBlock().isReplaceable(worldObj, pos)) {
						this.worldObj.setBlockState(pos, this.liquid.getStateFromMeta(pos == getPosition() ? 15 : 1));
						this.worldObj.notifyBlockOfStateChange(pos, this.liquid);
					}
			}
			return true;
		}
		return false;
	}
}