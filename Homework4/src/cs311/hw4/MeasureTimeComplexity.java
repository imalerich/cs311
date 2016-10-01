package cs311.hw4;

import java.util.ArrayList;
import java.util.List;

public class MeasureTimeComplexity implements IMeasureTimeComplexity {
	
	public static final long NANO_PER_MILLI_SECOND = 1000000;

	@Override
	public int normalize(IMeasurable m, long timeInMilliseconds) {
		int count = 0;
		long timeInNanoseconds = timeInMilliseconds * NANO_PER_MILLI_SECOND;
		for (long start = System.nanoTime(); currentTime(start) < timeInNanoseconds; count++) {
			m.execute();
		}

		return count;
	}

	@Override
	public List<? extends IResult> measure(IMeasureFactory factory, int nmeasures, int startsize, int endsize, int stepsize) {
		ArrayList<Result> results = new ArrayList<Result>();

		for (int s=startsize; s<=endsize; s+=stepsize) {
			IMeasurable m = factory.createRandom(s);
			long start = System.nanoTime();
			for (int i=0; i<nmeasures; i++) {
				m.execute();
			}

			long time = (System.nanoTime() - start) / NANO_PER_MILLI_SECOND;
			results.add(new Result(nmeasures, time));
		}

		return results;
	}
	
	/**
	 * Returns the current elapsed time for the given start point (system time).
	 * @param start The System.nanoTime() the simulation was started on.
	 * @return The current time elapsed.
	 */
	private long currentTime(long start) {
		return System.nanoTime() - start;
	}
}

class Result implements IResult {
	/**
	 * The number of measurements that were made to produce this result.
	 */
	private int size;
	
	/**
	 * The length of time (in milliseconds) that it took to compute this result.
	 */
	private long time;
	
	/**
	 * Construct a new result with the input size and time pair.
	 */
	public Result(int Size, long Time) {
		size = Size;
		time = Time;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public long getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return "Size: " + size + " " + "\tTime: " + time;
	}
}