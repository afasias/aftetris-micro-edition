package com.giannistsakiris.aftetris.me.rms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class ObjectStore
{
	private String recordStoreName;
	
	public ObjectStore(String recordStoreName)
	{
		this.recordStoreName = recordStoreName;
	}
	
	public Storable loadObject() throws ObjectStoreException
	{
		DataInputStream dis = null;
		ByteArrayInputStream bais = null;
		RecordStore rs = null;

		Storable object = null;
		
		try
		{
			rs = RecordStore.openRecordStore(recordStoreName,true);
			
			if (rs.getNumRecords() > 0)
			{
				byte[] bytes = rs.enumerateRecords(null, null, false).nextRecord();
				
				if (bytes == null)
					bytes = new byte[0];

				bais = new ByteArrayInputStream(bytes);
				dis = new DataInputStream(bais);
				
				String className = dis.readUTF();
				object = (Storable)Class.forName(className).newInstance();
				object.restore(dis);

				dis.close();
				dis = null;
				
				bais.close();
				bais = null;
			}
		}
		catch (Exception ex)
		{
			throw new ObjectStoreException(ex);
		}
		finally
		{
			if (dis != null)
				try { dis.close(); } catch (IOException ex) {}
			
			if (bais != null)
				try { bais.close(); } catch (IOException ex) {}
			
			if (rs != null)
				try { rs.closeRecordStore(); } catch (RecordStoreException ex) { }
		}
		
		return object;
	}
	
	public void saveObject( Storable object ) throws ObjectStoreException
	{
		DataOutputStream dos = null;
		ByteArrayOutputStream baos = null;
		RecordStore rs = null;
		
		try
		{
			RecordStore.deleteRecordStore(recordStoreName);
			rs = RecordStore.openRecordStore(recordStoreName,true);
			
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeUTF(object.getClass().getName());
			object.store(dos);
			byte[] bytes = baos.toByteArray();
			rs.addRecord(bytes, 0, bytes.length);
			
			dos.close();
			dos = null;
			
			baos.close();
			baos = null;
		}
		catch (Exception ex)
		{
			throw new ObjectStoreException(ex);
		}
		finally
		{
			if (dos != null)
				try { dos.close(); } catch (IOException ex) {}
			
			if (baos != null)
				try { baos.close(); } catch (IOException ex) {}
			
			if (rs != null)
				try { rs.closeRecordStore(); } catch (RecordStoreException ex) { }
		}
	}
}
