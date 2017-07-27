package furgl.stupidThings.common.sound;

import furgl.stupidThings.common.StupidThings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModSoundEvents {

	public static final SoundEvent BALLOON_INFLATE = new SoundEvent(new ResourceLocation(StupidThings.MODID, "balloon_inflate")).setRegistryName("balloon_inflate");
	public static final SoundEvent BALLOON_POP = new SoundEvent(new ResourceLocation(StupidThings.MODID, "balloon_pop")).setRegistryName("balloon_pop");
	public static final SoundEvent RUBBER_CHICKEN = new SoundEvent(new ResourceLocation(StupidThings.MODID, "rubber_chicken")).setRegistryName("rubber_chicken");
	public static final SoundEvent MINE_TURTLE = new SoundEvent(new ResourceLocation(StupidThings.MODID, "mine_turtle")).setRegistryName("mine_turtle");
	public static final SoundEvent WORLDS_SMALLEST_VIOLIN = new SoundEvent(new ResourceLocation(StupidThings.MODID, "worlds_smallest_violin")).setRegistryName("worlds_smallest_violin");

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().register(BALLOON_INFLATE);
			event.getRegistry().register(BALLOON_POP);
			event.getRegistry().register(RUBBER_CHICKEN);
			event.getRegistry().register(MINE_TURTLE);
			event.getRegistry().register(WORLDS_SMALLEST_VIOLIN);
		}

	}
	
}