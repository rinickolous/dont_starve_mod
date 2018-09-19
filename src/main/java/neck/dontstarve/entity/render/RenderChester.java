package neck.dontstarve.entity.render;

import javax.swing.text.html.parser.Entity;

import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.entity.model.ModelChester;
import neck.dontstarve.util.Reference;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderChester extends RenderLiving<EntityChester>
{
	public static final ResourceLocation TEXTURES_NORMAL = new ResourceLocation(Reference.MOD_ID + ":textures/entity/chester.png");
	public static final ResourceLocation TEXTURES_SNOW = new ResourceLocation(Reference.MOD_ID + ":textures/entity/chester_snow.png");
	public static final ResourceLocation TEXTURES_SHADOW = new ResourceLocation(Reference.MOD_ID + ":textures/entity/chester_shadow.png");

	public RenderChester(RenderManager manager)
	{
		super(manager, new ModelChester(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityChester entity)
	{
		ResourceLocation textures;
		switch(entity.getType())
		{
			case("normal") : textures =  TEXTURES_NORMAL; break;
			case("snow") :  textures =  TEXTURES_SNOW; break;
			case("shadow") : textures =  TEXTURES_SHADOW; break;
			default: textures = TEXTURES_NORMAL; break;
		}
		
		return textures;
	}
	
	
	@Override
	protected void applyRotations(EntityChester entity, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entity, p_77043_2_, rotationYaw, partialTicks);
	}
}
