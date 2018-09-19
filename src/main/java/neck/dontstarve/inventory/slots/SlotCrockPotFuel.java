package neck.dontstarve.inventory.slots;

import neck.dontstarve.tileentity.TileEntityCrockPot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCrockPotFuel extends Slot
{

	public SlotCrockPotFuel(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
		
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityCrockPot.isItemFuel(stack);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return super.getItemStackLimit(stack);
	}
	
}
