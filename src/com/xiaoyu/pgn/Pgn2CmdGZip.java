package com.xiaoyu.pgn;

import com.xiaoyu.pgn.pgncmdtool.PgnToCmdByte;
import com.xiaoyu.pgn.pgntool.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0
 */
public class Pgn2CmdGZip extends PgnToCmdByte {

    //文件解析
    public static File parsePgn(File file, String filePath, String fileName) throws IOException, PGNParseException, MalformedMoveException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(file);
        List<PGNGame> pgnGames = source.listGames();
        PGNGame pgnGame = pgnGames.get(0);
        return processPgnAndGetGzFile(pgnGame, filePath, fileName);
    }

    //Url解析
    public static File parsePgn(URL url, String filePath, String fileName) throws IOException, PGNParseException, MalformedMoveException {
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(url);
        List<PGNGame> pgnGames = source.listGames();
        PGNGame pgnGame = pgnGames.get(0);
        return processPgnAndGetGzFile(pgnGame, filePath, fileName);
    }

    //String解析
    public static File parsePgn(String pgn, String filePath, String fileName) throws IOException, PGNParseException, MalformedMoveException {
        if (pgn == null || pgn.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgn);
        List<PGNGame> pgnGames = source.listGames();
        PGNGame pgnGame = pgnGames.get(0);
        return processPgnAndGetGzFile(pgnGame, filePath, fileName);
    }

    //InputStream解析
    public static File parsePgn(InputStream pgnInputStream, String filePath, String fileName) throws IOException, PGNParseException, MalformedMoveException {
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgnInputStream);
        List<PGNGame> pgnGames = source.listGames();
        PGNGame pgnGame = pgnGames.get(0);
        return processPgnAndGetGzFile(pgnGame, filePath, fileName);
    }

}
