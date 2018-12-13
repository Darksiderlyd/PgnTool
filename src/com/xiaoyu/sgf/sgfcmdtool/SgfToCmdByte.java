package com.xiaoyu.sgf.sgfcmdtool;

import com.xiaoyu.common.cmdtool.CmdDataOrFileProcess;
import com.xiaoyu.common.cmdtool.cmd.TeaMoveChessCmd;
import com.xiaoyu.common.cmdtool.cmd.TeaNewGameBoardExtensionCmd;
import com.xiaoyu.common.cmdtool.cmd.base.IRtsCmd;
import com.xiaoyu.common.model.ChessRole;
import com.xiaoyu.common.model.ChessType;
import com.xiaoyu.sgf.base.GameTree;
import com.xiaoyu.sgf.base.Move;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xiaoyu.common.Config.isDebug;

/**
 * @Author: lyd
 * @Date: 2018/12/13 下午3:57
 * @Version 1.0.0
 */
public class SgfToCmdByte {

    private static final String tag = "XYSGFToCmdByte";

    protected static File processSgfAndGetGzFile(GameTree gameTree, String filePath, String fileName) {
        File gzFile = CmdDataOrFileProcess.getFile(filePath, fileName, processSgf(gameTree), tag);
        if (gzFile == null) return null;
        return gzFile;
    }

    protected static byte[] processSgfAndGetBytes(GameTree gameTree) {
        return processSgf(gameTree);
    }

    private static byte[] processSgf(GameTree gameTree) {
        String name = "Sgf棋局";
        List<IRtsCmd> cmds = new ArrayList<>();
        if (isDebug) System.out.println("############################");

        String gameName = gameTree.getGameName();

        if (gameName != null && gameName.length() > 0) {
            name = gameName;
        }

        int count = 1;
        while (!gameTree.last()) {
            gameTree.next();
            Move move = gameTree.getMove();

            if (count == 1) {
                TeaNewGameBoardExtensionCmd.Ext.Builder extBuild = new TeaNewGameBoardExtensionCmd.Ext.Builder();
                TeaNewGameBoardExtensionCmd.Ext ext = extBuild.setOrder(move.getPlayer() == Move.WHITE ? 1 : 0).build();
                TeaNewGameBoardExtensionCmd teaNewGameBoardExtensionCmd = new TeaNewGameBoardExtensionCmd(ChessType.getWeiqiTypeByLineNum(gameTree.getBoardSize()), name, 1, ext);
                cmds.add(teaNewGameBoardExtensionCmd);
            }

            TeaMoveChessCmd teaMoveChessCmd = new TeaMoveChessCmd(ChessRole.fromParserRoleToWeiqiRole(move.getPlayer()).getRole(), -1, -1, move.getX(), move.getY());
            cmds.add(teaMoveChessCmd);

            if (isDebug) {
                System.out.println((move.getPlayer() == Move.BLACK ? "黑" : "白") + " (X,Y): " + "(" + move.getX() + "," + move.getY() + ")");
            }

            count++;
        }


        if (isDebug) System.out.println();

        if (cmds.size() == 0) {
            System.out.println("Cmds size is 0");
            return null;
        }
        //处理命令
        String data = CmdDataOrFileProcess.processCmds(cmds, isDebug);
        if (data == null || data.length() == 0) {
            return null;
        }
        //插入时间 + 包长 + data
        return CmdDataOrFileProcess.packAndStoreMsg(data, 200);
    }


}
