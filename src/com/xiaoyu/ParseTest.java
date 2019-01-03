package com.xiaoyu;

import com.xiaoyu.common.Config;
import com.xiaoyu.model.GameDataModel;
import com.xiaoyu.pgn.Pgn2Bytes;
import com.xiaoyu.pgn.Pgn2CmdGZip;
import com.xiaoyu.pgn.pgntool.MalformedMoveException;
import com.xiaoyu.pgn.pgntool.PGNParseException;
import com.xiaoyu.pgn.pgntool.PGNParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * PGN test 1.0.0
 */
public class ParseTest {

    /**
     * @param args
     * @throws IOException
     * @throws PGNParseException
     * @throws MalformedMoveException
     * @throws NullPointerException
     */
    public static void main(String[] args) throws IOException, PGNParseException, NullPointerException, MalformedMoveException {
//        if (args.length == 0) {
//            System.out.println("Usage:");
//            System.out.println("\tpath");
//        }
//
////        System.out.println(ChessType.getWeiqiTypeByLineNum(Board.SIZE_9).getCode());
//
//        //###===============PGN Test===================
        File file = new File("/Users/yaodonglv/Desktop/XQS文档/test6.pgn");
        File file2 = new File("/Users/yaodonglv/Desktop/XQS文档/test.pgn");
        File file3 = new File("/Users/yaodonglv/Desktop/XQS文档/test10.pgn");
        URL url = new URL("http://127.0.0.1:8080/test.pgn");

        String path = url.getPath();
        int index1 = path.lastIndexOf("/");
        int index2 = path.lastIndexOf(".");
        String substring = path.substring(index1 + 1,index2);

        Config.isDebug = true;
        List<File> files = Pgn2CmdGZip.parsePgn(file3, "/Users/yaodonglv/Desktop/XQS文档/pgnData.gz", "pgnData");

        List<GameDataModel> models = Pgn2Bytes.parsePgn(file3);

        byte[][] defaultBoard = PGNParser.createDefaultBoard();
        PGNParser.printBoard(defaultBoard);

        byte[][] fenBoard = PGNParser.createFENBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", new int[1]);

        PGNParser.printBoard(fenBoard);

        //###===============SGF Test===================
//        File file = new File("/Users/yaodonglv/Desktop/XQS文档/sgftest1.sgf");
//        File file2 = new File("/Users/yaodonglv/Desktop/XQS文档/sgftest2.sgf");
//        URL url = new URL("http://127.0.0.1:8080/sgftest.sgf");
//        Config.isDebug = true;
//        SGF2CmdGZip.parseSgf(file2, "/Users/yaodonglv/Desktop/XQS文档/", "sgfData");


//        SGFSource sgfSource = new SGFSource(file);

//        GameTree gameTree = sgfSource.getGameTree();

//        while (!gameTree.last()) {
//            gameTree.next();
//            Move move = gameTree.getMove();
//            System.out.println((move.getPlayer() == Move.BLACK ? "黑" : "白") + " (X,Y): " + "(" + move.getX() + "," + move.getY() + ")");
//        }
//        gameTree.rewind();
//        String source = sgfSource.getSourceFromGameTree(gameTree);
//        System.out.println(source);


        //###===============SGF Game Test===================
//        Game game;
//        GameHelper gh;
//        int boardSize;
//        int maxValidMoves;
//
//        boardSize = 19;
//        maxValidMoves = (boardSize * boardSize) + 1;    // all board positions plus pass
//        game = new GameImpl(boardSize);
//        gh = new GameHelperImpl();
//
//
//        String move0 = "BLACK A1";
//        String move1 = "WHITE A2";
//        String move2 = "BLACK A5";
//        String move3 = "WHITE B1";
//
//
//        System.out.println(game.isValidMove(move0));
//
//        game.play(move0);
//        game.play(move1);
//        game.play(move2);
//        game.play(move3);
//
//
//
//        game.moveToPlay();

//        GameHelper gameHelper = new GameHelperImpl();
//        int[] chineseScore = gameHelper.getChineseScore(game);
//        System.out.println("Empty ：" + chineseScore[Board.EMPTY] + ",Black ：" + chineseScore[Board.BLACK] + ",White ：" + chineseScore[Board.WHITE]);

//        int[] array = game.getBoard().getArray();

        //###===============SGF Board Test===================
//        Board board = new BoardImpl(19);
//
//        board.setP(0,Board.BLACK);
//        board.setP(1,Board.WHITE);
//        board.setP(360,Board.BLACK);
//
//        board.set("A1",Board.BLACK);
//        board.set("A2",Board.WHITE);
//        board.set("T19",Board.BLACK);
//
//        board.set(0, 0, Board.BLACK);
//        board.set(0, 1, Board.WHITE);
//        board.set(18, 18, Board.BLACK);
//
//        int[] array = board.getArray();
//        int length = array.length;
//
//        for (int i = 0; i < length; i++) {
//            if ((i+1) % 19 == 0) {
//                System.out.println(array[i] + "");
//            } else {
//                System.out.print(array[i] + "");
//            }
//        }


        //###===============PGN Test===================
//        System.out.println(Pos.fromString("e2"));

//        PGNSource source = new PGNSource(url);
//        Iterator<PGNGame> i = source.listGames().iterator();
//
//        while (i.hasNext()) {
//            PGNGame game = i.next();
//
//            System.out.println("############################");
//            Iterator<String> tagsIterator = game.getTagKeysIterator();
//
//            while (tagsIterator.hasNext()) {
//                String key = tagsIterator.next();
//                System.out.println(key + " {" + game.getTag(key) + "}");
//            }
//
//            System.out.println();
//            Iterator<PGNMove> movesIterator = game.getMovesIterator();
//            int num = 1;
//
//            while (movesIterator.hasNext()) {
//                PGNMove move = movesIterator.next();
//
//                if (num % 2 == 1 && !move.isEndGameMarked()) {
//                    System.out.print((num + 1) / 2 + ". ");
//                }
//
//                if (!move.isEndGameMarked()) {
//                    String color = move.getColor() == Color.white ? "白" : "黑";
//                    System.out.print(color + "." + move.getPiece());
//                }
//
//                num++;
//
//                if (move.isEndGameMarked()) {
//                    System.out.print("(" + move.getMove() + ")");
//                } else if (move.isKingSideCastle()) {
//                    System.out.print("[O-O] ");
//                } else if (move.isQueenSideCastle()) {
//                    System.out.print("[O-O-O] ");
//                } else {
//                    System.out.print("[" + move.getFromSquare() + "]->[" + move.getToSquare() + "] ");
//                }
//            }
//
//            System.out.println();
//        }
    }

}