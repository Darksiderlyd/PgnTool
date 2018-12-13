package com.xiaoyu.sgf.base.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.ChainsLiberties;
import com.xiaoyu.sgf.base.Game;
import com.xiaoyu.sgf.base.Move;

public class GameImpl implements Game {
	
/* Handicap list
  	2	D4 Q16
	3	D4 Q16 D16
	4	D4 Q16 D16 Q4
	5	D4 Q16 D16 Q4 K10
	6	D4 Q16 D16 Q4 D10 Q10
	7	D4 Q16 D16 Q4 D10 Q10 K10
	8	D4 Q16 D16 Q4 D10 Q10 K4 K16
	9	D4 Q16 D16 Q4 D10 Q10 K4 K16 K10
*/
	Board _board;
	ChainsLiberties _cl;
	int _moveToPlay;
	int _playerToPlay;
	int _handicap;
	float _komi;
	boolean _isFinished;
	boolean _lastMoveIsPass;
	Move _lastMove;
	
	int _lastEatPos; // ko
	int _lastEatMoveno;
	
	int[] _deadStones;
	
	Move _isValidCache;	

	private void gameImplCommonSetup(int size) {
		_board = new BoardImpl(size);
		_cl = new ChainsLibertiesImpl();
		_moveToPlay = 1;
		_playerToPlay = Board.BLACK;
		_isFinished = false;
		_handicap = 0;
		_komi = 5.5f;
		_lastMoveIsPass = false;
		_lastMove = null;
		_lastEatPos = -1;
		_lastEatMoveno = -1;
		_deadStones = new int[3];
	}
	
	public GameImpl(int size) {
		gameImplCommonSetup(size);
	}
	
	public GameImpl(int size, int handicap) {
		gameImplCommonSetup(size);
		_handicap=handicap;
		_playerToPlay = Board.WHITE;
	}

	public Game clone() {
		GameImpl cloned = new GameImpl(_board.getSize());
		cloned._board = _board.clone();
		cloned._cl = _cl.clone();
		cloned._moveToPlay = _moveToPlay;
		cloned._playerToPlay = _playerToPlay;
		cloned._isFinished = _isFinished;
		cloned._handicap = _handicap;
		cloned._komi = _komi;
		cloned._lastMoveIsPass = _lastMoveIsPass;
		cloned._lastMove = _lastMove;
		cloned._lastEatPos = _lastEatPos;
		cloned._lastEatMoveno = _lastEatMoveno;
		cloned._deadStones = _deadStones.clone();
		return cloned;
	}

	

	public int playerToPlay() {
		return _playerToPlay;
	}

	public int moveToPlay() {
		return _moveToPlay;
	}

	public boolean finished() {
		return _isFinished;
	}

	public Board getBoard() {
		return _board;
	}
	
	public void setHandicap(int handicap) {
		throw new IllegalArgumentException("Handicap not implemented yet. I'm sorry.");
		//_handicap = handicap;
	}

	public int getHandicap() {
		return _handicap;
	}
	
	public void setKomi(float komi) {
		Float fract = komi - (long)komi;
		if ( fract != 0.5f )
			throw new IllegalArgumentException("Komi fractional part should be 1/2");
		if ( _moveToPlay != 1 )
			throw new IllegalStateException("Can't change komi after game started.");
		_komi = komi;
	}
	
	public float getKomi() {
		return _komi;
	}

	public int getDeadStones(int player) {
		return _deadStones[player];
	}
	
	public boolean isValidMove(Move move) {
		if (_isValidCache == move) return true;
		
		if (move.isPass()) {
			_isValidCache = move;
			return true;
		} else { 
			if (_board.get(move.getX(), move.getY()) != Board.EMPTY) return false;
		}
		if (move.getPlayer() != _playerToPlay) return false;
		if (finished()) return false;
		
		int P = _board.convertToP(move.getX(), move.getY());
		
		if (_board.getNearP(P, Board.EMPTY).length > 0) {
			_isValidCache = move;
			return true;  // done, we dont need to check any other thing. 
		}
		// if we are here, there is no more empty places near ... we should look carefoul
		int[] grps = _cl.getGroupsWithLibertyPoint(P);
		
		// if group with one liberty is opponent, we are eating, so allowed.
		boolean areWeEating = false;
		for (int g = 0; g<grps.length; g++) {
			if (_cl.getLibertiesPoints(grps[g]).length == 1) {
				int chainColor = _board.getP(_cl.getChainPoints(grps[g])[0]); // XXX: this should be getOneChainPoint (its enough)
				if (chainColor != move.getPlayer()) {
					areWeEating = true;
				}
			}
		}
		if (areWeEating) {
			if (_lastEatMoveno == moveToPlay() // because moveno didn´t change yet.
				&& _lastEatPos == _board.convertToP(move.getX(), move.getY())) {
				// I´m sorry, Its a KO.
				return false;
			} else {
				// not KO, fine.
				_isValidCache = move;
				return true;
			}
		}

		// finally, we are not eating, so, we should check if at least one of OUR groups arround have
		// more than one liberty. If not, we are commiting suicide. 
		
		for (int g = 0; g<grps.length; g++) {
			int chainColor = _board.getP(_cl.getChainPoints(grps[g])[0]);
			if (chainColor == move.getPlayer()) {
				// our group
				if (_cl.getLibertiesPoints(grps[g]).length > 1) {
					_isValidCache = move;
					return true; // good!
				}
			}
		}
		
		// so, we are not eating, and we are occuping our last liberty, suicide, not allowed.
		return false;
		
	}
	
	public boolean isValidMove(String move) {
		return isValidMove(MoveImpl.fromString(move));	
	}

	public boolean play(Move move) {
		if (!isValidMove(move)) return false;
		
///		((ChainsLibertiesImpl)_cl).integrityCheck(); // XXX: debug

		_isValidCache = null;
		
		_moveToPlay++;
		_playerToPlay = (_playerToPlay==Board.BLACK)?Board.WHITE:Board.BLACK;
		
		if (move.isPass()) {
			// pass move
			if (_lastMoveIsPass) {
				_isFinished = true;
			}
			_lastMoveIsPass = true;
		} else {
			// normal move
			_lastMoveIsPass = false;
			
			// nearEMPTY
			// nearPLAYER
			// nearOTHER
			// libertyCount(nearOTHER)==1?
				// delete group from board, from chainsliberties
				// update liberties to neighboord of this group. (how?)			
			// nearPLAYER>0:
				// extend group adding this play
				// extends to others groups.
			// adds nearEMPTY to list of liberties groups to move group.
			// updates board with this move.
			// deletes this position from liberty lists
			
			int playerColor = move.getPlayer();
			int opponentColor = (move.getPlayer()==Board.BLACK)?Board.WHITE:Board.BLACK;
			int P = _board.convertToP(move.getX(), move.getY());
			// nearEMPTY
			// nearPLAYER
			// nearOTHER
			int[] nearEMPTY = _board.getNearP(P, Board.EMPTY);
			int[] nearPLAYER = _board.getNearP(P, playerColor );
			int[] nearOTHER = _board.getNearP(P, opponentColor );
				
			// libertyCount(nearOTHER)==1?
				// delete group from board, from chainsliberties
				// update liberties to neighboord of this group. (how?)
			for (int i=0; i<nearOTHER.length; i++) {
				int group = _cl.getGroupWithChainPoint(nearOTHER[i]);
				if (group!=-1 && _cl.getLibertiesCount(group)==1) { 
					
					int[] toDelete = _cl.getChainPoints(group);

					// Its a 1 stone eat move, store data for ko check
					if (toDelete.length==1) {
						_lastEatPos = toDelete[0];
						_lastEatMoveno = moveToPlay();
					}
					
					for (int p=0; p<toDelete.length; p++) 
						_board.setP(toDelete[p], Board.EMPTY);
					_cl.deleteGroup(group);
					_deadStones[opponentColor]+=toDelete.length;
					
					// for each point deleted, searchs for nearpoints, and adds as liberties if group
					for (int d=0; d<toDelete.length; d++) {
						int[] near = _board.getNearP(toDelete[d]);
						for (int n=0; n<near.length; n++) {
							int gg = _cl.getGroupWithChainPoint(near[n]);
							if (gg!=-1) _cl.AddLibertiesPointsToGroup(gg, new int[] {toDelete[d]});
						}
					}
					
					nearEMPTY = _board.getNearP(P, Board.EMPTY); // later is used
				}
			}
			
			
			
			// nearPLAYER>0:
				// extend group adding this play
				// extends to others groups.
			int groupSet = -1;
			for (int i=0; i<nearPLAYER.length; i++) {
				if (groupSet==-1) {
					//System.out.println(((ChainsLibertiesImpl)_cl).debugString());
					groupSet = _cl.getGroupWithChainPoint( nearPLAYER[i] );
					if (groupSet != -1)
						_cl.addChainPointsToGroup(groupSet, new int[] {P});
				} else {
					int otherGroup = _cl.getGroupWithChainPoint(nearPLAYER[i]);
					if (otherGroup!=groupSet && otherGroup!=-1) {
						groupSet = _cl.unionGroups(groupSet, otherGroup); // XXX: otherGroup SHOULD NEVER BE -1 (???)
						//System.out.println("joining " + otherGroup + " with " + groupSet);
					}
						
				}
			}
			if (groupSet == -1) {
				_cl.addGroup(P, nearEMPTY);
			} else {
				// adds nearEMPTY to list of liberties groups to move group.
				_cl.AddLibertiesPointsToGroup(groupSet, nearEMPTY);
			}
			
			// updates board with this move.
			_board.setP(P, playerColor);
			
			// deletes this position from liberty lists
			int groups[] = _cl.getGroupsWithLibertyPoint(P);
			for (int g=0; g<groups.length; g++)
				_cl.deleteLiberty(groups[g], P);
			
		}
		
///		((ChainsLibertiesImpl)_cl).integrityCheck(); // XXX: debug

		_lastMove = move;
		
		return true;
	}
	
	public boolean play(String move) {
		return play(MoveImpl.fromString(move));
	}

	public void setupBoard(int x, int y, int value) {
		// TODO Auto-generated method stub		
	}
	
	public ChainsLiberties getChainsLiberties() {
		return _cl;
	}

	public Move getLastMove() {
		return _lastMove;
	}

}
