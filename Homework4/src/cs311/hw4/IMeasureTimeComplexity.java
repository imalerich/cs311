package cs311.hw4;

import java.util.List;

/**
 *  This interface is the controlling entity for the measurement framework.  It
 * consists of two methods.  The first is used to calibrate the measurement based
 * on how fast the processor is in the computer performing the measurements and
 * the second method performs measurements of algorithms using the calibration
 * at various sizes and returns the result as a list.
 */
public interface IMeasureTimeComplexity
{

    /**
     *  This method calibrates the measurement by computing the number of iterations
     *  of the execute method are required to match or exceed a specified time
     *  in milliseconds.
     * 
     * @param m is the IMeasureable object to run the execute method in.
     * 
     * @param timeInMilliseconds is the number of milliseconds to calibrate to. 
     * 
     * @return the number of iterations required for the execute method to run
     * in order to match or exceed the specified time given. (timeInMilliseconds)
     */
    public int normalize(IMeasurable m, long timeInMilliseconds);
    
    /**
     * The measure method is used measure the execution time of a sequence of
     * sizes.
     * 
     * @param factory is a IMeasureFactory implementation for creating instances of
     * random data of specified sizes.
     * 
     * @param nmeasures is the number of iterations to perform of the measurement function
     * (Usually the result returned from the normalize method.)
     * 
     * @param startsize is the initial size of the data to measure
     * @param endsize is the final size of the data to measure
     * @param stepsize is how much to change the size for each new data size.
     * 
     * @return an array list of IResult types that contains the size and time in
     * milliseconds the measurement took.
     */
    public List<? extends IResult> measure(IMeasureFactory factory, int nmeasures, int startsize, int endsize, int stepsize);
}
