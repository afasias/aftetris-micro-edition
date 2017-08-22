package com.giannistsakiris.aftetris.core;

public class Block
{
	private BlockStates states;
	private int index = 0;
	private BlockType blockType;
	
	private Square[][] squares;
	
	public Block( BlockStates states, BlockType blockType )
	{
		this.states = states;
		this.blockType = blockType;
		updateSquares();
	}

	public int getRows()
	{
		return states.getAt(index).getRows();
	}
	
	public int getColumns()
	{
		return states.getAt(index).getColumns();
	}
	
	public int getCx()
	{
		return states.getAt(index).getCx();
	}
	
	public int getCy()
	{
		return states.getAt(index).getCy();
	}
	
	public byte valueAt( int row, int column )
	{
		return states.getAt(index).valueAt(row,column);
		
	}
	
	public void rotateClockwise()
	{
		index--;
		if (index < 0)
			index = states.length()-1;
		updateSquares();
	}
	
	public void rotateCounterClockwise()
	{
		index++;
		if (! (index < states.length()))
			index = 0;
		updateSquares();
	}
	
	public BlockType getBlockType()
	{
		return blockType;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < getRows(); row++)
		{
			for (int column = 0; column < getColumns(); column++)
			{
				sb.append(valueAt(row, column) == 0 ? '.' : 'x');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public Square[][] getSquares()
	{
		return squares;
	}
	
	private void updateSquares()
	{
		squares = new Square[getRows()][];
		for (int row = 0; row < getRows(); row++)
		{
			squares[row] = new Square[getColumns()];
			for (int column = 0; column < getColumns(); column++)
			{
				if (valueAt(row,column) != 0)
				{
					boolean northBorder = row == 0 || valueAt(row-1,column) == 0;
					boolean southBorder = row == getRows()-1 || valueAt(row+1,column) == 0;
					boolean westBorder = column == 0 || valueAt(row,column-1) == 0;
					boolean eastBorder = column == getColumns()-1 || valueAt(row,column+1) == 0;
					squares[row][column] = new Square(blockType,northBorder,southBorder,westBorder,eastBorder);
				}
			}
		}
	}
	
	public int totalStates()
	{
		return states.length();
	}
	
	public int stateIndex()
	{
		return index;
	}
}
