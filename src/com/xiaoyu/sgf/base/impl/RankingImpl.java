/*
 * Copyright (C) 2006,2007 Eduardo Sabbatella Riccardi
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Email: esabb <at> users.sourceforge.net
 */

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
