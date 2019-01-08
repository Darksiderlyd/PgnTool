package com.xiaoyu.sgf.base.util.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.GameTree;
import com.xiaoyu.sgf.base.Move;
import com.xiaoyu.sgf.base.impl.GameTreeImpl;
import com.xiaoyu.sgf.base.impl.MoveImpl;
import com.xiaoyu.sgf.base.util.SGFParser;

import java.io.*;


public class SGFParserImpl implements SGFParser {


    /*	(;GM[1]FF[4]CA[UTF-8]AP[CGoban:2]ST[2]RU[Japanese]SZ[19]KM[5.50]PW[player White]PB[Player Black])	*/


    private Move getSGFMove(String move, int boardSize, int player) {
        move = move.toUpperCase();
        // pass
        if (move.length() == 0 || move.equals("TT")) {
            return new MoveImpl(player);
        }
        // move
        char letter = move.charAt(0);
        char numba = move.charAt(1);
        return new MoveImpl(letter - 65, numba - 65, player);
    }

    private String writeSGFMove(Move move, int boardSize) {
        int player = move.getPlayer();
        char PC = (player == Board.BLACK) ? 'B' : 'W';
        if (move.isPass()) return PC + "[TT]";
        int x = move.getX();
        int y = move.getY();
        char letter = (char) (x + 97);
        char numba = (char) (y + 97);
        return PC + "[" + letter + numba + "]";
/*		v = boardSize-1-(numba-65)
		v - boardsize + 1 = -numba+65
		v - boardsize + 1 - 65 = - numba
		- v + boardsize - 1 + 65 = numba
*/
    }

    int skipCRLFSP(String data, int idx) {
        // skips CRs LFs
        if (idx == data.length()) return idx;
        char skip = data.charAt(idx);
        while (skip == '\n' || skip == '\r' || skip == ' ') {
            idx++;
            if (idx == data.length()) return idx;
            skip = data.charAt(idx);
        }
        return idx;
    }

    private int processCmd(GameTree gt, String cmd, String params, int stack) {

        // commenting not used ifs, doesnt affect perfomance. (parsing more than 4700 files doesnt seems to show
        // perfomance increase).

        if ("B".equals(cmd) || "Black".equals(cmd)) {  // B: Black move
            Move move = getSGFMove(params, gt.getBoardSize(), Board.BLACK);
            gt.addMove(move);
            stack++;
        } else if ("W".equals(cmd) || "White".equals(cmd)) {  // W: White move
            Move move = getSGFMove(params, gt.getBoardSize(), Board.WHITE);
            gt.addMove(move);
            stack++;
        }
//		else if ("CR".equals(cmd)) {  // CR: Circle (list of points)
//			// do nothing
//		}
//		else if ("RU".equals(cmd)) {  // RU: Rules
//			// do nothing
//		}
//		else if ("ST".equals(cmd)) {  // ST: Defines how variations should be shown
//			// do nothing
//		}
        else if ("GM".equals(cmd) || "GaMe".equals(cmd)) {  // GM: Game type
            if (!"1".equals(params))
                System.err.println("FATAL! We are trying to parse a SGF file from another kind of game.");
        } else if ("BR".equals(cmd) || "BlackRank".equals(cmd)) {  // BR: Black Rank
//			System.out.println("black rank:"+params);
        }
//		else if ("WR".equals(cmd) || "WhiteRank".equals(cmd)) {  // WR: White Rank
//			// do nothing.
//		}
//		else if ("RE".equals(cmd) || "REsult".equals(cmd)) {  // RE: Result
//			// do nothing.
//		}
//		else if ("CA".equals(cmd)) {  // CA: Charset
//			// do nothing
//		}
//		else if ("FF".equals(cmd) || "FileFormat".equals(cmd)) {  // FF: File Format
//			// do nothing.
//		}
//		else if ("AP".equals(cmd)) {  // AP: Application
//			// do nothing.
//		}
//		else if ("PC".equals(cmd) || "PlaCe".equals(cmd)) {  // PC: Place
//			// do nothing.
//		}
//		else if ("DT".equals(cmd) || "DaTe".equals(cmd)) {  // DT: Date
//			// do nothing.
//		}
        else if ("SZ".equals(cmd) || "SiZe".equals(cmd)) {  // SZ: Board Size ,default SiZe 19
            int size = Integer.parseInt(params);
            if (size == 9 || size == 13 || size == 19) {
                gt.setBoardSize(size);
            }
        } else if ("KM".equals(cmd) || "KoMi".equals(cmd)) {  // KM: Komi
            try {
                gt.setKomi(Float.parseFloat(params));
            } catch (NumberFormatException e) {
            }
        } else if ("PW".equals(cmd) || "PlayerWhite".equals(cmd)) {  // PW: Player White
            gt.setWhiteName(params);
        } else if ("PB".equals(cmd) || "PlayerBlack".equals(cmd)) {  // PB: Player Black
            gt.setBlackName(params);
        } else if ("C".equals(cmd) || "Comment".equals(cmd)) {   // C: Comment
            if (gt.moveNo() == 0) {
                gt.setGameComment(params);    // a game comment
            } else {
                gt.setComment(params); // a move comment
            }
        }
//		else if ("WL".equals(cmd)) {  // WL: White time left
//			// pass
//		}
//		else if ("BL".equals(cmd)) {  // WL: Black time left
//			// pass
//		}
//		else if ("VW".equals(cmd) || "VieW".equals(cmd)) {  // VW: View ( ?? )
//			// pass
//		}
        else if ("EV".equals(cmd) || "EVent".equals(cmd)) {  // EV: Event
            if (null == params || params.length() == 0) {
                gt.setGameEvent("赛事");
            } else {
                gt.setGameEvent(params);
            }
        } else if ("GN".equals(cmd) || "GameName".equals(cmd)) {  // GN: Game Name
            if (null == params || params.length() == 0) {
                gt.setGameName("Sgf棋谱");
            } else {
                gt.setGameName(params);
            }
        } else if ("AB".equals(cmd) || "AddBlack".equals(cmd)) {
            if (params.length() == 2) {
                if (gt.getABAdd() != null) {
                    gt.setABAdd(gt.getABAdd() + "," + params);
                } else {
                    gt.setABAdd(params);
                }
            } else {
                if (gt.getABAdd() != null) {
                    gt.setABAdd(gt.getABAdd() + "," + params.replace("][", ","));
                } else {
                    gt.setABAdd(params.replace("][", ","));
                }
            }
//            System.out.println(gt.getABAdd());
        } else if ("AW".equals(cmd) || "AddWhite".equals(cmd)) {
            if (params.length() == 2) {
                if (gt.getAWAdd() != null) {
                    gt.setAWAdd(gt.getAWAdd() + "," + params);
                } else {
                    gt.setAWAdd(params);
                }
            } else {
                if (gt.getAWAdd() != null) {
                    gt.setAWAdd(gt.getAWAdd() + "," + params.replace("][", ","));
                } else {
                    gt.setAWAdd(params.replace("][", ","));
                }
            }
//            System.out.println(gt.getAWAdd());
        }
//        else if ("AE".equals(cmd) || "AddEmpty".equals(cmd)) {
//
//        }
        else {
            //System.err.println("Unrecognized SGF command: " + cmd + "["+params+"]");
        }
        return stack;
    }

    private int recursiveGreedyParser(GameTree gt, String sgf, int idx) {

        idx = skipCRLFSP(sgf, idx);

        if (idx >= sgf.length() || sgf.charAt(idx) != '(')
            return -1; // we should be at the beginning of a new game!

        int stack = 0;
        idx = skipCRLFSP(sgf, ++idx);

        boolean processing = true;
        while (processing && idx < sgf.length()) {
            char nextChar = sgf.charAt(idx);
            if (nextChar == ';') {
                // just skip right now
                idx = skipCRLFSP(sgf, ++idx);
            } else if (nextChar == '(') {
                // new SubGame
                idx = recursiveGreedyParser(gt, sgf, idx);
            } else if (nextChar == ')') {
                // end of this subgame
                processing = false;
                idx++;
            } else {
                // A command
                int idx2 = sgf.indexOf('[', idx);
                String cmd = sgf.substring(idx, idx2);

                idx = idx2 + 1;
                // its parameter
                idx2 = sgf.indexOf(']', idx);
                while (idx != idx2 - 1 && sgf.charAt(idx2 - 1) == '\\') { // allows \] in comments
                    idx2 = sgf.indexOf(']', idx2 + 1);
                }

                while (sgf.charAt(idx2 + 1) == '[') {
                    idx2 = idx2 + 4;
                }

                String params = sgf.substring(idx, idx2);
                idx = idx2 + 1; // ready for next command
                // process the command
                stack = processCmd(gt, cmd, params, stack);
            }
            idx = skipCRLFSP(sgf, idx);
        }

        for (int i = 0; i < stack; i++) {
            gt.prev();
        }
        return skipCRLFSP(sgf, idx);
    }

    public GameTree parse(String sgf) {
        if (sgf == null) return null;
        GameTree gt = new GameTreeImpl();

        int lastPosParser = recursiveGreedyParser(gt, sgf, 0);
        if (sgf.length() == lastPosParser) {
            gt.rewind();
            return gt;
        } else {
//			System.err.println("DEBUG: PARSER NOT FINISHED, WITHOUT PARSE:" + sgf.substring(lastPosParser));
            return null;
        }
    }

    public GameTree parse(Reader sgfdata) {

        // im sure there is somewhere in the API a way to read the contents of a Readed into a String

        char[] cbuf = new char[65536];
        StringBuffer stringbuf = new StringBuffer();
        BufferedReader myBufReader = new BufferedReader(sgfdata);

        int read = 0;
        while (read != -1) {
            try {
                read = myBufReader.read(cbuf, 0, 65536);
                if (read > 0) stringbuf.append(cbuf, 0, read);
            } catch (IOException e) {
                System.err.println("exception");
                e.printStackTrace();
            }
        }

        return parse(stringbuf.toString());

    }

    public String write(GameTree gt) {
        StringWriter sw = new StringWriter();
        if (write(gt, sw)) {
            return sw.toString();
        } else {
            return null;
        }
    }

    private void writeRec(GameTree gt, Writer data) throws IOException {
        Move move = gt.getMove();
        if (move != null)
            data.append(";" + writeSGFMove(move, gt.getBoardSize()));
        String comment = gt.getComment();
        if (comment != null)
            data.append("C[" + comment + "]");

        int variants = gt.variantsInThisNode();
        for (int i = 0; i < variants; i++) {
            if (!gt.next(i)) return;
            if (variants > 1) data.append("\n(");
            writeRec(gt, data);
            if (variants > 1) data.append(")");
            gt.prev();
        }
    }

    public boolean write(GameTree gt, Writer data) {
        try {
            data.append("(;GM[1]FF[4]CA[UTF-8]AP[CGoban:2]ST[2]RU[Japanese]");
            data.append("SZ[" + gt.getBoardSize() + "]");
            data.append("KM[" + gt.getKomi() + "]");
            data.append("PW[" + gt.getWhiteName() + "]");
            data.append("PB[" + gt.getBlackName() + "]");
            String comment = gt.getGameComment();
            if (comment != null)
                data.append("C[" + comment + "]");
            data.append('\n');
            writeRec(gt, data);
            data.append(")\n");

        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
