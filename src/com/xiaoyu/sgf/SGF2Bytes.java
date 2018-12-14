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
        return getBytes(sgfSource);
    }

    //Url解析
    public static byte[] parseSgf(URL url) throws IOException{
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(url);
        return getBytes(sgfSource);
    }

    //String解析
    public static byte[] parseSgf(String sgf){
        if (sgf == null || sgf.length() == 0) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(sgf);
        return getBytes(sgfSource);
    }


    //InputStream解析
    public static byte[] parseSgf(InputStream pgnInputStream) throws IOException{
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgnInputStream);
        return getBytes(sgfSource);
    }

    private static byte[] getBytes(SGFSource sgfSource) {
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }
}
