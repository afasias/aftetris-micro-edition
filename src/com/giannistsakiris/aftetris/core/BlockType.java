package com.giannistsakiris.aftetris.core;

public final class BlockType
{
	public static final BlockType I = new BlockType();
	public static final BlockType J = new BlockType();
	public static final BlockType L = new BlockType();
	public static final BlockType O = new BlockType();
	public static final BlockType S = new BlockType();
	public static final BlockType Z = new BlockType();
	public static final BlockType T = new BlockType();
	
	private BlockType()
	{
	}
	
	public BlockType[] values()
	{
		return new BlockType[] { I, J, L, O, S, Z, T };
	}
}
