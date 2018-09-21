package neck.dontstarve.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import neck.dontstarve.Main;
import neck.dontstarve.capability.ChesterInventory;
import neck.dontstarve.capability.ChesterInventoryCapability;
import neck.dontstarve.init.ItemInit;
import neck.dontstarve.inventory.ContainerChester;
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
import net.minecraft.init.SoundEvents;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
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
    
    public float lidAngle, prevLidAngle;
//	private int numPlayersUsing;
	private boolean isOpen;
    
    // Core
    public EntityChester(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 0.9F);
		this.jumpHelper = new EntityChester.JumpHelper(this);
		this.moveHelper = new EntityChester.MoveHelper(this);
	}
    
	public void entityInit()
	{
		super.entityInit();
		this.dataManager.register(TYPE,(byte) 0);
	}
	
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0D, Items.CAKE, false));
//		this.tasks.addTask(4, new EntityChester.AIAvoidEntity(this, EntityCow.class, 8.0F, 2.2D, 2.2D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
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
					else if (stack.getTagCompound().getInteger("ChesterID") == this.getEntityId())
					{
						if (this.checkUpgrade())
						{
							this.startJumping();
							this.world.playSound((EntityPlayer)null, (double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
							this.setType(EnumChesterType.values()[(byte) 2]);
							this.clearInventory();
							stack.getTagCompound().setByte("Type",(byte) this.getType().ordinal());
						}
						else
						{
							this.world.playSound((EntityPlayer)null, (double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D, SoundEvents.ENTITY_WOLF_PANT, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
						}
					}
					else
					{
						player.sendMessage(new TextComponentString("This Eye Bone is already bound."));
						this.world.playSound((EntityPlayer)null, (double)this.posX + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D, SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
					}
				}
				else if (stack.getItem() instanceof ItemCane)
				{
					this.isOpen = false;
				}
				else
				{
					player.openGui(Main.instance, Reference.GUI_CHESTER, world, this.getEntityId(), (int)player.posY, (int)player.posZ);
					this.isOpen = true;
				}
			}
			else
			{
				player.openGui(Main.instance, Reference.GUI_CHESTER, world, this.getEntityId(), (int)player.posY, (int)player.posZ);
				this.isOpen = true;
			}
		}
		return true;
	}
		
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.getOwner() != null && this.getDistance(getOwner()) > 10)
        {
        	if (this.getDistance(getOwner()) > 40  && this.navigator.getPath() == null) this.setLocationAndAngles((double)(getOwner().posX + (world.rand.nextFloat()-0.5F) * 20.0F), getOwner().posY, (double)(getOwner().posZ + (world.rand.nextFloat()-0.5F) * 20.0F), 0.0F, 0.0F);
        	else this.summon(getOwner());
        }
        
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
        
        
        if (this.isOpen && this.lidAngle < 1.0f)
        {
        	this.lidAngle += 0.1f;
        }
        
        if (this.lidAngle > 1.0f)
        {
        	this.lidAngle = 1.0f;
        }
        
        if (!this.isOpen && this.lidAngle > 0.0f)
        {
        	this.lidAngle -= 0.1f;
        }
        
        if (this.lidAngle < 0.0f)
        {
        	this.lidAngle = 0.0f;
        }
        
//		if (!this.world.isRemote && this.numPlayersUsing != 0)
//        {
//            this.numPlayersUsing = 0;
//            float f = 5.0F;
//
//            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)this.posX - 5.0F), (double)((float)this.posY - 5.0F), (double)((float)this.posZ - 5.0F), (double)((float)(this.posX + 1) + 5.0F), (double)((float)(this.posY + 1) + 5.0F), (double)((float)(this.posZ + 1) + 5.0F))))
//            {
//                if (entityplayer.openContainer instanceof ContainerChester)
//                {
//                    if (((ContainerChester)entityplayer.openContainer).getChestInventory() == this)
//                    {
//                        ++this.numPlayersUsing;
//                    }
//                }
//            }
//        }
//		
//		this.prevLidAngle = this.lidAngle;
//        float f1 = 0.1F;
//
//        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
//        {
//            double d1 = (double)this.posX + 0.5D;
//            double d2 = (double)this.posZ + 0.5D;
//            this.world.playSound((EntityPlayer)null, d1, (double)this.posY + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
//        }
//
//        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
//        {
//            float f2 = this.lidAngle;
//
//            if (this.numPlayersUsing > 0)
//            {
//                this.lidAngle += 0.1F;
//            }
//            else
//            {
//                this.lidAngle -= 0.1F;
//            }
//
//            if (this.lidAngle > 1.0F)
//            {
//                this.lidAngle = 1.0F;
//            }
//
//            float f3 = 0.5F;
//
//            if (this.lidAngle < 0.5F && f2 >= 0.5F)
//            {
//                double d3 = (double)this.posX + 0.5D;
//                double d0 = (double)this.posZ + 0.5D;
//                this.world.playSound((EntityPlayer)null, d3, (double)this.posY + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
//            }
//
//            if (this.lidAngle < 0.0F)
//            {
//                this.lidAngle = 0.0F;
//            }
//        }		
    }

	/**
	 * Summons Chester to his owner. 'player' variable currently unused.
	 * @param player
	 */
	public void summon(EntityPlayer player)
	{
		EntityPlayer playerIn = this.getOwner();
//		playerIn.sendMessage(new TextComponentString("Summoning Chester to you..."));
		Vec3d aim = playerIn.getLookVec();
		getNavigator().setPath(getNavigator().getPathToXYZ(playerIn.posX + aim.x * 2.0, playerIn.posY, playerIn.posZ + aim.z * 2.0), 1.5D);
	}
	
	/**
	 * Checks if Chester can be upgraded (only works for upgarding to Shadow Chester currently)
	 */
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
	
	/**
	 * Checks all players for one with this Chester's Eye Bone.
	 */
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
	
	@Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("isOpen", this.isOpen);
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
        this.isOpen = compound.getBoolean("isOpen");
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
   
	//Inventory
	private int getSizeInventory()
	{
		if (this.getType() == EnumChesterType.SHADOW) return 12;
		else return 9;
	}
    
	//Misc
    @CapabilityInject(ChesterInventory.class)
	public static Capability<ChesterInventory> CAPABILITY;
    
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
	
	public EnumChesterType getType()
	{
		return EnumChesterType.values()[this.dataManager.get(TYPE)];
	}
	
	public void setType(EnumChesterType type)
	{
		this.dataManager.set(TYPE,(byte) type.ordinal());
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
	
	@Override
	public float getEyeHeight()
	{
		return 0.8F;
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		return null;
	}
	//End of Misc.
	
    //AI
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
    
	public void clearInventory()
	{
		for (int i=0; i < this.getSizeInventory(); ++i)
		{
			this.chesterInv.getInventory().setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
    protected float getJumpUpwardsMotion()
    {
        if (!this.collidedHorizontally && (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.25D))
        {
            Path path = this.navigator.getPath();

            if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength())
            {
                Vec3d vec3d = path.getPosition(this);

                if (vec3d.y > this.posY + 0.25D)
                {
                    return 1.0F;
                }
            }

            return this.moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
        }
        else
        {
            return 1.0F;
        }
    }

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
    
    @SideOnly(Side.CLIENT)
    public float setJumpCompletion(float completion)
    {
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + completion) / (float)this.jumpDuration;
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
    // End of Jumping
}

