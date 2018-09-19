package neck.dontstarve.util.handlers;

import neck.dontstarve.tileentity.TileEntityCrockPot;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler
{
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityCrockPot.class, "crock_pot");
	}
}
