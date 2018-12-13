package com.xiaoyu.sgf.base.util.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Game;
import com.xiaoyu.sgf.base.Move;
import com.xiaoyu.sgf.base.impl.MoveImpl;
import com.xiaoyu.sgf.base.util.BoardUtil;
import com.xiaoyu.sgf.base.util.GameHelper;

public class GameHelperImpl implements GameHelper {
	
	private BoardUtil _bu;
	
	public GameHelperImpl() {
		_bu = new BoardUtilImpl();
	}

	public int[] getValidMoves(Game game) {
		Board board = game.getBoard();
		int boardMatrix[] = board.getArray(); //its already cloned, we can destroy it.
		int size = board.getSize();
		int player = game.playerToPlay();
		
		int res[] = new int[boardMatrix.length+1]; // plus pass move
		res[res.length-1]++; // pass is always a valid move
		
		// first pass: greedy, if at right is empty, and not corner, valid move.
		// also, if the position down is empty, also is valid.
		for (int i=0; i<boardMatrix.length; i++) {
			if (boardMatrix[i]==Board.EMPTY) {
				if ( i+1<boardMatrix.length && boardMatrix[i+1]==Board.EMPTY && i%size != size-1 ) { // check right
					boardMatrix[i]=Board.INVALID;
					res[i]++;
				} 
				else
				if ( i+size<boardMatrix.length && boardMatrix[i+size]==Board.EMPTY ) { // down
					boardMatrix[i]=Board.INVALID;
					res[i]++;
				}
			}
		}
		
		// second pass (slow): call to game.isValid()
		for (int i=0; i<boardMatrix.length; i++) {
			if (boardMatrix[i]==Board.EMPTY) {
				Move move = new MoveImpl( board.getXfromP(i), board.getYfromP(i), player );
				if (game.isValidMove(move)) {
					boardMatrix[i]=Board.INVALID;
					res[i]++;
				}
			}
		}

		// creates result array
		int finalResSize = 0;
		for (int i=0; i<res.length; i++) if (res[i]>0) finalResSize++;
		
		int[] finalRes = new int[finalResSize];
		int finalResIndex = 0;
		for (int i=0; i<res.length; i++)
			if (res[i]>0)
				finalRes[finalResIndex++]=i;
				
		return finalRes;
	}

	public int[] getChineseScore(Game game) {
		Board board = game.getBoard();
		int[] boardMatrix = board.getArray();
		int[] res = new int[3];
		boolean printDebug = false;

		// counts stones
		for (int i=0; i<boardMatrix.length; i++) {
			int thisColor = boardMatrix[i];
			res[ thisColor ]++;
			
			if ( thisColor == Board.EMPTY ) { // EMPTY? is eye?
				int eyeColor = _bu.isEye(board, i, true);
				if (eyeColor != Board.INVALID) { // eye is count as point for eye's color
					res[thisColor]--;
					res[eyeColor]++;
				} else {
					//Move move = new MoveImpl( board.getXfromP(i), board.getYfromP(i), Board.WHITE);
					//System.err.println(move + " is invalid eye.");
					//printDebug = true;
				}
			}
		}
		
		if (printDebug) {
			System.err.println(board.toString());
			System.err.println("BLACK="+res[Board.BLACK]+" (+"+game.getDeadStones(Board.WHITE)+" deads)   WHITE="+res[Board.WHITE]+" (+"+game.getDeadStones(Board.BLACK)+" deads)  EMPTY="+res[Board.EMPTY]);
		}
		
		// adds deads
		res[Board.BLACK]+=game.getDeadStones(Board.WHITE);
		res[Board.WHITE]+=game.getDeadStones(Board.BLACK);
		
		return res;
	}

}
