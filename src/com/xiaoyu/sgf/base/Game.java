package com.xiaoyu.sgf.base;

public interface Game {
	
	void setHandicap(int stones);
	int getHandicap();
	
	void setKomi(float komi);
	float getKomi();
	
	int moveToPlay();
	int playerToPlay();
	
	boolean isValidMove(Move move);
	boolean isValidMove(String move);
	boolean play(Move move);
	boolean play(String move);

	int getDeadStones(int player);
	
	boolean finished();
	
	Board getBoard();
	void setupBoard(int x, int y, int value);
	
	Move getLastMove();

	Game clone();
	
}
