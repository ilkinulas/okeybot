package com.appsonfire.okey.bot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.appsonfire.okey.bot.Tile.Color;

public class Hand {
	private List<Tile> freeTiles = new ArrayList<Tile>();
	private List<List<Tile>> series = new LinkedList<List<Tile>>();

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
		return sum;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Hand : ");
		for(List<Tile> serie :  series) {
			sb.append(serie).append(", ");
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
	
}
