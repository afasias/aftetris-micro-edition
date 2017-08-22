package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Profiles implements Storable
{
	private int current;
	private Vector profiles;
	
	public Profiles()
	{
		profiles = new Vector();
		profiles.addElement(new Profile());
		current = 0;
	}
	
	public void addProfile( Profile profile, boolean setAsCurrent )
	{
		if (setAsCurrent)
			current = size();
		
		profiles.addElement(profile);
	}
	
	public void addProfile( Profile profile )
	{
		addProfile(profile,false);
	}
	
	public void removeProfile( Profile profile )
	{
		profiles.removeElement(profile);
	}
	
	public void removeProfile( int index )
	{
		profiles.removeElementAt(index);
	}
	
	public void removeAllProfiles()
	{
		profiles.removeAllElements();
	}
	
	public int size()
	{
		return profiles.size();
	}
	
	public Profile get( int index )
	{
		return (Profile)profiles.elementAt(index);
	}
	
	public void setCurrent( int index )
	{
		current = index;
	}
	
	public int getCurrent()
	{
		return current;
	}
	
	public Profile getCurrentProfile()
	{
		return get(current);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		int size = dis.readInt();
		removeAllProfiles();
		for (int i = 0; i < size; i++)
		{
			Profile profile = new Profile();
			profile.restore(dis);
			addProfile(profile);
		}
		current = dis.readInt();
	}
	
	public void store(DataOutputStream dos) throws IOException
	{
		dos.writeInt(size());
		for (int i = 0; i < size(); i++)
			get(i).store(dos);
		dos.writeInt(current);
	}
}
