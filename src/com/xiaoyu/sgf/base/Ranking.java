package com.xiaoyu.sgf.base;

public interface Ranking extends Comparable {
	
	static int KYU = 1;
	static int DAN = 2;
	static int PRO = 3;

	boolean isValid();
	boolean isValid(int ranktype, int rankno);
	
	int getRankType();
	int getRankNo();
	
	Ranking fromString(String ranking);
	String toString();
	
	int stones();
}
