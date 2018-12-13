package com.xiaoyu.sgf.base.util;

import com.xiaoyu.sgf.base.GameTree;

import java.io.Reader;
import java.io.Writer;


public interface SGFParser {
	
	GameTree parse(String sgffile);
	GameTree parse(Reader sgfdata);
	String write(GameTree gt);
	boolean write(GameTree gt, Writer data);

}