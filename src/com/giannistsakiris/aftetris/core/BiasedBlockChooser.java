package com.giannistsakiris.aftetris.core;

import java.util.Random;

public class BiasedBlockChooser implements BlockChooser
{
	private int iBias;
	private int jBias;
	private int lBias;
	private int oBias;
	private int sBias;
	private int zBias;

	int totalBias;

	private Random random = new Random();
	
	public BiasedBlockChooser( int iBias, int jBias, int lBias, int oBias, int sBias, int zBias, int tBias )
	{
		if (iBias == 0 && jBias == 0 && lBias == 0 && oBias == 0 && sBias == 0 && zBias == 0 && tBias == 0)
		{
			this.iBias = 1;
			this.jBias = 1;
			this.lBias = 1;
			this.oBias = 1;
			this.sBias = 1;
			this.zBias = 1;
			totalBias = 7;
		}
		else
		{
			this.iBias = iBias;
			this.jBias = jBias;
			this.lBias = lBias;
			this.oBias = oBias;
			this.sBias = sBias;
			this.zBias = zBias;
			totalBias = iBias+jBias+lBias+oBias+sBias+zBias+tBias;
		}
	}
	
	public Block chooseBlock()
	{
		int choice = random.nextInt(totalBias);
		
		if (choice < iBias)
			return BlockFactory.createIBlock();
		
		choice -= iBias;
		if (choice < jBias)
			return BlockFactory.createJBlock();
		
		choice -= jBias;
		if (choice < lBias)
			return BlockFactory.createLBlock();
		
		choice -= lBias;
		if (choice < oBias)
			return BlockFactory.createOBlock();
		
		choice -= oBias;
		if (choice < sBias)
			return BlockFactory.createSBlock();
		
		choice -= sBias;
		if (choice < zBias)
			return BlockFactory.createZBlock();
		
		return BlockFactory.createTBlock();
	}
}

