package neck.dontstarve.init;

import java.util.ArrayList;
import java.util.List;

import neck.dontstarve.item.ItemBase;
import neck.dontstarve.item.ItemCane;
import neck.dontstarve.item.ItemEyeBone;
import neck.dontstarve.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//public static final Item WALKING_CANE = new ItemBase("walking_cane");
	
	
	//Tools
	public static final ItemCane WALKING_CANE = new ItemCane("walking_cane", 5.0F, 0.0F, 0.03F);
	public static final Item EYE_BONE = new ItemEyeBone("eye_bone");
}
