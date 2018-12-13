package com.xiaoyu.pgn.pgntool;

import java.util.*;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class PGNGame {

	private Map<String, String> tags;
	
	private List<PGNMove> moves;
	
	private String pgn;
	
	PGNGame() {
		tags = new HashMap<String, String>();
		moves = new LinkedList<PGNMove>();
	}
	
	PGNGame(String pgn) {
		this();
		this.pgn = pgn;
	}
	
	@Override
	public String toString() {
		return pgn == null ? "" : pgn;
	}
	
	void addTag(String key, String value) {
		tags.put(key, value);
	}
	
	void removeTag(String key) {
		tags.remove(key);
	}
	
	void addMove(PGNMove move) {
		moves.add(move);
	}
	
	void removeMove(PGNMove move) {
		moves.remove(move);
	}
	
	void removeMove(int index) {
		moves.remove(index);
	}
	
	public String getTag(String key) {
		return tags.get(key);
	}
	
	public Iterator<String> getTagKeysIterator() {
		return tags.keySet().iterator();
	}
	
	public boolean containsTagKey(String key) {
		return tags.containsKey(key);
	}
	
	public int getTagsCount() {
		return tags.size();
	}
	
	public PGNMove getMove(int index) {
		return moves.get(index);
	}
	
	public Iterator<PGNMove> getMovesIterator() {
		return moves.iterator();
	}
	
	public int getMovesCount() {
		return moves.size();
	}
	
	public int getMovePairsCount() {
		return moves.size() / 2;
	}
	
}
