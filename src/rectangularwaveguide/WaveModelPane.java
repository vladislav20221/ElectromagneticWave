 package rectangularwaveguide;

import Charts.GraphicsModel;
import Charts.WaveLevelGraph;
import Data.TYPE;
import MyListeners.MouseMovement;
import Settings.myColor;
import Settings.myFont;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Vladislav
 */
public class WaveModelPane extends JPanel {
    private Dimension size = new Dimension ( GraphicsModel.WIDTH, GraphicsModel.HIGHT );
    private JLabel label = new JLabel ( "МОДЕЛЬ ЕЩЁ НЕ ПОСТРОЕННА!!" );
    
    private GraphicsModel model;
    private MouseMovement mouse;
    
    {        
        this.setOpaque( true );
        //this.setBackground( myColor.background );
        this.setSize( size );
        this.setPreferredSize( size );
        this.setMinimumSize( size );
        this.setMaximumSize( size );
        this.setLayout( null );
        this.label.setBounds( 80, GraphicsModel.HIGHT/2, 450, 40 );
        this.label.setFont( myFont.font_heading );
        this.label.setForeground( myColor.color_short );
        this.addComponentListener( new ComponentListener () {
            @Override
            public void componentResized( ComponentEvent ce ) {}
            @Override
            public void componentMoved( ComponentEvent ce ) {}
            @Override
            public void componentShown( ComponentEvent ce ) {}
            @Override
            public void componentHidden( ComponentEvent ce ) {}
        });
        mouse = new MouseMovement ();
        this.addMouseMotionListener ( mouse );
        this.addMouseListener( mouse );
    }
    
    public WaveModelPane ( final TYPE type ) {
        if ( type == TYPE.E ) this.setBorder( new border_E_wave() );
        else this.setBorder( new border_H_wave() );
        this.add( label );
    }
    
    public WaveModelPane ( final GraphicsModel model ) {
        this.model = model;
        if ( model.getWave().wave_type == TYPE.E )
            this.setBorder( new border_E_wave() );
        else
            this.setBorder( new border_H_wave() );
        model.set_texture_frame( MainWorkWindow.slider_frame.getValue() );
        model.renderPane = this;
    }
    
    public void setModel ( GraphicsModel model ) {        
        this.model = model;
        this.removeAll();
        System.out.println( "---pane-model-setTexture---" );
        model.set_texture_frame( MainWorkWindow.slider_frame.getValue() );
        model.renderPane = this;        
        this.removeAll();        
        model.indentRecalculation();  
        this.repaint();
    }
    public GraphicsModel getModel () {return this.model;}        
    
    @Override
    public void paintComponent( Graphics g ) {
        if ( model == null ) return;
        if ( model.get_buffer() == null ) return;
        Graphics2D gr = (Graphics2D) g.create();
        gr.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );
        gr.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
        
        gr.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED );
        gr.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED );
        gr.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE );
        gr.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF );
        gr.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE );        
        gr.scale( (double)(this.getWidth())/(double)(model.get_buffer().getWidth()), 
                  (double)(this.getHeight())/(double)(model.get_buffer().getHeight()) );
        gr.drawImage( model.get_buffer(), null, null );        
        //gr.dispose();
    }
}