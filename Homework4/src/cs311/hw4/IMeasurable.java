package cs311.hw4;

/**
 *  The interface an object must implement in order for it to be measured in the
 *  framework.
 */
public interface IMeasurable
{

    /**
     * The execute method contains code that is to be measured.  Typically a
     * simple call to the method that is to be measured in the object.
     */
    public void execute();
}
