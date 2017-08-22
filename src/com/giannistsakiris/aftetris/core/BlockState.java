package com.giannistsakiris.aftetris.core;

public class BlockState
{
	private byte[] values;
	private int rows;
	private int columns;
	private int cx;
	private int cy;
	
	public BlockState( byte[] values, int rows, int columns, int cy, int cx )
	{
		this.values = values;
		this.rows = rows;
		this.columns = columns;
		this.cx = cx;
		this.cy = cy;
	}

	public byte valueAt( int row, int column )
	{
		return values[row*columns+column];
	}
	
	public int getRows()
	{
		return rows;
	}
	
	public int getColumns()
	{
		return columns;
	}
	
	public int getCx()
	{
		return cx;
	}
	
	public int getCy()
	{
		return cy;
	}
}
