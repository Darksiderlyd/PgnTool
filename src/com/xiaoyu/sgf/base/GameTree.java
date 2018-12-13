package com.xiaoyu.sgf.base;

public interface GameTree {
	
	void setBlackName(String name);
	String getBlackName();

	void setWhiteName(String name);
	String getWhiteName();
	
	void setBoardSize(int size);
	int getBoardSize();
	
	void setHandicapStones(int stones);
	int getHandicapStones();
	
	void setKomi(float komi);
	float getKomi();
		
	int moveNo();
	Move getMove();
	
	void setComment(String comment);
	String getComment();
	
	void setGameComment(String comment);
	String getGameComment();

	int variantsInThisNode();
	boolean next();
	boolean next(int variant);
	boolean prev();
	boolean last();
	void rewind();
	
	void addMove(Move move);
	void addMove(String move);

	String getGameName();

	void setGameName(String gameName);

	String getGameEvent();

	void setGameEvent(String gameEvent);
	
}
