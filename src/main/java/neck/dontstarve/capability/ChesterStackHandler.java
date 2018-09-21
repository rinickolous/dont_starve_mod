package neck.dontstarve.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public class ChesterStackHandler extends ItemStackHandler
{
	public static int slots;
	
	public ChesterStackHandler(int slotNum)
	{
		super(slotNum);
	}
	
	public NonNullList<ItemStack> getStacks()
	{
		return this.stacks;
	}
	
	public void clear()
	{
		for (ItemStack stack : this.getStacks())
		{
			stack = ItemStack.EMPTY;
		}
	}
}
