package furgl.stupidThings.common.entity;


import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityBlockBomb extends EntityThrowable {

	public static final DataParameter<Optional<IBlockState>> STATE = EntityDataManager.<Optional<IBlockState>>createKey(EntityBlockBomb.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private IBlockState state;
	private ItemStack[] items;

	public EntityBlockBomb(World world) {
		super(world);
		this.setSize(0.6F, 0.6F);
		this.gravity = 0.1f;
	}

	public EntityBlockBomb(World world, EntityLivingBase thrower, ItemStack[] items) {
		super(world, thrower);
		this.setSize(0.6F, 0.6F);
		this.gravity = 0.1f;
		this.state = ((ItemBlock)items[0].getItem()).block.getStateForPlacement(worldObj, BlockPos.ORIGIN, EnumFacing.UP, 0, 0, 0, items[0].getMetadata(), thrower, items[0]);
		this.dataManager.set(STATE, Optional.of(state));
		this.items = items;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(STATE, Optional.absent());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);

		// write items to nbt
		NBTTagList list = new NBTTagList();
		for (ItemStack stack : items)
			list.appendTag(stack.writeToNBT(new NBTTagCompound()));
		nbt.setTag("items", list);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);

		// read items from nbt
		NBTTagList list = nbt.getTagList("items", 10);
		if (list != null && !list.hasNoTags()) {
			items = new ItemStack[list.tagCount()];
			for (int i=0; i<list.tagCount(); ++i)
				if (list.get(i) instanceof NBTTagCompound)
					items[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) list.get(i));
		}

		this.state = ((ItemBlock)items[0].getItem()).block.getStateForPlacement(worldObj, BlockPos.ORIGIN, EnumFacing.UP, 0, 0, 0, items[0].getMetadata(), thrower, items[0]);
		this.dataManager.set(STATE, Optional.of(state));
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source) {
		return source != DamageSource.outOfWorld;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		// get state for easy access
		if (state == null && this.dataManager.get(STATE).isPresent())
			state = this.dataManager.get(STATE).get();

		// spawn particles
		if (this.worldObj.isRemote && state != null) {
			for (int i=0; i<1; i++)
				this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, 
						posX+worldObj.rand.nextDouble()/2-0.25d, 
						posY+worldObj.rand.nextDouble()/2-0.25d, 
						posZ+worldObj.rand.nextDouble()/2-0.25d, 
						0, 0, 0, new int[] {Block.getStateId(state)});
			for (int i=0; i<1; i++)
				this.worldObj.spawnParticle(EnumParticleTypes.FALLING_DUST, 
						posX+worldObj.rand.nextDouble()/2-0.25d, 
						posY+worldObj.rand.nextDouble()/2-0.25d, 
						posZ+worldObj.rand.nextDouble()/2-0.25d, 
						0, 0, 0, new int[] {Block.getStateId(state)});
		}

		// spawn blocks on collision
		if (this.onGround || this.isCollidedHorizontally) {
			// explosion for particles/sound
			Explosion explosion = new Explosion(worldObj, this, posX, posY, posZ, 1, true, true);
			explosion.doExplosionB(true);

			if (!this.worldObj.isRemote) {
				// spawn blocks
				for (int radius=0; radius<8; ++radius) 
					for (int x=-radius/2; x<=radius/2; ++x)
						for (int y=-radius/2; y<=radius/2; ++y)
							for (int z=-radius/2; z<=radius/2; ++z) 
								if (items != null) {
									BlockPos pos = this.getPosition().add(x, y, z);
									if (this.worldObj.rand.nextBoolean() &&
											(this.worldObj.isAirBlock(pos) || this.worldObj.getBlockState(pos).getBlock().isReplaceable(worldObj, pos)) && 
											(!this.worldObj.isAirBlock(pos.up()) ||	!this.worldObj.isAirBlock(pos.down()) ||
													!this.worldObj.isAirBlock(pos.east()) || !this.worldObj.isAirBlock(pos.west()) ||
													!this.worldObj.isAirBlock(pos.north()) || !this.worldObj.isAirBlock(pos.south()))) {
										boolean itemFound = false;
										for (int i=0; i<items.length; ++i)
											if (items[i] != null && items[i].stackSize > 0 && items[i].getItem() instanceof ItemBlock &&
											thrower instanceof EntityPlayer && ((EntityPlayer) this.thrower).canPlayerEdit(pos, EnumFacing.UP, items[i])) {
												IBlockState state = ((ItemBlock)items[i].getItem()).block.getStateForPlacement(worldObj, pos, EnumFacing.UP, 0, 0, 0, items[i].getMetadata(), thrower, items[i]);
												if (((ItemBlock)items[i].getItem()).placeBlockAt(items[i], (EntityPlayer) thrower, worldObj, pos, EnumFacing.UP, 0, 0, 0, state)) {
													itemFound = true;
													--items[i].stackSize;
													break;
												}
											}
											else
												items[i] = null;

										if (!itemFound)
											items = null;
									}
								}

				// spawn remaining items as entityitems
				if (items != null)
					for (ItemStack stack : items)
						if (stack != null) 
							this.entityDropItem(stack, 0.5f);

				this.setDead();
			}
		}
	}
}