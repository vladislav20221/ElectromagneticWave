package rectangularwaveguide;

import Charts.GraphicsModel;
import Color.FILL;
import Settings.myColor;
import Settings.myFont;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 *
 * @author Vladislav
 */
public class ConfigurationTexture {    
    private static final Dimension SIZE = new Dimension ( 560, 500 );
    private static final Dimension SIZE_PANE = new Dimension ( 2_000, 75 );
    private static final Dimension size_lab = new Dimension ( 80, 30 );
    private static final Border border = BorderFactory.createLineBorder( myColor.color_inputFont, 1 );
    
    private static int fps;
    private static int count;
    private static int size_textur;
    private static int speed;
    private static FILL fill;
    private static int size_raster_x;
    private static int size_raster_y;
    
    // статический инициализирующий год
    static { 
        fps = MainWorkWindow.fps;
        count = MainWorkWindow.count_texteru;
        size_textur = MainWorkWindow.size_texteru;
        speed = MainWorkWindow.speed;
        fill = MainWorkWindow.fill;
        size_raster_x = GraphicsModel.WIDTH;
        size_raster_y = GraphicsModel.HIGHT;
    }
    
    private ConfigurationTexture (){}
    
    public static void showConfigTextureDialog ( final JComponent component, final JFrame frame ) {
        Point point = component.getLocationOnScreen();
        JDialog dialog = new JDialog ( frame, "Параметры текстур", true );
        
        dialog.setLayout( new BoxLayout( dialog.getContentPane(), BoxLayout.Y_AXIS ) );        
        
        dialog.add( getFpsPane() );
        dialog.add( getCountPane() );
        dialog.add( getTextureSizePane() );
        dialog.add( getSpeedPane() );
        dialog.add( getFillPane() );
        dialog.add( getSizeRasterPane() );
        dialog.add( Box.createVerticalGlue() );
        dialog.add( getMenuPane( dialog ) );
        
        dialog.getContentPane().setBackground( myColor.background );
        //dialog.setResizable( false );
        dialog.setLocation( point );
        dialog.setSize( SIZE );
        dialog.setVisible( true );
    }
    
    private static JPanel getFpsPane () {
        Border border_title = BorderFactory.createTitledBorder( border, 
                "Количество кадров", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );
        JPanel pane = getdefaultPane( border_title );            
        GridBagConstraints grid = new GridBagConstraints();
        JLabel label = new JLabel ( Integer.toString( ConfigurationTexture.fps ) );
            settingLabel ( label );
        JSlider slider = new JSlider( 15, 75, ConfigurationTexture.fps );
            slider.setPaintTicks( true );
            slider.setSnapToTicks( true );
            slider.setOpaque( false );
            slider.setMajorTickSpacing( 5 );
            
            slider.addChangeListener( (ch)-> {
                label.setText( slider.getValue()+"" );
                ConfigurationTexture.fps = slider.getValue();
            });
            
            grid.weightx = 1;            
            grid.fill = GridBagConstraints.HORIZONTAL;
            grid.gridx = 0; grid.gridy = 0;
        pane.add( slider, grid );
            grid.fill = GridBagConstraints.NONE;
            grid.weightx = 0;
            grid.gridx = 1; grid.gridy = 0;
        pane.add( label, grid );
        return pane;
    }    
    private static JPanel getCountPane () {
        Border border_title = BorderFactory.createTitledBorder( border,
                "Количество точек по оси x", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );
        JPanel pane = getdefaultPane( border_title );
        GridBagConstraints grid = new GridBagConstraints();
        JLabel label = new JLabel ( Integer.toString( ConfigurationTexture.count ) );
            settingLabel ( label );
        JSlider slider = new JSlider( 100, 600, ConfigurationTexture.count );
            settingSlider ( slider, 10 );
            slider.addChangeListener( (ch)-> {
                 label.setText( slider.getValue()+"" );
                 ConfigurationTexture.count = slider.getValue();
            });
        
            grid.weightx = 1;
            grid.fill = GridBagConstraints.HORIZONTAL;
            grid.gridx = 0; grid.gridy = 0;
        pane.add( slider, grid );
            grid.fill = GridBagConstraints.NONE;
            grid.weightx = 0;
            grid.gridx = 1; grid.gridy = 0;
        pane.add( label, grid );
        return pane;
    }    
    private static JPanel getTextureSizePane () {
        Border border_title = BorderFactory.createTitledBorder( border, 
                "Размер текстуры", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );
        JPanel pane = getdefaultPane( border_title );
        GridBagConstraints grid = new GridBagConstraints();
        
        JLabel label = new JLabel ( Integer.toString( ConfigurationTexture.size_textur ) );
            settingLabel ( label );
        JSlider slider = new JSlider( 100, 600, MainWorkWindow.size_texteru );
            settingSlider( slider, 20 );
            slider.addChangeListener( (ch)-> {
                label.setText( Integer.toString( slider.getValue() ) );
                ConfigurationTexture.size_textur = slider.getValue();
            });
            grid.weightx = 1;
            grid.fill = GridBagConstraints.HORIZONTAL;
            grid.gridx = 0; grid.gridy = 0;
        pane.add( slider, grid );
            grid.fill = GridBagConstraints.NONE;
            grid.weightx = 0;
            grid.gridx = 1; grid.gridy = 0;
        pane.add( label, grid );            
        return pane;
    }    
    private static JPanel getSpeedPane () {
        Border border_title = BorderFactory.createTitledBorder( border,
                "Скорость анимации", TitledBorder.LEFT, TitledBorder.CENTER,
                myFont.font_heading, myColor.color_short );
        JPanel pane = getdefaultPane( border_title );
        GridBagConstraints grid = new GridBagConstraints();
        
        JLabel label = new JLabel ( Integer.toString( ConfigurationTexture.speed ) );
            settingLabel ( label );
        JSlider slider = new JSlider( 1, 20, ConfigurationTexture.speed );
            settingSlider( slider, 1 );
            slider.addChangeListener( (ch)->{
                label.setText( Integer.toString( slider.getValue() ));
                ConfigurationTexture.speed = slider.getValue();
            });
            
        grid.weightx = 1;
            grid.fill = GridBagConstraints.HORIZONTAL;
            grid.gridx = 0; grid.gridy = 0;
        pane.add( slider, grid );
            grid.fill = GridBagConstraints.NONE;
            grid.weightx = 0;
            grid.gridx = 1; grid.gridy = 0;
        pane.add( label, grid );            
        return pane;
    }
    private static JPanel getFillPane () {
        Border border_title = BorderFactory.createTitledBorder( border, 
                "Заливка текстуры", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );         
        JPanel pane = getdefaultPane( border_title );
            pane.setLayout( new BoxLayout( pane, BoxLayout.X_AXIS ) );
      
        JToggleButton togle_gradient = new JToggleButton ( "gradient" );
            togle_gradient.setName( "togle_gradient" );
            togle_gradient.setForeground( myColor.color_short );
        JToggleButton togle_scale = new JToggleButton ( "scale" );
            togle_scale.setName( "togle_scale" );
            togle_scale.setForeground( myColor.color_short );
            
        if ( ConfigurationTexture.fill == FILL.Gradient )
            togle_gradient.setSelected( true );
        if ( ConfigurationTexture.fill == FILL.Scale )
            togle_scale.setSelected( true );
            
        togle_gradient.addActionListener( (ac)->ConfigurationTexture.fill = FILL.Gradient );
        togle_scale.addActionListener( (ac)->ConfigurationTexture.fill = FILL.Scale );
        ButtonGroup color_fill = new ButtonGroup ();
            color_fill.add( togle_gradient );
            color_fill.add( togle_scale );
        
        pane.add( togle_gradient );
        pane.add( Box.createHorizontalStrut( 5 ) );
        pane.add( togle_scale );
        pane.add( Box.createHorizontalGlue() );
        return pane;
    }    
    private static JPanel getSizeRasterPane () {
        Border border_title = BorderFactory.createTitledBorder( border, 
                "Размер растра", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );                
        Border border_title_x = BorderFactory.createTitledBorder( border, 
                "x", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );  
        Border border_title_y = BorderFactory.createTitledBorder( border, 
                "y", TitledBorder.LEFT, TitledBorder.CENTER, 
                myFont.font_heading, myColor.color_short );  
        JPanel pane = getdefaultPane( border_title );
        GridBagConstraints grid = new GridBagConstraints();
        
        JLabel label = new JLabel ( ConfigurationTexture.size_raster_x+"x"+ConfigurationTexture.size_raster_y );
            settingLabel( label );
        JSlider slider_x = new JSlider( 50, 1000, GraphicsModel.WIDTH );
            settingSlider ( slider_x, 25, border_title_x );
            slider_x.addChangeListener( (ch)->ConfigurationTexture.size_raster_x = slider_x.getValue() );
        JSlider slider_y = new JSlider( 50, 1000, GraphicsModel.HIGHT );
            settingSlider ( slider_y, 25, border_title_y );            
            slider_y.addChangeListener( (ch)->ConfigurationTexture.size_raster_y = slider_y.getValue() );
            
        ChangeListener listener = (ChangeEvent e)->label.setText( slider_x.getValue()+"x"+slider_y.getValue() );
            slider_x.addChangeListener( listener );
            slider_y.addChangeListener( listener );
            grid.weightx = 1;
            grid.fill = GridBagConstraints.HORIZONTAL;
            grid.gridx = 0; grid.gridy = 0;
        pane.add( slider_x, grid );
            grid.gridx = 1; grid.gridy = 0;
        pane.add( slider_y, grid );
            grid.weightx = 0;
            grid.fill = GridBagConstraints.NONE;        
            grid.gridx = 2; grid.gridy = 0;
        pane.add( label, grid );
        return pane;
    }    
    private static JPanel getMenuPane ( final JDialog dialog ) {
        Dimension size_pane = new Dimension ( 2_000, size_lab.height+10 );
        JPanel pane = getdefaultPane();
            pane.setLayout( new BoxLayout( pane, BoxLayout.X_AXIS ) );
            pane.setPreferredSize( size_pane );
            pane.setMaximumSize( size_pane );
        JButton save = new JButton ("Преминить");
            settingButton( save );            
            save.addActionListener( new ActionListener () {
                @Override
                public void actionPerformed( ActionEvent ae ) {
                    if ( ConfigurationTexture.fps != MainWorkWindow.fps ) {
                        MainWorkWindow.elec.set_fps( ConfigurationTexture.fps );
                        MainWorkWindow.fps = ConfigurationTexture.fps;
                    }
                    if ( ConfigurationTexture.count != MainWorkWindow.count_texteru )
                        MainWorkWindow.count_texteru = ConfigurationTexture.count;
                    if ( ConfigurationTexture.size_textur != MainWorkWindow.size_texteru )
                        MainWorkWindow.size_texteru = ConfigurationTexture.size_textur;
                    if ( ConfigurationTexture.speed != MainWorkWindow.speed )
                        MainWorkWindow.speed = ConfigurationTexture.speed;
                    if ( ConfigurationTexture.fill != MainWorkWindow.fill )
                        MainWorkWindow.fill = ConfigurationTexture.fill;
                    
                    if ( ConfigurationTexture.size_raster_x != GraphicsModel.WIDTH || ConfigurationTexture.size_raster_y != GraphicsModel.HIGHT ) {
                        GraphicsModel.WIDTH = ConfigurationTexture.size_raster_x;
                        GraphicsModel.HIGHT = ConfigurationTexture.size_raster_y;
                        if ( MainWorkWindow.pane_model_E.getModel() != null && MainWorkWindow.pane_model_H.getModel() != null ) {
                            for ( GraphicsModel model: MainWorkWindow.modelList ) {
                                if ( model == null ) break;
                                model.setRasterSize( ConfigurationTexture.size_raster_x, 
                                                     ConfigurationTexture.size_raster_y );
                            }                                
                            MainWorkWindow.pane_model_E.repaint();
                            MainWorkWindow.pane_model_H.repaint();
                        }
                    }
                    dialog.dispose();
                }
            });
        pane.add( Box.createHorizontalGlue() );
        pane.add( save );
        pane.add( Box.createHorizontalStrut( 10 ) );
        return pane;
    }
   
    private static void settingButton ( final JButton button ) {
        button.setBackground( myColor.color_short );
        button.setForeground( Color.white );
        button.setAlignmentX( JComponent.CENTER_ALIGNMENT );
        button.setAlignmentY( JComponent.CENTER_ALIGNMENT );
    }
    private static void settingLabel ( final JLabel label ) {
        label.setBorder( BorderFactory.createLineBorder( myColor.color_inputFont, 2 ) );
        label.setPreferredSize( size_lab );
        label.setMinimumSize( size_lab );
        label.setAlignmentX( JComponent.CENTER_ALIGNMENT );
        label.setAlignmentY( JComponent.CENTER_ALIGNMENT );
        label.setFont( myFont.font_heading );
        label.setForeground( Color.white );
        label.setOpaque( true );
        label.setBackground( myColor.color_short );
    }
    private static void settingSlider ( final JSlider slider, final int spacing ) {
        slider.setPaintTicks( true );
        slider.setSnapToTicks( true );
        slider.setOpaque( false );
        slider.setMajorTickSpacing( spacing );
    }
    private static void settingSlider ( final JSlider slider, final int spacing, final Border border ) {
        settingSlider( slider, spacing );
        slider.setBorder( border );
    }
    private static JPanel getdefaultPane ( final Border border ) {
        JPanel pane = new JPanel ();
            pane.setOpaque( false );
            pane.setLayout( new GridBagLayout () );
            pane.setBorder( border );    
            pane.setPreferredSize( SIZE_PANE );
            pane.setMaximumSize( SIZE_PANE );
        return pane;
    }
    private static JPanel getdefaultPane () {
        JPanel pane = new JPanel ();
            pane.setOpaque( false );
            pane.setLayout( new GridBagLayout () );
            pane.setPreferredSize( SIZE_PANE );
            pane.setMaximumSize( SIZE_PANE );
        return pane;
    }    
}