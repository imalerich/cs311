package cs311.hw4;

/**
 * 
 * Represents a matrix of n rows by m columns.  Note that row and column indexes 
 * start at 0.
 * 
 **/
public interface IMatrix
{

    /**
     * Creates a new Object that implements the IMatrix interface that is a
     * submatrix of this matrix.  The elements of the submatrix are deep
     * copied.
     * 
     * @param upperLeftRow is the upper left row of the submatrix, inclusive.
     * @param upperLeftCol is the upper left column of the submatrix, inclusive.
     * @param lowerRightRow is the lower right row of the submatrix, inclusive.
     * @param lowerRightCol is the lower right column of the submatrix, inclusive.
     * 
     * @return the new submatrix as an object that implements the IMatrix interface.
     * 
     * @throws IllegalArgumentException if any of the arguments are out of bounds.
     */
    public abstract IMatrix subMatrix( int upperLeftRow, int upperLeftCol, int lowerRightRow, int lowerRightCol) throws IllegalArgumentException;
    
    /**
     * Sets an element of the matrix.
     * 
     * @param row is the row at which to set the element.
     * @param col is the column at which to set the element.
     * @param val is the value that must be a Number type or subclass of Number type to set.
     * 
     * @throws IllegalArgumentException if row or column is not a valid index into the matrix.
     */
    public abstract void setElement( int row, int col, Number val) throws IllegalArgumentException;
    
    /**
     *  Returns the specified element in the matrix.
     * 
     * @param row is the row at which to retrieve the element.
     * @param col is the column at which to retrieve the element.
     * 
     * @return the value of the element at the specified row and column.
     * 
     * @throws IllegalArgumentException if row or column is out of bounds.
     */
    public abstract Number getElement( int row, int col ) throws IllegalArgumentException;
    
    /**
     * Performs a matrix multiplication of this matrix and the matrix given in the
     * parameter.  Note that the order of the multiplication is this matrix A multiplied
     * by the parameter matrix B.  That is A * B.  The result is returned as a new
     * IMatrix type.  The multiplication need only run in n^3 asymptotic time.
     * 
     * @param mat is the matrix that this matrix is multiplied by.
     * 
     * @return the resultant matrix of the multiplication.
     * @throws IllegalArgumentException if the sizes of the matricies are 
     * not compatible with multiplication.
     */
    public abstract IMatrix multiply( IMatrix mat ) throws IllegalArgumentException;
    
    /**
     * Performs a matrix addition of this matrix and the matrix given in the parameter.
     * Te result is returned as a new IMatrix type.  The matrix addition runs in
     * n^2 asymptotic time.
     * 
     * @param mat is the matrix to add
     * 
     * @return the resultant matrix of the addition
     * 
     * @throws IllegalArgumentException if the sizes of the matricies are not 
     * compatible with addition.
     */
    public abstract IMatrix add( IMatrix mat ) throws IllegalArgumentException;
}
