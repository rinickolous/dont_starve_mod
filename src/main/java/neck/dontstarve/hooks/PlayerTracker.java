package neck.dontstarve.hooks;

import neck.dontstarve.capability.CapabilityProvider;
import neck.dontstarve.entity.EntityChester;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerTracker
{
	public PlayerTracker()
	{
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onCapabilityAttach(AttachCapabilitiesEvent event)
	{
		Object entity = event.getObject();
		if ((entity instanceof EntityChester))
		{
			event.addCapability(CapabilityProvider.KEY, new CapabilityProvider((EntityChester)entity));
		}
	}
}
