package Tests;

import cs311.hw4.MeasureTimeComplexity;
import cs311.hw4.SlowMatrix;

public class Main {

	public static void main(String[] args) {
		int[] v0 = {2, 0, 1, 2};
		int[] v1 = {1, 2, 3, 4};
		SlowMatrix m0 = new SlowMatrix(v0);
		SlowMatrix m1 = new SlowMatrix(v1);
		SlowMatrix r = SlowMatrix.Random(4, Integer.MAX_VALUE);
		
//		System.out.println(m0);
//		System.out.println(m1);
//		System.out.println(m0.multiply(m1));
		
//		System.out.println(r);
//		System.out.println(r.multiply(r));
		
		MeasureTimeComplexity m = new MeasureTimeComplexity();
		System.out.println(r);
		System.out.println(m.normalize(r, 1000));
	}
}
