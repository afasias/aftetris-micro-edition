package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.giannistsakiris.aftetris.core.GameSettings;

public class Profile implements Storable
{
	private StoreActor settingsStoreActor = new GameSettingsStoreActor();
	
	private String name;
	private GameSettings settings;
	private long highScore;
	private String highScorer;
	
	public Profile()
	{
		name = "Default";
		settings = new GameSettings();
		highScore = 0;
		highScorer = "-";
	}
	
	public void setName(String name)
    {
	    this.name = name;
    }
	
	public String getName()
    {
	    return name;
    }

	public void setSettings(GameSettings settings)
    {
	    this.settings = settings;
    }

	public GameSettings getSettings()
    {
	    return settings;
    }
	
	public void restore(DataInputStream dis) throws IOException
	{
		name = dis.readUTF();
		settings = new GameSettings();
		settingsStoreActor.restore(dis,settings);
		highScore = dis.readLong();
		highScorer = dis.readUTF();
	}
	
	public void store(DataOutputStream dos) throws IOException
	{
		dos.writeUTF(name);
		settingsStoreActor.store(dos,settings);
		dos.writeLong(highScore);
		dos.writeUTF(highScorer);
	}

	public void setHighScore(long highScore)
	{
		this.highScore = highScore;
	}

	public long getHighScore()
	{
		return highScore;
	}

	public void setHighScorer(String highScorer)
	{
		this.highScorer = highScorer;
	}

	public String getHighScorer()
	{
		return highScorer;
	}
}
