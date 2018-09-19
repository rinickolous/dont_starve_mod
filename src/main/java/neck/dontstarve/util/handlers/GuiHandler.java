package neck.dontstarve.util.handlers;

import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.gui.GuiChester;
import neck.dontstarve.gui.GuiCrockPot;
import neck.dontstarve.inventory.ContainerChester;
import neck.dontstarve.inventory.ContainerCrockPot;
import neck.dontstarve.tileentity.TileEntityCrockPot;
import neck.dontstarve.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		EntityChester chester = (EntityChester)world.getEntityByID(x);
		if (ID == Reference.GUI_CROCK_POT) return new ContainerCrockPot(player.inventory, (TileEntityCrockPot)world.getTileEntity(new BlockPos(x,y,z)));
		if (ID == Reference.GUI_CHESTER) return new ContainerChester(player, chester);
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		EntityChester chester = (EntityChester)world.getEntityByID(x);
		if (ID == Reference.GUI_CROCK_POT) return new GuiCrockPot(player.inventory, (TileEntityCrockPot)world.getTileEntity(new BlockPos(x,y,z)));
		if (ID == Reference.GUI_CHESTER) return new GuiChester(player, chester);
		return null;
	}
}