package com.xiaoyu.sgf.base.impl;
import com.xiaoyu.sgf.base.Board;

public class BoardImpl implements Board {
	
	int[] _board;
	int _size;
	long _hash;
	
	public BoardImpl(int size) {
		_board = new int[size*size];
		_size = size;
		_recalcHash();
	}
	
	public BoardImpl(Board anotherBoard) {
		_size = anotherBoard.getSize();		
		_board = anotherBoard.getArray().clone();
	}

	public int get(int x, int y) {
		if (x<0 || x>=_size || y<0 || y>=_size) return INVALID;
		return _board[(y*_size)+x];
	}

	public int get(String pos) {
		int xy[] = fromString(pos); 
		return get(xy[0], xy[1]);
	}

	public int[] getArray() {
		return _board.clone(); // XXX: clone is ok?
	}

	public int getSize() {
		return _size;
	}

	public void set(int x, int y, int value) {
		if (value != Board.BLACK && value != Board.WHITE && value != Board.EMPTY) return;
		int P = (y*_size)+x;
		_hash = _hash ^ Zobrist.n4go[_board[P]][P];
		_board[P] = value;
		_hash = _hash ^ Zobrist.n4go[value][P];
	}
	
	public void set(String pos, int value) {
		int xy[] = fromString(pos); 
		set(xy[0], xy[1], value);
	}

	public void setP(int point, int value) {
		_hash = _hash ^ Zobrist.n4go[_board[point]][point];
		_board[point] = value;
		_hash = _hash ^ Zobrist.n4go[value][point];
	}
	
	public void setArray(int[] _value) {
		_board = _value;
		_size = (int) Math.sqrt(_board.length);
		_recalcHash();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()); sb.append("\n");


		sb.append(" ");
		for(int x=0;x<_size;x++) {
			sb.append( (char)(65+x) );  // XXX: should skip "I" GHJK  etc... 
		}
		sb.append("\n");
		
		for (int y=0;y<_size;y++) {
			sb.append((y+1) % 10);
			for(int x=0;x<_size;x++) {
				int val = _board[(y*_size)+x];
				if (val == EMPTY) sb.append(".");
				else if (val == BLACK) sb.append("X");
				else if (val == WHITE) sb.append("O");
				else if (val == INVALID) sb.append("?");
			}
			sb.append((y+1) % 10);
			sb.append("\n");
		}

		sb.append(" ");
		for(int x=0;x<_size;x++) {
			sb.append( (char)(65+x) );
		}
		
		sb.append("\n");
		
		return sb.toString();
	}

	static public int[] fromString(String pos) {
		pos = pos.toUpperCase();
		char moveLetterCh = pos.charAt(0);
		String moveNumberSt = pos.substring(1,pos.length());

		if (moveLetterCh<'A' || moveLetterCh>'Z') return null;
		int[] res = new int[2];
		res[0] = moveLetterCh - 65;
		if (res[0]>'I'-65) res[0]--;
		try {
			res[1] = Integer.parseInt(moveNumberSt) - 1;			
		} catch (NumberFormatException e) {
			return null;
		}
		
		return res;
	}
	
	public int getP(int P) {
		return _board[P];
	}

	public int convertToP(int x, int y) {
		return (y*_size)+x;
	}
	
	public static int convertToP(int size, int x, int y) {
		if (x==Board.INVALID || y==Board.INVALID) return -1;
		return (y*size)+x;
	}
	
	public int getXfromP(int point) {
		return point % _size;
	}

	public int getYfromP(int point) {
		return point / _size;
	}

	public int[] getNearP_NO(int point) {
		int res[] = new int[4];
		
		res[0] = point - _size;
		res[1] = point + 1;
		res[2] = point + _size;
		res[3] = point -1;

		int size = 4;
		if ( point < _size ) {
			res[0] = -1;
			size--;
		}
		if ( point % _size == _size-1 ) {
			res[1] = -1;
			size--;
		}
		if ( point / _size == _size-1 ) {
			res[2] = -1;
			size--;
		}
		if ( point % _size == 0 ) {
			res[3] = -1;
			size--;
		}		

		int[] res2 = new int[size];
		int ii=0;
		for(int i=0;i<res.length;i++)
			if (res[i]>=0)
				res2[ii++]=res[i];
		
		return res2;
	}

	public int[] getNearP(int point) {

		int size = 4;
		if ( point < _size ) size--;
		if ( point % _size == _size-1 ) size--;
		if ( point / _size == _size-1 ) size--;
		if ( point % _size == 0 ) size--;
		
		int res[] = new int[size];
		int i = 0;
		
		if ( point >= _size ) res[i++] = point - _size;
		if ( point % _size != _size-1 ) res[i++] = point + 1;
		if ( point / _size != _size-1 ) res[i++] = point + _size;
		if ( point % _size != 0 ) res[i++] = point - 1;

		return res;
	}

	public int[] getNearP(int point, int value) {
		int size = 4;
		if ( point < _size || getP(point-_size) != value) size--;
		if ( point % _size == _size-1 || getP(point+1) != value) size--;
		if ( point / _size == _size-1 || getP(point+_size) != value) size--;
		if ( point % _size == 0 || getP(point-1) != value) size--;
		
		int res[] = new int[size];
		int i = 0;
		
		if ( point >= _size && getP(point-_size) == value) res[i++] = point - _size;
		if ( point % _size != _size-1 && getP(point+1) == value) res[i++] = point + 1;
		if ( point / _size != _size-1 && getP(point+_size) == value) res[i++] = point + _size;
		if ( point % _size != 0 && getP(point-1) == value) res[i++] = point - 1;

		return res;
	}
	
	public int[] getNearP_(int point, int value) {
		int[] res = getNearP(point);
		int size = res.length;
		for (int i=0;i<res.length;i++)
			if (getP(res[i])!=value) {
				res[i]=-1;
				size--;
			}
		int[] res2 = new int[size];
		int ii=0;
		for(int i=0;i<res.length;i++)
			if (res[i]>=0)
				res2[ii++]=res[i];
			
		return res2;
	}
	
	public int[] getDiagonalP(int point) {
		int size = 4;
		
		int x = getXfromP(point);
		int y = getYfromP(point);

		if (x<1) size-=2;
		if (x>_size-2) size-=2;
		if (y<1) size-=2;
		if (y>_size-2) size-=2;
		if (size==0) size=1;

		int res[] = new int[size];
		int i = 0;
		
		if (x-1>=0 && y-1>=0) 		res[i++] = convertToP(x-1, y-1);
		if (x-1>=0 && y+1<_size)	res[i++] = convertToP(x-1, y+1);
		if (x+1<_size && y-1>=0)	res[i++] = convertToP(x+1, y-1);
		if (x+1<_size && y+1<_size) res[i++] = convertToP(x+1, y+1);
		
/*		
		if ( point - size - 1 < 0 || point % _size == 0 ) size--;
		if ( point - size + 1 < 0 || point % _size == _size-1 ) size--;
		if ( point + size - 1 > _board.length || point % _size == 0 ) size--;
		if ( point + size + 1 > _board.length || point % _size == _size-1 ) size--;

		int res[] = new int[size];
		int i = 0;
		
		if ( point - size - 1 >= 0 && point % _size != 0 ) res[i++] = point - size - 1;
		if ( point - size + 1 >= 0 && point % _size != _size-1 ) res[i++] = point - size + 1;
		if ( point + size - 1 <= _board.length && point % _size != 0 ) res[i++] = point + size - 1;
		if ( point + size + 1 <= _board.length && point % _size != _size-1 ) res[i++] = point + size + 1;
*/
		return res;		
	}

	
	public long getHash() {
		return _hash;
	}
	
	private void _recalcHash() {
		_hash = 0;
		for (int P=0; P<_board.length; P++) {
			_hash = _hash ^ Zobrist.n4go[_board[P]][P];
		}
	}
	
	public Board clone() {
		Board cloned = new BoardImpl(_size);
		cloned.setArray( getArray() ); 
		return cloned;	
	}

	public int[] getArrayNotCopy() {
		return _board;
	}

}
