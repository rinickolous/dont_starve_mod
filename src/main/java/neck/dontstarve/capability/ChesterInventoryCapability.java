package neck.dontstarve.capability;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ChesterInventoryCapability
{
	@CapabilityInject(ChesterInventory.class)
	public static Capability<ChesterInventory> CAPABILITY;
	
	public void register()
	{
		CapabilityManager.INSTANCE.register(ChesterInventory.class, new StorageHelper(), new DefaultInstanceFactory());
	}
	
	public static class StorageHelper implements Capability.IStorage<ChesterInventory>
	{
		public NBTBase writeNBT(Capability<ChesterInventory> capability, ChesterInventory instance, EnumFacing side)
		{
			return instance.writeData();
		}
		
		public void readNBT(Capability<ChesterInventory> capability, ChesterInventory instance, EnumFacing side, NBTBase nbt)
		{
			instance.readData(nbt);
		}
	}
	
	public static class DefaultInstanceFactory implements Callable<ChesterInventory>
	{
		public ChesterInventory call() throws Exception
		{
			return new ChesterInventory();
		}
	}
}
