package furgl.stupidThings.common.sound;

import furgl.stupidThings.common.item.ItemWorldsSmallestViolin;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundWorldsSmallestViolin extends MovingSound {

	private EntityPlayer player;

	public SoundWorldsSmallestViolin(EntityPlayer player) {
		super(ModSoundEvents.WORLDS_SMALLEST_VIOLIN, SoundCategory.PLAYERS);
		this.player = player;
		this.xPosF = (float) player.posX;
		this.yPosF = (float) player.posY;
		this.zPosF = (float) player.posZ;
	}

	@Override
	public void update() {
		if (player == null || player.getActiveItemStack() == null || 
				!(player.getActiveItemStack().getItem() instanceof ItemWorldsSmallestViolin))
			this.donePlaying = true;
		else {
			this.xPosF = (float) player.posX;
			this.yPosF = (float) player.posY;
			this.zPosF = (float) player.posZ;
		}
	}

}
