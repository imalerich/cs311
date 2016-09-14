import java.util.List;
import java.util.Comparator;

public class Log3Search {
	
	/**
	 * Searches the sorted input array for the given k in log3 time.
	 * @param arr Sorted input array to search.
	 * @param k Key to look for within the input array.
	 * @return True if k is found in arr, false otherwise.
	 */
	public static <T extends Comparable<? super T>> T log3search(List<T> arr, T k) {
		int start = 0; // inclusive start of the current subset
		int end = arr.size()-1; // inclusize end of the current subset
		while (start <= end) {
			// If there is only one element to check, is it the one we are looking for?
			if (end-start == 0) {
				if (arr.get(start).compareTo(k) == 0) {
					return arr.get(start);
				} else {
					break;
				}
			} else if (end-start == 1) {
				// If there are only 2 elements, we cannot further divide into thirds, so handle that here.
				if (arr.get(start).compareTo(k) == 0) {
					return arr.get(start);
				} else if (arr.get(end).compareTo(k) == 0) {
					return arr.get(end);
				} else {
					break;
				}
			}
			
			// Divide our sub-array into thirds, this is how we will get log3 time.
			int size = end - start + 1;
			int count = Math.min(size/3, 1);
			
			int M1 = start + count-1; // inclusive end of the first subset
			int M2 = start + 2*count; //inclusive start of the last subset

			// Find which sub-array k belongs to, and continue.
			if (k.compareTo(arr.get(M1)) <= 0) {
				end = M1;
			} else if (k.compareTo(arr.get(M2)) >= 0) {
				start = M2;
			} else {
				// It is not less then the smallest elements, or greater than the greatest,
				// Therefore if it exists in the array, it must be in the mid elements.
				start = M1 + 1;
				end = M2 - 1;
			}
		}
		
		return null;
	}
}