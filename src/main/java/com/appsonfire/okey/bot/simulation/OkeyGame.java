package com.appsonfire.okey.bot.simulation;

import com.appsonfire.okey.bot.GameState;
import com.appsonfire.okey.bot.Hand;
import com.appsonfire.okey.bot.SmartOkeyBot;
import com.appsonfire.okey.bot.util.Log;

public class OkeyGame {
	public static int gameFinished = 0;
	public static int noMoreTiles = 0;
	public static int numMoves = 0;
	
	public static void main(String[] args) {
		Log.level = Log.Level.INFO;
		
		OkeyGame game = new OkeyGame();
		for (int i=0; i<100; i++) {
			game.startSimulation();
		}
		System.out.println(gameFinished + " / " + noMoreTiles);
	}
	
	public void startSimulation() {
		Player[] players = new Player[4];
		for (int i = 0; i < 4; i++) {
			players[i] = new Player(i, new SmartOkeyBot());
		}
		GameState gameState = new GameState(0);
		numMoves = 0;
		while (true) {
			int turn = gameState.getTurn();
			Player player = players[turn];
			try {
				player.play(gameState);
				numMoves++;
				gameState.checkValid();
			} catch (GameFinishedException gfe) {
				if ( ! gfe.getHand().isFinished()) {
					// sanity check
					throw new RuntimeException("Hand is not finished. Check your implementation : " + gfe.getHand());
				}
				handleGameFinished(turn, gfe.getHand());
				gameFinished++;
				break;
			}
			if (gameState.getTiles().isEmpty()) {
				handleNoMoreTilesLeft();
				noMoreTiles++;
				break;
			}
		}
	}

	private void handleNoMoreTilesLeft() {
		Log.info("NO MORE TILES LEFT [ " + numMoves + " ]");

	}

	private void handleGameFinished(int winner, Hand hand) {
		Log.info("GAME FINISHED  [ " + numMoves + " ] Winner is " + winner + " " + hand);
	}

}
