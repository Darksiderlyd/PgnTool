package com.xiaoyu.sgf.base;

// XXX: isResign should be implemented.
public interface Move {

	final static byte BLACK = 1;
	final static byte WHITE = 2;
	
	public int getX();
	public int getY();
	public int getPlayer();
	public boolean isPass();
	
	/**
	 * Format examples(any case): WHITE-A1,W B13,black W12, White B2.
	 * @param move
	 * @return
	 */
	// static public Move fromString(String move); //XXX
	
}

