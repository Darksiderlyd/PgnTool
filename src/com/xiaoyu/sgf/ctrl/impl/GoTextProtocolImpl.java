package com.xiaoyu.sgf.ctrl.impl;

import com.xiaoyu.sgf.base.Board;
import com.xiaoyu.sgf.base.Move;
import com.xiaoyu.sgf.base.impl.MoveImpl;
import com.xiaoyu.sgf.ctrl.Controller;
import com.xiaoyu.sgf.ctrl.Engine;

import java.io.*;
import java.lang.reflect.Field;


/**
 * This class implements the interface between an InputStream/OutputStream and a go engine using the
 * Go Text Protocol as defined in: http://www.lysator.liu.se/~gunnar/gtp/gtp2-spec-draft2/gtp2-spec.html
 * This class implements the Controller interface. A generic interface for controlling Go engines.
 * (as defined by Engine interface)
 * 
 * XXX: This class should be unit tested. 
 * XXX: This class its a bit buggy.
 * XXX: BufferedWriter.newLine() could not be OS awared/GTP awared. GTP is always \n, Java is OS specific.
 *  
 * @author esabb - 22oct2006
 *
 */
public class GoTextProtocolImpl implements Controller {

	final static String PROTOCOL_VERSION	 = "2";
	final static String CMD_PROCOTOL_VERSION = "protocol_version";
	final static String CMD_NAME 			 = "name";
	final static String CMD_VERSION			 = "version";
	final static String CMD_KNOWN_COMMAND 	 = "known_command";
	final static String CMD_LIST_COMMANDS 	 = "list_commands";
	final static String CMD_QUIT 			 = "quit";
	final static String CMD_BOARDSIZE 		 = "boardsize";
	final static String CMD_CLEAR_BOARD		 = "clear_board";
	final static String CMD_KOMI			 = "komi";
	final static String CMD_PLAY			 = "play";
	final static String CMD_GENMOVE			 = "genmove";
	final static String CMD_SHOWBOARD		 = "showboard";
	final static String CMD_GET_RANDOM_SEED  = "get_random_seed";
	final static String CMD_SET_RANDOM_SEED  = "set_random_seed";
	final static String CMD_FINAL_SCORE		 = "final_score";
	final static String CMD_CPUTIME			 = "cputime";

	//final static String CMD_NAME = "name";
	//final static String CMD_NAME = "name";
	// undo, time_settings, time_left, final_score, final_status_list
	
	InputStream _in;
	OutputStream _out;
	BufferedReader _bis;
	BufferedWriter _bow;
	Engine _engine;
	
	public GoTextProtocolImpl(InputStream in, OutputStream out) {
		_in = in;
		_out = out;
		try {
			_bis = new BufferedReader(new InputStreamReader(in, "US-ASCII"));
			_bow = new BufferedWriter(new OutputStreamWriter(out, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public GoTextProtocolImpl() {
	}
	
	public void setStreams(InputStream in, OutputStream out) {
		_in = in;
		_out = out;
		try {
			_bis = new BufferedReader(new InputStreamReader(in, "US-ASCII"));
			_bow = new BufferedWriter(new OutputStreamWriter(out, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void setEngine(Engine engine) {
		_engine = engine;
	}

	public void run() {
		
		boolean running = true;
		while (running) {
			String rawCommand;
			try {
				rawCommand = _bis.readLine();
				if (rawCommand != null) {
//					rawCommand = rawCommand.replace('\r', ' ');
//					rawCommand = rawCommand.replace('\n', ' ');
					rawCommand = rawCommand.trim();
//					System.err.println(">>>"+rawCommand+"<<<"+rawCommand.length());
					if (rawCommand.length()>0) {
						String[] tokens = rawCommand.split(" ");
						running = running && processCommands(tokens);
					}
				}
				
				if (rawCommand == null)
					running = false;
				
			} catch (IOException e) {
				running = false;
				break;
			}
		}
		try {
			_in.close();
			_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_engine.finish();
	}

	private boolean processCommands(String[] tokens) throws IOException {

		tokens[0] = tokens[0].toLowerCase();

		if (CMD_PLAY.equals(tokens[0])) {
			cmdPlay(tokens);
		} else if (CMD_GENMOVE.equals(tokens[0])) {
			cmdGenMove(tokens);
		} else if (CMD_LIST_COMMANDS.equals(tokens[0])) {
			cmdListCommands();
		} else if (CMD_PROCOTOL_VERSION.equals(tokens[0])) {
			cmdProtocolVersion();
		} else if (CMD_NAME.equals(tokens[0])) {
			cmdName();
		} else if (CMD_VERSION.equals(tokens[0])) {
			cmdVersion();
		} else if (CMD_BOARDSIZE.equals(tokens[0])) {
			cmdBoardSize(tokens); 
		} else if (CMD_CLEAR_BOARD.equals(tokens[0])) {
			cmdClearBoard();
		} else if (CMD_KOMI.equals(tokens[0])) {
			cmdKomi(tokens);
		} else if (CMD_SHOWBOARD.equals(tokens[0])) {
			cmdShowBoard();
		} else if (CMD_GET_RANDOM_SEED.equals(tokens[0])) {
			cmdGetRandomSeed();
		} else if (CMD_SET_RANDOM_SEED.equals(tokens[0])) {
			cmdSetRandomSeed(tokens);
		} else if (CMD_CPUTIME.equals(tokens[0])) {
			cmdCpuTime(tokens);
		} else if (CMD_FINAL_SCORE.equals(tokens[0])) {
			cmdFinalScore(tokens);
		} else if (CMD_KNOWN_COMMAND.equals(tokens[0])) {
			cmdKnownCommand(tokens);
		} else if (CMD_QUIT.equals(tokens[0])) {
			cmdQuit();
			return false;
		} else {
			cmdNotUnderstood(tokens);
		}
		return true;
	}
	
	private void reply(String value) throws IOException {
//		System.err.println(">>"+value);
		_bow.write( "= " + value + "\n\n");
		_bow.flush();
	}
	
	private void ack() throws IOException {
		reply("");
	}

	private void replyError(String value) throws IOException {
//		System.err.println("E>>"+ value);
		_bow.write( "? " + value + "\n\n");
		_bow.flush();
	}
	
	
	private void cmdProtocolVersion() throws IOException {
		reply(PROTOCOL_VERSION);
	}
	
	private void cmdNotUnderstood(String[] tokens) throws IOException {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<tokens.length; i++) {
			sb.append(tokens[i]);
			sb.append(" ");
		}
		replyError( "unknown command '" + sb.toString() + "'");
	}
	
	private void cmdName() throws IOException {
		reply(_engine.getName());
	}
	
	private void cmdBoardSize(String[] tokens) throws IOException {
		int size = Integer.parseInt(tokens[1]);
		_engine.setBoardSize(size);
		ack();
	}
	
	private void cmdListCommands() throws IOException {
		// By reflexion get all the values of fields with name starting with CMD_
		StringBuffer sb = new StringBuffer();
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			if (fields[i].getName().startsWith("CMD_")) {
				try {
					sb.append( (String)fields[i].get(this) );
					sb.append('\n');
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		reply(sb.toString());		
	}
	
	private void cmdKnownCommand(String[] tokens) throws IOException {
		if (tokens.length<1 || tokens[1] == null ) {
			reply("false");
			return;
		}
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			if (fields[i].getName().equals(("CMD_"+tokens[1].toUpperCase()))) {
				reply("true");
				return;
			}
		}
		reply("false");
	}
	
	private void cmdClearBoard() throws IOException {
		_engine.clearBoard();
		ack();
	}
	
	private void cmdKomi(String[] tokens) throws IOException {
		float komi = Float.parseFloat(tokens[1]);
		_engine.setKomi(komi);
		ack();
	}
	
	private void cmdGenMove(String[] tokens) throws IOException {
		int player = _engine.getGame().playerToPlay();
		if (tokens.length>1 && tokens[1]!=null) {
			char tmp = tokens[1].toUpperCase().charAt(0);
			if (tmp == 'W') player = Board.WHITE;
			if (tmp == 'B') player = Board.BLACK;
		}
		Move move = _engine.genMove(player);
		if (move==null) {
			reply("resign");
		} else {
			reply(move.toString().substring(1).trim());
		}
	}
	
	private void cmdPlay(String[] tokens) throws IOException {
		Move move = MoveImpl.fromString(tokens[1]+" "+tokens[2]);
		//if ("PASS".equals(tokens[2])) move = MoveImpl.fromString("PASS");
		// already handled by fromString. check before commit.
		
		if (_engine.play(move)) {
			ack();
		} else {
			replyError("I'm sorry, i can't play '" + move.toString() + "' in this moment. Next player "+_engine.getGame().playerToPlay());
		}
	}
	
	private void cmdShowBoard() throws IOException {
		reply(_engine.getBoard().toString());
	}
	
	private void cmdGetRandomSeed() throws IOException {
		reply(""+_engine.getRandomSeed());
	}

	private void cmdSetRandomSeed(String[] tokens) throws IOException {
		long seed = Long.parseLong(tokens[1]);
		_engine.setRandomSeed(seed);
		ack();
	}
	
	private void cmdCpuTime(String[] tokens) throws IOException {
		long ms = _engine.getCpuTimeInMillis();
		reply( ""+(float)ms/(float)1000 );
	}
	
	private void cmdFinalScore(String[] tokens) throws IOException {
		reply(_engine.calcScore());
	}
	
	private void cmdVersion() throws IOException {
		reply(_engine.getVersion());
	}
	
	private void cmdQuit() throws IOException {
		reply("");
	}

}

