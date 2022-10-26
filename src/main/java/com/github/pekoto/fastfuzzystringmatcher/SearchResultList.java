package com.github.pekoto.fastfuzzystringmatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple class for returning results from the string matcher.
 * 
 * @author Graham McRobbie
 *
 * @param <T> The data associated with each result.
 */
public class SearchResultList<T> extends ArrayList<SearchResult<T>> {

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = 5315812187969680611L;

	public boolean containsKeyword(String keyword) {
		return this.stream().filter(r -> r.getKeyword().equals(keyword)).count() > 0;
	}

	public void sortByClosestMatch() {
        Collections.sort(this, new SortByClosestMatchComparator());
	}
	
	public void print() {
		for(SearchResult<T> result : this) {
			System.out.println(result);
		}
	}
	
	private class SortByClosestMatchComparator implements Comparator<SearchResult<T>> {
		
		public int compare(SearchResult<T> s1, SearchResult<T> s2) {
			if (s1.getMatchPercentage() < s2.getMatchPercentage()) {
				return 1;
			} else if (s1.getMatchPercentage() > s2.getMatchPercentage()) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
