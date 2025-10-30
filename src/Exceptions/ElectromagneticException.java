package Exceptions;

/**
* Исключение возникает когда длина волны меньше критической, или иные ошибки
* связанные с расчётом волны для прямоугольного волновода
*/
public class ElectromagneticException extends Exception {
    public ElectromagneticException ( String message ) {
        super ( message );
    }
}