package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.giannistsakiris.aftetris.me.ControlSettings;

public class SavedStuff implements Storable
{
	private ControlSettings controlSettings;
	private Profiles profiles;
	
	public SavedStuff()
	{
		controlSettings = new ControlSettings();
		profiles = new Profiles();
	}
	
	public void setControlSettings(ControlSettings controlSettings)
	{
		this.controlSettings = controlSettings;
	}
	
	public ControlSettings getControlSettings()
	{
		return controlSettings;
	}

	public void setProfiles(Profiles profiles)
	{
		this.profiles = profiles;
	}

	public Profiles getProfiles()
	{
		return profiles;
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		controlSettings = new ControlSettings();
		controlSettings.restore(dis);
		profiles = new Profiles();
		profiles.restore(dis);
	}
	
	public void store(DataOutputStream dos) throws IOException
	{
		controlSettings.store(dos);
		profiles.store(dos);
	}
}
