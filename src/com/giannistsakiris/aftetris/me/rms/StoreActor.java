package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface StoreActor
{
	public void store(DataOutputStream dos, Object object) throws IOException;
	public void restore(DataInputStream dis, Object object)  throws IOException;
}
