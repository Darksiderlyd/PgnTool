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


import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Move;

public class MoveImpl implements Move {

	private int _x;
	private int _y;
	private int _player;	
	private boolean _pass;
	
	public MoveImpl(int x, int y, int player) {
		_x = x;
		_y = y;
		_player = player;
		_pass = false;
	}
	
	public MoveImpl(int player) {
		_x = Board.INVALID;
		_y = Board.INVALID;
		_player = player;
		_pass = true;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public int getPlayer() {
		return _player;
	}

	public boolean isPass() {
		return _pass;
	}

	static public Move fromString(String move) {
		 // Format examples(any case): WHITE-A1,W B13,black W12, White B2.
		int player = Board.INVALID;
		int x = Board.INVALID;
		int y = Board.INVALID;

		String m = move.toUpperCase();
		int cut1 = m.indexOf("-");
		if (cut1<0) cut1 = m.indexOf(" ");
		if (cut1<0) return null;
		
		String playerSt = m.substring(0,cut1);
		String possiblePassSt = m.substring(cut1+1,m.length());
		char MoveLetterCh = m.charAt(cut1+1);
		String MoveNumberSt = m.substring(cut1+2,m.length());

		if ("W".equals(playerSt) || "WHITE".equals(playerSt)) player = Board.WHITE;
		if ("B".equals(playerSt) || "BLACK".equals(playerSt)) player = Board.BLACK;
		
		if ("PASS".equals(possiblePassSt)) {
			return new MoveImpl(player);
		}
		if (player == Board.INVALID) {
			return null;
		}
		
		x = MoveLetterCh - 65;
		if (x>'I'-65) x--;
		y = Integer.parseInt(MoveNumberSt) - 1;
		
		return new MoveImpl(x,y,player);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (_player == Board.BLACK) {
			sb.append("B ");
		} else 
		if (_player == Board.WHITE) {
			sb.append("W ");
		}
		
		if (_pass) {
			sb.append("PASS");			
		} else {
			int skipI = (_x+65>='I')?1:0 ;
			sb.append( (char)(65+_x+skipI) );
			sb.append(_y+1);
		}
		
		return sb.toString();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MoveImpl)) {
			if (obj instanceof Move) {
				return toString().equals(obj.toString());
			}
			return false;
		}
		MoveImpl m2 = (MoveImpl)obj;
		return _x==m2._x 
			&& _y==m2._y
			&& _player==m2._player
			&& _pass==m2._pass;
	}
}
