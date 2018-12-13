package com.xiaoyu.sgf.base.util;

import com.xiaoyu.sgf.base.Game;

public interface GameHelper {

	/*
	 * Returns a list of P, P=boardLengh=>pass
	 */
	int[] getValidMoves(Game game);
	
	int[] getChineseScore(Game game);
	
	
}
