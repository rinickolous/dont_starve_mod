package neck.dontstarve.init;

import java.util.ArrayList;
import java.util.List;

import neck.dontstarve.item.ItemCane;
import neck.dontstarve.item.ItemEyeBone;
import neck.dontstarve.item.ItemNightmareFuel;
import net.minecraft.item.Item;



public class ItemInit
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//public static final Item WALKING_CANE = new ItemBase("walking_cane");
	//Items
	public static final Item NIGHTMARE_FUEL = new ItemNightmareFuel("nightmare_fuel");
	
	
	//Tools
	public static final Item WALKING_CANE = new ItemCane("walking_cane", 5.0F, 0.0F, 0.03F);
	public static final Item EYE_BONE = new ItemEyeBone("eye_bone", true);
	public static final Item EYE_BONE_SHADOW = new ItemEyeBone("eye_bone_shadow", false);
	public static final Item EYE_BONE_SNOW = new ItemEyeBone("eye_bone_snow", false);
	public static final Item EYE_BONE_CLOSED = new ItemEyeBone("eye_bone_closed", false);
}
