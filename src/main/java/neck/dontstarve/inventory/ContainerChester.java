package neck.dontstarve.inventory;

import neck.dontstarve.capability.ChesterInventory;
import neck.dontstarve.capability.ChesterInventoryCapability;
import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.inventory.slots.SlotChester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerChester extends Container
{
	final int invCollumns;
	
	public ContainerChester(final EntityPlayer player, EntityChester chester)
	{
		System.out.println("BREAK_"+chester.getType());
		ChesterInventory chesterInventory= (ChesterInventory)chester.getCapability(ChesterInventoryCapability.CAPABILITY, null);
		if(chester.getType().equals("shadow")) this.invCollumns = 4;
		else this.invCollumns = 3;
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < this.invCollumns; ++x)
			{
				this.addSlotToContainer(new SlotChester(chesterInventory, x * 3+ y, 80 + x * 18, 18 + y * 18));
			}
		}
		
		for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9;	 ++x)
            {
                this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
	}
	
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < (this.invCollumns * 3))
			{
				if (!this.mergeItemStack(itemstack1, this.invCollumns * 3, this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, this.invCollumns * 3, false))
			{
				return ItemStack.EMPTY;
			}
			
			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}
}
