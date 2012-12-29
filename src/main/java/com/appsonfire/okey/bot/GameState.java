package com.appsonfire.okey.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.appsonfire.okey.bot.Tile.Color;
import com.appsonfire.okey.bot.util.Log;

public class GameState {

	private List<List<Tile>> discardedTiles;
	private List<List<Tile>> playerTiles;
	private List<Tile> tiles;
	private Tile faceUpTile;
	private int turn;

	public static final Random random = new Random(System.currentTimeMillis());

	public GameState(int turn) {
		this.turn = turn;
		this.faceUpTile = randomFaceUpTile();
		List<Tile> allTiles = shuffleTiles(this.faceUpTile);
		playerTiles = new ArrayList<List<Tile>>(4);
		discardedTiles = new ArrayList<List<Tile>>(4);

		for (int player = 0; player < 4; player++) {
			playerTiles.add(new LinkedList<Tile>());
			discardedTiles.add(new LinkedList<Tile>());
		}

		for (int i = 0; i < 14; i++) {
			for (int player = 0; player < 4; player++) {
				playerTiles.get(player).add(allTiles.remove(0));
			}
		}
		playerTiles.get(turn).add(allTiles.remove(0));
		this.tiles = allTiles;
	}

	public static final Tile randomFaceUpTile() {
		int color = random.nextInt(4);
		int value = random.nextInt(13) + 1; // random 1,2,3, ... ,13
		return new Tile(value, Color.values()[color]);
	}

	public static final List<Tile> shuffleTiles(Tile faceUpTile) {
		List<Tile> tiles = new ArrayList<Tile>(106);
		for (Color color : Color.values()) {
			for (int value = 1; value <= 13; value++) {
				tiles.add(new Tile(value, color));
				tiles.add(new Tile(value, color));
			}
		}
		tiles.remove(faceUpTile);
		tiles.add(Tile.falseJokerFromFaceUpTile(faceUpTile));
		tiles.add(Tile.falseJokerFromFaceUpTile(faceUpTile));
		Collections.shuffle(tiles);
		return tiles;
	}
	
	/**
	 * Look at the top of the center tiles.
	 * @return tile or null if no more tiles left in the center
	 */
	public Tile topCenterTile() {
		if (this.tiles.isEmpty()) {
			return null;
		}
		return this.tiles.get(0);
	}
	
	/**
	 * Look at the top of the discarded tiles of player.
	 * @param playerIndex 
	 * @return tile or null if there is not any discarded tiles of player
	 */
	public Tile topDiscardedTileOfPlayer(int playerIndex) {
		List<Tile> discared = this.discardedTiles.get(playerIndex);
		if (discared.isEmpty()) {
			return null;
		}
		return discared.get(0);
	}
	
	public Tile drawFromCenterTiles(int turn) {
		if (this.tiles.isEmpty()) {
			throw new IllegalStateException("Player " + turn + " can not draw tile. No more tiles left");
		}
		Tile tile = this.tiles.remove(0);
		this.playerTiles.get(turn).add(tile);
		Log.debug("Player " + turn + " draw from center tile " + tile);
		return tile;
	}
	
	public Tile drawFromDiscardedTiles(int turn) {
		int previousPlayer = OkeyUtil.previousPlayer(turn);
		List<Tile> discarded = this.discardedTiles.get(previousPlayer);
		if (discarded.isEmpty()) {
			throw new IllegalStateException("No discarded tiles found for player " + previousPlayer);
		}
		Tile tile = discarded.remove(0);
		this.playerTiles.get(turn).add(tile);
		Log.debug("Player " + turn + " draw discared tile " + tile);
		return tile;
		
	}
	
	public void discardTile(int turn, Tile tile) {
		List<Tile> tilesOfPlayer = this.playerTiles.get(turn);
		boolean removed = tilesOfPlayer.remove(tile);
		if (removed) {
			this.discardedTiles.get(turn).add(tile);
		} else {
			throw new IllegalStateException("Player " + turn + " does not have tile " + tile + ", can not discard it." );
		}
		Log.debug("Player " + turn + " discarded tile " + tile);
		this.turn = OkeyUtil.nextTurn(turn);
	}

	public Tile getJoker() {
		return Tile.jokerFromFaceUpTile(this.faceUpTile);
	}
	
	public List<List<Tile>> getDiscardedTiles() {
		return discardedTiles;
	}

	public List<List<Tile>> getPlayerTiles() {
		return playerTiles;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public Tile getFaceUpTile() {
		return faceUpTile;
	}

	public int getTurn() {
		return turn;
	}
	
	public void checkValid() {
		int tileCount = this.tiles.size();
		for (List<Tile> l : playerTiles) {
			tileCount = tileCount + l.size();
			if (l.size()>15 || l.size()<14) {
				throw new IllegalStateException("Players should have 14 or 15 tiles. " + playerTiles);
			}
		}
		for (List<Tile> l : discardedTiles) {
			tileCount = tileCount + l.size();
		}
		tileCount++; // face up tile
		int expectedTileCount = (13 * 2 * 4) + 2;
		if (tileCount != expectedTileCount) {
			throw new IllegalStateException("There are " + tileCount + " tiles. There should be " + expectedTileCount);
		}
		
		
		
	}
}
