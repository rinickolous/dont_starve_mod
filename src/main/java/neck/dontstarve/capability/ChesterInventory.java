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
	
	private float Health = 20.0F;
	
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
		
		if ((getName() != null) && (getName().length() > 0))
		{
			compound.setString("customName", getName());
		}
		compound.setString("type", getType());
		compound.setFloat("health", getHealth());
		
		return compound;
	}
	
	public void readData(NBTBase nbt)
	{
		NBTTagCompound compound = (NBTTagCompound)nbt;
		this.inventory.deserializeNBT((NBTTagCompound)nbt);
		if (compound.hasKey("customName"))
		{
			setName(compound.getString("customName"));
		}
		setType(compound.getString("type"));
		setHealth(compound.getFloat("health"));
	}
	
	public String getName()
	{
		return this.customName;
	}
	
	public void setName(String name)
	{
		this.customName = name;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public float getHealth()
	{
		return this.Health;
	}
	
	public void setHealth(float health)
	{
		this.Health = health;
	}
	
	public void setInstance(EntityChester chester)
	{
		this.instance = chester;
	}
	
	public EntityChester getInstnace()
	{
		return this.instance;
	}
}
