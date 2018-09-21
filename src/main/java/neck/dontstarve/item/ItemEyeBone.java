package neck.dontstarve.item;

import java.util.Random;

import neck.dontstarve.Main;
import neck.dontstarve.entity.EntityChester;
import neck.dontstarve.entity.EnumChesterType;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEyeBone extends ItemBase
{
	private boolean bound = false;
	private int chesterID;
	
	public ItemEyeBone(String name, boolean setTab)
	{
		super(name);
		this.maxStackSize = 1;
		if (!setTab) this.setCreativeTab(null);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		if (!item.hasTagCompound())
		{
			item.setTagCompound(new NBTTagCompound());
		}
		
		if (!worldIn.isRemote)
		{
			NBTTagCompound compound = item.getTagCompound();
			if (compound.getInteger("ChesterID") == 0)
			{
				EntityChester chester = spawnCreature(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
				bind(chester, item, compound);
			}
			else
			{
				EntityChester chester = (EntityChester) worldIn.getEntityByID(compound.getInteger("ChesterID"));
				if (chester != null)
				{
				chester.summon(playerIn);
				}
				else
				{
					playerIn.sendMessage(new TextComponentString("Your Chester could not be found."));
					compound.setInteger("ChesterID", -1);
				}
			}
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, item);
	}
	
	public void bind(EntityChester chester, ItemStack item, NBTTagCompound compound)
	{
		compound.setInteger("ChesterID", chester.getEntityId());
	}
	
	public static EntityChester spawnCreature(World worldIn, double x, double y, double z)
    {
			EntityChester entity = new EntityChester(worldIn);
			double xMod = (double)x + (worldIn.rand.nextDouble() - 0.5) * 30;
			double zMod = (double)z + (worldIn.rand.nextDouble() - 0.5) * 30;
			
			entity.setLocationAndAngles(xMod, y, zMod, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
			worldIn.spawnEntity(entity);
			return entity;
    }
}
