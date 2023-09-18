package rectangularwaveguide;


import Charts.WaveLevelGraph;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/** Выводит простое окно с отрендеренными кадрами */
class showTextures extends JFrame {
    public showTextures ( WaveLevelGraph model ) {
        super ("Кадры");
        int width = 300;                 
        setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );
        setLayout ( new GridBagLayout () );
        GridBagConstraints grid = new GridBagConstraints();           
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.anchor = GridBagConstraints.WEST;    
        JLabel labelXY = new JLabel ( scalingImage (new ImageIcon ( model.getFrameXY( 0 ) ), width) );
            labelXY.setBorder ( BorderFactory.createTitledBorder("XY") );
        JLabel labelXZ = new JLabel ( scalingImage (new ImageIcon ( model.getFrameXZ( 0 ) ), width) );
            labelXZ.setBorder ( BorderFactory.createTitledBorder("XZ") );
        JLabel labelYZ = new JLabel ( scalingImage (new ImageIcon ( model.getFrameYZ( 0 ) ), width) );
            labelYZ.setBorder ( BorderFactory.createTitledBorder("YZ") );
        JSlider slider = new JSlider ( JSlider.HORIZONTAL, 0, model.get_fps()-1, 0 );
        JLabel label = new JLabel ("0");
            label.setForeground( Color.red );
            slider.addChangeListener( new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int a = slider.getValue();
                    labelXY.setIcon( scalingImage (new ImageIcon ( model.getFrameXY(a) ), width) );
                    labelXZ.setIcon( scalingImage (new ImageIcon ( model.getFrameXZ(a) ), width) );
                    labelYZ.setIcon( scalingImage (new ImageIcon ( model.getFrameYZ(a) ), width) );
                    label.setText( a+"" );
                    repaint();
                }
            });
            
        grid.gridx = 0; grid.gridy = 0;            
        add ( labelXY, GridBagConstraints.RELATIVE );            
        add ( labelXZ, GridBagConstraints.RELATIVE );            
        add ( labelYZ, GridBagConstraints.RELATIVE );
        grid.gridx = 0; grid.gridy = 1;
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.gridwidth = 2;
        add ( slider, grid );
        grid.gridx = 2; grid.gridy = 1;
        grid.fill = GridBagConstraints.NONE;
        add ( label, grid );
        pack();
        setVisible( true );
    }
    /**
     * Масшабирует переданное изображение с сохранением пропорций.
     * @param icon изображение.
     * @param x новая ширина.
     * @return новое изображение.
    */
    private ImageIcon scalingImage ( ImageIcon icon, int x ) {                        
        int oldHeight = icon.getIconHeight();             
        Image image = icon.getImage().getScaledInstance( x, (int) ( ( x * oldHeight) / icon.getIconWidth() ), Image.SCALE_SMOOTH );
        return new ImageIcon( image, icon.getDescription());
    }
}