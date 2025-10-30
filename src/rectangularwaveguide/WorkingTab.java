package rectangularwaveguide;

import Settings.myColor;
import Settings.myFont;
import Settings.mySize;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Рабочая вкладка.
 * @author Vladislav
 */
public class WorkingTab extends JPanel implements myColor, myFont, mySize {
    // Панель на которой будет расположен весь контент
    private JPanel pane_content = new JPanel ();
    // Заголовок панели
    private JPanel pane_heading = new JPanel () {
            @Override
            public void paintComponent ( Graphics g ) {
                Graphics2D gr = (Graphics2D) g.create();
                GradientPaint gradient = new GradientPaint ( 0,0,color_short, (int)(getWidth()*0.7), (int)(this.getHeight()/2), background  );
                gr.setPaint(gradient);
                gr.fillRect( 0, 0, this.getWidth(), this.getHeight() );
            }
        };
    // Заголовок текущей панели
    private JLabel lab_heading = new JLabel ();
    // Основная панель на которой располагаются все эелменты
    private GridBagConstraints grid = new GridBagConstraints();
    
    public WorkingTab( String heading ){
        setBackground( background );       
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        pane_content.setName("pane_content");
        pane_content.setBackground( background );
        
        pane_content.setLayout( new GridBagLayout () );
        
            grid.insets = new Insets ( 5, 5, 5, 5 );   
            grid.gridwidth = GridBagConstraints.REMAINDER;          // Занимает все ячейки по горизонтали
            grid.fill = GridBagConstraints.HORIZONTAL;              // Заполняет всё пространство ячейки по горизонтали 
            grid.weightx = 1;                                       // Заполняет всё свободное пространство ячейки
            
        pane_heading.setName("pane_heading");
        pane_heading.setLayout( new BoxLayout( pane_heading, BoxLayout.X_AXIS ) );
        
        lab_heading.setName( "lab_heading" );
        lab_heading.setFont( font_heading );
        lab_heading.setForeground( Color.white );
        lab_heading.setText( heading );
        
        pane_heading.add( Box.createRigidArea( new Dimension ( 20, 30 ) ) );
        pane_heading.add( lab_heading );
        pane_heading.add( Box.createHorizontalStrut( 50 ) );
        pane_heading.add( Box.createHorizontalGlue() );
        add( pane_heading );
        add( pane_content );                
    }
    public WorkingTab(){
        setBackground( background );       
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        pane_content.setName("pane_content");
        pane_content.setBackground( background );        
        
        pane_content.setLayout( new GridBagLayout () );
        
            grid.insets = new Insets ( 5, 5, 5, 5 );   
            grid.gridwidth = GridBagConstraints.REMAINDER;      // Занимает все ячейки по горизонтали
            grid.fill = GridBagConstraints.HORIZONTAL;        // Заполняет всё пространство ячейки по горизонтали 
            grid.weightx = 1;           
            
        pane_heading.setName("pane_heading");
        pane_heading.setLayout( new BoxLayout( pane_heading, BoxLayout.X_AXIS ) );
        
        lab_heading.setName( "lab_heading" );
        lab_heading.setFont( font_heading );
        lab_heading.setForeground( Color.white );
        
        pane_heading.add( Box.createRigidArea( new Dimension ( 20, 30 ) ) );
        pane_heading.add( lab_heading );
        pane_heading.add( Box.createHorizontalStrut( 50 ) );
        pane_heading.add( Box.createHorizontalGlue() );
        add( pane_heading );
        add( pane_content );
    }
    
    public void setHeading ( String heading ) {
        lab_heading.setText( heading );
    }
    public JPanel getPaneContent () {
        return this.pane_content;
    }
    public JPanel getPaneHeading () {
        return this.pane_heading;
    }
    
    public void setMainPane ( JPanel pane ) {
        grid.gridwidth = GridBagConstraints.REMAINDER;          // Занимает все ячейки по горизонтали        
        grid.fill = GridBagConstraints.BOTH;              // Заполняет всё пространство ячейки по горизонтали 
        grid.weighty = 1;         
        grid.gridx = 0; grid.gridy = 0;            
        pane_content.add ( pane, grid );
        revalidate();
        repaint();
        grid.weighty = 0;         
        grid.fill = GridBagConstraints.HORIZONTAL;
    }
    public void setMenuPane ( JPanel pane ) {
        grid.gridx = 0; grid.gridy = 1;
        pane_content.add( pane, grid );
        revalidate();
        repaint();
    }
}
