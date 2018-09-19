package neck.dontstarve.util.handlers;

import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.entity.render.RenderChester;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityChester.class, new IRenderFactory<EntityChester>()
		{
			@Override
			public Render<? super EntityChester> createRenderFor(RenderManager manager)
			{
				return new RenderChester(manager);
			}
		});
	}
}
