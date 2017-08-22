package com.giannistsakiris.aftetris.me.rms;

public class ObjectStoreException extends Exception
{
	private static final long serialVersionUID = 1L;

	private Exception exception;
	
	public ObjectStoreException( Exception exception )
	{
		this.exception = exception;
	}
	
	public Exception getException()
	{
		return exception;
	}
	
	public String getMessage()
	{
		return exception.getMessage();
	}
}
