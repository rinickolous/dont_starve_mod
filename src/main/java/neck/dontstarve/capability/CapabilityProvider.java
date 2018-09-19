package neck.dontstarve.capability;

import javax.annotation.Nullable;

import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.util.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

//public class CapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
public class CapabilityProvider implements ICapabilityProvider
{
	final ChesterInventory chesterInv = new ChesterInventory();
	public static final ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID, "chester_inventory");
	
	public CapabilityProvider(EntityChester chester)
	{
		this.chesterInv.setInstance(chester);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == ChesterInventoryCapability.CAPABILITY)
		{
			return true;
		}
		return false;
	}

	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == ChesterInventoryCapability.CAPABILITY)
		{
			return (T) this.chesterInv;
		}
		return null;
	}

//	@Override
//	public NBTTagCompound serializeNBT()
//	{
//		return (NBTTagCompound)ChesterInventoryCapability.CAPABILITY.writeNBT(this.chesterInv, null);
//	}
//
//	@Override
//	public void deserializeNBT(NBTTagCompound nbt)
//	{
//		ChesterInventoryCapability.CAPABILITY.readNBT(this.chesterInv, null, nbt);
//	}
}
