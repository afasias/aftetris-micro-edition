package com.giannistsakiris.aftetris.me.screens;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.giannistsakiris.aftetris.core.Block;
import com.giannistsakiris.aftetris.core.BlockType;
import com.giannistsakiris.aftetris.core.EventListener;
import com.giannistsakiris.aftetris.core.GameSettings;
import com.giannistsakiris.aftetris.core.GameState;
import com.giannistsakiris.aftetris.core.Square;
import com.giannistsakiris.aftetris.me.Aftetris;
import com.giannistsakiris.aftetris.me.ControlSettings;

public class GameScreen extends Canvas implements Runnable, EventListener, CommandListener
{
	private static final int MARGIN = 10;
	private static final int BOARD_THICKNESS = 3;
	private static final int NORMAL_COLOR_FACTOR = 80;
	private static final int LIGHT_COLOR_FACTOR = 100;
	private static final int DARK_COLOR_FACTOR = 65;

	private int squareSize;
	private int boardX;
	private int boardY;
	private int boardWidth;
	private int boardHeight;
	private int horizontalMovementThreshold;
	private int verticalMovementThreshold;

	private boolean running = true;

	private Aftetris aftetris;
	private GameSettings settings;
	private ControlSettings controlSettings;
	private GameState state;

	private Command pauseCommand;
	private Command resumeCommand;
	private Command abandonCommand;
	private Command playCommand;
	private Command menuCommand;
	private Command yesCommand;
	private Command noCommand;

	private int dragX;
	private int dragY;
	private int originalX;
	private int originalY;
	private int originalCx;
	private boolean movingHorizontaly;
	private boolean noMoreDragging;
	private long pointerPressedTime;
	private boolean pointerPressed;

	private Vector completedRows;
	private int flashColor;
	private int bonusX;
	private int bonusY;
	private boolean displayBorder;
	private int[] fallDistance;
	private int[] fallFactor;
	private boolean completedRowsFalling;

	private boolean leftPressed;
	private boolean rightPressed;
	private boolean downPressed;
	private boolean upPressed;
	private boolean rotatePressed;

	private long leftPressedTime;
	private long rightPressedTime;
	private long lastMoveLeftRepeatTime;
	private long lastMoveRightRepeatTime;
	private long lastMoveDownRepeatTime;
	private long downPressedTime;

	private boolean omitNextLockDownAnimation;
	private boolean animating;
	private boolean noRepaintOnStateChanged;

	private int msPerUpdate;
	
	private boolean fastDownInitiated;

	private Vector pendingTasks = new Vector();

	public GameScreen( Aftetris aftetris )
	{
		this.aftetris = aftetris;

		settings = aftetris.getSavedStuff().getProfiles().getCurrentProfile().getSettings();
		controlSettings = aftetris.getSavedStuff().getControlSettings();
		state = new GameState(settings,this);

		squareSize = (getHeight()-MARGIN*2)/(settings.getRows());
		boardWidth = squareSize*settings.getColumns();
		boardHeight = squareSize*settings.getRows();
		boardX = MARGIN;
		boardY = (getHeight()-boardHeight)/2;
		horizontalMovementThreshold = (int)(squareSize*controlSettings.getHorizontalPrecision()/10);
		if (horizontalMovementThreshold < 1)
			horizontalMovementThreshold = 1;
		verticalMovementThreshold = (int)(squareSize*controlSettings.getVerticalPrecision()/10);
		if (verticalMovementThreshold < 1)
			verticalMovementThreshold = 1;
		msPerUpdate = 1000/controlSettings.getUpdatesPerSecond();

		pauseCommand = new Command("Pause", Command.SCREEN, 1 );
		resumeCommand = new Command("Resume", Command.SCREEN, 1 );

		playCommand = new Command("Play", Command.SCREEN, 1);
		menuCommand = new Command("Menu", Command.SCREEN, 2);
		abandonCommand = new Command("Abandon", Command.SCREEN, 2);

		yesCommand = new Command("Yes", Command.SCREEN, 1 );
		noCommand = new Command("No", Command.SCREEN, 1 );

		addCommand(pauseCommand);
		addCommand(abandonCommand);

		setCommandListener(this);

		Thread thread = new Thread(this);
		thread.start();
	}

	protected void paint(Graphics g)
	{
		g.setGrayScale(0);
		g.fillRect(0,0,getWidth(),getHeight());

		g.setColor(0xFF8000);
		g.setStrokeStyle(Graphics.SOLID);
		for (int i = 0; i < BOARD_THICKNESS; i++)
		{
			g.drawLine(boardX-i-1,boardY,boardX-i-1,boardY+settings.getRows()*squareSize+i);
			g.drawLine(boardX-i-1,boardY+settings.getRows()*squareSize+i,boardX+squareSize*settings.getColumns()+i,boardY+settings.getRows()*squareSize+i);
			g.drawLine(boardX+squareSize*settings.getColumns()+i,boardY+settings.getRows()*squareSize+i,boardX+squareSize*settings.getColumns()+i,boardY);
		}
		
		for (int row = 0; row < settings.getRows(); row++)
		{
			for (int column = 0; column < settings.getColumns(); column++)
			{
				int cx = boardX+column*squareSize;
				int cy = boardY+row*squareSize;
				if ((row+column)%2 == 0)
				{
					g.setColor(0x000030);
					g.fillRect(cx, cy, squareSize, squareSize);
				}
			}
		}

		for (int row = 0; row < settings.getActualRows(); row++)
		{
			for (int column = 0; column < settings.getColumns(); column++)
			{
				Square square = state.squareAt(row,column);

				if (square != null)
				{
					int cx = boardX+column*squareSize;
					int cy = boardY+(row-GameSettings.EXTRA_ROWS)*squareSize;
					if (completedRowsFalling)
					{
						cy += squareSize*fallDistance[row]/100-squareSize*fallFactor[row];
					}
					drawSquare(g, cx, cy, square, 0);
				}
			}
		}

		Block currentBlock = state.getCurrentBlock();
		if (currentBlock != null)
		{
			if (completedRows == null && controlSettings.isShowProjection())
				drawBlock(g, boardX+state.getCx()*squareSize, boardY+(state.getRockBottomCy()-GameSettings.EXTRA_ROWS)*squareSize, currentBlock, 1);

			drawBlock(g, boardX+state.getCx()*squareSize, boardY+(state.getCy()-GameSettings.EXTRA_ROWS)*squareSize, currentBlock, 0);
		}

		if (displayBorder)
			drawBlock(g, boardX+state.getCx()*squareSize, boardY+(state.getCy()-GameSettings.EXTRA_ROWS)*squareSize, state.getPreviousBlock(), 2);

		int orgX = getWidth()-(getWidth()-(boardX+boardWidth))/2;
		int stuffHeight = squareSize*settings.getRows()/4;
		int orgY = boardY;

		g.setColor(0xFFFFFF);
		g.drawString("NEXT", orgX, orgY, Graphics.HCENTER|Graphics.TOP);
		drawBlock(g, orgX-squareSize/2, orgY+stuffHeight/2+squareSize/2, state.getNextBlock(), 0);

		orgY += stuffHeight;
		g.setColor(0xFFFFFF);
		g.drawString("LEVEL", orgX, orgY+stuffHeight/3, Graphics.HCENTER|Graphics.TOP);
		g.setColor(0xFFFF00);
		g.drawString(""+state.getLevel(), orgX, orgY+2*stuffHeight/3, Graphics.HCENTER|Graphics.TOP);

		orgY += stuffHeight;
		g.setColor(0xFFFFFF);
		g.drawString("SCORE", orgX, orgY+stuffHeight/3, Graphics.HCENTER|Graphics.TOP);
		g.setColor(0xFFFF00);
		g.drawString(""+state.getScore(), orgX, orgY+2*stuffHeight/3, Graphics.HCENTER|Graphics.TOP);

		orgY += stuffHeight;
		g.setColor(0xFFFFFF);
		g.drawString("ROWS", orgX, orgY+stuffHeight/3, Graphics.HCENTER|Graphics.TOP);
		g.setColor(0xFFFF00);
		g.drawString(""+state.getRows(), orgX, orgY+2*stuffHeight/3, Graphics.HCENTER|Graphics.TOP);

		if (completedRows != null)
		{
			g.setGrayScale(flashColor);
			for (int i = 0; i < completedRows.size(); i++)
			{
				Integer completeRow = (Integer)completedRows.elementAt(i);
				g.fillRect(boardX, boardY+(completeRow.intValue()-GameSettings.EXTRA_ROWS)*squareSize, settings.getColumns()*squareSize, squareSize);
			}

			drawStringWithBorder(g,"+"+state.getLastBonus(), bonusX, bonusY, Graphics.HCENTER|Graphics.TOP, 0x00FF00, 0x008000);
		}

		if (state.isPaused())
			drawStringWithBorder(g, "PAUSED", boardX+boardWidth/2, boardY+boardHeight/2, Graphics.HCENTER|Graphics.BASELINE, 0x00FF00, 0x008000);

		if (state.isGameOver())
			drawStringWithBorder(g, "GAME OVER", boardX+boardWidth/2, boardY+boardHeight/2, Graphics.HCENTER|Graphics.BASELINE, 0xFF0000, 0x000000);
		
		if (fastDownInitiated)
		{
			int orgy = (state.getCy()-GameSettings.EXTRA_ROWS-currentBlock.getCy())*squareSize+boardY;
			int cx = state.getCx()*squareSize+boardX+squareSize/2;
			
			g.setGrayScale(0xFF);
			g.drawLine( cx, orgy-squareSize/2, cx-squareSize/2, orgy-squareSize );
			g.drawLine( cx, orgy-squareSize/2, cx+squareSize/2, orgy-squareSize );
			g.drawLine( cx, orgy-squareSize/2, cx, orgy-squareSize*5/2 );
		}
	}

	private void drawBlock( Graphics g, int x, int y, Block block, int type)
	{
		Square[][] squares = block.getSquares();
		for (int row = 0; row < block.getRows(); row++)
			for (int column = 0; column < block.getColumns(); column++)
			{
				Square square = squares[row][column];
				if (square != null)
				{
					int cx = x+(column-block.getCx())*squareSize;
					int cy = y+(row-block.getCy())*squareSize;

					drawSquare(g, cx, cy, square, type);
				}
			}
	}

	private static int byteValueMultipliedBy( int byteValue, int factor )
	{
		byteValue = (byteValue*factor)/100;

		if (byteValue > 255)
			return 255;
		else
			return byteValue;
	}

	public static int colorMultipliedBy( int color, int factor )
	{
		int alpha = byteValueMultipliedBy((color & 0xFF000000) >> 24, factor);
		int red = byteValueMultipliedBy((color & 0x00FF0000) >> 16, factor);
		int green = byteValueMultipliedBy((color & 0x0000FF00) >> 8, factor);
		int blue = byteValueMultipliedBy((color & 0x000000FF), factor);

		return (alpha << 24) + (red << 16) + (green << 8) + blue;
	}

	private void drawSquare(Graphics g, int cx, int cy, Square square, int type)
	{
		int color;
		int lightColor;
		int darkColor;

		if (type == 1)
		{
			color = 0x000000;
			darkColor = 0xFFFFFF;
			lightColor = 0xFFFFFF;
		}
		else if (type == 2)
		{
			color = 0xFFFFFF;
			darkColor = 0xFFFFFF;
			lightColor = 0xFFFFFF;
		}
		else
		{
			int plainColor = getColorFromBlockType(square.getBlockType());
			color = colorMultipliedBy(plainColor, NORMAL_COLOR_FACTOR);
			lightColor = colorMultipliedBy(plainColor, LIGHT_COLOR_FACTOR);
			darkColor = colorMultipliedBy(plainColor, DARK_COLOR_FACTOR);
		}

		if (type == 0 || type == 1 || type == 2)
		{
			g.setColor(color);
			g.fillRect(cx,cy,squareSize,squareSize);
		}

		if (type == 1)
			g.setStrokeStyle(Graphics.DOTTED);
		else
			g.setStrokeStyle(Graphics.SOLID);

		if (square.isNorthBorder())
		{
			g.setColor(darkColor);
			g.drawLine(cx,cy,cx+squareSize-1,cy);
		}

		if (square.isWestBorder())
		{
			g.setColor(darkColor);
			g.drawLine(cx,cy,cx,cy+squareSize-1);
		}

		if (square.isSouthBorder())
		{
			g.setColor(lightColor);
			g.drawLine(cx,cy+squareSize-1,cx+squareSize-1,cy+squareSize-1);
		}

		if (square.isEastBorder())
		{
			g.setColor(lightColor);
			g.drawLine(cx+squareSize-1,cy,cx+squareSize-1,cy+squareSize-1);
		}
	}

	public void run()
	{
		while (running)
		{
			// remember the starting time
			long cycleStartTime = System.currentTimeMillis();

			if (!animating && !state.isGameOver() && !state.isPaused())
			{
				if (controlSettings.getMoveDownMode() == 'm')
				{
					if (downPressed && System.currentTimeMillis() - downPressedTime > controlSettings.getPressAndHoldForFastDown())
					{
						if (! fastDownInitiated)
						{
							fastDownInitiated = true;
							lastMoveDownRepeatTime = System.currentTimeMillis();
							state.moveDown();
						}
					}
					else if (pointerPressed && ! movingHorizontaly &&
							System.currentTimeMillis() - pointerPressedTime > controlSettings.getPressAndHoldForFastDown())
					{
						fastDownInitiated = true;
					}
				}
				
				if (downPressed)
				{
					if (controlSettings.getMoveDownMode() == 'f' ||
							(controlSettings.getMoveDownMode() == 'm' && fastDownInitiated))
					{
						if (System.currentTimeMillis()-lastMoveDownRepeatTime > controlSettings.getKeyAutoRepeatPeriod())
						{
							lastMoveDownRepeatTime = System.currentTimeMillis();
							state.moveDown();
						}
					}
				}
				else if (leftPressed && System.currentTimeMillis()-leftPressedTime > controlSettings.getKeyAutoRepeatDelay())
				{
					if (System.currentTimeMillis()-lastMoveLeftRepeatTime > controlSettings.getKeyAutoRepeatPeriod())
					{
						lastMoveLeftRepeatTime = System.currentTimeMillis();
						state.moveLeft();
					}
				}
				else if (rightPressed && System.currentTimeMillis()-rightPressedTime > controlSettings.getKeyAutoRepeatDelay())
				{
					if (System.currentTimeMillis()-lastMoveRightRepeatTime > controlSettings.getKeyAutoRepeatPeriod())
					{
						lastMoveRightRepeatTime = System.currentTimeMillis();
						state.moveRight();
					}
				}

				while (pendingTasks.size() > 0)
				{
					Runnable task = (Runnable)pendingTasks.elementAt(0);
					pendingTasks.removeElementAt(0);
					task.run();
				}

				state.update();

				if (controlSettings.isRepaintOnUpdate())
					repaint();
			}

			// sleep if we’ve finished our work early
			long timeSinceStart = (System.currentTimeMillis() - cycleStartTime);
			if (timeSinceStart < msPerUpdate)
			{
				try
				{
					Thread.sleep(msPerUpdate - timeSinceStart);
				}
				catch(InterruptedException e)
				{ }
			}
		}
	}

	protected void keyPressed(final int key)
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				switch (getGameAction(key))
				{
				case LEFT:
					leftKeyPressed();
					break;

				case RIGHT:
					rightKeyPressed();
					break;

				case DOWN:
					downKeyPressed();
					break;

				case UP:
					upKeyPressed();
					break;
					
				case FIRE:
					rotateKeyPressed();
					break;
				}

				switch (key)
				{
				case KEY_NUM4:
					leftKeyPressed();
					break;

				case KEY_NUM8:
				case KEY_NUM5:
					rotateKeyPressed();
					break;

				case KEY_NUM6:
					rightKeyPressed();
					break;

				case KEY_NUM2:
					downKeyPressed();
					break;
				}
			}
		});
	}

	private void rotateKeyPressed()
	{
		if (! rotatePressed)
		{
			state.rotate();
			rotatePressed = true;
		}
	}

	private void downKeyPressed()
	{
		if (! downPressed)
		{
			downPressed = true;
			if (controlSettings.getMoveDownMode() == 'r')
			{
				omitNextLockDownAnimation = true;
				state.rockBottom();
			}
			else if (controlSettings.getMoveDownMode() == 'm')
			{
				fastDownInitiated = false;
				downPressedTime = System.currentTimeMillis();
			}
		}
	}

	private void upKeyPressed()
	{
		if (! upPressed)
		{
			upPressed = true;
			state.levelUp();
		}
	}

	private void rightKeyPressed()
	{
		if (! rightPressed)
		{
			rightPressed = true;
			rightPressedTime = lastMoveRightRepeatTime = System.currentTimeMillis();
			state.moveRight();
		}
	}

	private void leftKeyPressed()
	{
		if (! leftPressed)
		{
			leftPressed = true;
			leftPressedTime = lastMoveLeftRepeatTime = System.currentTimeMillis();
			state.moveLeft();
		}
	}

	protected void keyReleased(final int key)
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				switch (getGameAction(key))
				{
				case LEFT:
					leftKeyReleased();
					break;

				case RIGHT:
					rightKeyReleased();
					break;

				case DOWN:
					downKeyReleased();
					break;

				case UP:
					upKeyReleased();
					break;
					
				case FIRE:
					rotateKeyReleased();
					break;
				}

				switch (key)
				{
				case KEY_NUM4:
					leftKeyReleased();
					break;

				case KEY_NUM8:
				case KEY_NUM5:
					rotateKeyReleased();
					break;

				case KEY_NUM6:
					rightKeyReleased();
					break;

				case KEY_NUM2:
					downKeyReleased();
					break;
				}
			}
		});
	}

	private void rotateKeyReleased()
	{
		rotatePressed = false;
	}

	private void downKeyReleased()
	{
		downPressed = false;
		if (controlSettings.getMoveDownMode() == 'm')
		{
			if (! fastDownInitiated)
			{
				omitNextLockDownAnimation = true;
				state.rockBottom();
			}
			fastDownInitiated = false;
		}
	}

	private void upKeyReleased()
	{
		upPressed = false;
	}

	private void rightKeyReleased()
	{
		rightPressed = false;
	}

	private void leftKeyReleased()
	{
		leftPressed = false;
	}

	private int getColorFromBlockType( BlockType blockType )
	{
		if (blockType == BlockType.I)
			return 0x0000FF;
		else if (blockType == BlockType.O)
			return 0x00FF00;
		else if (blockType == BlockType.T)
			return 0x00FFFF;
		else if (blockType == BlockType.S)
			return 0xFF0000;
		else if (blockType == BlockType.Z)
			return 0xFF00FF;
		else if (blockType == BlockType.L)
			return 0xFFFF00;
		else
			return 0xFFFFFF;
	}

	public void gameOverOccured()
	{
		repaint();
		removeCommand(abandonCommand);
		removeCommand(pauseCommand);
		addCommand(playCommand);
		addCommand(menuCommand);
	}

	public void commandAction(Command command, Displayable displayable)
	{
		if (command == pauseCommand)
		{
			state.pause();
			repaint();
			removeCommand(pauseCommand);
			addCommand(resumeCommand);
		}
		else if (command == resumeCommand)
		{
			state.resume();
			repaint();
			removeCommand(resumeCommand);
			addCommand(pauseCommand);
		}
		else if (command == playCommand)
		{
			removeCommand(playCommand);
			removeCommand(menuCommand);
			addCommand(pauseCommand);
			addCommand(abandonCommand);
			state = new GameState(settings,this);
			pendingTasks.removeAllElements();
		}
		else if (command == menuCommand)
		{
			running = false;
			aftetris.goToMenu();
		}
		else if (command == abandonCommand)
		{
			commandAction(pauseCommand,null);
			Alert alert = new Alert("", "Are you sure that you want to abandon the game?", null, AlertType.CONFIRMATION);
			alert.setTimeout(Alert.FOREVER);
			alert.addCommand(yesCommand);
			alert.addCommand(noCommand);
			alert.setCommandListener(this);
			Display.getDisplay(aftetris).setCurrent(alert,this);
		}
		else if (command == yesCommand)
		{
			commandAction(menuCommand,null);
		}
		else if (command == noCommand)
		{
			Display.getDisplay(aftetris).setCurrent(this);
		}

	}

	protected void pointerPressed(final int x, final int y)
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				dragX = x;
				dragY = y;
				originalX = x;
				originalY = y;
				originalCx = state.getCx();
				movingHorizontaly = false;
				noMoreDragging = false;
				pointerPressed = true;
				pointerPressedTime = System.currentTimeMillis();
				fastDownInitiated = false;
			}
		});
	}


	protected void pointerDragged(final int x, final int y)
	{
		repaint();
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (noMoreDragging)
					return;

				if (controlSettings.getMoveDownMode() != 'm' || !fastDownInitiated)
				{
					if (Math.abs(x-originalX) >= horizontalMovementThreshold)
						movingHorizontaly = true;
				}

				if (movingHorizontaly)
				{
					int newCx = originalCx;

					if (x-dragX >= horizontalMovementThreshold)
						newCx = originalCx+(x-dragX)/horizontalMovementThreshold;
					else if (dragX-x >= horizontalMovementThreshold)
						newCx = originalCx-(dragX-x)/horizontalMovementThreshold;

					if (newCx != originalCx)
					{
						state.goToColumn(newCx);
						originalCx = newCx;
						dragX = x;
					}
					return;
				}
				
				if (y-dragY > verticalMovementThreshold)
				{
					if (controlSettings.getMoveDownMode() == 'r')
					{
						if (! movingHorizontaly)
						{
							omitNextLockDownAnimation = true;
							state.rockBottom();
							noMoreDragging = true;
						}
					}
					else if (controlSettings.getMoveDownMode() == 'f')
					{
						state.moveDown();
						dragY = y;
					}
					else if (controlSettings.getMoveDownMode() == 'm')
					{
						if (fastDownInitiated)
						{
							state.moveDown();
							dragY = y;
						}
						else
						{
							if (! movingHorizontaly)
							{
								omitNextLockDownAnimation = true;
								state.rockBottom();
								noMoreDragging = true;
							}
						}
					}
				}
			}
		});
	}

	protected void pointerReleased(final int x, final int y)
	{
		pendingTasks.addElement(new Runnable() {
			public void run()
			{
				if (! movingHorizontaly && ! noMoreDragging &&
						Math.abs(x-originalX) < horizontalMovementThreshold && Math.abs(y-originalY) < verticalMovementThreshold &&
						System.currentTimeMillis()-pointerPressedTime > controlSettings.getRotationClickMinDuration() &&
						System.currentTimeMillis()-pointerPressedTime < controlSettings.getRotationClickMaxDuration())
					state.rotate();
				
				pointerPressed = false;
				fastDownInitiated = false;
			}
		});
	}

	public static void drawStringWithBorder( Graphics g, String text, int x, int y, int pos, int fgColor, int bgColor )
	{
		g.setColor(bgColor);
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				g.drawString(text, x+i, y+j, pos);

		g.setColor(fgColor);
		g.drawString(text, x, y, pos);
	}

	public void rowsCompleted(final Vector newCompletedRows)
	{
		animating = true;
		Thread thread = new Thread(new Runnable() {
			public void run()
			{
				completedRows = newCompletedRows;
				long totalAnimationTime = 500;
				int steps = 25;
				for (int i = 0; i <= steps; i++)
				{
					flashColor = 0xFF-0xFF*i/steps;
					bonusX = boardX+state.getCx()*squareSize;
					bonusY = boardY+(((Integer)completedRows.elementAt(0)).intValue()-GameSettings.EXTRA_ROWS-1)*squareSize-(i*squareSize*2/steps);
					repaint();
					try { Thread.sleep(totalAnimationTime/steps); }
					catch (InterruptedException ex) { }
				}
				completedRows = null;
				noRepaintOnStateChanged = true;
				animating = false;
			}
		});
		thread.start();
	}

	public void rowsDepressed(final Vector newCompletedRows)
	{
		animating = true;
		Thread thread = new Thread(new Runnable() {
			public void run()
			{
				fallFactor = new int[settings.getActualRows()];
				
				for (int i = 0; i < newCompletedRows.size(); i++)
				{
					int row = ((Integer)newCompletedRows.elementAt(i)).intValue();
					for (int k = 0; k < row; k++)
						fallFactor[k]++;
				}
				
				for (int i = 0; i < newCompletedRows.size(); i++)
				{
					int row = ((Integer)newCompletedRows.elementAt(i)).intValue();
					for (int k = 0; k < row; k++)
						fallFactor[k+1] = fallFactor[i];
					fallFactor[0] = 0;
				}
				
				boolean hasFallingBlocks = false;
				for (int i = 0; i < settings.getActualRows(); i++)
					if (fallFactor[i] > 0)
					{
						hasFallingBlocks = true;
						break;
					}
				
				if (hasFallingBlocks)
				{
					fallDistance = new int[settings.getActualRows()];
					int speed = 0;
					completedRowsFalling = true;
					int count;
					do
					{
						speed += 5;
						count = 0;
						for (int i = 0; i < settings.getActualRows(); i++)
						{
							fallDistance[i] += speed;
							if (fallDistance[i] > fallFactor[i]*100)
							{
								fallDistance[i] = fallFactor[i]*100;
								count++;
							}
						}
						repaint();
						try { Thread.sleep(15); }
						catch (InterruptedException ex) { }
					}
					while (count < settings.getActualRows());
				}
				
				completedRowsFalling = false;
				animating = false;
				noRepaintOnStateChanged = true;
			}
		});
		thread.start();
	}

	public void pause()
	{
		if (! state.isPaused() && ! state.isGameOver())
		{
			commandAction(pauseCommand,null);
		}
	}

	public void lockDownOccured()
	{
		if (omitNextLockDownAnimation)
		{
			omitNextLockDownAnimation = false;
		}
		else
		{
			animating = true;
			Thread thread = new Thread(new Runnable() {
				public void run()
				{
					displayBorder = true;
					repaint();
					try { Thread.sleep(50); }
					catch (InterruptedException ex) { }
					displayBorder = false;
					repaint();
					try { Thread.sleep(50); }
					catch (InterruptedException ex) { }
					animating = false;
				}
			});
			thread.start();
		}
	}

	public void stateChanged()
	{
		if (! noRepaintOnStateChanged)
			repaint();
	}
}
