package neck.dontstarve.gui;

import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.entity.EnumChesterType;
import neck.dontstarve.inventory.ContainerChester;
import neck.dontstarve.util.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.util.ResourceLocation;

public class GuiChester extends GuiContainer
{
	private static final ResourceLocation TEXTURES_REGULAR = new ResourceLocation(Reference.MOD_ID + ":textures/gui/chester.png");
//	private static final ResourceLocation TEXTURES_LARGE = new ResourceLocation(Reference.MOD_ID + "textures/gui/chester_large.png");
	
	private final IInventory playerInventory;
	private EntityChester chesterEntity;
	private float mousePosx;
	private float mousePosY;
	
	public GuiChester(final EntityPlayer player, EntityChester chester)
	{
		super(new ContainerChester(player, chester));
		this.chesterEntity = chester;
		this.playerInventory = player.inventory;
		
	}
	
	public void initGui()
	{
		super.initGui();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
		super.drawScreen(mouseX, mouseY, partialTicks);	
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(this.chesterEntity.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
//		GlStateManager.color(1.	0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURES_REGULAR);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (this.chesterEntity.getType() == EnumChesterType.SHADOW)
		{
			
			this.drawTexturedModalRect(this.guiLeft + 133, this.guiTop + 17, 176, 0, 18, 54);
		}
		
		 GuiInventory.drawEntityOnScreen(i + 51, j + 60, 30, (float)(i + 51) - this.mousePosx, (float)(j + 75 - 50) - this.mousePosY, this.chesterEntity);
	}
}
