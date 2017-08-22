package com.giannistsakiris.aftetris.me.screens;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.giannistsakiris.aftetris.me.Aftetris;

public class InstructionsScreen extends Form implements CommandListener
{
	private Aftetris aftetris;
	
	public InstructionsScreen( Aftetris aftetris )
	{
		super("Instructions");
		
		append("There is not much to say about a Tetris variant, the story is simple: ");
		append("A random sequence of shapes composed of four square blocks (tetrominoes) each fall down the playing field. ");
		append("The object of the game is to manipulate these tetrominoes, by moving each one sideways and rotating it by 90 degree units, with the aim of creating a horizontal line of blocks without gaps. ");
		append("When such a line is created, it disappears, and any block above the deleted line will fall. ");
		append("As the game progresses, the tetrominoes fall faster, and the game ends when the stack of tetrominoes reaches the top of the playing field and no new tetrominoes are able to enter. ");
		append("[excerpt from Wikipedia, the Free Encyclopedia]");
		append("");
		append("The game can be played both using the arrow keys or alternatively using a pointing device. ");
		append("");
		append("1. Using arrow keys: ");
		append("LEFT or NUM_4: Moves left");
		append("RIGHT or NUM_6: Moves right");
		append("DOWN or NUM_8: Rock bottom");
		append("FIRE or NUM_5: Rotate");
		append("");
		append("2. Using a pointing device:");
		append("Drag to the left: Moves left");
		append("Drag to the right: Moves right");
		append("Drag downwards: Rock bottom");
		append("Press and release: Rotate");
		append("");
		append("In the Profiles section you can customize most of the game's attributes. You can save your setups separately and easily switch from one to another. ");
		append("In the Settings section you can also set a number of parameters related to the controls and appearance. ");
		append("");
		append("Enjoy Tetris!");
		
		addCommand(new Command("Back", Command.BACK, 1));
		
		setCommandListener(this);
		
		this.aftetris = aftetris;
	}
	
	public void commandAction(Command command, Displayable displayable)
	{
		aftetris.goToMenu();
	}
}
