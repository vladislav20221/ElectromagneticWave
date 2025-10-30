package rectangularwaveguide;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.Serializable;
import javax.swing.border.Border;

/**
 *
 * @author Vladislav
 */
class border_E_wave implements Border, Serializable  {
    private final Color colorCentr = new Color ( 198, 200, 255 );
    private final Color colorOuterPart = new Color ( 198, 200, 255, 70 );
        
    @Override
    public void paintBorder( Component cmpnt, Graphics grphcs, int i, int i1, int i2, int i3) {
        Graphics2D gr = (Graphics2D) grphcs.create();
        Font font = new Font ("Arial", Font.ITALIC+Font.BOLD, 20 );
        gr.setFont(font);
        gr.setPaint ( colorCentr );
        int opt = 2;
        int width = 2;
        gr.fillRect( opt, opt+18, opt+width, cmpnt.getHeight()-25 );
        gr.fillRect( opt+18, opt, cmpnt.getWidth()-6-18, opt+width );
           
        gr.fillRect( opt, cmpnt.getHeight()-6, cmpnt.getWidth()-6, opt+width );
           
        gr.fillRect( cmpnt.getWidth()-7, opt, opt+width, cmpnt.getHeight()-5 );
        gr.drawString( "E", 2, 16 );
    }
    @Override
    public Insets getBorderInsets(Component cmpnt) {
        return new Insets ( 5, 5, 5, 5 );
    }
    @Override
    public boolean isBorderOpaque() {return false;}        
}
