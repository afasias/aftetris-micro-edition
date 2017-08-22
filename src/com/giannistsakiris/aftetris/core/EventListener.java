package com.giannistsakiris.aftetris.core;

import java.util.Vector;

public interface EventListener
{
	public void gameOverOccured();
	public void rowsCompleted(Vector completedRows);
	public void lockDownOccured();
	public void stateChanged();
	public void rowsDepressed(Vector completedRows);
}
