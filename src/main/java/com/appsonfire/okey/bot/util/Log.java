package com.appsonfire.okey.bot.util;

public class Log {

	public enum Level {
		DEBUG, INFO, WARN, ERROR, FATAL;
	};

	public static Level level = Level.DEBUG;

	public static final void debug(String s) {
		if (Level.DEBUG.compareTo(level) >= 0) {
			System.out.println("DEBUG : " + s);
		}
	}
	
	public static final void info(String s) {
		if (Level.INFO.compareTo(level) >= 0) {
			System.out.println("INFO  : " + s);
		}
	}
	
	public static final void warn(String s) {
		if (Level.WARN.compareTo(level) >= 0) {
			System.out.println("WARN  : " + s);
		}
	}
	
	public static final void error(String s) {
		if (Level.ERROR.compareTo(level) >= 0) {
			System.out.println("ERROR  : " + s);
		}
	}
}
