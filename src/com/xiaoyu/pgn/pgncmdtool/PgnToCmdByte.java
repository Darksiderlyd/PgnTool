package com.xiaoyu.pgn.pgncmdtool;

import com.xiaoyu.common.cmdtool.cmd.TeaMoveChessCmd;
import com.xiaoyu.common.cmdtool.CmdDataOrFileProcess;
import com.xiaoyu.common.cmdtool.cmd.TeaNewGameBoardExtensionCmd;
import com.xiaoyu.common.cmdtool.cmd.base.IRtsCmd;
import com.xiaoyu.common.utils.StringUtils;
import com.xiaoyu.model.ChessRole;
import com.xiaoyu.model.ChessType;
import com.xiaoyu.model.GameDataModel;
import com.xiaoyu.pgn.pgntool.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.xiaoyu.common.Config.isDebug;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class PgnToCmdByte {

    private static final String tag = "XYPgnToCmdByte";


    protected static List<File> processPgnAndGetGzFile(List<PGNGame> pgnGames, String filePath, String fileName) {
        List<GameDataModel> gameDataModels = processPgnAndGetBytes(pgnGames);
        List<File> gzfileList = new ArrayList<>();
        for (int i = 0; i < gameDataModels.size(); i++) {
            GameDataModel gameDataModel = gameDataModels.get(i);
            gzfileList.add(CmdDataOrFileProcess.getFile(filePath, fileName, gameDataModel.getPgnDatas(), tag));
        }
        return gzfileList;
    }

    protected static List<GameDataModel> processPgnAndGetBytes(List<PGNGame> pgnGames, String name) {
        List<GameDataModel> gameDataModels = new ArrayList<>();
        GameDataModel gameDataModel;
        for (int i = 0; i < pgnGames.size(); i++) {
            PGNGame pgnGame = pgnGames.get(i);
            gameDataModel = new GameDataModel(processPgn(pgnGame, name), name, i);
            gameDataModels.add(gameDataModel);
        }
        return gameDataModels;
    }

    protected static List<GameDataModel> processPgnAndGetBytes(List<PGNGame> pgnGames) {
        List<GameDataModel> gameDataModels = new ArrayList<>();
        GameDataModel gameDataModel;
        for (int i = 0; i < pgnGames.size(); i++) {
            PGNGame pgnGame = pgnGames.get(i);
            String name = "XqsPgn棋局";

            Iterator<String> tagsIterator = pgnGame.getTagKeysIterator();
            //解析头部
            while (tagsIterator.hasNext()) {
                String key = tagsIterator.next();
                String tag = pgnGame.getTag(key);
                if (key.equals("Event")) {
                    name = tag.replace(" ", "");
                }
                if (isDebug) System.out.println(key + " {" + tag + "}");
            }


            gameDataModel = new GameDataModel(processPgn(pgnGame, name), name, i);
            gameDataModels.add(gameDataModel);
        }
        return gameDataModels;
    }

    private static byte[] processPgn(PGNGame pgnGame, String name) {

        List<IRtsCmd> cmds = new ArrayList<>();

        if (isDebug) {
            System.out.println("############################");
            System.out.println();
        }

        Iterator<PGNMove> movesIterator = pgnGame.getMovesIterator();
        int num = 1;
        //解析每一步
        while (movesIterator.hasNext()) {
            PGNMove move = movesIterator.next();
            boolean chessColor = move.getColor() == Color.white;
            //初始化添加创建棋盘命令
            if (num == 1) {
                TeaNewGameBoardExtensionCmd.Ext.Builder extBuild = new TeaNewGameBoardExtensionCmd.Ext.Builder();
                TeaNewGameBoardExtensionCmd.Ext ext = extBuild.setOrder(chessColor ? 1 : 0).setLayout(0).setOpen(pgnGame.getFenOpen()).build();
                TeaNewGameBoardExtensionCmd teaNewGameBoardExtensionCmd = new TeaNewGameBoardExtensionCmd(ChessType.Mode_Chess, name, 1, ext);
                teaNewGameBoardExtensionCmd.toByte();
                cmds.add(teaNewGameBoardExtensionCmd);
            }


            TeaMoveChessCmd teaMoveChessCmd = null;


            if (num % 2 == 1 && !move.isEndGameMarked()) {
                if (isDebug) System.out.print((num + 1) / 2 + ". ");
            }

            num++;
            //短易位
            if (move.isEndGameMarked()) {
                if (isDebug) System.out.print("(" + move.getMove() + ")");
            } else if (move.isKingSideCastle()) {
                //black x 4 y 0  x2 7 y2 0
                //white x 4 y 7  x2 7 y2 7
                teaMoveChessCmd = new TeaMoveChessCmd(ChessRole.fromParserRoleToChessRole(PGNParser.KING, move.getColor()).getRole(), 4, chessColor ? 7 : 0, 7, chessColor ? 7 : 0);
                cmds.add(teaMoveChessCmd);
                if (isDebug) {
                    String color = chessColor ? "白" : "黑";
                    System.out.print(color + "." + PGNParser.KING + "[O-O] ");
                }
                //长易位
            } else if (move.isQueenSideCastle()) {
                //black x 4 y 0  x2 0 y2 0
                //white x 4 y 7  x2 0 y2 7
                teaMoveChessCmd = new TeaMoveChessCmd(ChessRole.fromParserRoleToChessRole(PGNParser.KING, move.getColor()).getRole(), 4, chessColor ? 7 : 0, 0, chessColor ? 7 : 0);
                cmds.add(teaMoveChessCmd);
                if (isDebug) {
                    String color = chessColor ? "白" : "黑";
                    System.out.print(color + "." + PGNParser.KING + "[O-O-O] ");
                }
                //普通Move以及升变
            } else {
                int from = Pos.fromString(move.getFromSquare());
                int to = Pos.fromString(move.getToSquare());

                int x = Pos.col(from);
                int y = Pos.row(from);
                int x2 = Pos.col(to);
                int y2 = Pos.row(to);

                teaMoveChessCmd = new TeaMoveChessCmd(ChessRole.fromParserRoleToChessRole(move.isPromoted() ? move.getPromotion() : move.getPiece(), move.getColor()).getRole(), x, y, x2, y2);
                cmds.add(teaMoveChessCmd);
                if (isDebug) {
                    if (move.isPromoted()) {
                        //升变目标子
                        System.out.println(move.getPromotion());
                    }

                    String color = chessColor ? "白" : "黑";
                    System.out.print(color + "." + move.getPiece());
                    System.out.print("[" + move.getFromSquare() + "]->[" + move.getToSquare() + "] ");
                }
            }

        }

        if (isDebug) System.out.println();

        if (cmds.size() == 0) {
            System.out.println("Cmds size is 0");
            return null;
        }
        //处理命令
        String data = CmdDataOrFileProcess.processCmds(cmds, isDebug);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        //插入时间 + 包长 + data
        return CmdDataOrFileProcess.packAndStoreMsg(data, 200);
    }

}
