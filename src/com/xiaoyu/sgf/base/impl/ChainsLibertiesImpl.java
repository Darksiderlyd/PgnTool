package com.xiaoyu.sgf.base.impl;

import com.xiaoyu.sgf.base.ChainsLiberties;

import java.util.Arrays;


/**
 * Nice array cached structure. Seems to be efficient.
 * Needs to implement binary search in array elements. 
 * Adding a group of liberties is not efficient, have O(n) for checking repeated liberties
 */

public class ChainsLibertiesImpl implements ChainsLiberties {

	int[][] _chainp;  // chain points
	int[] _chainp_s;
	int[][] _libp;  // liberty points
	int[] _libp_s;
	
	int _lastGroup;
	
	static int PREALLOC_GROUPS = 200;
	static int PREALLOC_CHAIN_POINTS = 10;
	static int PREALLOC_LIBERTIES_POINTS = 20;
	
	static int REALLOC_LIBERTIES_POINTS = 20; 
	static int REALLOC_CHAIN_POINTS = 10; 

	public ChainsLibertiesImpl() {
		_chainp = new int[PREALLOC_GROUPS][];
		_chainp_s = new int[PREALLOC_GROUPS];
		
		_libp = new int[PREALLOC_GROUPS][];
		_libp_s = new int[PREALLOC_GROUPS];
		
		// preallocate for perfomance
		for (int i=0; i<_chainp.length; i++) {
			_chainp[i] = new int[PREALLOC_CHAIN_POINTS];
			_chainp_s[i] = 0;
			_libp[i] = new int[PREALLOC_LIBERTIES_POINTS];
			_libp_s[i] = 0;
		}
		_lastGroup = 0;
	}
	
	public ChainsLiberties clone() {
		ChainsLibertiesImpl cloned = new ChainsLibertiesImpl();
		cloned._chainp = new int[_chainp.length][];
		for (int i=0; i<_chainp.length; i++)
			cloned._chainp[i] = _chainp[i].clone();
		cloned._libp = new int[_libp.length][];
		for (int i=0; i<_libp.length; i++)
			cloned._libp[i] = _libp[i].clone();
		cloned._chainp_s = _chainp_s.clone();
		cloned._libp_s = _libp_s.clone();
		cloned._lastGroup = _lastGroup;
		return cloned;
	}
	

	private void extendLibertyPoints(int group, int positionsNeeded) {
		int[] newLib = new int[ positionsNeeded + REALLOC_LIBERTIES_POINTS ];
		System.arraycopy(_libp[group], 0, newLib, 0, _libp_s[group]);
		_libp[group] = newLib;
	}

	private void extendChainPoints(int group, int positionsNeeded) {
		int[] newChain = new int[ positionsNeeded + REALLOC_CHAIN_POINTS ];
		System.arraycopy(_chainp[group], 0, newChain, 0, _chainp_s[group]);
		_chainp[group] = newChain;
	}
	
	public int[] getChainPoints(int group) {
		if (group>=_lastGroup)
			return null;
		
		int[] res = new int[_chainp_s[group]];
		System.arraycopy(_chainp[group], 0, res, 0, _chainp_s[group]);
		return res;
	}

	public int getGroupWithChainPoint(int point) {

		for (int g=0; g<_lastGroup; g++) {			
			if (_chainp_s[g] < 5 || true) { // direct search, doesnt make sense to make a binary search
				for (int i=0; i<_chainp_s[g]; i++)
					if (_chainp[g][i] == point) return g;
			} else {
				// binary search here
			}
		}
		return -1;
	}

	public int[] getGroupsWithLibertyPoint(int point) {
		int[] res = new int[4]; // no more than 4 groups will be share a liberty
		int lastRes = 0;
		
		for (int g=0; g<_lastGroup; g++) {
			if (_libp_s[g]<5 || true) { // direct search
				for (int i=0; i<_libp_s[g]; i++)
					if(_libp[g][i] == point) {
						res[lastRes++] = g;
						break;
					}
			} else {
				// binary search here				
			}
		}
		// returns a resized array
		int[] res2 = new int[lastRes];
		System.arraycopy(res, 0, res2, 0, lastRes);
		return res2;		
	}

	public int[] getLibertiesPoints(int group) {
		if (group>=_lastGroup)
			return null;
		
		int[] res = new int[_libp_s[group]];
		System.arraycopy(_libp[group], 0, res, 0, _libp_s[group]);
		return res;
	}

	public int size() {
		return _lastGroup;
	}

	public int addGroup(int chainpoint, int[] libertypoints) {
		int g = _lastGroup;
		_lastGroup++;
		
		_chainp[g][0] = chainpoint;
		_chainp_s[g]=1;
		
		if (libertypoints.length>_libp[g].length) // extend it
			extendLibertyPoints(g, libertypoints.length);

		System.arraycopy(libertypoints, 0, _libp[g], 0, libertypoints.length);
		Arrays.sort(_libp[g],0,libertypoints.length);
		_libp_s[g] = libertypoints.length;

		return g;
	}


	public void AddLibertiesPointsToGroup(int group, int[] liberties) {
		if (group>=_lastGroup) return;
		
		if (liberties.length + _libp_s[group] >= _libp[group].length) // lets extend it
			extendLibertyPoints(group, liberties.length + _libp_s[group]);
		
		int _s = _libp_s[group];
		for (int i=0; i<liberties.length; i++) {
			_libp[group][_s++] = liberties[i];
		}
		_libp_s[group] = _s;
		
		Arrays.sort(_libp[group],0,_libp_s[group]); // XXX: meanwhile binary search is not used for search... should not be used
		

		//XXX: ultra ineficient, i should check if some liberty already exist
		int last = _libp[group][0];
		int deltasize = 0;
		for (int i=1; i<_libp_s[group]; i++) {
			if (last == _libp[group][i]) {
				_libp[group][i] = Integer.MAX_VALUE; // so it get upper when sorted
				deltasize--;
			} else {
				last = _libp[group][i];
			}
		}
		if (deltasize!=0) {
			Arrays.sort(_libp[group],0,_libp_s[group]);
			_libp_s[group] += deltasize;
		}
	}

	public void addChainPointsToGroup(int group, int[] points) {
		if (group>=_lastGroup) return;
		
		if (points.length + _chainp_s[group] >= _chainp[group].length) // lets extend it
			extendChainPoints(group, points.length + _chainp_s[group]);
		
		int _s = _chainp_s[group];
		for (int i=0; i<points.length; i++) {
			_chainp[group][_s++] = points[i];
		}
		_chainp_s[group] = _s;
		
		Arrays.sort(_chainp[group],0,_chainp_s[group]); // XXX: meanwhile binary search is not used for search... should not be used
	}


	public void addToGroup(int group, int chainpoint, int[] libertypoints) {
		addChainPointsToGroup(group, new int[]{chainpoint});
		AddLibertiesPointsToGroup(group, libertypoints);
	}
	
	private void swapArrays(int i1, int i2) {
		int[] atmp;
		int itmp;
		
		atmp = _chainp[i2];
		_chainp[i2] = _chainp[i1];
		_chainp[i1] = atmp;
		
		itmp = _chainp_s[i2];
		_chainp_s[i2] =  _chainp_s[i1];
		_chainp_s[i1] = itmp;
		
		atmp = _libp[i2];
		_libp[i2] = _libp[i1];
		_libp[i1] = atmp;
		
		itmp = _libp_s[i2];
		_libp_s[i2] = _libp_s[i1];
		_libp_s[i1] = itmp;
	}


	public int unionGroups(int group1, int group2) {
		// group1 < group2 to make swapArrays work. 
		// (fixes bug tested in GameImplTest_GameRegression.testReplay_tango9_vs_tommy2626())
		if (group1>group2) {
			int tmp = group2;
			group2 = group1;
			group1 = tmp;
		}
		// first of all, move everything from group2 to group1
		int[] points = new int[_chainp_s[group2]];
		System.arraycopy(_chainp[group2], 0, points, 0, _chainp_s[group2]);
		addChainPointsToGroup(group1, points);
		
		int[] liberties = new int[_libp_s[group2]];
		System.arraycopy(_libp[group2], 0, liberties, 0, _libp_s[group2]);
		AddLibertiesPointsToGroup(group1, liberties);

		// delete last record, if neccesary swaps last with removed one
		_lastGroup--;
		if (group2!=_lastGroup)
			swapArrays(group2, _lastGroup);
		
		return group1;
	}


	public int getLibertiesCount(int group) {
		if (group<_lastGroup) {
			return _libp_s[group];			
		} else {
			return -1;
		}
	}


	public void deleteGroup(int group) {
		if (group<_lastGroup) {
			_lastGroup--;
			if (group!=_lastGroup)
				swapArrays(group, _lastGroup);
/*			
			if (group+1==_lastGroup) {
				_lastGroup--;
			} else {
				_lastGroup--;
				swapArrays(group, _lastGroup);
			}
*/			
		}
	}

	public void deleteLiberty(int group, int liberty) {

		int i = 0;
		while (i<_libp_s[group] && _libp[group][i]!=liberty)
			i++;

		if (_libp[group][i]==liberty) {	// its here!
			_libp_s[group]--;
			
			while (i<_libp_s[group])
				_libp[group][i]=_libp[group][++i];
		}

	}

	
	public String debugString() {
		StringBuffer sb = new StringBuffer();
		
		for (int g=0; g<_lastGroup; g++) {
			sb.append(g + ": [");
			for (int i=0; i<_chainp_s[g]; i++)
					sb.append(_chainp[g][i]+",");
			sb.append("] {");
			for (int i=0; i<_libp_s[g]; i++)
				sb.append(_libp[g][i]+",");
			sb.append("}\n");
		}	
		return sb.toString();
	}
	
	public void integrityCheck() {
		boolean err = false;
		// 1 liberty couldnt be part of a chain point
		
		for (int g=0; g<_lastGroup; g++) {
			int[] lib = getLibertiesPoints(g);
			for (int g2=g+1; g2<_lastGroup; g2++) {
				int[] chain = getChainPoints(g2);
				
				for (int i=0; i<lib.length; i++)
					for (int i2=0; i2<chain.length; i2++)
						if (lib[i] == chain[i2]) {
							err = true;
							System.err.print("Error! chain "+g2+" contains point "+chain[i2]+", which is a liberty of group "+g+"!\n");
						}
			}
		}
		
		// 2 chains couldnt share a chain point
		for (int g=0; g<_lastGroup; g++) {
			int[] chain = getChainPoints(g);
			for (int g2=g+1; g2<_lastGroup; g2++) {
				int[] chain2 = getChainPoints(g2);

				for (int i=0; i<chain.length; i++)
					for (int i2=0; i2<chain2.length; i2++)
						if (chain[i] == chain2[i2]) {
							err = true;
							System.err.print("Error! chain "+g2+" contains point "+chain2[i2]+", which is also in group "+g+"!\n");
						}
			}
		}
				
		if (err)
			System.err.println(debugString());	
	}

}
