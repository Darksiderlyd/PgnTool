package com.xiaoyu.sgf.base.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Move;

public class MoveFactory {
	
	int MAXBOARDSIZE = 25;
	
	private static MoveFactory _instance;
	
	private Move[][][] _cache; // player,x,y
	private Move[] _passCache; // player
	
	private MoveFactory() {
		_cache = new Move[3][][];
		for (int p=0; p<3; p++) {
			_cache[p] = new Move[MAXBOARDSIZE][];
			for (int x=0; x<MAXBOARDSIZE; x++) {
				_cache[p][x] = new Move[MAXBOARDSIZE];
				for (int y=0; y<MAXBOARDSIZE; y++) {
					_cache[p][x][y] = new MoveImpl(x,y,p);
				}
			}
		}
		_passCache = new Move[3];
		_passCache[Board.BLACK] = new MoveImpl(Board.BLACK);
		_passCache[Board.WHITE] = new MoveImpl(Board.WHITE);
	}
	
	static public MoveFactory getInstance() {
		if (_instance == null) {
			synchronized (MoveFactory.class) {
				if (_instance == null)
					_instance = new MoveFactory();
			}
		}
		return _instance;
	}
	
	/**
	 * Gets a Move.
	 * @param x x value.
	 * @param y y value.
	 * @param player to move.
	 * @return a Move object.
	 */
	public Move get(int x, int y, int player) {
		return _cache[player][x][y];
	}
	
	/**
	 * Gets a pass move.
	 * @param player to pass.
	 * @return a pass Move object.
	 */
	public Move get(int player) {
		return _passCache[player];
	}
	
}
