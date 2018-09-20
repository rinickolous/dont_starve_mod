package neck.dontstarve.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import neck.dontstarve.Main;
import neck.dontstarve.capability.ChesterInventory;
import neck.dontstarve.capability.ChesterInventoryCapability;
import neck.dontstarve.init.ItemInit;
import neck.dontstarve.util.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntityChester extends EntityTameable implements ICapabilitySerializable<NBTTagCompound>
{
	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityChester.class, DataSerializers.BYTE);
//	private String type;
	private EntityPlayer owner;
    private int index;
    final ChesterInventory chesterInv = new ChesterInventory();
    
    @CapabilityInject(ChesterInventory.class)
	public static Capability<ChesterInventory> CAPABILITY;
	
	public EntityChester(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 0.9F);
	}
	
	public void entityInit()
	{
		super.entityInit();
		this.dataManager.register(TYPE,(byte) 0);
	}
		
	@Override
	public float getEyeHeight()
	{
		return 0.8F;
	}

	private int getSizeInventory()
	{
		if (this.getType() == EnumChesterType.SHADOW) return 12;
		else return 9;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}
    
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof ItemFood)
			{
				if (getHealth() < getMaxHealth())
				{
					heal(((ItemFood)stack.getItem()).getHealAmount(stack));
					stack.shrink(1);
					if (stack.getCount() == 0) stack = ItemStack.EMPTY;
					if(this.world.isRemote)
					{
						this.world.spawnParticle(EnumParticleTypes.HEART, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
				return true;
			}
		}
		else
		{
//			getNavigator().setPath(getNavigator().getPathToEntityLiving(getOwner()), 1.0D);
			player.openGui(Main.instance, Reference.GUI_CHESTER, world, this.getEntityId(), (int)player.posY, (int)player.posZ);
		
		return true;
		}
		
		return true;
	}
	
	public EntityPlayer getOwner()
	{
		for (EntityPlayer player: world.playerEntities)
		{
			for (ItemStack stack: player.inventory.mainInventory)
			{
				if (stack.getItem() == ItemInit.EYE_BONE)
					return player;
			}
		}
		return null;
	}
	
	public EnumChesterType getType()
	{
		return EnumChesterType.values()[this.dataManager.get(TYPE)];
	}
	
	public void setType(EnumChesterType type)
	{
		this.dataManager.set(TYPE,(byte) type.ordinal());
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setByte("Type", (byte) this.getType().ordinal());
        NBTTagList inventory = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i)
        {
        	ItemStack stack = this.chesterInv.getInventory().getStackInSlot(i);
        	
        	if (!stack.isEmpty())
        	{
        		NBTTagCompound nbttagcompound = new NBTTagCompound();
        		nbttagcompound.setByte("Slot", (byte)i);
        		stack.writeToNBT(nbttagcompound);
        		inventory.appendTag(nbttagcompound);
        	}
        }
        compound.setTag("Items", inventory);
        if (this.getOwner() == null)
        {
            compound.setString("OwnerUUID", "");
        }
        else
        {
            compound.setString("OwnerUUID", this.getOwner().getUniqueID().toString());
        }
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setType(EnumChesterType.values()[compound.getByte("Type")]);
        NBTTagList inventory = compound.getTagList("Items", 10);
        for(int i = 0; i < inventory.tagCount(); ++i)
        {
        	NBTTagCompound nbttagcompound = inventory.getCompoundTagAt(i);
        	int j = nbttagcompound.getByte("Slot");
        	this.chesterInv.setStackInSlot(j, new ItemStack(nbttagcompound));
        	
        }
        String s;
        if (compound.hasKey("OwnerUUID", 8))
        {
            s = compound.getString("OwnerUUID");
        }
        else
        {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty())
        {
            try
            {
                this.setOwnerId(UUID.fromString(s));
                this.setTamed(true);
            }
            catch (Throwable var4)
            {
                this.setTamed(false);
            }
        }
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

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == ChesterInventoryCapability.CAPABILITY)
		{
			return (T) this.chesterInv;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)ChesterInventoryCapability.CAPABILITY.writeNBT(this.chesterInv, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ChesterInventoryCapability.CAPABILITY.readNBT(this.chesterInv, null, nbt);
	}
}

