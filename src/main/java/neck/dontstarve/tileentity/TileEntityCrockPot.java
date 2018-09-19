package neck.dontstarve.tileentity;

import neck.dontstarve.block.BlockCrockPot;
import neck.dontstarve.inventory.ContainerCrockPot;
import neck.dontstarve.item.crafting.CrockPotRecipes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCrockPot extends TileEntityLockable implements ITickable
{
    private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    
    private int burnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String customName;
    
    // Gets the inventory size.
    public int getSizeInventory()
    {
    	return this.inventory.size();
    }
    
    // Checks if the inventory is empty
    public boolean isEmpty()
    {
    	for (ItemStack itemstack : this.inventory)
    	{
    		if (!itemstack.isEmpty())
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    // Fetches the stack in the specified slot
    public ItemStack getStackInSlot(int index)
    {
    	return this.inventory.get(index);
    }
    
    // Removes items from stack. Called when the stack itself is not removed.
    public ItemStack decrStackSize(int index, int count)
    {
    	return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }
    
    // Removes the stack from the specified slot.
    public ItemStack removeStackFromSlot(int index)
    {
    	return ItemStackHelper.getAndRemove(this.inventory, index);
    }
    
    // Sets a stack to the specified slot.
    public void setInventorySlotContents(int index, ItemStack stack)
    {
    	ItemStack itemstack = this.inventory.get(index);
    	boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
    	this.inventory.set(index, stack);
    	
    	if (stack.getCount() > this.getInventoryStackLimit())
    	{
    		stack.setCount(this.getInventoryStackLimit());
    	}
    	
    	if (index == 0 && !flag)
    	{
    		this.totalCookTime = this.getCookTime(stack);
    		this.cookTime = 0;
    		this.markDirty();
    	}
    }
    
    // Get the name of the object.
    @Override
    public String getName()
    {
    	return this.hasCustomName() ? this.customName : "container.crock_pot";
    }
    
    // Checks if the object has a custom name.
    @Override
    public boolean hasCustomName()
    {
    	return this.customName != null && !this.customName.isEmpty();
    }
    
    // Sets a custom name for the object
    public void setCustomName(String customname)
    {
    	this.customName = customname;
    }
    
    // Gets the formatted name for the object (for use in chat).
    @Override
    public ITextComponent getDisplayName()
    {
    	return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }
    
    // Reads object properties from its NBT tags.
    public void readFromNBT(NBTTagCompound compound)
    {
    	super.readFromNBT(compound);
    	this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
    	ItemStackHelper.loadAllItems(compound, this.inventory);
    	this.burnTime = compound.getInteger("BurnTime");
    	this.cookTime = compound.getInteger("CookTime");
    	this.totalCookTime = compound.getInteger("CookTimeTotal");
    	this.currentItemBurnTime = getItemBurnTime(this.inventory.get(4));
    }
    
    // Writes object properties to corresponding NBT tags.
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	super.writeToNBT(compound);
    	compound.setInteger("BurnTime", (short)this.burnTime);
    	compound.setInteger("CookTime", (short)this.cookTime);
    	compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
    	ItemStackHelper.saveAllItems(compound, this.inventory);
    	
    	if (this.hasCustomName())
    	{
    		compound.setString("CustomName", this.customName);
    	}
    	
    	return compound;
    }
    
    // Sets the maximum stack size for slots in the object's inventory.
    public int getInventoryStackLimit()
    {
    	return 64;
    }
    
    // Checks if the object is active.
    public boolean isBurning()
    {
    	return this.burnTime > 0;
    }
    
    // Checks if the object is active for the client.
    // Checks for presence of item in ingredient slot.
    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }
    
    // Updates the status every tick.
    public void update()
    {
    	// *flag* checks if block state needs to be changed (Burning OR Not Burning).
    	boolean flag = this.isBurning();
    	// *flag1* checks to see if it needs to mark the tile as dirty.
    	boolean flag1 = false;
    	
    	// If the object is active, decrease the active fuel amount.
    	if (this.isBurning())
    	{
    		--this.burnTime;
    	}
    	
    	if (!this.world.isRemote)
    	{
    		ItemStack fuel = this.inventory.get(4);
    		
    		// Execute if (there is burn time left OR there is fuel left) AND there is something to smelt.
    		if (this.isBurning() || !fuel.isEmpty() && !((ItemStack)this.inventory.get(0)).isEmpty())
    		{
    			// Execute if the object is not active AND the item in the ingredient slot can be smelted.
    			if (!this.isBurning() && this.canSmelt())
    			{
    				// Sets the burn time to that of the fuel in the fuel slot.
    				this.burnTime = getItemBurnTime(fuel);
    				this.currentItemBurnTime = this.burnTime;
    				
    				// Execute if the item provides fuel.
    				if (this.isBurning())
    				{
    					// Mark as dirty.
    					flag1 = true;
    					
    					// Execute if the fuel slot is not empty.
    					if (!fuel.isEmpty())
    					{
    						// Decrease the fuel item amount by 1.
    						Item item = fuel.getItem();
    						fuel.shrink(1);
    						
    						// Execute if decreasing the fuel amount has emptied the stack.
    						if (fuel.isEmpty())
    						{
    							// <++>
    							ItemStack item1 = item.getContainerItem(fuel);
    							this.inventory.set(4, item1);
    						}
    					}
    				}
    			}
    			
    			// Execute if the object is active and there is something to smelt.
    			if (this.isBurning() && this.canSmelt())
    			{
    				// Add progress to the cooking.
    				++this.cookTime;
    				
    				// Execute if cooking is done
    				if (this.cookTime == this.totalCookTime)
    				{
    					// Reset cooking time
    					this.cookTime = 0;
    					// Get new total cooking time from next item in input.
    					this.totalCookTime = this.getCookTime(this.inventory.get(0));
    					// Smelt the item
    					this.smeltItem();
    					// Mark as dirty.
    					flag1 = true;
    				}
    			}
    			else
    			{
    				// <++>
    				this.cookTime = 0;
    			}
    		}
    		
    		// Execute if the object is out of fuel but is in the process of cooking.
    		else if (!this.isBurning() && this.cookTime > 0)
    		{
    			// Reduce cooking progress.
    			this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
    		}
    		
    		// Check if the block state needs to be changed.
    		if (flag != this.isBurning())
    		{
    			flag1 = true;
    			BlockCrockPot.setState(this.isBurning(), this.world, this.pos);
    		}
    	}
    	
    	if (flag1)
    	{
    		this.markDirty();
    	}
    }
    
    // modify to change how long something cooks for
    public int getCookTime(ItemStack stack)
    {
    	return 200;
    }
    
    // Checks if the smelting process can happen.
    private boolean canSmelt()
    {
    	// Check if the ingredient slots are empty.
    	boolean ingredientsEmpty = false;
    	for (int i = 0; i < 4; ++i)
    	{
    		if (((ItemStack)this.inventory.get(0)).isEmpty()) ingredientsEmpty = true;
    	}
    	
    	if (ingredientsEmpty)
    	{
    		return false;
    	}
    	else
    	{
    		// Checks if the recipe is valid.
    		ItemStack i1 = this.inventory.get(0);
    		ItemStack i2 = this.inventory.get(1);
    		ItemStack i3 = this.inventory.get(2);
    		ItemStack i4 = this.inventory.get(3);
    		
    		ItemStack result = CrockPotRecipes.instance().getCrockPotResult(i1,i2,i3,i4);
    		
    		if (result.isEmpty())
    		{
    			return false;
    		}
    		else
    		{
    			ItemStack output = this.inventory.get(5);
    		
    			// Checks if output slot is empty. If so, smelting can happen.
    			if (output.isEmpty())
    			{
    				return true;
    			}
    			// If the output and the result are not equal, smelting can't happen.
    			else if (!output.isItemEqual(result))
    			{
    				return false;
    			}
    			// If the output stack, when updated, has less items than the object stack limit,
    			// and less items than the stack limit of the output item itself,
    			// smelting can happen.
    			else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && output.getCount() + output.getCount() <= output.getMaxStackSize())
    			{
    				return true;
    			}
    			else
    			{
    				// Double checks if the new output slot stack size is smaller
    				// than the upper limit on the result item.
    				// Might cause issues/remove data from output item.
    				return output.getCount() + result.getCount() <= result.getMaxStackSize();
    			}
    		}
    	}
    }
    
    // Conducts the smelting process.
    //
    // Might change to allow for more than 1 item to 
    // disappear from input.
    public void smeltItem()
    {
    	if (this.canSmelt())
    	{
    		
    		ItemStack i1 = this.inventory.get(0);
    		ItemStack i2 = this.inventory.get(1);
    		ItemStack i3 = this.inventory.get(2);
    		ItemStack i4 = this.inventory.get(3);
    		ItemStack result = CrockPotRecipes.instance().getCrockPotResult(i1,i2,i3,i4);
    		ItemStack output = this.inventory.get(5);
    		
    		if (output.isEmpty())
    		{
    			this.inventory.set(5, result.copy());
    		}
    		else if (output.getItem() == result.getItem())
    		{
    			output.grow(result.getCount());
    		}
    		
    		i1.shrink(1);
    		i2.shrink(1);
    		i3.shrink(1);
    		i4.shrink(1);
    	}
    }
    
    // Gets the burn time for various fuels.
    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOOL))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.CARPET))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
    }
    
    // Checks if the item in the fuel slot is fuel.
    public static boolean isItemFuel(ItemStack stack)
    {
    	return getItemBurnTime(stack) > 0;
    }
	
    // Error check.
    public boolean isUsableByPlayer(EntityPlayer player)
    {
    	if (this.world.getTileEntity(this.pos) != this)
    	{
    		return false;
    	}
    	else
    	{
    		return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    	}
    }
    
    public void openInventory(EntityPlayer player)
    {
    	//not used.
    }
    
    public void closeInventory(EntityPlayer player)
    {
    	//not used.
    }
    
    // Checks where the player can put an item.
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
    	// Player can't put items in output slot.
    	if (index == 2)
    	{
    		return false;
    	}
    	// Player can put anything in ingredient slot.
    	else if (index != 1)
    	{
    		return true;
    	}
    	else
    	{
    		
    		ItemStack fuel = this.inventory.get(4);
    		return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && fuel.getItem() != Items.BUCKET;
    	}
    }
    
    //Gets the GUI ID for this tile entity's GUI.
    public String getGuiID()
    {
    	return "dont_starve_mod:crock_pot";
    }
    
    // Calls rendering of container.
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
    	return new ContainerCrockPot(playerInventory, this);
    }
    
    // <++>
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.burnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }
    
    //<++>
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.burnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }
    
    //<++>
    public int getFieldCount()
    {
        return 4;
    }
    
    // Clears the object inventory.
    public void clear()
    {
        this.inventory.clear();
    }
}
