package com.xiaoyu.pgn;

import com.xiaoyu.common.utils.StringUtils;
import com.xiaoyu.model.GameDataModel;
import com.xiaoyu.pgn.pgncmdtool.PgnToCmdByte;
import com.xiaoyu.pgn.pgntool.MalformedMoveException;
import com.xiaoyu.pgn.pgntool.PGNGame;
import com.xiaoyu.pgn.pgntool.PGNParseException;
import com.xiaoyu.pgn.pgntool.PGNSource;

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
public class Pgn2Bytes extends PgnToCmdByte {

    //文件解析 使用源文件的名称
    public static List<GameDataModel> parsePgn(File file) throws IOException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(file);
        return getPgnGameDataModels(source, StringUtils.getPathName(file.getPath()));
    }


    //Url解析  使用源文件名称
    public static List<GameDataModel> parsePgn(URL url) throws IOException {
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(url);
        return getPgnGameDataModels(source, StringUtils.getPathName(url.getPath()));
    }

    //String解析 使用解析出来的文件名称
    public static List<GameDataModel> parsePgn(String pgn) {
        if (pgn == null || pgn.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgn);
        return getPgnGameDataModels(source);
    }

    //InputStream解析 使用解析出来的文件名称
    public static List<GameDataModel> parsePgn(InputStream pgnInputStream) throws IOException {
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        PGNSource source = new PGNSource(pgnInputStream);
        return getPgnGameDataModels(source);
    }

    private static List<GameDataModel> getPgnGameDataModels(PGNSource source) {
        List<PGNGame> pgnGames;
        try {
            pgnGames = source.listGames();
            return processPgnAndGetBytes(pgnGames);
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

    private static List<GameDataModel> getPgnGameDataModels(PGNSource source, String name) {
        List<PGNGame> pgnGames;
        try {
            pgnGames = source.listGames();
            return processPgnAndGetBytes(pgnGames, name);
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
