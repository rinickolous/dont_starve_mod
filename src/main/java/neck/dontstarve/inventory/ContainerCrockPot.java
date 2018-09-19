package neck.dontstarve.inventory;

import java.util.List;

import com.google.common.collect.Lists;

import neck.dontstarve.inventory.slots.SlotCrockPotFuel;
import neck.dontstarve.inventory.slots.SlotCrockPotOutput;
import neck.dontstarve.item.crafting.CrockPotRecipes;
import neck.dontstarve.tileentity.TileEntityCrockPot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrockPot extends Container
{
	private final IInventory tileentity;
	private int cookTime;
	private int totalCookTime;
	private int burnTime;
	private int currentItemBurnTime;
	
	public ContainerCrockPot(InventoryPlayer playerInventory, IInventory crockpotInventory)
	{
		this.tileentity = crockpotInventory;
		// food inputs
		for (int i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new Slot(crockpotInventory, i, 53 + 18 * i, 17));
		}
		this.addSlotToContainer(new SlotCrockPotFuel(crockpotInventory, 4, 26, 53));
		
		this.addSlotToContainer(new SlotCrockPotOutput(playerInventory.player, crockpotInventory, 5, 79, 49));
		
		for (int y = 0; y < 3; ++y)
        {
            for (int x = 0; x < 9;	 ++x)
            {
                this.addSlotToContainer(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
	}
	
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileentity);
	}
	
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener listener = this.listeners.get(i);

            if(this.cookTime != this.tileentity.getField(2)) listener.sendWindowProperty(this, 2, this.tileentity.getField(2));
			if(this.burnTime != this.tileentity.getField(0)) listener.sendWindowProperty(this, 0, this.tileentity.getField(0));
			if(this.currentItemBurnTime != this.tileentity.getField(1)) listener.sendWindowProperty(this, 1, this.tileentity.getField(1));
			if(this.totalCookTime != this.tileentity.getField(3)) listener.sendWindowProperty(this, 3, this.tileentity.getField(3));
        }

        this.cookTime = this.tileentity.getField(2);
        this.burnTime = this.tileentity.getField(0);
        this.currentItemBurnTime = this.tileentity.getField(1);
        this.totalCookTime = this.tileentity.getField(3);
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
    	this.tileentity.setField(id, data);
    }
    
    public boolean canInteractWith(EntityPlayer playerIn)
    {
    	return this.tileentity.isUsableByPlayer(playerIn);
    }
    
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
    	ItemStack itemstack = ItemStack.EMPTY;
    	Slot slot = this.inventorySlots.get(index);
    	
    	if (slot != null && slot.getHasStack())
    	{
    		ItemStack stack1 = slot.getStack();
    		itemstack = stack1.copy();
    		
    		if (index == 5)
    		{
    			if (!this.mergeItemStack(stack1, 3, 39, true))
    			{
    				return ItemStack.EMPTY;
    			}
    			
    			slot.onSlotChange(stack1, itemstack);
    		}
    		else if (index != 0 && index != 1 && index != 2 && index != 3)
    		{
    			
    			if (CrockPotRecipes.instance().isItemInRecipes(stack1))
    			{
    				if (!this.mergeItemStack(stack1, 0, 4, false))
    				{
    					return ItemStack.EMPTY;
    				}
    			}
    			else if (TileEntityCrockPot.isItemFuel(stack1))
        		{
        			if (!this.mergeItemStack(stack1, 4, 5, false)) return ItemStack.EMPTY;
        		}
    			else if (index >= 6 && index < 33)
    			{
    				if(!this.mergeItemStack(stack1, 33, 42, false)) return ItemStack.EMPTY;
    			}
    			else if (index >= 6 && index < 42 && !this.mergeItemStack(stack1, 6, 33, false)) return ItemStack.EMPTY;
    		}
    		else if (!this.mergeItemStack(stack1, 6, 42, false))
    		{
    			return ItemStack.EMPTY;
    		}
    		
    		if (stack1.isEmpty())
    		{
    			slot.putStack(ItemStack.EMPTY);
    		}
    		else
    		{
    			slot.onSlotChanged();
    		}
    		
    		if (stack1.getCount() == itemstack.getCount())
    		{
    			return ItemStack.EMPTY;
    		}
    		
    		slot.onTake(playerIn, stack1);
    	}
    	
    	return itemstack;
    }
}
