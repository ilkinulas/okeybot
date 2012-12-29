package com.appsonfire.okey.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.appsonfire.okey.bot.Tile.Color;

public class GameStateTest {

	@Test
	public void testInit() {
		GameState gs = new GameState(1);
		// players have 14 + 15 + 14 + 14 = 57 tiles
		// 1 tile is faceUpTile, there should be 106 - 58 = 48 tiles left
		assertEquals(48, gs.getTiles().size());
		
		assertEquals(14, gs.getPlayerTiles().get(0).size());
		assertEquals(15, gs.getPlayerTiles().get(1).size()); // turn is 1
		assertEquals(14, gs.getPlayerTiles().get(2).size());
		assertEquals(14, gs.getPlayerTiles().get(3).size());
		
		assertNotNull(gs.getFaceUpTile());
		
		assertEquals(0, gs.getDiscardedTiles().get(0).size());
		assertEquals(0, gs.getDiscardedTiles().get(1).size());
		assertEquals(0, gs.getDiscardedTiles().get(2).size());
		assertEquals(0, gs.getDiscardedTiles().get(3).size());
	}
	
	@Test
	public void ccc() {
		GameState game = new GameState(0);
		OkeyBotInterface bot = new SmartOkeyBot();
		Hand hand = bot.play(Tile.jokerFromFaceUpTile(game.getFaceUpTile()), game.getPlayerTiles().get(0));
		System.out.println(hand);
	}
	
	@Test
	public void testShuffleTiles() {
		Tile faceUpTile = new Tile(5, Color.GREEN);
		List<Tile> tiles = GameState.shuffleTiles(faceUpTile);
		Collections.sort(tiles);
		assertEquals(105, tiles.size());
		assertEquals(1, Collections.frequency(tiles, faceUpTile));
	}
}
