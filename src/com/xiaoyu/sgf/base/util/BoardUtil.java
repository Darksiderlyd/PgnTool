package com.xiaoyu.sgf.base.util;

import com.xiaoyu.sgf.base.Board;

public interface BoardUtil {

    void flipHorizontal(Board board);

    void flipVertical(Board board);

    void rotateClockwise(Board board);

    void invert(Board board);

    Board[] allFlipInvertCombinations(Board board);

    Board[] allUniqueFlipInvertCombinations(Board board);

    /**
     * @param board
     * @param P
     * @return Board.INVALID, Board.WHITE, Board.BLACK
     */
    int isEye(Board board, int P);

    /**
     * @param board
     * @param P
     * @param relaxed means only look at horiz/vert colors (for chinese counting)
     * @return
     */
    int isEye(Board board, int P, boolean relaxed);

}
