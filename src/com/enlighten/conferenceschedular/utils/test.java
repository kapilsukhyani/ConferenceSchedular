package com.enlighten.conferenceschedular.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {

	public static void main(String[] args) {

		/*
		 * ArrayList<Integer> talks = testMethod(180, new int[] { 100, 20, 30,
		 * 40, 50, 60, 70, 80 }); System.out.println(talks.toString());
		 */

		/*
		 * combinations(new int[] { 100, 20, 30, 40, 50, 60, 70, 80 }, new
		 * int[3], 0, 8, 0, 2);
		 */

		List<int[]> combinations = combinations(7, 6,
				new int[] { 1, 2, 3, 4, 5,6,7 }, 0);

		for (int[] combination : combinations) {

			System.out.println(Arrays.toString(combination));
		}

	}

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

	public static void combinations(int A[], int comb[], int start, int n,
			int current_k, int k) {
		if (k < 0)
			return;

		// Base case just print all the numbers 1 at a time
		if (k == 0) {
			for (int i = 0; i < n; i++)
				System.out.println(A[i] + "-");
		}

		// current_k goes from 0 to k-1 and simulates a total of
		// k iterations
		if (current_k < k) {
			// if current_k = 0, and k = 3 (i.e. we need to find combinations of
			// 4)
			// then we need to leave out 3 numbers from the end because there
			// are 3
			// more nested loops
			for (int i = start; i < n - (k - current_k); i++) {
				// Store the number in the comb array and recursively call with
				// the remaining sub-array
				comb[current_k] = A[i];
				// This will basically pass a sub array starting at index
				// 'start' and going till n-1
				combinations(A, comb, i + 1, n, current_k + 1, k);
			}
		} else if (current_k == k) {
			for (int i = start; i < n; i++) {
				comb[current_k] = A[i];
				for (int j = 0; j <= k; j++)
					System.out.println(comb[j] + "-");

			}
		} else
			return;
	}

	public static ArrayList<Integer> testMethod(int sessionDuration, int[] talks) {
		ArrayList<Integer> talksInSession = new ArrayList<Integer>();
		ArrayList<Integer> optimizedTalksInSession = new ArrayList<Integer>();
		int minSessionDurationLeft = sessionDuration;
		int sessionDurationLeft = sessionDuration;

		for (int i = 0; i < talks.length; i++) {
			if (talks[i] == sessionDuration) {
				sessionDurationLeft = 0;
				minSessionDurationLeft = 0;
				optimizedTalksInSession.clear();
				optimizedTalksInSession.add(talks[i]);
				break;

			}

			else if (talks[i] < sessionDurationLeft) {
				talksInSession.add(talks[i]);

				sessionDurationLeft = sessionDuration - talks[i];

				for (int j = i + 1; j < talks.length; j++) {
					for (int k = j; k < talks.length; k++) {

						if (talks[k] <= sessionDurationLeft) {
							talksInSession.add(talks[k]);
							sessionDurationLeft -= talks[k];
						}

					}

					if (sessionDurationLeft < minSessionDurationLeft) {
						minSessionDurationLeft = sessionDurationLeft;
						optimizedTalksInSession.clear();
						optimizedTalksInSession.addAll(talksInSession);
					}
					talksInSession.clear();
					sessionDurationLeft = sessionDuration - talks[i];

				}
			}

			else {
				// ignoring talk as its duration is larger than the session
				// duration
			}

		}

		return optimizedTalksInSession;
	}

	public static List<Integer> fillSession(int sessionDuration,
			List<Integer> talks) {
		List<Integer> selectedTalks = null;

		for (Integer integer : talks) {

		}

		return selectedTalks;

	}

	public static int factorial(int n) {
		if (n == 0) {
			return 1;
		}
		return n * factorial(n - 1);
	}
}
