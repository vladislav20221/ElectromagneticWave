package Color;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Vladislav
 */
public class Scale implements ColorFill {
    private double max;
    // Палитра цветов для заполнения различных уровней
    private final LinkedHashMap<Double,Color> scale = new LinkedHashMap<>(20);
    
    public Scale ( double max ) {
        this.max = max;
        settingPaintScale();
    }
    /** Градиент цветов для графика */
    private void settingPaintScale () {  
        int n = 10;
        scale.clear();
        scale.put( max*0.0625d, new Color ( 0, 70-n, 255-n ) );
        scale.put( max*0.125d, new Color ( 0, 100-n, 255-n ) );
        scale.put( max*0.1875d, new Color ( 0, 145-n, 255-n ) );
        scale.put( max*0.25d, new Color ( 0, 220-n, 255-n ) );
        scale.put( max*0.3125d, new Color ( 0, 255-n, 220-n ) );
        scale.put( max*0.375d, new Color ( 0, 255-n, 150-n ) );
        scale.put( max*0.4375d, new Color ( 0, 255-n, 75-n ) );
        scale.put( max*0.5d, new Color ( 0, 255-n, 0 ) );
        scale.put( max*0.5625d, new Color ( 75-n, 255-n, 0 ) );
        scale.put( max*0.625d, new Color ( 150-n, 255-n, 0 ) );
        scale.put( max*0.6875d, new Color ( 220-n, 255-n, 0 ) );
        scale.put( max*0.75d, new Color ( 255-n, 220-n, 0 ) );
        scale.put( max*0.8125d, new Color ( 255-n, 150-n, 0 ) );
        scale.put( max*0.875d, new Color ( 255-n, 100-n, 0 ) );
        scale.put( max*0.9375d, new Color ( 255-n, 75-n, 0 ) );
        scale.put( max, new Color ( 255-n, 0, 0 ) );
    }
    
    @Override
    public Color get_Color( double x ) {
        for ( Map.Entry<Double, Color> set: scale.entrySet() ) {
            if ( Double.compare( set.getKey(), x ) >= 0 ) {
                return set.getValue();
            }
        }
        return Color.pink;
    }
}