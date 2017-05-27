package furgl.stupidThings.common.sound;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSoundEvents {

	public static SoundEvent balloonInflate;
	public static SoundEvent balloonPop;
	public static SoundEvent rubberChicken;
	public static SoundEvent mineTurtle;

	public static void preInit() {
		balloonInflate = registerSound("balloon_inflate");
		balloonPop = registerSound("balloon_pop");
		rubberChicken = registerSound("rubber_chicken");
		mineTurtle = registerSound("mine_turtle");
	}
	
	private static SoundEvent registerSound(String soundName) {
		ResourceLocation loc = new ResourceLocation(StupidThings.MODID, soundName);
		SoundEvent sound = new SoundEvent(loc);
		GameRegistry.register(sound, loc);
		return sound;
	}
}