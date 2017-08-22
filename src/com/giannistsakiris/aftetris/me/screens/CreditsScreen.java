package com.giannistsakiris.aftetris.me.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.giannistsakiris.aftetris.me.Aftetris;
import com.giannistsakiris.aftetris.me.images.Images;

public class CreditsScreen extends Form implements CommandListener
{
	private Aftetris aftetris;
	
	public CreditsScreen( Aftetris aftetris )
	{
		super("About");
		
		append("Created by: Giannis Tsakiris");
		append(Images.loadImage("/piperia.jpg"));
		append("This game is FREE software (\"free\" as in \"free beer\"). You should have acquired your copy at no expense. If you have been charged for this software please contact the author at: giannis.tsakiris@gmail.com");
		
		addCommand(new Command("Back", Command.BACK, 1));
		
		setCommandListener(this);
		
		this.aftetris = aftetris;
	}
	
	public void commandAction(Command arg0, Displayable arg1)
	{
		aftetris.goToMenu();
	}
}
