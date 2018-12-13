package com.xiaoyu.sgf.base.impl;

import com.xiaoyu.sgf.base.GameTree;
import com.xiaoyu.sgf.base.Move;

public class GameTreeImpl implements GameTree {

	class Node {
		Node _parent;
		Move _move;
		String _comment;
		Node[] _next;
		
		Node(Move move, Node parent) {
			_move = move;
			_parent = parent;
			_next = new Node[1];
		}
		void setComment(String comment) {
			_comment = comment;
		}
		String getComment() {
			return _comment;
		}
		Move getMove() {
			return _move;
		}
		Node getParent() {
			return _parent;
		}
		void addChild(Node node) {
			if (_next[0] == null) {
				_next[0] = node;
			} else {
				Node[] newArray = new Node[ _next.length +1 ];
				for (int i=0;i<_next.length;i++) 
					newArray[i]=_next[i];
				newArray[_next.length] = node;
				_next = newArray;
			}
		}
		int getChildCount() {
			if (_next[0] == null)
				return 0;
			else return _next.length;
		}
		Node getChild(int nro) {
			return _next[nro];
		}

	}
	
	
	String _blackName;
	String _whiteName;
	int _boardSize;
	int _handicap;
	String _gameComment;
	float _komi;
	Node[] _root;
	Node _currentNode;
	int _moveno;
	
	
	public void setBlackName(String name) {
		_blackName = name;
	}
	
	public String getBlackName() {
		return _blackName;
	}

	public void setWhiteName(String name) {
		_whiteName = name;
	}

	public String getWhiteName() {
		return _whiteName;
	}
	public String getGameComment() {
		return _gameComment;
	}

	public void setGameComment(String comment) {
		_gameComment = comment;
	}
	
	public void setBoardSize(int size) {
		if (size>0 && size<50)
			_boardSize = size;
	}
	
	public int getBoardSize() {
		return _boardSize;
	}
	
	public void setHandicapStones(int stones) {
		if (stones>=0 && stones<=9)
			_handicap = stones;
	}
	
	public int getHandicapStones() {
		return _handicap;
	}
	
	public void setKomi(float komi) {
		if (komi>0.0f 
			&& Math.round(komi/0.5f)==(komi/0.5f) )
			_komi = komi;
	}
	
	public float getKomi() {
		return _komi;
	}
	
	public void addMove(Move move) {
		Node newNode = new Node(move,_currentNode);
		if (_currentNode != null) {
			_currentNode.addChild( newNode );
		} else if (_root == null) {
			_root = new Node[1];
			_root[0] = newNode;
		} else if (_currentNode == null) {
			Node[] newRoot = new Node[_root.length+1];
			for (int i=0;i<_root.length;i++)
				newRoot[i] = _root[i];
			newRoot[_root.length] = newNode;
			_root = newRoot;
		} 
		_currentNode = newNode;
		_moveno++;
	}
	
	public void addMove(String move) {
		addMove (MoveImpl.fromString(move));
	}

	public int moveNo() {
		return _moveno;
	}
	
	public Move getMove() {
		if (_currentNode == null) {
			return null;
		} else {
			return _currentNode.getMove();			
		}
	}

	public boolean last() {
		if (_currentNode == null) {
			return _root == null;
		} else {
			return _currentNode.getChildCount() == 0;
		}
	}

	public boolean next(int variant) {
		if (_currentNode != null && variant<_currentNode.getChildCount()) {
			_currentNode = _currentNode.getChild(variant);
			_moveno++;
			return true;
		}
		if (_currentNode == null && _root != null && variant<_root.length) {
			_currentNode = _root[variant];
			_moveno++;
			return true;
		}
		return false;
	}

	public boolean next() {
		return next(0);
	}

	public boolean prev() {
		if (_currentNode != null) {
			_currentNode = _currentNode.getParent();
			_moveno--;
			return true;
		} else {
			return false;
		}
	}

	public void rewind() {
		_currentNode = null;
		_moveno = 0;
	}

	public int variantsInThisNode() {
		if (_currentNode == null) {
			if (_root == null) return 0;
			else return _root.length;
		} else {
			return _currentNode.getChildCount();
		}
	}

	public String getComment() {
		if (_currentNode != null) {
			return _currentNode.getComment();
		} else {
			return null;
		}
	}
	
	public void setComment(String comment) {
		if (_currentNode != null)
			_currentNode.setComment(comment);
	}

}
