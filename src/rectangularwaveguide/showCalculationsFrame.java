package rectangularwaveguide;

import Computation.ElectromagneticWave;
import static Settings.myColor.color_short;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * Окно отображающее расчётные данные
 * @author Vladislav
 */
class showCalculationsFrame extends JFrame {
        private JTextArea show = new JTextArea ("Пока пусто...");
        private ElectromagneticWave elec;
        
        public showCalculationsFrame ( ElectromagneticWave elec ) {
            super ("Расчёты");
            this.elec = elec;
            setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );
            setSize( 600, 600 );            
            
            show.setBackground( color_short );
            show.setForeground( Color.white );   
            
            add ( show );
        }    
        public void update () {
            if ( elec != null )
                show.setText( elec.toString() );
            else
                show.setText( "Пока пусто..." );
        }
    }
