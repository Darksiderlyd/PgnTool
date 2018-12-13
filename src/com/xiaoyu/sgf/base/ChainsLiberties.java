package com.xiaoyu.sgf.base;

public interface ChainsLiberties {
	
	int getGroupWithChainPoint(int point); // -1 no group
	int[] getGroupsWithLibertyPoint(int point); 
	
	int[] getLibertiesPoints(int group);
	int[] getChainPoints(int group);
	
	int getLibertiesCount(int group);
	
	int unionGroups(int group1, int group2);

	int addGroup(int chainpoint, int[] libertypoints);

	void addToGroup(int group, int chainpoint, int[] libertypoints);
	void addChainPointsToGroup(int group, int[] points);
	void AddLibertiesPointsToGroup(int group, int[] liberties);
	
	void deleteGroup(int group);
	
	void deleteLiberty(int group, int liberty);
	
	int size();
	
	ChainsLiberties clone();
}
