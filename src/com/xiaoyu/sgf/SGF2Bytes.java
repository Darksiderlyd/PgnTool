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
 * @Date: 2018/12/13 上午10:21
 * @Version 1.0.0
 */
public class SGF2Bytes extends SgfToCmdByte {

    //文件解析
    public static byte[] parseSgf(File file) throws IOException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(file);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }

    //Url解析
    public static byte[] parseSgf(URL url) throws IOException{
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(url);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }

    //String解析
    public static byte[] parseSgf(String pgn){
        if (pgn == null || pgn.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgn);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }

    //InputStream解析
    public static byte[] parseSgf(InputStream pgnInputStream) throws IOException{
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgnInputStream);
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }

}
