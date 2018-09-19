package neck.dontstarve.init;

import java.util.ArrayList;
import java.util.List;

import neck.dontstarve.block.BlockBase;
import neck.dontstarve.block.BlockCrockPot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit
{
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block BLOCK_TEST = new BlockBase("block_test", Material.ROCK);
	public static final Block CROCK_POT = new BlockCrockPot("crock_pot");
}
