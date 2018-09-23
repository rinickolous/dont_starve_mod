package neck.dontstarve.client.render;

import neck.dontstarve.client.model.ModelChester;
import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.entity.EnumChesterType;
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
		if (entity.getType() == EnumChesterType.SHADOW) textures = TEXTURES_SHADOW;
		else if (entity.getType() == EnumChesterType.SNOW) textures = TEXTURES_SNOW;
		else textures = TEXTURES_NORMAL;
		return textures;
	}
	
	@Override
	protected void applyRotations(EntityChester entity, float rotationPitch, float rotationYaw, float partialTicks)
	{
		super.applyRotations(entity, rotationPitch, rotationYaw, partialTicks);
	}
}
