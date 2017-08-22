package com.giannistsakiris.aftetris.core;

public final class BlockFactory
{
	private BlockFactory() { }

	public static Block createIBlock()
	{
		return new Block(BlockStates.I_BLOCK_STATES, BlockType.I);
	}	

	public static Block createJBlock()
	{
		return new Block(BlockStates.J_BLOCK_STATES, BlockType.J);
	}

	public static Block createLBlock()
	{
		return new Block(BlockStates.L_BLOCK_STATES, BlockType.L);
	}

	public static Block createOBlock()
	{
		return new Block(BlockStates.O_BLOCK_STATES, BlockType.O);
	}
	
	public static Block createSBlock()
	{
		return new Block(BlockStates.S_BLOCK_STATES, BlockType.S);
	}
	
	public static Block createZBlock()
	{
		return new Block(BlockStates.Z_BLOCK_STATES, BlockType.Z);
	}
	
	public static Block createTBlock()
	{
		return new Block(BlockStates.T_BLOCK_STATES, BlockType.T);
	}
}
