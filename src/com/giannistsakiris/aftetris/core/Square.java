package com.giannistsakiris.aftetris.core;


public class Square
{
	private BlockType blockType;
	private boolean northBorder;
	private boolean southBorder;
	private boolean westBorder;
	private boolean eastBorder;

	public Square( BlockType blockType, boolean northBorder, boolean southBorder, boolean westBorder, boolean eastBorder )
	{
		this.setBlockType(blockType);
		this.setNorthBorder(northBorder);
		this.setSouthBorder(southBorder);
		this.setWestBorder(westBorder);
		this.setEastBorder(eastBorder);
	}

	public void setBlockType(BlockType blockType)
	{
		this.blockType = blockType;
	}

	public BlockType getBlockType()
	{
		return blockType;
	}

	public void setNorthBorder(boolean northBorder)
	{
		this.northBorder = northBorder;
	}

	public boolean isNorthBorder()
	{
		return northBorder;
	}

	public void setSouthBorder(boolean southBorder)
	{
		this.southBorder = southBorder;
	}

	public boolean isSouthBorder()
	{
		return southBorder;
	}

	public void setWestBorder(boolean westBorder)
	{
		this.westBorder = westBorder;
	}

	public boolean isWestBorder()
	{
		return westBorder;
	}

	public void setEastBorder(boolean eastBorder)
	{
		this.eastBorder = eastBorder;
	}

	public boolean isEastBorder()
	{
		return eastBorder;
	}
}
