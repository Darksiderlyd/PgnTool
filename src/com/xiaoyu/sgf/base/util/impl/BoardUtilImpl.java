package com.xiaoyu.sgf.base.util.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.util.BoardUtil;

public class BoardUtilImpl implements BoardUtil {

	static private BoardUtil __instance = new BoardUtilImpl();
	
	static public BoardUtil getInstance() {
		return __instance;
	}

	
	public void flipHorizontal(Board board) {
		int size = board.getSize();
		for (int x=0; x<size; x++) {
			for (int y=0; y<size/2; y++) {
				int v1 = board.get(x,y);
				int v2 = board.get(x,size-1-y);
				if (v1!=v2) {
					board.set(x,y, v2);
					board.set(x, size-1-y, v1);
				}
			}
		}
	}
	
	public void flipVertical(Board board) {
		int size = board.getSize();
		for (int y=0; y<size; y++) {
			for (int x=0; x<size/2; x++) {
				int v1 = board.get(x,y);
				int v2 = board.get(size-1-x,y);
				if (v1!=v2) {
					board.set(x, y, v2);
					board.set(size-1-x,y, v1);
				}
			}
		}
	}

	public void invert(Board board) {
		int size = board.getSize();
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				int v1 = board.get(x,y);
				if (v1 == Board.BLACK) board.set(x,y, Board.WHITE);
				if (v1 == Board.WHITE) board.set(x,y, Board.BLACK);
			}
		}
	}

	public void rotateClockwise(Board board) {
		int size = board.getSize();
		Board origBoard = board.clone();
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {
				int nx = y;
				int ny = size-x-1;
				int v1 = origBoard.get(nx,ny);
				int v2 = board.get(x,y);
				if (v1!=v2) {
					board.set(x,y,v1);
				}
			}
		}
	}

	public Board[] allFlipInvertCombinations(Board board) {
		Board[] res = new Board[16];
		int boardNo = 0;		
		Board work = board.clone();
		for (int inv=0; inv<2; inv++) {
			for (int flip=0; flip<2; flip++) {
				for (int rot=0; rot<4; rot++) {
					res[boardNo++] = work.clone();
					rotateClockwise(work);					
				}
				flipHorizontal(work);
			}
			invert(work);
		}
		return res;
	}


	public Board[] allUniqueFlipInvertCombinations(Board board) {
		Board[] tmp = allFlipInvertCombinations(board);

		for (int a=0; a<tmp.length; a++) {
			for (int b=a+1; tmp[a]!=null && b<tmp.length; b++) {
				if (tmp[a].getHash() == tmp[b].getHash())
					tmp[a] = null;
			}
		}

		int count = 0;
		for (int a=0; a<tmp.length; a++)
			if (tmp[a]!=null) count++;
		
		Board[] res = new Board[count];
		int idx = 0;
		for (int a=0; a<tmp.length; a++)
			if (tmp[a]!=null)
				res[idx++] = tmp[a];
		
		return res;
	}


	public int isEye(Board board, int P, boolean relaxed) {
		
		//  first of all, point should be empty
		if (board.getP(P) != Board.EMPTY) return Board.INVALID;
		
		// all surrounding stones should be same color (but color should not be 'empty')
		int[] nearP = board.getNearP(P);
		for (int i=1; i<nearP.length; i++)
			if ( board.getP(nearP[i-1]) != board.getP(nearP[i]) ) return Board.INVALID;
		
		int ourColor = board.getP(nearP[0]);
		if ( ourColor == Board.EMPTY ) return Board.INVALID;
		
		// maximun, one diagonal empty/not same color, and at least one diagonal of our color.
		int[] diagP = board.getDiagonalP(P);
		int nOurColor = 0;
		int nOtherColor = 0;
		for (int i = 0; i<diagP.length; i++) {
			int diagColor = board.getP( diagP[i] );
			if (diagColor != ourColor ) nOtherColor++;
			if (diagColor == ourColor ) nOurColor++;
		}
		if (!relaxed && nOtherColor>1) return Board.INVALID;
		if (!relaxed && nOurColor<1) return Board.INVALID;
		
		// otherwise, its an eye
		return ourColor;
	}
	
	public int isEye(Board board, int P) {
		return isEye(board, P, false);
	}

}
