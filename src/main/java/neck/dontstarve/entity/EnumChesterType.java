package neck.dontstarve.entity;

public enum EnumChesterType
{
	NORMAL(0,"normal", 3, false),
	SNOW(1,"snowl", 3, true),
	SHADOW(2,"shadow", 3, false);
	
	private static final EnumChesterType[] META_LOOKUP = new EnumChesterType[values().length];
	private int id;
	private final String name;
	private final int invSize;
	private final boolean chillFood;
	
	private EnumChesterType(int id, String nameIn, int invSizeIn, boolean chillFoodIn)
	{
		this.name = nameIn;
		this.invSize = invSizeIn;
		this.chillFood = chillFoodIn;
	}
}
