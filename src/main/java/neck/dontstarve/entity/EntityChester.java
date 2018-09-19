package neck.dontstarve.entity;

import java.util.UUID;

import neck.dontstarve.Main;
import neck.dontstarve.init.ItemInit;
import neck.dontstarve.util.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityChester extends EntityTameable
{
	private String type;
	private EntityPlayer owner;
    private int index;
	
	public EntityChester(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 0.9F);
		if (this.type == null)
		{
			this.setType("normal");
		}
	}
		
	@Override
	public float getEyeHeight()
	{
		return 0.8F;
	}

	private int getInventorySize()
	{
		return 12;
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
	
	public String getType()
	{
		return this.type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setString("Type", this.getType());
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
        this.setType(compound.getString("Type"));
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
    
}
