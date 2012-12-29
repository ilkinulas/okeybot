package com.appsonfire.okey.bot;


public class OkeyUtil {

	public static final int nextTurn(int turn) {
		return (turn + 1) % 4;
	}

	public static final int previousPlayer(int player) {
		if (player == 0)
			return 3;
		return player - 1;
	}
}
