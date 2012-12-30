package com.appsonfire.okey.bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Hand {
	private List<Tile> freeTiles = new ArrayList<Tile>();
	private List<List<Tile>> series = new LinkedList<List<Tile>>();
	private List<Pair> pairs = new LinkedList<Pair>();
	
	public List<Pair> getPairs() {
		return pairs;
	}

	public List<Tile> getFreeTiles() {
		return freeTiles;
	}

	public void setFreeTiles(List<Tile> freeTiles) {
		this.freeTiles = freeTiles;
	}

	public List<List<Tile>> getSeries() {
		return series;
	}

	public void addSerie(List<Tile> serie) {
		this.series.add(serie);
	}
	
	public int numberOfTiles() {
		int sum = freeTiles.size();
		for (List<Tile> serie : series) {
			sum += serie.size();
		}
		for (@SuppressWarnings("unused") Pair pair : pairs) {
			sum += 2;
		}
		return sum;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hand : ");
		for(List<Tile> serie :  series) {
			sb.append(serie).append(", ");
		}
		if (pairs.size() > 1) {
			sb.append(pairs.toString());
		}
		sb.append(" -> ").append(freeTiles);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((freeTiles == null) ? 0 : freeTiles.hashCode());
		result = prime * result + ((series == null) ? 0 : series.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hand other = (Hand) obj;
		if (freeTiles == null) {
			if (other.freeTiles != null)
				return false;
		} else if (!freeTiles.equals(other.freeTiles))
			return false;
		if (series == null) {
			if (other.series != null)
				return false;
		} else {
			for (List<Tile> serie : this.series) {
				if ( ! other.series.contains(serie)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isFinished() {
		if (numberOfTiles() != 15 ) {
			return false;
		}
		if (freeTiles.size() != 1) {
			return false;
		}
		return true;
	}
	
	public int numberOfJokers(Tile joker) {
		int sum = Collections.frequency(this.freeTiles, joker);
		for (List<Tile> tiles : this.series) {
			sum = sum + Collections.frequency(tiles, joker);
		}
		return sum;
	}
	
	public boolean contains(Tile tile) {
		for (Tile freeTile : freeTiles) {
			if (tile.equals(freeTile)) {
				return true;
			}
		}
		for (List<Tile> serie : series) {
			for (Tile aTile : serie) {
				if (tile.equals(aTile)) {
					return true;
				}
			}
		}
		for (Pair pair : pairs) {
			if (tile.equals(pair.getTile1()) || tile.equals(pair.getTile2())) {
				return true;
			}
		}
		return false;
	}
}
