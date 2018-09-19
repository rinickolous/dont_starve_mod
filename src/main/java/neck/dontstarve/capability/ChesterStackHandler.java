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
}
