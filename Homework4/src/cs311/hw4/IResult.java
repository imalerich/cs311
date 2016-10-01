package cs311.hw4;

/**
 *  An implementation of this interface encapsulates a measurement result
 * that is a size and time (in milliseconds) pair.
 */
public interface IResult
{

    /**
     *
     * @return the size associated with the measurement
     */
    public int getSize();

    /**
     *
     * @return the time associated with the measurement
     */
    public long getTime();
}
