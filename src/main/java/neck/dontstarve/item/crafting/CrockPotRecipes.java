package neck.dontstarve.item.crafting;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import neck.dontstarve.init.BlockInit;
import neck.dontstarve.init.ItemInit;
import neck.dontstarve.tileentity.TileEntityCrockPot;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class CrockPotRecipes
{
	private static final CrockPotRecipes INSTANCE = new CrockPotRecipes();
	private final Map<List<ItemStack>, ItemStack> recipeList = Maps.<List<ItemStack>, ItemStack>newHashMap();
	private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();
	
	public static CrockPotRecipes instance()
	{
		return INSTANCE;
	}
	
	private CrockPotRecipes()
	{
		addCrockPotRecipe(new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.STONE), 1.0F);
		addCrockPotRecipe(new ItemStack(BlockInit.BLOCK_TEST), new ItemStack(BlockInit.BLOCK_TEST), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(ItemInit.EYE_BONE), 1.0F);
	}
	
	public void addCrockPotRecipe(ItemStack i1, ItemStack i2, ItemStack i3, ItemStack i4, ItemStack result, float xp)
	{
		List<ItemStack> itemList = this.sortItems(i1, i2, i3, i4);
		
		if(getCrockPotResult(i1,i2,i3,i4) != ItemStack.EMPTY) return;
		this.recipeList.put(itemList, result);
		this.experienceList.put(result, xp);
	}
	
	
	public ItemStack getCrockPotResult(ItemStack i1, ItemStack i2, ItemStack i3, ItemStack i4)
	{
		List<ItemStack> itemList = sortItems(i1,i2,i3,i4);
		for (Entry<List<ItemStack>, ItemStack> entry: this.recipeList.entrySet())
		{
			if (this.compareItemLists(itemList, entry.getKey()))
			{
				return entry.getValue();
			}
		}
		return ItemStack.EMPTY;
	}
	
	public float getCrockPotExperience(ItemStack stack)
    {
        float ret = stack.getItem().getSmeltingExperience(stack);
        if (ret != -1) return ret;

        for (Entry<ItemStack, Float> entry : this.experienceList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }
	private boolean compareItemLists(List<ItemStack> list1, List<ItemStack> list2)
	{
		for (int index = 0; index < 4; ++index)
		{
			if (!this.compareItemStacks(list1.get(index), list2.get(index))) return false;
		}
		return true;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
	
	public boolean isItemInRecipes(ItemStack stack1)
	{
		for (Entry<List<ItemStack>, ItemStack> entry: this.recipeList.entrySet())
		{
			for (ItemStack stack2 : entry.getKey())
			{
				if (this.compareItemStacks(stack1, stack2)) return true;
			}
		}
		return false;
	}
	
	public List sortItems(ItemStack i1, ItemStack i2, ItemStack i3, ItemStack i4)
	{
		
		List<ItemStack> itemList = Lists.newArrayList(i1,i2,i3,i4);
		Collections.sort(itemList, new Comparator<ItemStack>() {
		    @Override
		    public int compare(ItemStack s1, ItemStack s2)
		    {
		    	return s1.getDisplayName().compareTo(s2.getDisplayName());
		    }
		});
		return itemList;
	}
}
