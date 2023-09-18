package Exceptions;

/**
 * Созданное исключение для матриц.
 * @author Vladislav
 */
public class MatrixDifferentDimensionsException extends Exception{
    public  MatrixDifferentDimensionsException ( String message ){
        super( message );
    }
}
