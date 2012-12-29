package com.appsonfire.okey.bot.simulation;

import java.util.LinkedList;
import java.util.List;

import com.appsonfire.okey.bot.GameState;
import com.appsonfire.okey.bot.Hand;
import com.appsonfire.okey.bot.HandComparator;
import com.appsonfire.okey.bot.OkeyBotInterface;
import com.appsonfire.okey.bot.OkeyUtil;
import com.appsonfire.okey.bot.Tile;
import com.appsonfire.okey.bot.util.Log;

public class Player {
	private int index;
	private OkeyBotInterface logic;

	public Player(int index, OkeyBotInterface logic) {
		this.index = index;
		this.logic = logic;
	}

	public void play(GameState gameState) throws GameFinishedException {
		List<Tile> myTiles = gameState.getPlayerTiles().get(this.index);
		final Tile joker = gameState.getJoker();
		HandComparator handComparator = new HandComparator(joker);
		if (myTiles.size() == 15) {
			// should drop tile...
			Hand hand = this.logic.play(joker, myTiles);
			discardTile(gameState, hand);
		} else if (myTiles.size() == 14) {
			int previousPlayer = OkeyUtil.previousPlayer(this.index);
			Tile topDiscardedTile = gameState.topDiscardedTileOfPlayer(previousPlayer);
			Tile topCenterTile = gameState.topCenterTile();
			if (topCenterTile == null && topDiscardedTile == null) {
				throw new IllegalStateException("Player " + index + " can not draw tile. center tiles and discarded tiles are empty.");
			}

			if (topDiscardedTile == null && topCenterTile != null) {
				gameState.drawFromCenterTiles(this.index);
				Hand hand = this.logic.play(joker, myTiles);
				discardTile(gameState, hand);
			} else if (topCenterTile == null && topDiscardedTile != null) {
				gameState.drawFromDiscardedTiles(previousPlayer);
				Hand hand = this.logic.play(joker, myTiles);
				discardTile(gameState, hand);
			} else {
				List<Tile> tmpTiles = new LinkedList<Tile>(myTiles); 
				tmpTiles.add(topCenterTile);
				Hand handDrawCenter = this.logic.play(joker, tmpTiles);
				
				tmpTiles = new LinkedList<Tile>(myTiles); 
				tmpTiles.add(topDiscardedTile);
				Hand handDrawDiscarded = this.logic.play(joker, tmpTiles);
				
				int compareResult = handComparator.compare(handDrawCenter, handDrawDiscarded);
				if (compareResult > 0 ) {
					gameState.drawFromDiscardedTiles(this.index);
				} else if (compareResult < 0) {
					gameState.drawFromCenterTiles(this.index);
				} else {
					//both hands have equal scores. Randomly select one.
					if (Math.random() > 0.1f) {
						gameState.drawFromDiscardedTiles(this.index);
					} else {
						gameState.drawFromCenterTiles(this.index);	
					}
				}
				Hand hand = this.logic.play(joker, myTiles);
				discardTile(gameState, hand);
			}
		} else {
			throw new IllegalStateException("Player must have 14 or 15 tiles. Player " + this.index + " has " + myTiles.size() + " tiles");
		}
	}

	private void discardTile(GameState gameState, Hand hand) throws GameFinishedException {
		List<Tile> freeTiles = hand.getFreeTiles();
		if (freeTiles.isEmpty()) {
			// TODO decide which set or run should be discarded.
			gameState.discardTile(this.index, (Tile)((List) (hand.getSeries().toArray()[0])).get(0));
		} else {
			if (freeTiles.size() == 1) {
				Log.debug("Winner hand : " + hand);
				throw new GameFinishedException(gameState.getTurn(), hand);
			} else {
				// TODO decide which free tile to discard.
				gameState.discardTile(this.index, freeTiles.get(0));
			}
		}
		Log.debug("Hand player " + gameState.getTurn() + " " + hand);
	}

	@Override
	public String toString() {
		return "Player [index=" + index + "]";
	}

}
