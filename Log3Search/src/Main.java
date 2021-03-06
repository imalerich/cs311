import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {
	
	public static void main(String[] args) {
		Random r = new Random();
		int PASS = 0;
		int TOTAL = 0;
		
		for (int j=0; j<100; j++) {
			// Generate a pseudo-random array.
			ArrayList<Integer> arr = new ArrayList<Integer>();
			int count = (int)(r.nextDouble() * 1000);
			for (int i=0; i<count; i++) {
				arr.add(new Integer(r.nextInt() % 10));
			}
			
			// Array must be sorted.
			Collections.sort(arr);
			
			// Test 'log3search' against the built in 'contains' method.
			for (int i=0; i<count; i++) {
				Integer key = new Integer(r.nextInt() % 10);
				boolean pass = (Log3Search.log3search(arr, key) != null) == arr.contains(key);
				TOTAL++;
				PASS += pass ? 1 : 0;
				if (!pass) {
					System.out.println("FAIL");
					System.out.println("\t" + arr + ": " + key);
				}
			}
		}
		
		System.out.println("PASSED: (" + PASS + "/" + TOTAL + ")");
	}
}
