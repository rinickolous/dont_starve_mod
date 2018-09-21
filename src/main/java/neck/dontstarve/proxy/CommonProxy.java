package neck.dontstarve.proxy;

import neck.dontstarve.item.ItemEyeBone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonProxy
{
	public void registerItemRenderer(Item item, int meta, String id)
	{
		// blank
	}
	
    public World getClientWorld() {
        return null;
    }
    
    public void registerRenders() {
    }
    
    public EntityPlayer getClientPlayer()
    {
        return null;
    }
}
