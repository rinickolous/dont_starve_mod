package neck.dontstarve.tabs;

import neck.dontstarve.init.BlockInit;
import neck.dontstarve.init.ItemInit;

import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DontStarveCreativeTab extends CreativeTabs
{
	public DontStarveCreativeTab(String label)
	{
		super("dontstarvetab");
		this.setBackgroundImageName("dontstarve.png");
		
	}

	public ItemStack getTabIconItem()
	{
		return new ItemStack(Item.getItemFromBlock(BlockInit.CROCK_POT));
	}
}
