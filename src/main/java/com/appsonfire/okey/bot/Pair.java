package com.appsonfire.okey.bot;

public class Pair {
	private Tile tile1;
	private Tile tile2;

	public Pair(Tile tile1, Tile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}

	public Tile getTile1() {
		return tile1;
	}

	public Tile getTile2() {
		return tile2;
	}
	
	@Override
	public String toString() {
		return "<" + tile1 + ", " + tile2 + ">";
	}

}
