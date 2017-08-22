package com.giannistsakiris.aftetris.me.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Storable
{
	public void store(DataOutputStream dos) throws IOException;
	public void restore(DataInputStream dis)  throws IOException;
}
