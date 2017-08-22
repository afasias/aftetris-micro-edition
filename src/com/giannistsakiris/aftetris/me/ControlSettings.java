package com.giannistsakiris.aftetris.me;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.giannistsakiris.aftetris.me.rms.Storable;

public class ControlSettings implements Storable
{
	private int rotationClickMinDuration = 10;
	private int rotationClickMaxDuration = 500;
	private int horizontalPrecision = 10;
	private int verticalPrecision = 10;
	private int keyAutoRepeatPeriod = 50;
	private int keyAutoRepeatDelay = 200;
	private boolean showProjection = true;
	private int updatesPerSecond = 50;
	private boolean repaintOnUpdate = true;
	private int pressAndHoldForFastDown = 500;
	private char moveDownMode = 'm';
	
	public void setRotationClickMinDuration(int rotationClickMinDuration)
	{
		this.rotationClickMinDuration = rotationClickMinDuration;
	}

	public int getRotationClickMinDuration()
	{
		return rotationClickMinDuration;
	}
	
	public void setRotationClickMaxDuration(int rotationClickMaxDuration)
	{
		this.rotationClickMaxDuration = rotationClickMaxDuration;
	}

	public int getRotationClickMaxDuration()
	{
		return rotationClickMaxDuration;
	}

	public void setHorizontalPrecision(int horizontalPrecision)
	{
		this.horizontalPrecision = horizontalPrecision;
	}

	public int getHorizontalPrecision()
	{
		return horizontalPrecision;
	}

	public void setVerticalPrecision(int verticalPrecision)
	{
		this.verticalPrecision = verticalPrecision;
	}

	public int getVerticalPrecision()
	{
		return verticalPrecision;
	}

	public void setKeyAutoRepeatPeriod(int keyAutoRepeatPeriod)
	{
		this.keyAutoRepeatPeriod = keyAutoRepeatPeriod;
	}

	public int getKeyAutoRepeatPeriod()
	{
		return keyAutoRepeatPeriod;
	}

	public void setKeyAutoRepeatDelay(int keyAutoRepeatDelay)
	{
		this.keyAutoRepeatDelay = keyAutoRepeatDelay;
	}

	public int getKeyAutoRepeatDelay()
	{
		return keyAutoRepeatDelay;
	}

	public void setShowProjection(boolean showProjection)
	{
		this.showProjection = showProjection;
	}

	public boolean isShowProjection()
	{
		return showProjection;
	}

	public void setUpdatesPerSecond(int updatesPerSecond)
	{
		this.updatesPerSecond = updatesPerSecond;
	}

	public int getUpdatesPerSecond()
	{
		return updatesPerSecond;
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		rotationClickMinDuration = dis.readInt();
		horizontalPrecision = dis.readInt();
		verticalPrecision = dis.readInt();
		keyAutoRepeatPeriod = dis.readInt();
		keyAutoRepeatDelay = dis.readInt();
		showProjection = dis.readBoolean();
		updatesPerSecond = dis.readInt();
	}
	
	public void store(DataOutputStream dos) throws IOException
	{
		dos.writeInt(rotationClickMinDuration);
		dos.writeInt(horizontalPrecision);
		dos.writeInt(verticalPrecision);
		dos.writeInt(keyAutoRepeatPeriod);
		dos.writeInt(keyAutoRepeatDelay);
		dos.writeBoolean(showProjection);
		dos.writeInt(updatesPerSecond);
	}

	public void setRepaintOnUpdate(boolean repaintOnUpdate)
	{
		this.repaintOnUpdate = repaintOnUpdate;
	}

	public boolean isRepaintOnUpdate()
	{
		return repaintOnUpdate;
	}

	public void setPressAndHoldForFastDown(int pressAndHoldForFastDown)
	{
		this.pressAndHoldForFastDown = pressAndHoldForFastDown;
	}

	public int getPressAndHoldForFastDown()
	{
		return pressAndHoldForFastDown;
	}

	public void setMoveDownMode(char moveDownMode)
	{
		this.moveDownMode = moveDownMode;
	}

	public char getMoveDownMode()
	{
		return moveDownMode;
	}
}
