package cs311.hw4;

public class SlowMatrixFactory implements IMeasureFactory {

	@Override
	public IMeasurable createRandom(int size) {
		return SlowMatrix.Random(size, Integer.MAX_VALUE);
	}
}
