package furgl.stupidThings.common.sound;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSoundEvents {

	public static SoundEvent balloonInflate;

	public static void preInit() {
		balloonInflate = registerSound("balloon_inflate");
	}
	
	private static SoundEvent registerSound(String soundName) {
		ResourceLocation loc = new ResourceLocation(StupidThings.MODID, soundName);
		SoundEvent sound = new SoundEvent(loc);
		GameRegistry.register(sound, loc);
		return sound;
	}
}