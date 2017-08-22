package com.giannistsakiris.aftetris.me.screens;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.giannistsakiris.aftetris.core.GameSettings;
import com.giannistsakiris.aftetris.me.Aftetris;
import com.giannistsakiris.aftetris.me.rms.Profile;
import com.giannistsakiris.aftetris.me.rms.Profiles;

public class ProfilesScreen extends Form implements CommandListener
{
	private Aftetris aftetris;
	private Profiles profiles;
	
	private Command addProfileCommand;
	private Command editProfileCommand;
	private Command removeProfileCommand;
	private Command okCommand;
	private Command cancelCommand;
	
	private ChoiceGroup choices;
	
	public ProfilesScreen( Aftetris aftetris )
	{
		super("");
		
		this.aftetris = aftetris;
		this.profiles = aftetris.getSavedStuff().getProfiles();
		
		setTitle("Choose Profile");
		
		choices = new ChoiceGroup( "Profiles:", ChoiceGroup.EXCLUSIVE );
		for (int i = 0; i < profiles.size(); i++)
			choices.append(profiles.get(i).getName(),null);
		choices.setSelectedIndex(profiles.getCurrent(),true);
		append(choices);
		
		addCommand(okCommand = new Command("OK",Command.OK,1));
		addCommand(cancelCommand = new Command("Cancel",Command.CANCEL,1));
		addCommand(addProfileCommand = new Command("Add",Command.ITEM,2));
		addCommand(editProfileCommand = new Command("Edit",Command.ITEM,3));
		addCommand(removeProfileCommand = new Command("Remove",Command.ITEM,4));
		
		setCommandListener(this);
	}
	
	public void commandAction(Command command, Displayable displayable)
	{
		if (command == okCommand)
		{
			aftetris.getSavedStuff().getProfiles().setCurrent(choices.getSelectedIndex());
			aftetris.saveSavedStuff();
			aftetris.goToMenu();
		}
		else if (command == cancelCommand)
		{
			aftetris.goToMenu();
		}
		else if (command == addProfileCommand)
		{
			addNewProfileAndHighlight();
			editHighlightedProfile();
		}
		else if (command == editProfileCommand)
		{
			editHighlightedProfile();
		}
		else if (command == removeProfileCommand)
		{
			if (choices.size() == 1)
			{
				Alert alert = new Alert("", "You are not allowed to remove the last profile left.", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
			}
			else
			{
				removeHighlightedProfile();
			}
		}
	}

	private void removeHighlightedProfile()
    {
	    profiles.removeProfile(choices.getSelectedIndex());
	    choices.delete(choices.getSelectedIndex());
	    
	    aftetris.saveSavedStuff();
    }

	private void editHighlightedProfile()
    {
	    final Profile profile = profiles.get(choices.getSelectedIndex());
	    EditProfileScreen editProfileScreen = new EditProfileScreen(aftetris,profile,this);
	    editProfileScreen.addUpdateListener(new UpdateListener() {
	    	public void updated()
	    	{
	    		choices.set(choices.getSelectedIndex(), profile.getName(), null);
	    	}
	    });
	    Display.getDisplay(aftetris).setCurrent(editProfileScreen);
    }

	private void addNewProfileAndHighlight()
    {
	    Profile profile = new Profile();
	    profile.setName("<new profile>");
	    profile.setSettings(new GameSettings());
	    profiles.addProfile(profile);
	    choices.append(profile.getName(),null);
	    choices.setSelectedIndex(choices.size()-1,true);
	    
	    aftetris.saveSavedStuff();
    }
}
