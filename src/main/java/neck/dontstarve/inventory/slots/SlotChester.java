package neck.dontstarve.inventory.slots;

import neck.dontstarve.capability.ChesterInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class SlotChester extends SlotItemHandler
{
	private ChesterInventory chesterInventory = null;
	
	public SlotChester(ChesterInventory inventory, int index, int posX, int posY)
	{
		super(inventory.getInventoryHandler(), index, posX, posY);
		this.chesterInventory = inventory;
	}
	
	public ChesterInventory getChesterInventory()
	{
		return this.chesterInventory;
	}
	
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);
	}
	
	public int getSlotStackLimit()
	{
		return 64;
	}
}
