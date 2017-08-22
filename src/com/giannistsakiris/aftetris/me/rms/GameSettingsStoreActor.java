package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.giannistsakiris.aftetris.core.GameSettings;

public class GameSettingsStoreActor implements StoreActor
{
	public void store(DataOutputStream dos, Object object) throws IOException
	{
		GameSettings gameSettings = (GameSettings)object;
		
		dos.writeInt(gameSettings.getColumns());
		dos.writeInt(gameSettings.getRows());
		dos.writeInt(gameSettings.getPointsForBlock());
		dos.writeInt(gameSettings.getPointsForSingle());
		dos.writeInt(gameSettings.getPointsForDouble());
		dos.writeInt(gameSettings.getPointsForTriple());
		dos.writeInt(gameSettings.getPointsForTetris());
		dos.writeInt(gameSettings.getRowsPerLevel());
		dos.writeInt(gameSettings.getMinLevel());
		dos.writeInt(gameSettings.getMaxLevel());
		dos.writeInt(gameSettings.getMinLevelSpeed());
		dos.writeInt(gameSettings.getMaxLevelSpeed());
		dos.writeInt(gameSettings.getLockDownTime());
		dos.writeInt(gameSettings.getIBias());
		dos.writeInt(gameSettings.getJBias());
		dos.writeInt(gameSettings.getLBias());
		dos.writeInt(gameSettings.getOBias());
		dos.writeInt(gameSettings.getSBias());
		dos.writeInt(gameSettings.getZBias());
		dos.writeInt(gameSettings.getTBias());
		dos.writeBoolean(gameSettings.isLockDownOnRockBottom());
		dos.writeBoolean(gameSettings.isWallKicksEnabled());
	}
	
	public void restore(DataInputStream dis, Object object) throws IOException
	{
		GameSettings gameSettings = (GameSettings)object;
		
		gameSettings.setColumns(dis.readInt());
		gameSettings.setRows(dis.readInt());
		gameSettings.setPointsForBlock(dis.readInt());
		gameSettings.setPointsForSingle(dis.readInt());
		gameSettings.setPointsForDouble(dis.readInt());
		gameSettings.setPointsForTriple(dis.readInt());
		gameSettings.setPointsForTetris(dis.readInt());
		gameSettings.setRowsPerLevel(dis.readInt());
		gameSettings.setMinLevel(dis.readInt());
		gameSettings.setMaxLevel(dis.readInt());
		gameSettings.setMinLevelSpeed(dis.readInt());
		gameSettings.setMaxLevelSpeed(dis.readInt());
		gameSettings.setLockDownTime(dis.readInt());
		gameSettings.setIBias(dis.readInt());
		gameSettings.setJBias(dis.readInt());
		gameSettings.setLBias(dis.readInt());
		gameSettings.setOBias(dis.readInt());
		gameSettings.setSBias(dis.readInt());
		gameSettings.setZBias(dis.readInt());
		gameSettings.setTBias(dis.readInt());
		gameSettings.setLockDownOnRockBottom(dis.readBoolean());
		gameSettings.setWallKicksEnabled(dis.readBoolean());
	}
}
