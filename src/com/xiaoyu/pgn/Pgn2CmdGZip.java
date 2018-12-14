package com.xiaoyu.pgn;

import com.xiaoyu.pgn.pgncmdtool.PgnToCmdByte;
import com.xiaoyu.pgn.pgntool.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0
 */
public class Pgn2CmdGZip extends PgnToCmdByte {

    //文件解析
    public static List<File> parsePgn(File file, String filePath, String fileName) throws IOException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(file);
        return getFiles(filePath, fileName, source);
    }

    //Url解析
    public static List<File> parsePgn(URL url, String filePath, String fileName) throws IOException {
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(url);
        return getFiles(filePath, fileName, source);
    }

    //String解析
    public static List<File> parsePgn(String pgn, String filePath, String fileName) {
        if (pgn == null || pgn.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgn);
        return getFiles(filePath, fileName, source);
    }

    //InputStream解析
    public static List<File> parsePgn(InputStream pgnInputStream, String filePath, String fileName) throws IOException {
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgnInputStream);
        return getFiles(filePath, fileName, source);
    }

    private static List<File> getFiles(String filePath, String fileName, PGNSource source) {
        List<PGNGame> pgnGames;
        try {
            pgnGames = source.listGames();
            return processPgnAndGetGzFile(pgnGames, filePath, fileName);
        } catch (PGNParseException e) {
            e.printStackTrace();
            System.out.println("Pgn Parse error");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Pgn IO error");
        } catch (MalformedMoveException e) {
            e.printStackTrace();
            System.out.println("Pgn content error. ex. piece,move... error");
        }
        return new ArrayList<>();
    }

}
