package com.giannistsakiris.aftetris.me;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.giannistsakiris.aftetris.me.rms.ObjectStore;
import com.giannistsakiris.aftetris.me.rms.ObjectStoreException;
import com.giannistsakiris.aftetris.me.rms.SavedStuff;
import com.giannistsakiris.aftetris.me.screens.CreditsScreen;
import com.giannistsakiris.aftetris.me.screens.EditSettingsScreen;
import com.giannistsakiris.aftetris.me.screens.GameScreen;
import com.giannistsakiris.aftetris.me.screens.InstructionsScreen;
import com.giannistsakiris.aftetris.me.screens.MenuScreen;
import com.giannistsakiris.aftetris.me.screens.ProfilesScreen;

public class Aftetris extends MIDlet
{
	public static final String RECORD_STORE = "Aftetris_Record_Store";
	
	private ObjectStore objectStore;
	private MenuScreen menuScreen;
	private InstructionsScreen instructionsScreen;
	private CreditsScreen creditsScreen;
	private GameScreen gameScreen;
	
	private SavedStuff savedStuff;
	private boolean gameStarted;
	private boolean firstStartApp = true;

	public Aftetris()
	{
		objectStore = new ObjectStore(RECORD_STORE);
		
		menuScreen = new MenuScreen(this);
		instructionsScreen = new InstructionsScreen(this);
		creditsScreen = new CreditsScreen(this);
		
		loadSavedStuff();
		
		if (savedStuff == null)
		{
			savedStuff = new SavedStuff();
			saveSavedStuff();
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException
	{
	}

	protected void pauseApp()
	{
		if (gameStarted)
		{
			gameScreen.pause();
		}
	}

	protected void startApp() throws MIDletStateChangeException
	{
		if (firstStartApp)
		{
			firstStartApp = false;
			goToMenu();
		}
	}
	
	public void startGame()
	{
		gameStarted = true;
		gameScreen = new GameScreen(this);
		Display.getDisplay(this).setCurrent(gameScreen);
	}
	
	public void goToMenu()
	{
		Display.getDisplay(this).setCurrent(menuScreen);
	}
	
	public void goToProfiles()
	{
		Display.getDisplay(this).setCurrent(new ProfilesScreen(this));
	}
	
	public void goToSettings()
	{
		Display.getDisplay(this).setCurrent(new EditSettingsScreen(this));
	}
	
	public void goToInstructions()
	{
		Display.getDisplay(this).setCurrent(instructionsScreen);
	}
	
	public void goToCredits()
	{
		Display.getDisplay(this).setCurrent(creditsScreen);
	}

	public void loadSavedStuff()
    {
	    try
		{
			savedStuff = (SavedStuff)objectStore.loadObject();
		}
		catch (ObjectStoreException ex)
		{
			ex.printStackTrace();
		}
    }

	public void saveSavedStuff()
    {
	    try
		{
	    	objectStore.saveObject(savedStuff);
		}
		catch (ObjectStoreException ex)
		{
			ex.printStackTrace();
		}
    }
	
	public SavedStuff getSavedStuff()
	{
		return savedStuff;
	}
}
