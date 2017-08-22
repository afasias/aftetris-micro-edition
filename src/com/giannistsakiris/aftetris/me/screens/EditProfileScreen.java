package com.giannistsakiris.aftetris.me.screens;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

import com.giannistsakiris.aftetris.me.Aftetris;
import com.giannistsakiris.aftetris.me.images.Images;
import com.giannistsakiris.aftetris.me.rms.Profile;

public class EditProfileScreen extends Form implements CommandListener
{
	private Aftetris aftetris;
	private Profile profile;
	private Displayable caller;

	private TextField nameTextField;
	private TextField rowsTextField;
	private TextField columnsTextField;
	private TextField pointsForBlockTextField;
	private TextField pointsForSingleTextField;
	private TextField pointsForDoubleTextField;
	private TextField pointsForTripleTextField;
	private TextField pointsForTetrisTextField;
	private TextField rowsPerLevelTextField;
	private TextField minLevelTextField;
	private TextField maxLevelTextField;
	private TextField minLevelSpeedTextField;
	private TextField maxLevelSpeedTextField;
	private TextField lockDownTimeTextField;
	private TextField iBiasTextField;
	private TextField jBiasTextField;
	private TextField lBiasTextField;
	private TextField oBiasTextField;
	private TextField sBiasTextField;
	private TextField zBiasTextField;
	private TextField tBiasTextField;
	private TextField lockDownOnRockBottomTextField;
	private TextField wallKicksEnabledTextField;
	private TextField gravityPauseUponRotationTimeTextField;
	
	private Image iBlockImage = Images.loadImage("/I.jpg");
	private Image jBlockImage = Images.loadImage("/J.jpg");
	private Image lBlockImage = Images.loadImage("/L.jpg");
	private Image oBlockImage = Images.loadImage("/O.jpg");
	private Image sBlockImage = Images.loadImage("/S.jpg");
	private Image tBlockImage = Images.loadImage("/T.jpg");
	private Image zBlockImage = Images.loadImage("/Z.jpg");
	
	private Vector listeners = new Vector();

	private Command okCommand;

	public EditProfileScreen( Aftetris aftetris, Profile profile, Displayable caller )
	{
		super(null);

		this.aftetris = aftetris;
		this.profile = profile;
		this.caller = caller;

		setTitle("Edit Profile");

		nameTextField = new TextField("Name:", profile.getName(), 20, TextField.ANY);
		append(nameTextField);

		rowsTextField = new TextField("Rows:", ""+profile.getSettings().getRows(), 2, TextField.NUMERIC);
		append(rowsTextField);

		columnsTextField = new TextField("Columns:", ""+profile.getSettings().getColumns(), 2, TextField.NUMERIC);
		append(columnsTextField);

		pointsForBlockTextField = new TextField("Points per lock-down:", ""+profile.getSettings().getPointsForBlock(), 5, TextField.NUMERIC);
		append(pointsForBlockTextField);

		pointsForSingleTextField = new TextField("Bonus for Single-row:", ""+profile.getSettings().getPointsForSingle(), 5, TextField.NUMERIC);
		append(pointsForSingleTextField);

		pointsForDoubleTextField = new TextField("Bonus for Double-row:", ""+profile.getSettings().getPointsForDouble(), 5, TextField.NUMERIC);
		append(pointsForDoubleTextField);

		pointsForTripleTextField = new TextField("Bonus for Triple-row:", ""+profile.getSettings().getPointsForTriple(), 5, TextField.NUMERIC);
		append(pointsForTripleTextField);

		pointsForTetrisTextField = new TextField("Bonus for Tetris:", ""+profile.getSettings().getPointsForTetris(), 5, TextField.NUMERIC);
		append(pointsForTetrisTextField);

		rowsPerLevelTextField = new TextField("Rows per level:", ""+profile.getSettings().getRowsPerLevel(), 3, TextField.NUMERIC);
		append(rowsPerLevelTextField);

		minLevelTextField = new TextField("Min. Level:", ""+profile.getSettings().getMinLevel(), 3, TextField.NUMERIC);
		append(minLevelTextField);

		maxLevelTextField = new TextField("Max. Level:", ""+profile.getSettings().getMaxLevel(), 3, TextField.NUMERIC);
		append(maxLevelTextField);
		
		minLevelSpeedTextField = new TextField("Min. Level Speed (ms):", ""+profile.getSettings().getMinLevelSpeed(), 4, TextField.NUMERIC);
		append(minLevelSpeedTextField);
		
		maxLevelSpeedTextField = new TextField("Max. Level Speed (ms):", ""+profile.getSettings().getMaxLevelSpeed(), 4, TextField.NUMERIC);
		append(maxLevelSpeedTextField);

		lockDownTimeTextField = new TextField("Lock-down time (ms):", ""+profile.getSettings().getLockDownTime(), 4, TextField.NUMERIC);
		append(lockDownTimeTextField);
		
		lockDownOnRockBottomTextField = new TextField("Lock-down on Rock Bottom (y/n):", profile.getSettings().isLockDownOnRockBottom() ? "y" : "n", 1, TextField.ANY);
		append(lockDownOnRockBottomTextField);
		
		wallKicksEnabledTextField = new TextField("Wall Kicks enabled (y/n):", profile.getSettings().isWallKicksEnabled() ? "y" : "n", 1, TextField.ANY);
		append(wallKicksEnabledTextField);
		
		gravityPauseUponRotationTimeTextField = new TextField("Gravity Pause upon Rotation Time (ms):", ""+profile.getSettings().getGravityPauseUponRotationTime() , 4, TextField.ANY);
		append(gravityPauseUponRotationTimeTextField);
		
		append(iBlockImage);
		iBiasTextField = new TextField("I-block possibility:", ""+profile.getSettings().getIBias(), 1, TextField.NUMERIC);
		append(iBiasTextField);
		
		append(jBlockImage);
		jBiasTextField = new TextField("J-block possibility:", ""+profile.getSettings().getJBias(), 1, TextField.NUMERIC);
		append(jBiasTextField);
		
		append(lBlockImage);
		lBiasTextField = new TextField("L-block possibility:", ""+profile.getSettings().getLBias(), 1, TextField.NUMERIC);
		append(lBiasTextField);
		
		append(oBlockImage);
		oBiasTextField = new TextField("O-block possibility:", ""+profile.getSettings().getOBias(), 1, TextField.NUMERIC);
		append(oBiasTextField);
		
		append(sBlockImage);
		sBiasTextField = new TextField("S-block possibility:", ""+profile.getSettings().getSBias(), 1, TextField.NUMERIC);
		append(sBiasTextField);
		
		append(zBlockImage);
		zBiasTextField = new TextField("Z-block possibility:", ""+profile.getSettings().getZBias(), 1, TextField.NUMERIC);
		append(zBiasTextField);
		
		append(tBlockImage);
		tBiasTextField = new TextField("T-block possibility:", ""+profile.getSettings().getTBias(), 1, TextField.NUMERIC);
		append(tBiasTextField);

		addCommand(okCommand = new Command("OK",Command.OK,1));
		addCommand(new Command("Cancel",Command.CANCEL,2));

		setCommandListener(this);
	}

	public void commandAction(Command command, Displayable displayable)
	{
		if (command == okCommand)
		{
			String name = nameTextField.getString().trim();
			if (name.equals(""))
			{
				Alert alert = new Alert("", "The field 'Name' cannot be left empty", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}

			int rows = Integer.parseInt(rowsTextField.getString());
			if (rows < 4)
			{
				Alert alert = new Alert("", "The field 'Rows' accepts values in the range 4-99", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}

			int columns = Integer.parseInt(columnsTextField.getString());
			if (columns < 4)
			{
				Alert alert = new Alert("", "The field 'Columns' accepts values in the range 4-99", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}

			int pointsForBlock = Integer.parseInt(pointsForBlockTextField.getString());
			int pointsForSingle = Integer.parseInt(pointsForSingleTextField.getString());
			int pointsForDouble = Integer.parseInt(pointsForDoubleTextField.getString());
			int pointsForTriple = Integer.parseInt(pointsForTripleTextField.getString());
			int pointsForTetris = Integer.parseInt(pointsForTetrisTextField.getString());
			
			int rowsPerLevel = Integer.parseInt(rowsPerLevelTextField.getString());
			if (rowsPerLevel < 1)
			{
				Alert alert = new Alert("", "The field 'Rows per Level' accepts values in the range 1-999", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			int minLevel = Integer.parseInt(minLevelTextField.getString());
			int maxLevel = Integer.parseInt(maxLevelTextField.getString());
			if (! (minLevel <= maxLevel))
			{
				Alert alert = new Alert("", "The value in the field 'Min. Level' should be less than or equal to the value of the field 'Max. Level'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}

			int minLevelSpeed = Integer.parseInt(minLevelSpeedTextField.getString());
			if (minLevelSpeed < 10)
			{
				Alert alert = new Alert("", "The field 'Min. Level Speed' accepts values in the range 10-9999", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			int maxLevelSpeed = Integer.parseInt(maxLevelSpeedTextField.getString());
			if (maxLevelSpeed < 10)
			{
				Alert alert = new Alert("", "The field 'Max. Level Speed' accepts values in the range 10-9999", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			if (! (minLevelSpeed >= maxLevel))
			{
				Alert alert = new Alert("", "The value in the field 'Min. Level Speed' should be greater than or equal to the value of the field 'Max. Level Speed'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			
			int lockDownTime = Integer.parseInt(lockDownTimeTextField.getString());
			int gravityPauseUponRotationTime = Integer.parseInt(gravityPauseUponRotationTimeTextField.getString());

			int iBias = Integer.parseInt(iBiasTextField.getString());
			int jBias = Integer.parseInt(jBiasTextField.getString());
			int lBias = Integer.parseInt(lBiasTextField.getString());
			int oBias = Integer.parseInt(oBiasTextField.getString());
			int sBias = Integer.parseInt(sBiasTextField.getString());
			int zBias = Integer.parseInt(zBiasTextField.getString());
			int tBias = Integer.parseInt(tBiasTextField.getString());
			
			String lockDownOnRockBottomText = lockDownOnRockBottomTextField.getString().toLowerCase();
			if (! (lockDownOnRockBottomText.equals("y") || lockDownOnRockBottomText.equals("n")))
			{
				Alert alert = new Alert("", "The field 'Lock-down on Rock Bottom' accepts the values 'y' or 'n'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			boolean lockDownOnRockBottom = lockDownOnRockBottomText.equals("y");

			String wallKicksEnabledText = wallKicksEnabledTextField.getString().toLowerCase();
			if (! (wallKicksEnabledText.equals("y") || wallKicksEnabledText.equals("n")))
			{
				Alert alert = new Alert("", "The field 'Wall Kicks enabled' accepts the values 'y' or 'n'", null, AlertType.INFO);
				Display.getDisplay(aftetris).setCurrent(alert,this);
				return;
			}
			boolean wallKicksEnabled = wallKicksEnabledText.equals("y");

			profile.setName(name);
			profile.getSettings().setRows(rows);
			profile.getSettings().setColumns(columns);
			profile.getSettings().setPointsForBlock(pointsForBlock);
			profile.getSettings().setPointsForSingle(pointsForSingle);
			profile.getSettings().setPointsForDouble(pointsForDouble);
			profile.getSettings().setPointsForTriple(pointsForTriple);
			profile.getSettings().setPointsForTetris(pointsForTetris);
			profile.getSettings().setRowsPerLevel(rowsPerLevel);
			profile.getSettings().setMinLevel(minLevel);
			profile.getSettings().setMaxLevel(maxLevel);
			profile.getSettings().setMinLevelSpeed(minLevelSpeed);
			profile.getSettings().setMaxLevelSpeed(maxLevelSpeed);
			profile.getSettings().setLockDownTime(lockDownTime);
			profile.getSettings().setIBias(iBias);
			profile.getSettings().setJBias(jBias);
			profile.getSettings().setLBias(lBias);
			profile.getSettings().setOBias(oBias);
			profile.getSettings().setSBias(sBias);
			profile.getSettings().setZBias(zBias);
			profile.getSettings().setTBias(tBias);
			profile.getSettings().setLockDownOnRockBottom(lockDownOnRockBottom);
			profile.getSettings().setWallKicksEnabled(wallKicksEnabled);
			profile.getSettings().setGravityPauseUponRotationTime(gravityPauseUponRotationTime);

			notifyListeners();
			aftetris.saveSavedStuff();
		}
		Display.getDisplay(aftetris).setCurrent(caller);
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
