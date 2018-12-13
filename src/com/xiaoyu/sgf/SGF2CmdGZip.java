package com.xiaoyu.sgf;

import com.xiaoyu.sgf.base.GameTree;
import com.xiaoyu.sgf.sgfcmdtool.SgfToCmdByte;
import com.xiaoyu.sgf.sgftool.SGFSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Author: lyd
 * @Date: 2018/12/13 下午4:27
 * @Version 1.0.0
 */
public class SGF2CmdGZip extends SgfToCmdByte {

    //文件解析
    public static File parseSgf(File file, String filePath, String fileName) throws IOException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(file);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetGzFile(gameTree, filePath, fileName);
    }

    //Url解析
    public static File parseSgf(URL url, String filePath, String fileName) throws IOException {
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(url);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetGzFile(gameTree, filePath, fileName);
    }

    //String解析
    public static File parseSgf(String pgn, String filePath, String fileName) {
        if (pgn == null || pgn.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgn);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetGzFile(gameTree, filePath, fileName);
    }

    //InputStream解析
    public static File parseSgf(InputStream pgnInputStream, String filePath, String fileName) throws IOException {
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgnInputStream);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetGzFile(gameTree, filePath, fileName);
    }

}
