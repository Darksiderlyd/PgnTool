package com.xiaoyu.pgntool;

import java.io.*;
import java.net.URL;
import java.util.List;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class PGNSource {

	private String source;
	
	public PGNSource(String pgn) {
		if (pgn == null) {
			throw new NullPointerException("PGN data is null");
		}
		
		this.source = pgn;
	}
	
	public PGNSource(File file) throws IOException {
		this(new FileInputStream(file));
	}
	
	public PGNSource(URL url) throws IOException {
		this(url.openStream());
	}
	
	public PGNSource(InputStream inputStream) throws IOException {
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
	
	public List<PGNGame> listGames() throws PGNParseException, IOException, NullPointerException, MalformedMoveException {
		return PGNParser.parse(source);
	}
	
	public List<PGNGame> listGames(boolean force) throws PGNParseException, IOException, NullPointerException, MalformedMoveException {
		return PGNParser.parse(source, force);
	}
	
}
