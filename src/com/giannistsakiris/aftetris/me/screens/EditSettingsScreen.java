package com.giannistsakiris.aftetris.me.screens;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.giannistsakiris.aftetris.me.Aftetris;
import com.giannistsakiris.aftetris.me.ControlSettings;

public class EditSettingsScreen extends Form implements CommandListener
{
	private Aftetris aftetris;
	private ControlSettings settings;

	private TextField rotationClickMinDurationTextField;
	private TextField rotationClickMaxDurationTextField;
	private TextField horizontalPrecisionTextField;
	private TextField verticalPrecisionTextField;
	private TextField keyAutoRepeatPeriodTextField;
	private TextField keyAutoRepeatDelayTextField;
	private TextField showProjectionTextField;
	private TextField updatesPerSecondTextField;
	private TextField repaintOnUpdateTextField;
	private TextField moveDownModeTextField;
	private TextField pressAndHoldForDastDownTextField;
	
	private Vector listeners = new Vector();

	private Command okCommand;

	public EditSettingsScreen( Aftetris aftetris )
	{
		super(null);

		this.aftetris = aftetris;
		this.settings = aftetris.getSavedStuff().getControlSettings();

		setTitle("Settings");
		
		moveDownModeTextField = new TextField("Move Down Mode (r/f/m):", String.valueOf(settings.getMoveDownMode()), 1, TextField.ANY);
		append(moveDownModeTextField);

		pressAndHoldForDastDownTextField = new TextField("Press & Hold for Fast Down (ms):", ""+settings.getPressAndHoldForFastDown(), 4, TextField.NUMERIC);
		append(pressAndHoldForDastDownTextField);
		
		rotationClickMinDurationTextField = new TextField("Min. Click duration for Rotation (ms):", ""+settings.getRotationClickMinDuration(), 3, TextField.NUMERIC);
		append(rotationClickMinDurationTextField);
		
		rotationClickMaxDurationTextField = new TextField("Max. Click duration for Rotation (ms):", ""+settings.getRotationClickMaxDuration(), 4, TextField.NUMERIC);
		append(rotationClickMaxDurationTextField);
		
		horizontalPrecisionTextField = new TextField("Horizontal precision:", ""+settings.getHorizontalPrecision(), 2, TextField.NUMERIC);
		append(horizontalPrecisionTextField);

		verticalPrecisionTextField = new TextField("Vertical precision:", ""+settings.getVerticalPrecision(), 2, TextField.NUMERIC);
		append(verticalPrecisionTextField);

		keyAutoRepeatDelayTextField = new TextField("Key Auto-Repeat Delay (ms):", ""+settings.getKeyAutoRepeatDelay(), 4, TextField.NUMERIC);
		append(keyAutoRepeatDelayTextField);
		
		keyAutoRepeatPeriodTextField = new TextField("Key Auto-Repeat Period (ms):", ""+settings.getKeyAutoRepeatPeriod(), 4, TextField.NUMERIC);
		append(keyAutoRepeatPeriodTextField);
		
		showProjectionTextField = new TextField("Show Projection (y/n):", settings.isShowProjection() ? "y" : "n", 1, TextField.ANY);
		append(showProjectionTextField);
		
		updatesPerSecondTextField = new TextField("Cycles per second:", ""+settings.getUpdatesPerSecond(), 3, TextField.NUMERIC);
		append(updatesPerSecondTextField);

		repaintOnUpdateTextField = new TextField("Repaint on Update (y/n):", settings.isRepaintOnUpdate() ? "y" : "n", 1, TextField.ANY);
		append(repaintOnUpdateTextField);
		
		addCommand(okCommand = new Command("OK",Command.OK,1));
		addCommand(new Command("Cancel",Command.CANCEL,2));

		setCommandListener(this);
	}

	public void commandAction(Command command, Displayable displayable)
	{
		if (command == okCommand)
		{
			String moveDownModeText = moveDownModeTextField.getString().toLowerCase();
			if (! (moveDownModeText.equals("r") || moveDownModeText.equals("f") || moveDownModeText.equals("m")))
			{
				Alert alert = new Alert("", "The field 'Move Down Mode' accepts the values 'r', 'f' or 'm'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			char moveDownMode = moveDownModeText.charAt(0);

			int pressAndHoldForFastDown = Integer.parseInt(pressAndHoldForDastDownTextField.getString());
			int rotationClickMinDuration = Integer.parseInt(rotationClickMinDurationTextField.getString());
			int rotationClickMaxDuration = Integer.parseInt(rotationClickMaxDurationTextField.getString());

			int horizontalPrecision = Integer.parseInt(horizontalPrecisionTextField.getString());
			int verticalPrecision = Integer.parseInt(verticalPrecisionTextField.getString());
			
			int keyAutoRepeatDelay = Integer.parseInt(keyAutoRepeatDelayTextField.getString());
			if (keyAutoRepeatDelay < 10)
			{
				Alert alert = new Alert("", "The field 'Key Auto-Repeat Delay' accepts values in the range 10-999", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			int keyAutoRepeatPeriod = Integer.parseInt(keyAutoRepeatPeriodTextField.getString());
			if (keyAutoRepeatPeriod < 10)
			{
				Alert alert = new Alert("", "The field 'Key Auto-Repeat Period' accepts values in the range 10-999", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			String showProjectionText = showProjectionTextField.getString().toLowerCase();
			if (! (showProjectionText.equals("y") || showProjectionText.equals("n")))
			{
				Alert alert = new Alert("", "The field 'Show Projection' accepts the values 'y' or 'n'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			boolean showProjection = showProjectionText.equals("y");
			
			int updatesPerSecond = Integer.parseInt(updatesPerSecondTextField.getString());

			String repaintOnUpdateText = repaintOnUpdateTextField.getString().toLowerCase();
			if (! (repaintOnUpdateText.equals("y") || repaintOnUpdateText.equals("n")))
			{
				Alert alert = new Alert("", "The field 'Repaint on Update' accepts the values 'y' or 'n'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			boolean repaintOnUpdate = repaintOnUpdateText.equals("y");
			
			settings.setHorizontalPrecision(horizontalPrecision);
			settings.setKeyAutoRepeatDelay(keyAutoRepeatDelay);
			settings.setKeyAutoRepeatPeriod(keyAutoRepeatPeriod);
			settings.setRotationClickMinDuration(rotationClickMinDuration);
			settings.setRotationClickMaxDuration(rotationClickMaxDuration);
			settings.setShowProjection(showProjection);
			settings.setVerticalPrecision(verticalPrecision);
			settings.setUpdatesPerSecond(updatesPerSecond);
			settings.setRepaintOnUpdate(repaintOnUpdate);
			settings.setPressAndHoldForFastDown(pressAndHoldForFastDown);
			settings.setMoveDownMode(moveDownMode);

			notifyListeners();
			aftetris.saveSavedStuff();
		}
		aftetris.goToMenu();
	}
	
	public void addUpdateListener( UpdateListener listener )
	{
		listeners.addElement(listener);
	}
	
	private void notifyListeners()
	{
		for (int i = 0; i < listeners.size(); i++)
			((UpdateListener)listeners.elementAt(i)).updated();
	}
}
