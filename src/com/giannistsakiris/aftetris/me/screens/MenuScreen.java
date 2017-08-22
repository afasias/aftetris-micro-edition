package com.giannistsakiris.aftetris.me.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import com.giannistsakiris.aftetris.me.Aftetris;

public class MenuScreen extends List implements CommandListener
{
	private Aftetris aftetris;
	
	public MenuScreen( Aftetris aftetris )
	{
		super("Af-Tetris", List.IMPLICIT, new String[] {
				"Play!",
				"Profiles",
				"Settings",
				"Instructions", 
				"About",
				"Exit"
		}, null);

		this.aftetris = aftetris;

		setCommandListener(this);
	}

	public void commandAction(Command command, Displayable displayable)
	{
		switch (getSelectedIndex())
		{
		case 0:
			aftetris.startGame();
			break;
		case 1:
			aftetris.goToProfiles();
			break;
		case 2:
			aftetris.goToSettings();
			break;
		case 3:
			aftetris.goToInstructions();
			break;
		case 4:
			aftetris.goToCredits();
			break;
		case 5:
			aftetris.notifyDestroyed();
			break;
		}
	}
}
