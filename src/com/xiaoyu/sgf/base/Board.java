package com.xiaoyu.sgf.base;

/**
 * This Interface represents Go Board contract.
 * <br>
 * About P: 
 * 	P=boardLength(19*19=361) means PASS
 *  XXX: check if code complies with this. This was introduced because of TranspositionTable needs to
 *  store a P value as a PASS.
 *  
 */

public interface Board extends Cloneable {

	/**
	 * Constant value for an Invalid intersection.
	 */
	final static int INVALID = -1;
	
	/**
	 * Constant value for an Empty intersection.
	 */
	final static int EMPTY = 0;
	
	/**
	 * Constant value for Black.
	 */
	final static int BLACK = 1;
	
	/**
	 * Constant value for White.
	 */
	final static int WHITE = 2;
	
	/**
	 * Returns the board size.
	 * @return board size i.e. 9, 13, 19.
	 */
	int getSize();

	/**
	 * Converts a cartesian coordinate into a linear coordinate for this board size.
	 * @param x value
	 * @param y value
	 * @return a linear intesection
	 */
	int convertToP(int x, int y);
	
	/**
	 * Returns the x value of cartesian coordinate for the given intersection in linear coordinate.
	 * @param point linear intersection.
	 * @return x value in cartesian coordinates for the given point.
	 */
	int getXfromP(int point);
	
	/**
	 * Returns the y value of cartesian coordinates for the given intersection in linear coordinate.
	 * @param point lineas intersection.
	 * @return y value in cartesian coordinates for the given point.
	 */
	int getYfromP(int point);
	
	/**
	 * Returns board intersection's value.
	 * @param x value
	 * @param y value
	 * @return board constant
	 */
	int get(int x, int y);
	
	/**
	 * Returns board intersection's value.
	 * @param point linear intersection
	 * @return board constant
	 */
	int getP(int point);
	
	int get(String pos);
	
	/**
	 * Sets an intersection's value.
	 * @param x position
	 * @param y position
	 * @param value to set
	 */
	void set(int x, int y, int value);
	
	
	void set(String pos, int value);
	
	/**
	 * Sets an intersection's value using a linear coordinate.
	 * @param point linear intersection
	 * @param value to set
	 */
	void setP(int point, int value);
	
	/**
	 * Gets an array of valid horizontal / vertical neighbors for the intersection.
	 * @param point linear intersection
	 * @return an array of valid horizontal / vertical neighbors
	 */
	int[] getNearP(int point);
	
	/**
	 * Similar to getNearP(int) but returns intersections with the given value.
	 * @param point linear intersection.
	 * @param value neighbors have to have this value.
	 * @return an array of valid neighbords having the given value.
	 */
	int[] getNearP(int point, int value);
	
	/**
	 * Gets an array of valid diagonal intersections for the given intersection.
	 * @param point linear intersection.
	 * @return an array of valid diagonal intersections.
	 */
	int[] getDiagonalP(int point);
	
	/**
	 * Gets a linear array of ints with the board values. 
	 * @return an linear array with the contents of the board.
	 */
	int[] getArray();
	
	/**
	 * Gets a linear array of ints with the board values.
	 * @return an reference to the internal array with contents of the board (if valid in implementation).
	 */
	int[] getArrayNotCopy();
	
	/**
	 * Sets the values of every intersection by an array.
	 * @param value a linear array of intersections.
	 */
	void setArray(int[] value);

	/**
	 * Zorbish hash for this board.
	 * @return Zorbish hash value
	 */
	long getHash();
	
	/**
	 * Clones the board
	 * @return a board copy
	 */
	Board clone();

}
