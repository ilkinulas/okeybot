package com.appsonfire.okey.bot.simulation;

import com.appsonfire.okey.bot.Hand;

public class GameFinishedException extends Exception {

	private static final long serialVersionUID = -965529888588085486L;

	private int playerIndex;
	private Hand hand;

	public GameFinishedException(int playerIndex, Hand hand) {
		super();
		this.playerIndex = playerIndex;
		this.hand = hand;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public Hand getHand() {
		return hand;
	}

}
