package Color;

import java.awt.Color;

/**
 * класс вычисляющий градиент для текстур.
 * Считается что минимальное значение - 0.
 * @author Vladislav
 */
public interface ColorFill {
      
    public static ColorFill getFill ( final double max, FILL fill ) {        
        if ( fill == FILL.Gradient )
            return new Gradient( max );
        if ( fill == FILL.Scale )
            return new Scale ( max );
        return new Gradient( max );
    }   
             
    public Color get_Color ( double x );
}