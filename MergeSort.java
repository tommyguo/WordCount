package project5;

import java.util.Comparator;
import java.util.Map;
import java.util.ArrayList;

/**
 * This class allows a client to sort an ArrayList with objects of type
 * Map.Entry<String, Integer> using merge sort. If no comparator is provided,
 * the ArrayList is automatically sorted by alphabetical order. Otherwise, the
 * ArrayList will be sorted based on the comparator provided.
 * 
 * @author Thomas Guo
 */
public class MergeSort {
	/**
	 * This method applies a merge sort to the ArrayList passed as a parameter.
	 * No comparator is required and the ArrayList is sorted alphabetically by
	 * default.
	 * 
	 * @param array
	 *            ArrayList to be sorted.
	 */
	public static void mergeSort(ArrayList<Map.Entry<String, Integer>> array) {
		// terminates method if array is null
		if (array == null) {
			return;
		}
		mergeSortRec(array, 0, array.size() - 1, new CompareWordsByName());
	}

	/**
	 * This method applies a merge sort to the ArrayList passed as a parameter.
	 * The ArrayList is sorted using the comparator provided.
	 * 
	 * @param array
	 *            ArrayList to be sorted.
	 * @param comparator
	 *            comparator used to sort the ArrayList.
	 */
	public static void mergeSort(ArrayList<Map.Entry<String, Integer>> array,
			Comparator<Map.Entry<String, Integer>> comparator) {
		// terminates method if array is null
		if (array == null) {
			return;
		}
		mergeSortRec(array, 0, array.size() - 1, comparator);
	}

	/**
	 * Private recursive merge sort method used by the wrapper merge sort
	 * method.
	 * 
	 * @param array
	 *            ArrayList to be sorted
	 * @param first
	 *            first index of the array to be sorted
	 * @param last
	 *            last index of the array to be sorted
	 * @param comparator
	 *            comparator used to sort ArrayList.
	 */
	private static void mergeSortRec(ArrayList<Map.Entry<String, Integer>> array, int first, int last,
			Comparator<Map.Entry<String, Integer>> comparator) {
		// base case
		if (first == last) {
			return;
		}
		int mid = (first + last) / 2;
		// split array in half and run recursive method on both halves
		mergeSortRec(array, first, mid, comparator);
		mergeSortRec(array, mid + 1, last, comparator);
		// merge both halves after they are sorted
		merge(array, first, mid, mid + 1, last, comparator);
	}

	/**
	 * Private merge method used by recursive merge sort method. Merges two
	 * sorted arrays.
	 * 
	 * @param array
	 *            ArrayList to be sorted.
	 * @param leftFirst
	 *            first index of the first array to be merged
	 * @param leftLast
	 *            last index of the first array to be merged
	 * @param rightFirst
	 *            first index of the second array to be merged
	 * @param rightLast
	 *            last index of the second array to be merged
	 * @param comparator
	 *            comparator used to sort ArrayList
	 */
	private static void merge(ArrayList<Map.Entry<String, Integer>> array, int leftFirst, int leftLast, int rightFirst,
			int rightLast, Comparator<Map.Entry<String, Integer>> comparator) {
		ArrayList<Map.Entry<String, Integer>> tmp = new ArrayList<Map.Entry<String, Integer>>();
		int left = leftFirst;
		int right = rightFirst;
		// while both arrays still have elements, add the "smaller" into the
		// temp array
		while (left <= leftLast && right <= rightLast) {
			if (comparator.compare(array.get(left), array.get(right)) < 0) {
				tmp.add(array.get(left));
				left++;
			} else {
				tmp.add(array.get(right));
				right++;
			}
		}
		// add remaining elements left in the array into the temp array
		while (left <= leftLast) {
			tmp.add(array.get(left));
			left++;
		}

		while (right <= rightLast) {
			tmp.add(array.get(right));
			right++;
		}

		// transfer elements in temp array into original array
		for (int i = 0; i < tmp.size(); i++) {
			array.set(i + leftFirst, tmp.get(i));
		}
	}
}

/**
 * This class is used by the MergeSort class to sort the array of the words
 * based on alphabetical order.
 */

class CompareWordsByName implements Comparator<Map.Entry<String, Integer>> {
	@Override
	public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
		return m1.getKey().compareTo(m2.getKey());
	}
}

/**
 * This class is used by the MergeSort class to sort the array of the words
 * based on frequency.
 */
class CompareWordsByFrequency implements Comparator<Map.Entry<String, Integer>> {
	@Override
	public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
		if (m2.getValue() - m1.getValue() != 0) {
			return m2.getValue() - m1.getValue();
		} else {
			return m1.getKey().compareTo(m2.getKey());
		}
	}
}

/**
 * This class is used by the MergeSort class to sort the array of the words
 * based on scarcity.
 */
class CompareWordsByScarcity implements Comparator<Map.Entry<String, Integer>> {
	@Override
	public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
		if (m1.getValue() - m2.getValue() != 0) {
			return m1.getValue() - m2.getValue();
		} else {
			return m1.getKey().compareTo(m2.getKey());
		}
	}
}