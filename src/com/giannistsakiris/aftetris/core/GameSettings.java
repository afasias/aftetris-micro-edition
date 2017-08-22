package com.giannistsakiris.aftetris.core;

public class GameSettings
{
	public static final int EXTRA_ROWS = 4;
	
	private int columns = 10;
	private int rows = 20;
	private int pointsForBlock = 4;
	private int pointsForSingle = 10;
	private int pointsForDouble = 25;
	private int pointsForTriple = 50;
	private int pointsForTetris = 100;
	private int rowsPerLevel = 10;
	private int minLevel = 0;
	private int maxLevel = 9;
	private int minLevelSpeed = 750;
	private int maxLevelSpeed = 75;
	private int lockDownTime = 500;
	private int iBias = 3;
	private int jBias = 3;
	private int lBias = 3;
	private int oBias = 3;
	private int sBias = 3;
	private int zBias = 3;
	private int tBias = 3;
	private boolean lockDownOnRockBottom = true;
	private boolean wallKicksEnabled = true;
	private int gravityPauseUponRotationTime = 150;
	
	public void setColumns(int columns)
	{
		this.columns = columns;
	}
	
	public int getColumns()
	{
		return columns;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	public int getRows()
	{
		return rows;
	}
	
	public int getActualRows()
	{
		return getRows()+EXTRA_ROWS;
	}

	public void setPointsForSingle(int pointsForSingle)
	{
		this.pointsForSingle = pointsForSingle;
	}

	public int getPointsForSingle()
	{
		return pointsForSingle;
	}

	public void setPointsForDouble(int pointsForDouble)
	{
		this.pointsForDouble = pointsForDouble;
	}

	public int getPointsForDouble()
	{
		return pointsForDouble;
	}

	public void setPointsForTriple(int pointsForTriple)
	{
		this.pointsForTriple = pointsForTriple;
	}

	public int getPointsForTriple()
	{
		return pointsForTriple;
	}

	public void setPointsForTetris(int pointsForTetris)
	{
		this.pointsForTetris = pointsForTetris;
	}

	public int getPointsForTetris()
	{
		return pointsForTetris;
	}

	public void setPointsForBlock(int pointsForBlock)
	{
		this.pointsForBlock = pointsForBlock;
	}

	public int getPointsForBlock()
	{
		return pointsForBlock;
	}

	public void setRowsPerLevel(int rowsPerLevel)
	{
		this.rowsPerLevel = rowsPerLevel;
	}

	public int getRowsPerLevel()
	{
		return rowsPerLevel;
	}

	public void setMinLevel(int minLevel)
	{
		this.minLevel = minLevel;
	}

	public int getMinLevel()
	{
		return minLevel;
	}

	public void setMaxLevel(int maxLevel)
	{
		this.maxLevel = maxLevel;
	}

	public int getMaxLevel()
	{
		return maxLevel;
	}

	public void setMinLevelSpeed(int minLevelSpeed)
	{
		this.minLevelSpeed = minLevelSpeed;
	}

	public int getMinLevelSpeed()
	{
		return minLevelSpeed;
	}

	public void setMaxLevelSpeed(int maxLevelSpeed)
	{
		this.maxLevelSpeed = maxLevelSpeed;
	}

	public int getMaxLevelSpeed()
	{
		return maxLevelSpeed;
	}

	public void setLockDownTime(int lockDownTime)
	{
		this.lockDownTime = lockDownTime;
	}

	public int getLockDownTime()
	{
		return lockDownTime;
	}

	public void setIBias(int iBias)
	{
		this.iBias = iBias;
	}

	public int getIBias()
	{
		return iBias;
	}

	public void setJBias(int jBias)
	{
		this.jBias = jBias;
	}

	public int getJBias()
	{
		return jBias;
	}

	public void setLBias(int lBias)
	{
		this.lBias = lBias;
	}

	public int getLBias()
	{
		return lBias;
	}

	public void setOBias(int oBias)
	{
		this.oBias = oBias;
	}

	public int getOBias()
	{
		return oBias;
	}

	public void setSBias(int sBias)
	{
		this.sBias = sBias;
	}

	public int getSBias()
	{
		return sBias;
	}

	public void setZBias(int zBias)
	{
		this.zBias = zBias;
	}

	public int getZBias()
	{
		return zBias;
	}

	public void setTBias(int tBias)
	{
		this.tBias = tBias;
	}

	public int getTBias()
	{
		return tBias;
	}
	
	public void setLockDownOnRockBottom(boolean lockDownOnRockBottom)
	{
		this.lockDownOnRockBottom = lockDownOnRockBottom;
	}

	public boolean isLockDownOnRockBottom()
	{
		return lockDownOnRockBottom;
	}

	public void setWallKicksEnabled(boolean wallKicksEnabled)
	{
		this.wallKicksEnabled = wallKicksEnabled;
	}

	public boolean isWallKicksEnabled()
	{
		return wallKicksEnabled;
	}

	public void setGravityPauseUponRotationTime(int gravityPauseUponRotationTime)
	{
		this.gravityPauseUponRotationTime = gravityPauseUponRotationTime;
	}

	public int getGravityPauseUponRotationTime()
	{
		return gravityPauseUponRotationTime;
	}
}
