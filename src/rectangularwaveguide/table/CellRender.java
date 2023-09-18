package rectangularwaveguide.table;

import Settings.myColor;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Vladislav
 */
public class CellRender extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent ( JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column ) {
        JLabel label = (JLabel)super.getTableCellRendererComponent( table, value, 
                                                                    isSelected, hasFocus, 
                                                                    row, column );
        label.setOpaque( true );
        if ( isSelected ) {
            label.setBackground( myColor.color_back_font );
        } else {
            label.setBackground( table.getSelectionBackground() );
        }
        
        return label;
    }
}