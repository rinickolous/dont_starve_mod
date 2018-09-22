package neck.dontstarve.init;

import java.util.ArrayList;
import java.util.List;

import neck.dontstarve.item.ItemBase;
import neck.dontstarve.item.ItemCane;
import neck.dontstarve.item.ItemEyeBone;
import net.minecraft.item.Item;



public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//public static final Item WALKING_CANE = new ItemBase("walking_cane");
	//Items
	public static final Item NIGHTMARE_FUEL = new ItemBase("nightmare_fuel");
	public static final Item BLUE_GEM = new ItemBase("blue_gem");
	
	
	//Tools
	public static final Item WALKING_CANE = new ItemCane("walking_cane", 5.0F, 0.0F, 0.03F);
	public static final Item EYE_BONE = new ItemEyeBone("eye_bone", true);
}
