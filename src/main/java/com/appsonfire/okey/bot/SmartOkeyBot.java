package com.appsonfire.okey.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.appsonfire.okey.bot.Tile.Color;

public class SmartOkeyBot implements OkeyBotInterface {
	/**
	 * If there are more than or equal to pairThreshold pairs
	 * bot will try to collect pairs.
	 */
	private int pairThreshold = 4;
	
	public Hand play(Tile joker, List<Tile> tiles) {
		List<Pair> pairs = findPairs(joker, tiles);
		if (pairs.size() >= this.pairThreshold) {
			return playForPairs(tiles, pairs);
		}
		List<Hand> hands = findAllPossibleHands(joker, tiles);
		if (hands.size() == 0) {
			System.err.println(joker);
			System.err.println(tiles);
		}
		List<Hand> handsAsList = new LinkedList<Hand>(hands);
		Collections.sort(handsAsList, new HandComparator(joker));
		return handsAsList.get(0);
	}

	private Hand playForPairs(List<Tile> tiles, List<Pair> pairs) {
		Hand hand = new Hand();
		hand.getPairs().addAll(pairs);
		for (Tile tile : tiles) {
			if (! hand.contains(tile)) {
				hand.getFreeTiles().add(tile);
			}
		}
		return hand;
	}
	
	final List<Hand> findAllPossibleHands(Tile joker, List<Tile> tiles) {
		PlayContext context = new PlayContext();
		doPlay(context, joker, tiles, tiles.size());
		return context.hands;
	}
	
	
	/**
	 * Finds out all possible hands recursively.
	 * @param context
	 * @param joker
	 * @param tiles
	 * @param numberOfTilesInAHand
	 */
	private void doPlay(PlayContext context, Tile joker, List<Tile> tiles, int numberOfTilesInAHand) {
		List<List<Tile>> series = findAllSeries(joker, tiles);
		if (series.isEmpty()) {
			context.currentHand.setFreeTiles(tiles);
			if (context.currentHand.numberOfTiles() == numberOfTilesInAHand) {
				context.packCurrentHand();
			}
		} else {
			for (List<Tile> serie : series) {
				context.currentHand.addSerie(serie);
				List<Tile> rest = rest(tiles, serie);
				doPlay(context, joker, rest, numberOfTilesInAHand);
			}
		}
	}
	
	private static final class PlayContext {
		private List<Hand> hands = new LinkedList<Hand>();
		private Hand currentHand = new Hand();
		void packCurrentHand() {
			if ( ! hands.contains(currentHand)) {
				hands.add(currentHand);
				currentHand = new Hand();
			}
		}
	}
	
	private List<List<Tile>> findAllSeries(Tile joker, List<Tile> tiles) {
		if(tiles.isEmpty()) {
			Collections.emptyList();
		}
		List<Set<Tile>> sets = findAllSets(joker, tiles);
		List<List<Tile>> runs = findAllRuns(joker, tiles);
		List<List<Tile>> allSeries = new ArrayList<List<Tile>>(runs);
		for (Set<Tile> set : sets) {
			List<Tile> list = toList(set);
			if ( ! list.isEmpty()) {
				allSeries.add(list);
			}
		}
		return allSeries;
	}
	
	final List<Pair> findPairs(Tile joker, List<Tile> tiles) {
		Set<Tile> tileSet = new HashSet<Tile>();
		List<Pair> pairs = new LinkedList<Pair>();
		for (Tile tile : tiles) {
			if (tileSet.contains(tile)) {
				Pair pair = new Pair(tile, tile);
				pairs.add(pair);
			} else {
				tileSet.add(tile);
			}
		}
		
		return pairs;
	}
	
	/**
	 * Finds unique sets.
	 * 
	 * @param joker
	 * @param tiles
	 * @return List of sets
	 */
	final List<Set<Tile>> findAllSets(final Tile joker, final List<Tile> tiles) {
		List<Set<Tile>> sets = findAllSets(3, joker, tiles);
		int numberOfJokers = Collections.frequency(tiles, joker);
		List<Set<Tile>> setsWithJokers = new ArrayList<Set<Tile>>();
		if (numberOfJokers == 1) {
			sets = findAllSets(2, joker, tiles);
			for (Set<Tile> set : sets) {
				if (set.size() == 2) {
					set.add(joker);
				} else if (set.size() == 3) {
					Set<Tile> newSet = new HashSet<Tile>(set);
					newSet.add(joker);
					setsWithJokers.add(newSet);
				}
			}
		} else if (numberOfJokers == 2) {
			sets = findAllSets(2, joker, tiles);
			for (Set<Tile> set : sets) {
				if (set.size() == 2) {
					set.add(joker);
					set.add(joker);
				} else if (set.size() == 3) {
					Set<Tile> newSet = new HashSet<Tile>(set);
					newSet.add(joker);
					setsWithJokers.add(newSet);
				}
			}
		}
		sets.addAll(setsWithJokers);
		return sets;
	}

	/**
	 * @param minLength
	 *            result sets contains at least minLength elements
	 * @param joker
	 *            Joker tile is excluded from sets
	 * @param tiles
	 * @return List of sets
	 */
	private final List<Set<Tile>> findAllSets(int minLength, final Tile joker, final List<Tile> tiles) {
		List<Set<Tile>> result = new ArrayList<Set<Tile>>();
		for (int i = 1; i <= 13; i++) {
			Set<Tile> aSet = filterByValue(i, tiles);
			aSet.remove(joker);
			if (aSet.size() >= minLength) {
				result.add(aSet);
				if (aSet.size() == 4) {
					for (Tile tile : aSet) {
						Set<Tile> rest = new HashSet<Tile>(aSet);
						rest.remove(tile);
						result.add(rest);
					}
				}
			}
		}
		return result;
	}

	final List<List<Tile>> findAllRuns(final Tile joker, final List<Tile> tiles) {
		List<List<Tile>> runs = new ArrayList<List<Tile>>();
		int numberOfJokers = Collections.frequency(tiles, joker);
		for (Color color : Color.values()) {
			List<Tile> sameColorTiles = filterByColor(color, tiles);
			Collections.sort(sameColorTiles);
			List<Tile> serie = new ArrayList<Tile>();
			for (int i = 0; i < sameColorTiles.size(); i++) {
				Tile currentTile = sameColorTiles.get(i);
				if (serie.isEmpty()) {
					serie.add(currentTile);
					continue;
				}
				Tile lastTileInSerie = serie.get(serie.size() - 1);
				if (lastTileInSerie.equals(currentTile) 
					&& i < sameColorTiles.size() - 1 ) {
					Tile removed = sameColorTiles.remove(i);
					sameColorTiles.add(removed);
					i = i - 1;
					continue;
				}

				if (lastTileInSerie.getValue() + 1 == currentTile.getValue() || lastTileInSerie.getValue() == 13
						&& currentTile.getValue() == 1) {
					if (serie.size() >= 2) {
						Tile tileBeforeTheLast = serie.get(serie.size() - 2);
						if (tileBeforeTheLast.getValue() == 13) {
							continue;
						}
					}
					serie.add(currentTile);
					continue;
				}
				if ((lastTileInSerie.getValue() + 2 == currentTile.getValue() || lastTileInSerie.getValue() == 12
						&& currentTile.getValue() == 1)
						&& numberOfJokers > 0) {
					serie.add(joker);
					serie.add(currentTile);
					numberOfJokers--;
					continue;
				}

				if (serie.size() >= 3) {
					runs.add(copyTileList(serie));
				}
				serie.clear();
				serie.add(currentTile);
			}
			if (serie.size() >= 3) {
				runs.add(copyTileList(serie));
			}
			serie.clear();
		}
		splitRuns(runs, tiles);
		List<List<Tile>> result = new ArrayList<List<Tile>>();
		for (List<Tile> run : runs) {
			List<List<Tile>> exploded = explodeRun(run);
			result.addAll(exploded);
		}
		return result;
	}

	/*
	 * B1,B2,B3,B4 => (B1,B2,B3), (B1,B2,B3,B4), (B2,B3,B4)
	 * 
	 */
	final List<List<Tile>> explodeRun(List<Tile> run) {
		List<List<Tile>> runs = new ArrayList<List<Tile>>();
		if (run.size() < 4) {
			runs.add(run);
			return runs;
		}
		int size = run.size();
		for (int i = 0; i < size - 2; i++) {
			for (int j = 2; j < size; j++) {
				if (j > i && (j - i) >= 2) {
					List<Tile> subList = subList(run, i, j);
					runs.add(subList);
				}
			}
		}
		return runs;
	}

	/**
	 * 1,2,3,3,4,5,6,7 --> 1,2,3 + 3,4,5,6,7
	 * 
	 * @param orderedSeries
	 * @param originalList
	 */
	private void splitRuns(List<List<Tile>> orderedSeries, List<Tile> originalList) {
		List<List<Tile>> splitedLists = new LinkedList<List<Tile>>();
		for (List<Tile> serie : orderedSeries) {
			if (serie.size() < 5) {
				continue;
			}
			int size = serie.size();
			for (int i = 2; i <= size - 3; i++) {
				Tile p = serie.get(i);
				int count = Collections.frequency(originalList, p);
				if (count == 2) {
					// split serie 0...i , i+1,...
					List<Tile> list1 = subList(serie, 0, i);
					List<Tile> list2 = subList(serie, i + 1, serie.size() - 1);
					list2.add(0, p);
					splitedLists.add(list1);
					splitedLists.add(list2);
				}
			}
		}
		orderedSeries.addAll(splitedLists);
	}

	/**
	 * 
	 * @param list
	 * @param start
	 * @param end
	 * @return returns a new list containing elements between start and
	 *         end(included)
	 */
	public static final List<Tile> subList(List<Tile> list, int start, int end) {
		List<Tile> result = new LinkedList<Tile>();
		for (int i = start; i <= end; i++) {
			result.add(list.get(i));
		}
		return result;
	}

	public static final Set<Tile> filterByValue(int value, final List<Tile> tiles) {
		Set<Tile> result = new HashSet<Tile>();
		for (Tile tile : tiles) {
			if (tile.getValue() == value) {
				result.add(tile);
			}
		}
		return result;
	}

	public static final List<Tile> filterByColor(Color color, final List<Tile> tiles) {
		List<Tile> result = new ArrayList<Tile>();
		for (Tile tile : tiles) {
			if (tile.getColor() == color) {
				result.add(tile);
			}
		}
		return result;
	}

	private static final List<Tile> copyTileList(List<Tile> l) {
		List<Tile> result = new ArrayList<Tile>();
		for (Tile tile : l) {
			result.add(tile);
		}
		return result;
	}

	public static final List<Tile> toList(Set<Tile> set) {
		if (set == null || set.size() == 0) {
			return Collections.emptyList();
		}
		List<Tile> list = new ArrayList<Tile>();
		for (Tile tile : set) {
			list.add(tile);
		}
		Collections.sort(list); //preserve consistent order
		return list;
	}
	
    public static final List<Tile> rest(List<Tile> tiles, List<Tile> serie) {
        List<Tile> rest = new LinkedList<Tile>();
        for (Tile tile : tiles) {
            rest.add(tile);
        }
        for (Tile tile: serie) {
            rest.remove(tile);
        }
        return rest;
    }

	public int getPairThreshold() {
		return pairThreshold;
	}

	public void setPairThreshold(int pairThreshold) {
		this.pairThreshold = pairThreshold;
	} 	
}
