package com.enlighten.conferenceschedular.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
	/**
	 * Calculates and returns all combinations of selected number of items from total number of items provided.
	 * 
	 * @param totalItems Number of total items
	 * @param noOfItemsToBeSelected Number of items to be selected
	 * @param items Collection of items from which combinations needs to be selected 
	 * @param startIndex Index of the collection from where combinations need to be selected
	 * @return Collection of all combinations of selected number of items
	 */

	public static List<int[]> combinations(int totalItems,
			int noOfItemsToBeSelected, int[] items, int startIndex) {
		List<int[]> results = new ArrayList<int[]>(
				factorial(totalItems)
						/ (factorial(totalItems - noOfItemsToBeSelected) * factorial(noOfItemsToBeSelected)));

		if (noOfItemsToBeSelected == 1) {
			for (int i = startIndex; i < items.length; i++) {
				results.add(new int[] { items[i] });
			}

			return results;

		}

		else if (noOfItemsToBeSelected == totalItems) {
			results.add(Arrays.copyOfRange(items, startIndex, items.length));
			return results;
		}

		else if (noOfItemsToBeSelected < totalItems) {

			List<int[]> itermediateResult = combinations(totalItems - 1,
					noOfItemsToBeSelected - 1, items, startIndex + 1);

			for (int[] combination : itermediateResult) {
				int[] newCombination = new int[combination.length + 1];
				newCombination[0] = items[startIndex];
				for (int index = 0; index < combination.length; index++) {
					newCombination[index + 1] = combination[index];
				}

				results.add(newCombination);
			}

			results.addAll(combinations(totalItems - 1, noOfItemsToBeSelected,
					items, startIndex + 1));

			return results;

		}

		return results;
	}

	
	/**
	 * Calculates factorial of a number
	 * 
	 * @param n 
	 * @return factorial of given number
	 */
	
	public static int factorial(int n) {
		if (n == 0) {
			return 1;
		}
		return n * factorial(n - 1);
	}
}
