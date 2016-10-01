package cs311.hw4;

/**
 *  This is the interface for creating IMeasureable objects of a specified size
 * for giving to the framework for measurement.
 */
public interface IMeasureFactory
{

    /**
     * This method creates an object that implements the IMeasurable interface
     * of the specified size.  The instance of the object is initialized with
     * random data.
     * 
     * @param size is the size of the object to create.
     * 
     * @return a new object that implements the IMeasureable interface.
     */
    public abstract IMeasurable createRandom( int size );
}
