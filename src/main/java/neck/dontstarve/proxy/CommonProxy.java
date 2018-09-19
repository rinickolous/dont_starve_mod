package neck.dontstarve.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
    
    public EntityPlayer getClientPlayer() {
        return null;
    }
    
    public void registerKey() {
    }
    
    public ResourceLocation getFriendSkin() {
        return null;
    }
}
