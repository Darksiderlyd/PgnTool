package com.xiaoyu.sgf.base.impl;

import com.xiaoyu.sgf.base.Ranking;

public class RankingImpl implements Ranking, Comparable {

	private int _ranktype;
	private int _rankno;
	
	RankingImpl(int type, int no) {
		_ranktype = type;
		_rankno = no;
	}

	public boolean isValid(int ranktype, int rankno) {
		if (ranktype != DAN && ranktype != KYU && ranktype != PRO )	return false;
		if (ranktype == DAN && (rankno<1 || rankno>6)) return false; 
		if (ranktype == PRO && (rankno<1 || rankno>9)) return false; 
		if (ranktype == KYU && (rankno<1 || rankno>30)) return false; 
		return true;
	}
	
	public boolean isValid() {
		return isValid(_ranktype, _rankno);
	}
	
	public int getRankType() {
		return _ranktype;
	}	

	public int getRankNo() {
		return _rankno;
	}

	public int compareTo(Object o) {
		Ranking r = (Ranking)o;
		// same ranking
		if (_ranktype == r.getRankType() ) {
			if (_ranktype == KYU) { // inverted
				return r.getRankNo() - _rankno;
			} else {
				return _rankno - r.getRankNo();
			}
		} else
		// different rank
		if (_ranktype == KYU) {
			return -1; // we are lesser than any other ranking
		} else
		if (_ranktype == PRO) {
			return 1; // we are stronger than any other
		} else {
			if (r.getRankType() == KYU) {
				return 1; // stronger
			} else {
				return -1; // lower
			}
		}
	}

	public int stones() {
		int stones = 0;
		if (_ranktype == KYU) {
			return (30-_rankno);
		} else {
			stones += 30;
		}
		
		if (_ranktype == DAN) {
			return stones + _rankno;
		} else {
			stones += 6;
		}
		
		// PRO
		return stones + _rankno;
	}

	public Ranking fromString(String ranking) {
		// TODO Auto-generated method stub
		return null;
	}




}
