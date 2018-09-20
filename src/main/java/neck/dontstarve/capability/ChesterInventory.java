package neck.dontstarve.capability;

import neck.dontstarve.entity.EntityChester;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class ChesterInventory
{
	private ChesterStackHandler inventory;
	private String customName = null;
	private String type = "default";
	private EntityChester instance;
	
	public ChesterInventory()
	{
		int slotNum = 12;
		this.inventory = new ChesterStackHandler(slotNum);
	}

	public ItemStackHandler getInventoryHandler()
	{
		return this.inventory;
	}
	
	public void setStackInSlot(int index, ItemStack stack)
	{
		this.inventory.setStackInSlot(index, stack);
	}
	
	public NBTBase writeData()
	{
		NBTTagCompound compound = this.inventory.serializeNBT();		
		return compound;
	}
	
	public void readData(NBTBase nbt)
	{
		NBTTagCompound compound = (NBTTagCompound)nbt;
		this.inventory.deserializeNBT((NBTTagCompound)nbt);
	}
	
	public EntityChester getInstnace()
	{
		return this.instance;
	}
	
	public ChesterStackHandler getInventory()
	{
		return this.inventory;
	}
}
