package com.xiaoyu.sgf;

import com.xiaoyu.common.utils.StringUtils;
import com.xiaoyu.model.GameDataModel;
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
    public static GameDataModel parseSgf(File file) throws IOException {
        if (file == null || !file.exists()) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(file);
        return getBytes(sgfSource, StringUtils.getPathName(file.getPath()));
    }


    //Url解析
    public static GameDataModel parseSgf(URL url) throws IOException {
        if (url == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(url);
        return getBytes(sgfSource, StringUtils.getPathName(url.getPath()));
    }

    //String解析
    public static GameDataModel parseSgf(String sgf) {
        if (StringUtils.isEmpty(sgf)) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(sgf);
        return getBytes(sgfSource);
    }


    //InputStream解析
    public static GameDataModel parseSgf(InputStream pgnInputStream) throws IOException {
        if (pgnInputStream == null) {
            System.out.println("File does not exist!");
            return null;
        }
        SGFSource sgfSource = new SGFSource(pgnInputStream);
        return getBytes(sgfSource);
    }

    //不使用源文件名称
    private static GameDataModel getBytes(SGFSource sgfSource) {
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree);
    }

    //使用源文件名称 子文件为源文件名称 + i
    private static GameDataModel getBytes(SGFSource sgfSource, String name) {
        GameTree gameTree = sgfSource.getGameTree();
        return processSgfAndGetBytes(gameTree, name);
    }
}
