package com.xiaoyu.sgf.ctrl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Game;
import com.xiaoyu.sgf.base.Move;

public interface Engine {

	String getName();
	String getVersion();
	
	Board getBoard();
	Game getGame();
	void setBoardSize(int size);
	void clearBoard();
	
	boolean setKomi(float komi);
	boolean setHandicap(int stones);
	
	boolean play(Move move);
	Move genMove(int player);  // null=resign
	Move genMove();
	boolean undo();
	void reset();
	
	String calcScore(); // XXX: should be a Score Object.
	
	void setTime(int main, int byoYomi, int stones);
	void correctTime(int color, int time, int stones);

	long getRandomSeed();
	void setRandomSeed(long seed);
	
	long getCpuTimeInMillis();

	void finish();
	
}
