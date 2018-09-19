package neck.dontstarve.init;

import net.minecraft.entity.Entity;

import java.util.function.Function;

import neck.dontstarve.Main;
import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit
{
	public static void registerEntities()
	{
		registerEntity("chester", EntityChester.class, Reference.ENTITY_CHESTER, 50, 12284754, 5836305);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, color1, color2);
		
	}
}
