package com.appsonfire.okey.bot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.appsonfire.okey.bot.Tile.Color;

public class OkeyHelperTest {

	private final SmartOkeyBot smartBot = new SmartOkeyBot();

	@Test
	public void testSortTiles() {
		List<Tile> tiles = toShuffledTiles("Y3, B3, R3, G3");
		Collections.sort(tiles);
		// color order R B G Y
		assertEquals(Color.RED, tiles.get(0).getColor());
		assertEquals(Color.BLACK, tiles.get(1).getColor());
		assertEquals(Color.GREEN, tiles.get(2).getColor());
		assertEquals(Color.YELLOW, tiles.get(3).getColor());

		tiles = toShuffledTiles("Y3, Y5, Y1, Y4, Y2");
		Collections.sort(tiles);
		assertEquals(Color.YELLOW, tiles.get(0).getColor());
		assertEquals(1, tiles.get(0).getValue());
		assertEquals(2, tiles.get(1).getValue());
		assertEquals(3, tiles.get(2).getValue());
		assertEquals(4, tiles.get(3).getValue());
		assertEquals(5, tiles.get(4).getValue());
	}
	
	@Test
	public void testSortTilesWithJoker() {
		List<Tile> tiles = toShuffledTiles("Y3, Y5, JY1, Y4, Y2");
		Collections.sort(tiles);
		assertEquals(Color.YELLOW, tiles.get(0).getColor());
		assertEquals(1, tiles.get(0).getValue());
		assertEquals(2, tiles.get(1).getValue());
		assertEquals(3, tiles.get(2).getValue());
		assertEquals(4, tiles.get(3).getValue());
		assertEquals(5, tiles.get(4).getValue());
		assertTrue(tiles.get(0).isFalseJoker());
	}

	@Test
	public void testFilterByValue() {
		List<Tile> tiles = toShuffledTiles("B4, B5, B6, Y4, Y5, Y6, G3, G4, G5, G6, R6, B4, R6");
		Set<Tile> setOf_4 = SmartOkeyBot.filterByValue(4, tiles);
		assertEquals(3, setOf_4.size());
		
		Set<Tile> setOf_3 = SmartOkeyBot.filterByValue(3, tiles);
		assertEquals(1, setOf_3.size());
		
		Set<Tile> setOf_6 = SmartOkeyBot.filterByValue(6, tiles);
		assertEquals(4, setOf_6.size());
	}
	
	@Test
	public void testToTiles() {
		String s = "B1,B2,B3,B4,B5,B6,B7,B8,B9,B10,B11,B12,B13";

		s = "Y11, G4   ,  R9, b3, y6";
		List<Tile> tiles = toShuffledTiles(s);
		assertEquals(5, tiles.size());
		assertTrue(tiles.remove(new Tile(11, Color.YELLOW)));
		assertTrue(tiles.remove(new Tile(4, Color.GREEN)));
		assertTrue(tiles.remove(new Tile(9, Color.RED)));
		assertTrue(tiles.remove(new Tile(3, Color.BLACK)));
		assertTrue(tiles.remove(new Tile(6, Color.YELLOW)));
		assertTrue(tiles.isEmpty());
	}

	@Test
	public void testFindAllSets() {
		List<Tile> tiles = toShuffledTiles("R3,Y3,G3,B3");
		List<Set<Tile>> allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		assertEquals(5, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3, B3"))); 
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3")));
		assertTrue(allSets.remove(toTileSet("R3, Y3, B3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, B3")));
		assertTrue(allSets.remove(toTileSet("Y3, G3, B3")));
		assertTrue(allSets.isEmpty());
		
		tiles = toShuffledTiles("R3,Y3,G3,B3,R3");
		allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		assertEquals(5, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3, B3"))); 
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3")));
		assertTrue(allSets.remove(toTileSet("R3, Y3, B3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, B3")));
		assertTrue(allSets.remove(toTileSet("Y3, G3, B3")));
		assertTrue(allSets.isEmpty());

	}
	
	@Test
	public void testSameSets() {
		List<Tile> tiles = toShuffledTiles("R3,Y3,G3,R3,Y3,G3");
		List<Set<Tile>> allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		//only unique sets are selected
		assertEquals(1, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3"))); 
		assertTrue(allSets.isEmpty());
	}
	
	@Test
	public void testSetsWithOneJoker() {
		List<Tile> tiles = toShuffledTiles("R3,Y3,G3,*B8");
		List<Set<Tile>> allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		//[[R3, G3, Y3], [R3, Y3, G3, B8]]
		assertEquals(2, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3, *B8")));
		assertTrue(allSets.isEmpty());
		
		//Testing with false a joker
		allSets = smartBot.findAllSets(Tile.joker(3, Color.BLACK), toShuffledTiles("R3,Y3,G3,JB3"));
		assertEquals(5, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3, JB3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, JB3")));
		assertTrue(allSets.remove(toTileSet("R3, Y3, JB3")));
		assertTrue(allSets.remove(toTileSet("G3, Y3, JB3")));
		assertTrue(allSets.isEmpty());
	}
	
	@Test
	public void testSetsWithDoubleJokers() {
		List<Tile> tiles = toShuffledTiles("R3,Y3,G3,*B8, *B8");
		List<Set<Tile>> allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		
		tiles = toTiles("B3,R3,G3,Y3,*B8, *B8");
		allSets = smartBot.findAllSets(Tile.joker(8, Color.BLACK), tiles);
		assertEquals(9, allSets.size());
		assertTrue(allSets.remove(toTileSet("R3, Y3, G3, B3")));
		assertTrue(allSets.remove(toTileSet("B3, G3, Y3")));
		assertTrue(allSets.remove(toTileSet("R3, B3, G3")));
		assertTrue(allSets.remove(toTileSet("R3, B3, Y3")));
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3")));
		assertTrue(allSets.remove(toTileSet("B3, G3, Y3, *B8")));
		assertTrue(allSets.remove(toTileSet("R3, B3, G3, *B8")));
		assertTrue(allSets.remove(toTileSet("R3, B3, Y3, *B8")));
		assertTrue(allSets.remove(toTileSet("R3, G3, Y3, *B8")));
		assertTrue(allSets.isEmpty());
	}
	
	@Test
	public void testFindRunsSimple() {
		List<List<Tile>> runs = smartBot.findAllRuns(Tile.joker(9, Color.BLACK), toShuffledTiles("R3,R4,R5"));
		assertTrue(runs.remove(toTiles("R3,R4,R5")));
		assertTrue(runs.isEmpty());

		runs = smartBot.findAllRuns(Tile.joker(9, Color.BLACK), toShuffledTiles("R1,R3,R4,R5, R7"));
		assertTrue(runs.remove(toTiles("R3,R4,R5")));
		assertTrue(runs.isEmpty());

		runs = smartBot.findAllRuns(Tile.joker(9, Color.BLACK), toShuffledTiles("R1,B2,R3,R4,R5,G6,R7"));
		assertTrue(runs.remove(toTiles("R3,R4,R5")));
		assertTrue(runs.isEmpty());
		
		runs = smartBot.findAllRuns(Tile.joker(9, Color.BLACK), toShuffledTiles("R1,B2,R3,R4,R5,G6,R7"));
		assertTrue(runs.remove(toTiles("R3,R4,R5")));
		assertTrue(runs.isEmpty());
	}
	
	@Test
	public void testFindRunsWithFalseJoker() {
		List<List<Tile>> runs = smartBot.findAllRuns(Tile.joker(6, Color.RED), toShuffledTiles("R3,R4,R5,JR6,R7,R8"));
		assertEquals(10, runs.size());
		assertTrue(runs.remove(toTiles("R3,R4,R5,JR6,R7,R8")));

		runs = smartBot.findAllRuns(Tile.joker(2, Color.RED), toShuffledTiles("JR2,R3,R4,R5,R6"));
		assertEquals(6, runs.size());
		assertTrue(runs.remove(toTiles("JR2,R3,R4,R5,R6")));
		
		//Double false jokers
		runs = smartBot.findAllRuns(Tile.joker(6, Color.RED), toShuffledTiles("R3,R4,R5,JR6,JR6,R7"));
		assertEquals(6, runs.size());
		assertTrue(runs.remove(toTiles("R3,R4,R5,JR6,R7")));
	}
	
	@Test
	public void testFindRunsWithJoker() {
		List<List<Tile>> runs = smartBot.findAllRuns(Tile.joker(6, Color.RED), toShuffledTiles("R3,R4,R5,*R6,R7,R8"));
		assertEquals(10, runs.size());
		assertTrue(runs.remove(toTiles("R3,R4,R5,*R6,R7,R8")));
		
		runs = smartBot.findAllRuns(Tile.joker(2, Color.RED), toShuffledTiles("*R2,R3,R4,R5,R6"));
		assertEquals(6, runs.size());
		assertTrue(runs.remove(toTiles("*R2,R3,R4,R5,R6")));

		// Double jokers
		runs = smartBot.findAllRuns(Tile.joker(6, Color.RED), toShuffledTiles("R3,R4,R5,*R6,*R6,R7"));
		assertEquals(6, runs.size());
		assertTrue(runs.remove(toTiles("R3,R4,R5,*R6,R7")));
	}
	
	@Test
	public void testFindRuns_sameRunOccursTwice() {
		List<List<Tile>> runs = smartBot.findAllRuns(Tile.joker(6, Color.RED), toShuffledTiles("B2, B3, B4, B2,B3, B4"));
		assertEquals(2, runs.size());
	}
	
	@Test
	public void testExplodeRuns() {
		List<Tile> run = toTiles("B3,B4,B5,B6,B7,B8,B9");
		List<List<Tile>> exploded = smartBot.explodeRun(run);
		assertEquals(15, exploded.size());
		assertTrue(exploded.remove(toTiles("B3,B4,B5")));
		assertTrue(exploded.remove(toTiles("B3,B4,B5,B6")));
		assertTrue(exploded.remove(toTiles("B3,B4,B5,B6,B7")));
		assertTrue(exploded.remove(toTiles("B3,B4,B5,B6,B7,B8")));
		assertTrue(exploded.remove(toTiles("B3,B4,B5,B6,B7,B8,B9")));
		assertTrue(exploded.remove(toTiles("B4,B5,B6")));
		assertTrue(exploded.remove(toTiles("B4,B5,B6,B7")));
		assertTrue(exploded.remove(toTiles("B4,B5,B6,B7,B8")));
		assertTrue(exploded.remove(toTiles("B4,B5,B6,B7,B8,B9")));
		assertTrue(exploded.remove(toTiles("B5,B6,B7")));
		assertTrue(exploded.remove(toTiles("B5,B6,B7,B8")));
		assertTrue(exploded.remove(toTiles("B5,B6,B7,B8,B9")));
		assertTrue(exploded.remove(toTiles("B6,B7,B8")));
		assertTrue(exploded.remove(toTiles("B6,B7,B8,B9")));
		assertTrue(exploded.remove(toTiles("B7,B8,B9")));
		assertTrue(exploded.isEmpty());
	}
	
	@Test
	public void testPlay() {
		List<Hand> hands = smartBot.findAllPossibleHands(new Tile(10, Color.BLACK), toShuffledTiles("B3,B4,B5,B6,G1,Y1,R1"));
		System.err.println(hands);
		assertEquals(3, hands.size());
		Hand hand = new Hand();
		hand.addSerie(toTiles("R1, G1, Y1"));
		hand.addSerie(toTiles("B3, B4,B5,B6"));
		assertTrue(hands.remove(hand));
		
		Hand bestHand = smartBot.play(new Tile(10, Color.BLACK), toShuffledTiles("B3,B4,B5,B6,G1,Y1,R1"));
		assertEquals(hand, bestHand);
		
		hand = new Hand();
		hand.addSerie(toTiles("R1, G1, Y1"));
		hand.addSerie(toTiles("B3, B4,B5"));
		hand.getFreeTiles().add(new Tile(6, Color.BLACK));
		assertTrue(hands.remove(hand));

		hand = new Hand();
		hand.addSerie(toTiles("R1, G1, Y1"));
		hand.addSerie(toTiles("B4,B5, B6"));
		hand.getFreeTiles().add(new Tile(3, Color.BLACK));
		assertTrue(hands.remove(hand));
	}
	
	@Test
	public void testPlay_1() {
		List<Hand> hands = smartBot.findAllPossibleHands(new Tile(7, Color.RED), toShuffledTiles("B5, B6, B7, B5, B6, B7"));
		System.out.println(hands);
	}
	
	@Test
	public void testHand_equalsMethod() {
		Hand hand1 = new Hand();
		hand1.addSerie(toTiles("B3,B4,B5"));
		hand1.addSerie(toTiles("R1,G1,Y1"));
		hand1.setFreeTiles(toTiles("B6"));
		Hand hand2 = new Hand();
		hand2.addSerie(toTiles("R1,G1,Y1"));
		hand2.addSerie(toTiles("B3,B4,B5"));
		hand2.setFreeTiles(toTiles("B6"));
		System.out.println("Hand 1 " + hand1);
		System.out.println("Hand 2 " + hand2);
		assertTrue(hand1.equals(hand2));
	}
	
	/**
	 * False Joker starts with 'J', joker starts with '*'
	 * @param s
	 * @return
	 */
	public static final List<Tile> toTiles(String s) {
		String[] parts = s.split(",");
		List<Tile> tiles = new ArrayList<Tile>();
		for (String part : parts) {
			part = part.trim();
			String firstLetter = part.substring(0, 1);
			if ("J".equalsIgnoreCase(firstLetter)) {
				firstLetter = part.substring(1, 2);
				Integer value = Integer.valueOf(part.substring(2, part.length()));
				Tile falseJoker = Tile.falseJoker(toTile(firstLetter, value));
				tiles.add(falseJoker);
			} else if ("*".equalsIgnoreCase(firstLetter)) {
				firstLetter = part.substring(1, 2);
				Integer value = Integer.valueOf(part.substring(2, part.length()));
				Tile joker = Tile.joker(toTile(firstLetter, value));
				tiles.add(joker);
			} else {
				Integer value = Integer.valueOf(part.substring(1, part.length()));
				Tile tile = toTile(firstLetter, value);
				tiles.add(tile);
			}
		}
		return tiles;
	}
	
	public static final List<Tile> toShuffledTiles(String s) {
		List<Tile> tiles = toTiles(s);
		Collections.shuffle(tiles);
		return tiles;
	}

	public static final Set<Tile> toTileSet(String s) {
		List<Tile> list = toTiles(s);
		Set<Tile> set = new HashSet<Tile>();
		set.addAll(list);
		return set;
	}
	
	private static Tile toTile(String letter, Integer value) {
		if ("B".equalsIgnoreCase(letter)) {
			return new Tile(value, Color.BLACK);
		} else if ("R".equalsIgnoreCase(letter)) {
			return new Tile(value, Color.RED);
		} else if ("Y".equalsIgnoreCase(letter)) {
			return new Tile(value, Color.YELLOW);
		} else if ("G".equalsIgnoreCase(letter)) {
			return new Tile(value, Color.GREEN);
		} else {
			throw new RuntimeException("Unexpected character " + letter);
		}
	}
}
