package neck.dontstarve.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import neck.dontstarve.Main;
import neck.dontstarve.capability.ChesterInventory;
import neck.dontstarve.capability.ChesterInventoryCapability;
import neck.dontstarve.init.ItemInit;
import neck.dontstarve.item.ItemCane;
import neck.dontstarve.item.ItemEyeBone;
import neck.dontstarve.item.ItemNightmareFuel;
import neck.dontstarve.util.Reference;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityChester extends EntityTameable implements ICapabilitySerializable<NBTTagCompound>
{
	private static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityChester.class, DataSerializers.BYTE);
	private EntityPlayer owner;
    private int index;
    final ChesterInventory chesterInv = new ChesterInventory();
    
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    
    @CapabilityInject(ChesterInventory.class)
	public static Capability<ChesterInventory> CAPABILITY;
	
	public EntityChester(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 0.9F);
		this.jumpHelper = new EntityChester.JumpHelper(this);
		this.moveHelper = new EntityChester.MoveHelper(this);
	}
	
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.CAKE, false));
//		this.tasks.addTask(4, new EntityChester.AIAvoidEntity(this, EntityCow.class, 8.0F, 2.2D, 2.2D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
	}
	
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 1)
        {
            this.createRunningParticles();
            this.jumpDuration = 20;
            this.jumpTicks = 0;
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
	
    protected float getJumpUpwardsMotion()
    {
        if (!this.collidedHorizontally && (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.5D))
        {
            Path path = this.navigator.getPath();

            if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength())
            {
                Vec3d vec3d = path.getPosition(this);

                if (vec3d.y > this.posY + 0.5D)
                {
                    return 1.0F;
                }
            }

            return this.moveHelper.getSpeed() <= 0.6D ? 0.4F : 0.6F;
        }
        else
        {
            return 1.0F;
        }
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        super.jump();
        double d0 = this.moveHelper.getSpeed();

        if (d0 > 0.0D)
        {
            double d1 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d1 < 0.010000000000000002D)
            {
                this.moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }

        if (!this.world.isRemote)
        {
            this.world.setEntityState(this, (byte)1);
        }
    }
	
	public class JumpHelper extends EntityJumpHelper
	{
		private final EntityChester chester;
		private boolean canJump;
		
		public JumpHelper(EntityChester chester)
        {
            super(chester);
            this.chester = chester;
        }

        public boolean getIsJumping()
        {
            return this.isJumping;
        }

        public boolean canJump()
        {
            return this.canJump;
        }

        public void setCanJump(boolean canJumpIn)
        {
            this.canJump = canJumpIn;
        }

        /**
         * Called to actually make the entity jump if isJumping is true.
         */
        public void doJump()
        {
            if (this.isJumping)
            {
                this.chester.startJumping();
                this.isJumping = false;
            }
        }
    }
	
	static class MoveHelper extends EntityMoveHelper
    {
        private final EntityChester chester;
        private double nextJumpSpeed;

        public MoveHelper(EntityChester chester)
        {
            super(chester);
            this.chester = chester;
        }

        public void onUpdateMoveHelper()
        {
            if (this.chester.onGround && !this.chester.isJumping && !((EntityChester.JumpHelper)this.chester.jumpHelper).getIsJumping())
            {
                this.chester.setMovementSpeed(0.0D);
            }
            else if (this.isUpdating())
            {
                this.chester.setMovementSpeed(this.nextJumpSpeed);
            }

            super.onUpdateMoveHelper();
        }

        /**
         * Sets the speed and location to move to
         */
        public void setMoveTo(double x, double y, double z, double speedIn)
        {
            if (this.chester.isInWater())
            {
                speedIn = 1.5D;
            }

            super.setMoveTo(x, y, z, speedIn);

            if (speedIn > 0.0D)
            {
                this.nextJumpSpeed = speedIn;
            }
        }
    }
	
    public void setMovementSpeed(double newSpeed)
    {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }
    
    public void setJumping(boolean jumping)
    {
        super.setJumping(jumping);

        if (jumping)
        {
//            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }
	
    public void startJumping()
    {
        this.setJumping(true);
        this.jumpDuration = 20;
        this.jumpTicks = 0;
    }
    
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.jumpTicks != this.jumpDuration)
        {
            ++this.jumpTicks;
        }
        else if (this.jumpDuration != 0)
        {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
    }
    
    public void updateAITasks()
    {
    	if (this.currentMoveTypeDuration > 0)
        {
            --this.currentMoveTypeDuration;
        }

        if (this.onGround)
        {
            if (!this.wasOnGround)
            {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            EntityChester.JumpHelper entitychester$jumphelper = (EntityChester.JumpHelper)this.jumpHelper;

            if (!entitychester$jumphelper.getIsJumping())
            {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0)
                {
                    Path path = this.navigator.getPath();
                    Vec3d vec3d = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());

                    if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength())
                    {
                        vec3d = path.getPosition(this);
                    }

                    this.calculateRotationYaw(vec3d.x, vec3d.z);
                    this.startJumping();
                }
            }
            else if (!entitychester$jumphelper.canJump())
            {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround;
    }
    
    private void calculateRotationYaw(double x, double z)
    {
        this.rotationYaw = (float)(MathHelper.atan2(z - this.posZ, x - this.posX) * (180D / Math.PI)) - 90.0F;
    }
    
    private void enableJumpControl()
    {
        ((EntityChester.JumpHelper)this.jumpHelper).setCanJump(true);
    }
	
    private void disableJumpControl()
    {
    	((EntityChester.JumpHelper)this.jumpHelper).setCanJump(false);
    }
    
    private void checkLandingDelay()
    {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }
    
    private void updateMoveTypeDuration()
    {
        if (this.moveHelper.getSpeed() < 2.2D)
        {
            this.currentMoveTypeDuration = 10;
        }
        else
        {
            this.currentMoveTypeDuration = 1;
        }
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
		
		if (!player.world.isRemote)
		{
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
				}
				else if (stack.getItem() instanceof ItemEyeBone)
				{
					if (!stack.hasTagCompound())
					{
						stack.setTagCompound(new NBTTagCompound());
						stack.getTagCompound().setInteger("ChesterID", this.getEntityId());
						player.sendMessage(new TextComponentString("Bound your Eye Bone to Chester."));
					}
					else if (stack.getTagCompound().getInteger("ChesterID") == -1)
					{
						stack.getTagCompound().setInteger("ChesterID", this.getEntityId());
						player.sendMessage(new TextComponentString("Bound your Eye Bone to Chester."));
					}
					else if (this.checkUpgrade())
					{
						this.setType(EnumChesterType.values()[(byte) 2]);
						this.chesterInv.getInventory().clear();
						stack.getTagCompound().setByte("Type",(byte) this.getType().ordinal());
					}
				}
				else if (stack.getItem() instanceof ItemCane)
				{
					for (int i = 0; i < 10; ++i) this.startJumping();
				}
			}
			else
			{
				player.openGui(Main.instance, Reference.GUI_CHESTER, world, this.getEntityId(), (int)player.posY, (int)player.posZ);
			}
		}
		return true;
	}
	
	public void summon(EntityPlayer player)
	{
		EntityPlayer playerIn = this.getOwner();
		playerIn.sendMessage(new TextComponentString("Summoning Chester to you..."));
		Vec3d aim = playerIn.getLookVec();
		getNavigator().setPath(getNavigator().getPathToXYZ(playerIn.posX + aim.x * 1.8, playerIn.posY, playerIn.posZ + aim.z * 1.8), 0.5D);
	}
	
	private boolean checkUpgrade()
	{
		boolean check = true;
		if (this.getType() == EnumChesterType.SHADOW) return false;
		else
		{
			for (int i = 0; i < 9; i++)
			{
				ItemStack stack = this.chesterInv.getInventory().getStackInSlot(i);
				if (!stack.isEmpty())
				{
					if (!(stack.getItem() instanceof ItemNightmareFuel)) check = false;
				}
				else check = false;
			}
			return check;
		}
	}
	
	public EntityPlayer getOwner()
	{
		for (EntityPlayer player: world.playerEntities)
		{
			for (ItemStack stack: player.inventory.mainInventory)
			{
				if (stack.getItem() == ItemInit.EYE_BONE && stack.hasTagCompound())
				{
					if (stack.getTagCompound().getInteger("ChesterID") == this.getEntityId())
					{
						return player;
					}
				}
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
        compound.setInteger("EyeBoneID", this.getEntityId());
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
        this.setEntityId(compound.getInteger("EyeBoneID"));
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
	
    @SideOnly(Side.CLIENT)
    public float setJumpCompletion(float p_175521_1_)
    {
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
    }
}

