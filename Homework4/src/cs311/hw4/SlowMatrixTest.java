package cs311.hw4;

import java.util.List;

public class SlowMatrixTest {
	public static void main(String[] args) {
		SlowMatrixFactory factory = new SlowMatrixFactory();
		MeasureTimeComplexity measure = new MeasureTimeComplexity();

		// 1. Find the number of iterations required to multiply a 2x2 matrix in 2ms.
		int iterations = measure.normalize(factory.createRandom(2), 2);
		// 2. Determine the amount of time for matrix multiplications from 2 to 100.
		List<? extends IResult> res = measure.measure(factory, iterations, 2, 100, 1);

		// 3. Output the results.
		System.out.println(iterations);
		for (IResult r : res) {
			System.out.println(r.getSize() + " " + r.getTime());
		}
	}
}
