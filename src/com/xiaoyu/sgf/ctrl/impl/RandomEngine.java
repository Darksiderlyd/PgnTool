package com.xiaoyu.sgf.ctrl.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Game;
import com.xiaoyu.sgf.base.Move;
import com.xiaoyu.sgf.base.impl.GameImpl;
import com.xiaoyu.sgf.base.impl.MoveImpl;
import com.xiaoyu.sgf.ctrl.Engine;

import java.util.Random;

public class RandomEngine implements Engine {
	
	Game _game = new GameImpl(19);
	Random _rnd = new Random( System.currentTimeMillis() );
	int _lastPlayer = Board.INVALID;
	int im = Board.INVALID; // based on first genMove i will guest who i'm
	boolean _finish = false;

	public String calcScore() {
		return "cannot score";
	}

	public void clearBoard() {
		int size = _game.getBoard().getSize();
		_game = new GameImpl(size);
		im = Board.INVALID;
		_lastPlayer = Board.INVALID;
		_finish = false;
	}
	
	public void correctTime(int color, int time, int stones) {
	}

	public void finish() {
	}

	public Move genMove(int player) {
		if (im==Board.INVALID) im = player;
		System.err.println("IM "+im);
		
		if (player==_lastPlayer) _finish = true;
		_lastPlayer = player;
		
		if (_finish) return new MoveImpl(player);

		int size = _game.getBoard().getSize();
		Move move = null;
		while (move == null) {
			int x = _rnd.nextInt(size);
			int y = _rnd.nextInt(size);
			move = new MoveImpl(x,y,player);
			if (!_game.isValidMove(move)) move = null;
		}
		return move;
	}
	
	public Move genMove() {
		return genMove(_game.playerToPlay());
	}

	public Board getBoard() {
		return _game.getBoard();
	}

	public String getName() {
		return "RandomEngine";
	}

	public String getVersion() {
		return "0.1";
	}

	public boolean play(Move move) {
		if (move.isPass()) _finish = true;
		_lastPlayer = move.getPlayer();
		return _game.play(move);
	}

	public void reset() {
		_game = new GameImpl(19);
	}

	public void setBoardSize(int size) {
		_game = new GameImpl(size);
		clearBoard();
	}

	public boolean setHandicap(int stones) {
		// TODO Implement Handicap
		return false;
	}

	public boolean setKomi(float komi) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTime(int main, int byoYomi, int stones) {
		// TODO Auto-generated method stub
	}

	public boolean undo() {
		// TODO When UndableGame is implemented
		return false;
	}

	public Game getGame() {
		return _game;
	}

	public long getRandomSeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setRandomSeed(long seed) {
		// TODO Auto-generated method stub
		
	}

	public long getCpuTimeInMillis() {
		// TODO Auto-generated method stub
		return 0;
	}

}
