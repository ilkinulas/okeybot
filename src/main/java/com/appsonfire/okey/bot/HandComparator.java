package com.appsonfire.okey.bot;

import java.util.Comparator;

public class HandComparator  implements Comparator<Hand> {
	
	private final Tile joker;

	public HandComparator(Tile joker) {
		this.joker = joker;
	}
	
	@Override
	public int compare(Hand h1, Hand h2) {
		int h1FreeTiles = h1.getFreeTiles().size();
		int h2FreeTiles = h2.getFreeTiles().size();
		if (h1FreeTiles < h2FreeTiles) {
			return -1;
		} else if (h1FreeTiles > h2FreeTiles) {
			return 1;
		} else {
			//TODO needs more detailed comparison
			int h1JokerCount = h1.numberOfJokers(this.joker);
			int h2JokerCount = h2.numberOfJokers(this.joker);
			if (h1JokerCount > h2JokerCount) {
				System.out.println("h1 *  : " + h1);
				System.out.println("h2 : " + h2);
				return -1;
			} else if (h1JokerCount < h2JokerCount) {
				System.out.println("h1 : " + h1);
				System.out.println("h2 * : " + h2);
				return 1;
			}
			
		}
		return 0;
	}

}
