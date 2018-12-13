package com.xiaoyu.sgf.sgftool;

import com.xiaoyu.sgf.base.GameTree;
import com.xiaoyu.sgf.base.util.SGFParser;
import com.xiaoyu.sgf.base.util.impl.SGFParserImpl;

import java.io.*;
import java.net.URL;

/**
 * @Author: lyd
 * @Date: 2018/12/13 上午10:45
 * @Version 1.0.0
 */
public class SGFSource {
    private String source;

    public SGFSource(String pgn) {
        if (pgn == null) {
            throw new NullPointerException("PGN data is null");
        }

        this.source = pgn;
    }

    public SGFSource(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public SGFSource(URL url) throws IOException {
        this(url.openStream());
    }

    public SGFSource(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder buffer = new StringBuilder();

        while ((line = br.readLine()) != null) {
            buffer.append(line + "\r\n");
        }

        br.close();
        this.source = buffer.toString();
    }

    @Override
    public String toString() {
        return source;
    }



    public GameTree getGameTree() {
        SGFParser sgfParser = new SGFParserImpl();
        return sgfParser.parse(source);
    }

    public String getSourceFromGameTree(GameTree gameTree) {
        SGFParser sgfParser = new SGFParserImpl();
        return sgfParser.write(gameTree);
    }

}
