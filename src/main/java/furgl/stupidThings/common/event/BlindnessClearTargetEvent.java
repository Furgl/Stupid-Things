package furgl.stupidThings.common.event;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.MobEffects;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlindnessClearTargetEvent {

	@SubscribeEvent
	public void setTarget(LivingSetAttackTargetEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityLiving && 
				event.getEntityLiving().getActivePotionEffect(MobEffects.BLINDNESS) != null &&
				event.getEntityLiving().getActivePotionEffect(MobEffects.BLINDNESS).getDuration() > 0 &&
				event.getEntityLiving().isNonBoss() &&
				((EntityLiving)event.getEntity()).getAttackTarget() != null)
			((EntityLiving)event.getEntity()).setAttackTarget(null);
	}
	
}