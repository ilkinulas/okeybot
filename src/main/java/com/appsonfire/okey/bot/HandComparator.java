package com.appsonfire.okey.bot;

import java.util.Comparator;

public class HandComparator  implements Comparator<Hand> {

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
		}
		return 0;
	}

}
