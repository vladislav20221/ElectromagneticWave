package rectangularwaveguide;

import Charts.GraphicsModel;
import Computation.ElectromagneticWave;
import Data.FIELD;
import Settings.myColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import rectangularwaveguide.table.CellRender;

/**
 *
 * @author Vladislav
 */
public class ModelManager {
    private static final Path rootModel = Paths.get( "models\\" );
    private static final Path defaultModel = Paths.get( "models\\default" );
    private static final Path lastModel = Paths.get( "models\\last" );
    
    private static final DefaultTableModel tableModel = new DefaultTableModel (){
        @Override
        public boolean isCellEditable( int row, int column ) {return false;}
    };
    private static final JTable table = new JTable ( tableModel );
    
    private ModelManager(){}
    
    
    public static void showFileList ( MainWorkWindow window ) {
        JDialog dialog = new JDialog ( window, "Привте", true );
        dialog.getContentPane().setBackground( myColor.background );
        dialog.setLayout( new BorderLayout () );
        dialog.setSize( 700, 500 );
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets ( 10, 10, 10, 10 );
        grid.fill = GridBagConstraints.BOTH;
        grid.weightx = 1;
        grid.weighty = 1;
        JPanel menu = new JPanel ();
        menu.setOpaque( false );
        menu.setLayout( new BoxLayout( menu, BoxLayout.X_AXIS ) );
        
        JPanel pane = new JPanel ();
        pane.setOpaque( false );
        pane.setLayout( new GridBagLayout () );
        pane.setBorder( BorderFactory.createLineBorder( Color.yellow, 3 ) );
        tableModel.setColumnIdentifiers( new String []{"Изображение", "Частота (ГГц)", "Ширина (мм)", 
                                                       "Высота (мм)", "Мода-m", "Мода-n", "Кадров","Размер" } );        
        
        table.setOpaque( false );
        table.setShowVerticalLines( false );
        table.setShowVerticalLines( true );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        table.getColumnModel().getSelectionModel().setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
        tableModel.setRowCount( 0 );
        
        Enumeration<TableColumn> e = table.getColumnModel().getColumns();
        while ( e.hasMoreElements() ) {
            TableColumn column = (TableColumn)e.nextElement();
            column.setCellRenderer( new CellRender () );
            column.setMaxWidth( 90 );
            column.setMinWidth( 90 );
            column.setWidth( 90 );
            column.setPreferredWidth( 90 );
        }
        table.getColumnModel().getColumn( 4 ).setMaxWidth( 60 );
        table.getColumnModel().getColumn( 4 ).setMinWidth( 60 );
        table.getColumnModel().getColumn( 4 ).setPreferredWidth( 60 );
        table.getColumnModel().getColumn( 5 ).setMaxWidth( 60 );
        table.getColumnModel().getColumn( 5 ).setMinWidth( 60 );
        table.getColumnModel().getColumn( 5 ).setPreferredWidth( 60 );
        
                
        table.getColumnModel().getColumn( 0 ).setMaxWidth( 100 );
        table.getColumnModel().getColumn( 1 ).setWidth( 20 );
        table.getColumnModel().getColumn( 2 ).setWidth( 40 );
        table.getColumnModel().getColumn( 3 ).setWidth( 50 );
        table.getColumnModel().getColumn( 4 ).setWidth( 10 );
        table.getColumnModel().getColumn( 5 ).setWidth( 50 );
        
        table.getTableHeader().setResizingAllowed( false );
        table.getTableHeader().setReorderingAllowed( false );        
        table.getTableHeader().setForeground( myColor.graphics_background );
        table.getTableHeader().setBackground( myColor.color_short );
        
        //table.setAutoResizeMode( AutoResizeMode );
        
        JScrollPane scroll = new JScrollPane( table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        scroll.setOpaque( false );
        scroll.getViewport().setOpaque( false );
        scroll.setBorder( BorderFactory.createLineBorder( myColor.background, 2 ) );
        pane.add( scroll, grid );
        dialog.add( pane, BorderLayout.CENTER );
        
        
        for ( File file: rootModel.toFile().listFiles(  ) )
            if ( file.isFile() ) {
                String [] name = file.getName().replaceAll( "(waweModel=)|(.data)|(E9)|(E-3)|(F)|(a)|(b)|(m)|(n)|(frame)", "" ).replaceAll( "_", "," ).split("=");
                String [] list = new String [8];
                System.arraycopy( name, 0, list, 1, name.length );
                list[0] = "---";
                list[7] = String.format( "%.3f мб", (file.length()/1024f)/1000f);
                System.out.println( Arrays.toString( list ) );
                tableModel.addRow( list );
            }
       
        JButton select = new JButton ( "Загрузить" );
        select.addActionListener( (ac)->{            
            File [] filelist = rootModel.toFile().listFiles( new FileFilter() {
                @Override
                public boolean accept( File file ) {
                    return file.isFile();
                }            
            } );
            File selectFile = filelist[ table.getSelectedRow() ];
            Thread read = new Thread ( ()->{
                    MainWorkWindow.progress_bar.setIndeterminate(true);
                    ArrayList<GraphicsModel> modList = new ArrayList<>();
                    try ( FileInputStream fis = new FileInputStream( selectFile ) ) {
                        ObjectInputStream obIn = new ObjectInputStream ( fis );
                        Object model;
                        while ( ( model = obIn.readObject() ) != null ) {
                            //System.out.println( "считано" );
                            modList.add( (GraphicsModel) model );
                        }
                    } catch ( FileNotFoundException ex ) {
                        Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
                    } catch ( IOException | ClassNotFoundException ex ) {
                        MainWorkWindow.modelList = new GraphicsModel[4];
                        if ( modList.size() == 2 ) {
                            MainWorkWindow.pane_model_E.setModel( modList.get(0) );
                            MainWorkWindow.pane_model_H.setModel( modList.get(1) );
                            MainWorkWindow.modelList[0] = modList.get(0);
                            MainWorkWindow.modelList[1] = modList.get(1);
                        } else {                            
                            if ( MainWorkWindow.field == FIELD.TM ) {
                                MainWorkWindow.pane_model_E.setModel( modList.get(2) );
                                MainWorkWindow.pane_model_H.setModel( modList.get(3) );
                            } else {
                                MainWorkWindow.pane_model_E.setModel( modList.get(0) );
                                MainWorkWindow.pane_model_H.setModel( modList.get(1) );
                            }
                            MainWorkWindow.modelList[0] = modList.get(0);
                            MainWorkWindow.modelList[1] = modList.get(1);
                            MainWorkWindow.modelList[2] = modList.get(2);
                            MainWorkWindow.modelList[3] = modList.get(3);
                        }
                        MainWorkWindow.elec = MainWorkWindow.modelList[0].getWave().get_elec();
                        window.update_short_info();
                        window.update_pane_type();
                        MainWorkWindow.but_calculation.doClick();
                        MainWorkWindow.fps = MainWorkWindow.modelList[0].getWave().get_fps();
                        MainWorkWindow.count_texteru = MainWorkWindow.modelList[0].getWave().getTextureCount();
                        MainWorkWindow.size_texteru = MainWorkWindow.modelList[0].getWave().getTextureSize();
                        MainWorkWindow.slider_frame.setMaximum( MainWorkWindow.modelList[0].getWave().get_fps()-1 );
                    }
                    MainWorkWindow.progress_bar.setIndeterminate(false);
                } );
                read.start();
            //System.out.println( "Выбран файл: "+filelist[table.getSelectedRow()] );
        });
        menu.add( Box.createHorizontalGlue() );
        menu.add( select );        
        dialog.add( menu, BorderLayout.SOUTH );
        dialog.setVisible( true );
        //ElectromagneticWave elec = new ElectromagneticWave ( 11e9, 20e-3, 10e-3, (byte) 1,(byte) 0 );        
        //System.out.println( createFileName ( elec ) );
    }
    
    public static String createFileName ( ElectromagneticWave elec ) {
        String F = String.format( "F%.3fE9", elec.getF()*1e-9 ).replaceAll( "(\\.)|(,)", "_" );
        String a = String.format( "a%.3fE-3", elec.get_a()*1e3 ).replaceAll( "(\\.)|(,)", "_" );
        String b = String.format( "b%.3fE-3", elec.get_b()*1e3 ).replaceAll( "(\\.)|(,)", "_" );
        String m = String.format( "m%.0f", elec.get_m() );
        String n = String.format( "n%.0f", elec.get_n() );
        String fps = "frame"+elec.get_fps()+"";
        
        return rootModel.toFile().getPath()+"\\waweModel="+F+"="+a+"="+b+"="+m+"="+n+"="+fps+".data";
    }
}