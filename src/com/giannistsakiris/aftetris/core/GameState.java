package com.giannistsakiris.aftetris.core;

import java.util.Vector;

public class GameState
{
	private GameSettings settings;
	private int cx;
	private int cy;
	private Square[][] board;

	private Block previous;
	private Block current;
	private Block next;
	private long lastGravityTime;
	private long lastRotationTime;

	private int rows;
	private int score;
	private int level;
	private long gravityTime;

	private boolean hitGround;
	private long hitGroundTime;

	private boolean gameOver;
	private EventListener eventListener;
	private BlockChooser blockChooser;

	private long currentTimeMillis;
	private long lastUpdateTime;

	private boolean paused;

	private Vector completedRows = new Vector();

	private Vector pendingTasks = new Vector();

	private int speedIncrementPerLevel;
	private int lastBonus;

	public GameState( GameSettings settings, EventListener eventListener )
	{
		this.settings = settings;
		this.eventListener = eventListener;

		board = new Square[settings.getActualRows()][settings.getColumns()];

		blockChooser = new BiasedBlockChooser(
				settings.getIBias(),
				settings.getJBias(),
				settings.getLBias(),
				settings.getOBias(),
				settings.getSBias(),
				settings.getZBias(),
				settings.getTBias());

		updateNextBlock();
		doNewBlock();

		rows = 0;
		score = 0;
		updateLevelAndGravity();

		currentTimeMillis = 0;
		lastGravityTime = currentTimeMillis;
		lastUpdateTime = System.currentTimeMillis();

		if (settings.getMaxLevel() > settings.getMinLevel())
			speedIncrementPerLevel = (settings.getMinLevelSpeed()-settings.getMaxLevelSpeed())/(settings.getMaxLevel()-settings.getMinLevel());
	}

	public void update()
	{
		if (gameOver)
			return;
		
		if (paused)
		{
			lastUpdateTime = System.currentTimeMillis();
			return;
		}

		long currentUpdateTime = System.currentTimeMillis();
		long timeSlice = currentUpdateTime-lastUpdateTime;
		currentTimeMillis += timeSlice;
		lastUpdateTime = currentUpdateTime;

		while (pendingTasks.size() > 0)
		{
			Runnable runnable = (Runnable)pendingTasks.elementAt(0);
			runnable.run();
			pendingTasks.removeElementAt(0);
			return;
		}

		if (currentTimeMillis-lastRotationTime > settings.getGravityPauseUponRotationTime() && 
				currentTimeMillis-lastGravityTime > gravityTime)
		{
			if (! doMoveDown() && ! hitGround)
			{
				hitGround = true;
				hitGroundTime = 0;
			}
			lastGravityTime = currentTimeMillis;
		}

		if (hitGround)
		{
			hitGroundTime += timeSlice;
			if (hitGroundTime > settings.getLockDownTime())
				lockDownCurrentBlock();
		}
		else
		{
			hitGroundTime -= timeSlice;
			if (hitGroundTime < 0)
				hitGroundTime = 0;
		}
	}

	public Square squareAt( int row, int column )
	{
		return board[row][column];
	}

	public void moveLeft()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (gameOver || paused || current == null)
					return;

				cx--;
				if (collisionCheck())
					cx++;
				else
				{
					if (eventListener != null)
						eventListener.stateChanged();
					checkHitGround();
				}
			}
		});
	}

	public void moveRight()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (gameOver || paused || current == null)
					return;

				cx++;
				if (collisionCheck())
					cx--;
				else
				{
					if (eventListener != null)
						eventListener.stateChanged();
					checkHitGround();
				}
			}
		});
	}

	private boolean doMoveDown()
	{
		if (gameOver || paused || current == null)
			return false;

		cy++;
		if (collisionCheck())
		{
			cy--;
			return false;
		}
		else
		{
			if (eventListener != null)
				eventListener.stateChanged();
		}
		return true;
	}
	
	public void moveDown()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				doMoveDown();
			}
		});
	}

	public void rockBottom()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (gameOver || paused || current == null)
					return;

				do
				{
					cy++;
				}
				while (! collisionCheck());

				cy--;

				if (eventListener != null)
					eventListener.stateChanged();

				if (settings.isLockDownOnRockBottom())
				{
					lockDownCurrentBlock();
				}
				else
				{
					hitGround = true;
					hitGroundTime = 0;
				}
			}
		});
	}

	private boolean testRotation()
	{
		current.rotateCounterClockwise();
		boolean canRotate = ! collisionCheck();
		current.rotateClockwise();
		return canRotate;
	}

	public void rotate()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (gameOver || paused || current == null)
					return;

				boolean rotationOccured = false;

				if (testRotation())
				{
					current.rotateCounterClockwise();
					rotationOccured = true;
				}
				else if (settings.isWallKicksEnabled())
				{
					int originalCx = cx;
					int originalCy = cy;

					cx = originalCx - 1;
					cy = originalCy;
					boolean canRotateLeft = testRotation();

					cx = originalCx + 1;
					cy = originalCy;
					boolean canRotateRight = testRotation();

					cx = originalCx + 2;
					cy = originalCy;
					boolean canRotateRightRight = testRotation();

					cx = originalCx;
					cy = originalCy - 1;
					boolean canRotateUp = testRotation();

					cx = originalCx;
					cy = originalCy + 1;
					boolean canRotateDown = testRotation();

					if (canRotateDown)
					{
						cx = originalCx;
						cy = originalCy + 1;
						current.rotateCounterClockwise();
						rotationOccured = true;
					}
					else if (canRotateUp)
					{
						cx = originalCx;
						cy = originalCy - 1;
						current.rotateCounterClockwise();
						rotationOccured = true;
					}
					else if (canRotateLeft || canRotateRight)
					{
						if (canRotateLeft && ! canRotateRight)
						{
							cx = originalCx - 1;
							cy = originalCy;
							current.rotateCounterClockwise();
							rotationOccured = true;
						}
						else if (canRotateRight && ! canRotateLeft)
						{
							cx = originalCx + 1;
							cy = originalCy;
							current.rotateCounterClockwise();
							rotationOccured = true;
						}
					}
					else if (current.getColumns() == 1 && canRotateRightRight)
					{
						cx = originalCx + 2;
						cy = originalCy;
						current.rotateCounterClockwise();
						rotationOccured = true;
					}
					else
					{
						cx = originalCx;
						cy = originalCy;
					}
				}

				if (rotationOccured)
				{
					if (eventListener != null)
						eventListener.stateChanged();
					lastRotationTime = currentTimeMillis;
					checkHitGround();
				}
			}
		});
	}

	private void checkHitGround()
	{
		if (hitGround)
		{
			cy++;
			if (! collisionCheck())
				hitGround = false;
			cy--;
		}
	}

	private boolean collisionCheck()
	{
		for (int row = 0; row < current.getRows(); row++)
			for (int column = 0; column < current.getColumns(); column++)
			{
				if (current.valueAt(row, column) == 0)
					continue;

				int boardX = cx+column-current.getCx();
				int boardY = cy+row-current.getCy();

				if (boardX < 0 || ! (boardX < settings.getColumns()) || ! (boardY < settings.getActualRows()))
					return true;

				if (! (boardY < 0) && squareAt(boardY,boardX) != null)
					return true;
			}
		return false;
	}
	
	public void doNewBlock()
	{
		current = next;
		cx = (settings.getColumns()-current.getColumns())/2+current.getCx();
		cy = current.getCy()+GameSettings.EXTRA_ROWS-(current.getRows()-1);
		hitGround = false;
		lastGravityTime = currentTimeMillis;
		
		updateNextBlock();

		if (eventListener != null)
			eventListener.stateChanged();

		if (collisionCheck())
			doGameOver();
	}

	private void doGameOver()
	{
		gameOver = true;
		if (eventListener != null)
			eventListener.gameOverOccured();
	}

	private void updateNextBlock()
	{
		next = blockChooser.chooseBlock();
	}

	public Block getCurrentBlock()
	{
		return current;
	}

	public Block getNextBlock()
	{
		return next;
	}

	public Block getPreviousBlock()
	{
		return previous;
	}

	public int getCx()
	{
		return cx;
	}

	public int getCy()
	{
		return cy;
	}

	private void lockDownCurrentBlock()
	{
		Square[][] squares = current.getSquares();
		for (int row = 0; row < current.getRows(); row++)
		{
			for (int column = 0; column < current.getColumns(); column++)
			{
				Square square = squares[row][column];
				if (square != null)
				{
					int y = cy-current.getCy()+row;
					int x = cx-current.getCx()+column;
					board[y][x] = square;
				}
			}
		}
		previous = current;
		current = null;
		score = score + settings.getPointsForBlock();

		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				checkForCompletedRows();
			}
		});

		if (eventListener != null)
			eventListener.lockDownOccured();
	}

	private void checkForCompletedRows()
	{
		completedRows.removeAllElements();
		nextRow:
			for (int row = 0; row < settings.getActualRows(); row++)
			{
				for (int column = 0; column < settings.getColumns(); column++)
				{
					if (squareAt(row, column) == null)
						continue nextRow;
				}
				completedRows.addElement(new Integer(row));
			}

		if (completedRows.size() > 0)
		{
			if (completedRows.size() == 1)
				lastBonus = settings.getPointsForSingle();
			else if (completedRows.size() == 2)
				lastBonus = settings.getPointsForDouble();
			else if (completedRows.size() == 3)
				lastBonus = settings.getPointsForTriple();
			else if (completedRows.size() == 4)
				lastBonus = settings.getPointsForTetris();

			score = score + lastBonus;

			chopBorders();

			emptyCompletedRows();

			pendingTasks.addElement(new Runnable() {
				public void run()
				{
					depressEmptyRows();
					
					pendingTasks.addElement(new Runnable() {
						public void run()
						{
							doNewBlock();
						}
					});
					
					if (eventListener != null)
						eventListener.rowsDepressed(completedRows);
				}
			});

			if (eventListener != null)
				eventListener.rowsCompleted(completedRows);
		}
		else
		{
			doNewBlock();
		}
	}

	private void chopBorders()
	{
		for (int i = 0; i < completedRows.size(); i++)
		{
			Integer completedRow = (Integer)completedRows.elementAt(i);

			if (! (completedRow.intValue()-1 < 0))
			{
				for (int column = 0; column < settings.getColumns(); column++)
				{
					Square square = squareAt(completedRow.intValue()-1,column);
					if (square != null)
						square.setSouthBorder(true);
				}
			}

			if (completedRow.intValue()+1 < settings.getActualRows())
			{
				for (int column = 0; column < settings.getColumns(); column++)
				{
					Square square = squareAt(completedRow.intValue()+1,column);
					if (square != null)
						square.setNorthBorder(true);
				}
			}
		}
	}

	private void emptyCompletedRows()
	{
		for (int i = 0; i < completedRows.size(); i++)
		{
			Integer completedRow = (Integer)completedRows.elementAt(i);
			for (int column = 0; column < settings.getColumns(); column++)
				board[completedRow.intValue()][column] = null;
		}
	}

	private void depressEmptyRows() 
	{
		boolean levelNeedsUpdate = false;

		for (int i = 0; i < completedRows.size(); i++)
		{
			Integer completedRow = (Integer)completedRows.elementAt(i);

			for (int row = completedRow.intValue()-1; row >= 0; row--)
			{
				for (int column = 0; column < settings.getColumns(); column++)
					board[row+1][column] = board[row][column];
			}

			for (int column = 0; column < settings.getColumns(); column++)
				board[0][column] = null;

			rows++;
			levelNeedsUpdate = true;
		}

		if (levelNeedsUpdate)
			updateLevelAndGravity();

		if (eventListener != null)
			eventListener.stateChanged();
	}

	public int getScore()
	{
		return score;
	}

	public int getLevel()
	{
		return level;
	}

	private void updateLevelAndGravity()
	{
		int newLevel = rows/settings.getRowsPerLevel()+settings.getMinLevel();
		if (newLevel > settings.getMaxLevel())
			newLevel = settings.getMaxLevel();
		
		if (newLevel > level)
			level = newLevel;

		updateGravity();
	}

	private void updateGravity()
	{
		gravityTime = settings.getMinLevelSpeed()-(level-settings.getMinLevel())*speedIncrementPerLevel;
	}

	public void levelUp()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (level < settings.getMaxLevel())
				{
					level++;
					updateGravity();
				}
			}
		});
	}

	public void goToColumn( final int targetCx )
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (gameOver || paused || cx == targetCx || current == null)
					return;

				int step = targetCx > cx ? +1 : targetCx < cx ? -1 : 0;
				int originalCx = cx;

				while (cx != targetCx)
				{
					cx += step;
					if (collisionCheck())
					{
						cx -= step;
						break;
					}
				}

				if (originalCx != cx)
				{
					if (eventListener != null)
						eventListener.stateChanged();
					checkHitGround();
				}
			}
		});
	}

	public int getRockBottomCy()
	{
		int originalCy = cy;

		do
		{
			cy++;
		}
		while (! collisionCheck());

		int rockBottomCy = cy-1;

		cy = originalCy;

		return rockBottomCy;
	}

	private void setPaused( boolean paused )
	{
		this.paused = paused;
		if (eventListener != null)
			eventListener.stateChanged();
	}
	
	public void pause()
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				setPaused(true);
			}
		});
	}

	public void resume()
	{
		setPaused(false);
	}

	public boolean isPaused()
	{
		return paused;
	}

	public boolean isGameOver()
	{
		return gameOver;
	}

	public int getRows()
	{
		return rows;
	}

	public int getLastBonus()
	{
		return lastBonus;
	}
}