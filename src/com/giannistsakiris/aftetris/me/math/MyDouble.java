package com.giannistsakiris.aftetris.me.math;

public class MyDouble
{
	private static final long PRECISION_FACTOR = 1000;
	
	private long value;
	
	public MyDouble()
	{
		this(0);
	}
	
	public MyDouble( long x )
	{
		assign(x);
	}
	
	public MyDouble( MyDouble x )
	{
		assign(x);
	}
	
	public long integerPart()
	{
		return value/PRECISION_FACTOR;
	}
	
	public long decimalPart()
	{
		return value%PRECISION_FACTOR;
	}
	
	public void divideBy( long x )
	{
		value /= x;
	}
	
	public long longFloor()
	{
		long floor = integerPart();
		if (value < 0 && decimalPart() > 0)
			floor--;
		return floor;
	}
	
	public long longCeil()
	{
		long ceil = integerPart();
		if (value > 0 && decimalPart() > 0)
			ceil++;
		return ceil;
	}
	
	public void assign( long x )
	{
		value = x * PRECISION_FACTOR;
	}
	
	public void add( long x )
	{
		value += x * PRECISION_FACTOR;
	}
	
	public void subtract( long x )
	{
		value -= x * PRECISION_FACTOR;
	}
	
	public void subtract( MyDouble x )
	{
		value -= x.value;
	}
	
	public void assign( MyDouble x )
	{
		value = x.value;
	}
	
	public void multiplyBy( MyDouble x )
	{
		value = (value*x.value)/PRECISION_FACTOR;
	}
	
	public void add( MyDouble x )
	{
		value += x.value;
	}
	
	public void divideBy( MyDouble x )
	{
		value = (value*PRECISION_FACTOR)/x.value;
	}
}
