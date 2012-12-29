package com.appsonfire.okey.bot;

public class Tile implements Comparable<Tile> {

	public static enum Color {
		RED, BLACK, GREEN, YELLOW
	}

	private int value;
	private Color color;
	private boolean falseJoker;
	private boolean joker;

	public Tile(int value, Color color) {
		this.value = value;
		this.color = color;
		this.falseJoker = false;
		this.joker = false;
	}

	public Tile(Tile tile) {
		this.color = tile.color;
		this.value = tile.value;
		this.falseJoker = tile.falseJoker;
		this.joker = tile.joker;
	}

	public static Tile falseJoker(int value, Color color) {
		Tile tile = new Tile(value, color);
		tile.falseJoker = true;
		tile.joker = false;
		return tile;
	}

	public static Tile joker(int value, Color color) {
		Tile tile = new Tile(value, color);
		tile.falseJoker = false;
		tile.joker = true;
		return tile;
	}

	public static Tile falseJoker(Tile t) {
		Tile tile = new Tile(t.value, t.color);
		tile.falseJoker = true;
		tile.joker = false;
		return tile;
	}

	public static Tile joker(Tile t) {
		Tile tile = new Tile(t.value, t.color);
		tile.falseJoker = false;
		tile.joker = true;
		return tile;
	}

	public static Tile falseJokerFromFaceUpTile(Tile faceUpTile) {
		Color color = faceUpTile.getColor();
		int value = faceUpTile.getValue() + 1;
		if (value > 13) {
			value = 1;
		}
		return falseJoker(value, color);
	}

	public static Tile jokerFromFaceUpTile(Tile faceUpTile) {
		Color color = faceUpTile.getColor();
		int value = faceUpTile.getValue() + 1;
		if (value > 13) {
			value = 1;
		}
		return joker(value, color);
	}

	public boolean isFalseJoker() {
		return this.falseJoker;
	}

	public boolean isJoker() {
		return this.joker;
	}

	@Override
	public int compareTo(Tile tile) {
		if (this.color.ordinal() < tile.color.ordinal()) {
			return -1;
		} else if (this.color.ordinal() > tile.color.ordinal()) {
			return 1;
		}
		// same color.
		if (this.value < tile.value) {
			return -1;
		} else if (this.value > tile.value) {
			return 1;
		}
		return 0;
	}

	public int getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + value;
		return result;
	}

	/**
	 * (Joker Red 4) is equal to (Red 4)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tile other = (Tile) obj;
		if (color != other.color) {
			return false;
		}
		if (value != other.value) {
			return false;
		}
		if (this.falseJoker != other.falseJoker) {
			return false;
		}
		if (this.joker != other.joker) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String prefix = "";
		if (this.isFalseJoker()) {
			prefix = "J";
		} else if (this.isJoker()) {
			prefix = "*";
		}
		switch (this.color) {
		case BLACK:
			return prefix + "B" + this.value;
		case RED:
			return prefix + "R" + this.value;
		case GREEN:
			return prefix + "G" + this.value;
		case YELLOW:
			return prefix + "Y" + this.value;
		default:
			return "NA";
		}
	}

}
