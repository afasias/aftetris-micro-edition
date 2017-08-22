package com.giannistsakiris.aftetris.core;

public class BlockStates
{
	public static final BlockStates I_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,1,1}, 1, 4, 0, 2 ),
			new BlockState( new byte[] {1,1,1,1}, 4, 1, 2, 0 ),
	});
	
	public static final BlockStates J_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,1,0,0,1}, 2, 3, 0, 1 ),
			new BlockState( new byte[] {1,1,1,0,1,0}, 3, 2, 1, 0 ),
			new BlockState( new byte[] {1,0,0,1,1,1}, 2, 3, 1, 1 ),
			new BlockState( new byte[] {0,1,0,1,1,1}, 3, 2, 1, 1 ),
	});
	
	public static final BlockStates L_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,1,1,0,0}, 2, 3, 0, 1 ),
			new BlockState( new byte[] {1,0,1,0,1,1}, 3, 2, 1, 0 ),
			new BlockState( new byte[] {0,0,1,1,1,1}, 2, 3, 1, 1 ),
			new BlockState( new byte[] {1,1,0,1,0,1}, 3, 2, 1, 1 ),
	});
	
	public static final BlockStates O_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,1,1}, 2, 2, 0, 0 ),
	});

	public static final BlockStates S_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {0,1,1,1,1,0}, 2, 3, 1, 1 ),
			new BlockState( new byte[] {1,0,1,1,0,1}, 3, 2, 1, 1 ),
	});

	public static final BlockStates Z_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,0,0,1,1}, 2, 3, 1, 1 ),
			new BlockState( new byte[] {0,1,1,1,1,0}, 3, 2, 1, 0 ),
	});

	public static final BlockStates T_BLOCK_STATES = new BlockStates( new BlockState[] {
			new BlockState( new byte[] {1,1,1,0,1,0}, 2, 3, 0, 1 ),
			new BlockState( new byte[] {1,0,1,1,1,0}, 3, 2, 1, 0 ),
			new BlockState( new byte[] {0,1,0,1,1,1}, 2, 3, 1, 1 ),
			new BlockState( new byte[] {0,1,1,1,0,1}, 3, 2, 1, 1 ),
	});
	
	private BlockState[] states;
	
	private BlockStates( BlockState[] states )
	{
		this.states = states;
	}
	
	public int length()
	{
		return states.length;
	}
	
	public BlockState getAt( int index )
	{
		return states[index];
	}
}
