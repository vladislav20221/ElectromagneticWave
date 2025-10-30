package rectangularwaveguide;

import Color.FILL;
import Charts.WaveLevelGraph;
import Charts.GraphicsModel;
import Charts.GUI;
import Computation.ElectromagneticWave;
import Settings.myColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartPanel;
import Data.FIELD;
import Data.TYPE;
import Exceptions.ElectromagneticException;
import Settings.myFont;
import Settings.mySize;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
/**
 * Главное рабочее окно программы
 * @author Vladislav
 */
public class MainWorkWindow extends JFrame implements myColor, myFont, mySize {    
    
    static { // Статический инициализирующий блок.
        //System.setProperty("file.encoding", "UTF-8");
        //System.out.println( "java.class.path: "+System.getProperty( "java.class.path" ) );
        //System.out.println( "java.home: "+System.getProperty( "java.home" ) );
        //System.out.println( "user.dir: "+System.getProperty( "user.dir" ) );
        System.out.println( "java.version: "+System.getProperty( "java.version" ) );
        //System.out.println( "java.encoding: "+System.getProperty( "file.encoding" ) );
    }
    // Файл сериализуемого объекта электромагнитной волны
    private static final File file = new File ( "files\\serialization\\ElectromagneticWavePrevious" );
        
//------------------------Нижняя строка меню------------------------------------
    private JPanel south_menu = new JPanel();               // Нижнее миню
    private JPanel center = new JPanel();
    private JMenuBar main_menu = new JMenuBar();            // главное меню
    private JPanel param_input = new JPanel();              // Панель ввода данных
    private JPanel short_menu = new JPanel();               // Панель вывода краткой информации
//------------------------------Центральная панель------------------------------
//----------------------------Главная панель -----------------------------------
    //private JTabbedPane main_tabbed = new JTabbedPane( JTabbedPane.TOP );    
    private WorkingTab pane_type = new WorkingTab();                // Панель определения дуступных типов волн
    private WorkingTab pane_axis_projection = new WorkingTab();     // Панель с проекциями на оси
    
    private final showCalculationsFrame calculat_frame;     // Оно с расчётами
//------------------------Прогрес бар для приложения----------------------------
    public static JProgressBar progress_bar = new JProgressBar ();
    public static DefaultBoundedRangeModel progress_model = new DefaultBoundedRangeModel();
//---------- Параметры влияющие на производительность и отрисовку --------------
    public static int fps = 25;                             // Количество кадров в секунды для анимации.
    public static int size_texteru = 200;                   // Размер текстур.
    public static int count_texteru = 400;                  // Определяет количество точек по оси x.
    public static double l = 2d;                            // Определяет длину стеники волновода - Lv*l.
    public static int speed = 2;                           // Влияет на скорость анимации.
    public static FILL fill = FILL.Gradient;                // Вид заливки цветом.    
    public static FIELD field = FIELD.TE;                   // Тип волны H - TE...E - TM/
    
//------------Экземпляры классов необходимые для работы-------------------------    
    public static ElectromagneticWave elec;                       // Объект электромагнитного поля
//--------Класс для подоготовки текстур плотности электромагнитного поля--------
    private GUI gui;                                        // Объект для построения графиков    
//--------------------- Сериализация -------------------------------------------
    private static ObjectOutputStream objectOut;
    private static ObjectInputStream objectIn;
    
    // Две основные панели отображения модели волновода. E волны и H волны.
    public static WaveModelPane pane_model_E;
    public static WaveModelPane pane_model_H;
        
    // Ползунок прокрутки кадров
    public static JSlider slider_frame;
    private static JLabel frame_info = new JLabel ();
    
    public static void main ( String ... args ) {
        System.gc();
        try {
            FileInputStream streamIn = new FileInputStream ( file );
            objectIn = new ObjectInputStream ( streamIn );            
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch ( IOException ex ) {
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(()-> new MainWorkWindow ());
    }
        
    /** Главное рабоочее окно */
    public MainWorkWindow () {
        super("Электромагнитные волны");        
        long time1 = System.currentTimeMillis();
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );        
        setSize( MAIN_WINDOW );
        setMinimumSize( MAIN_WINDOW_MIN );   // Минимальный размер окна
        this.addWindowListener( new WindowAdapter () {
            @Override
            public void windowClosing( WindowEvent we ) {
                try {                    
                    objectOut.writeObject( elec );
                    objectOut.close();
                    objectIn.close();
                } catch ( IOException ex ) {
                    Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        try {                        
            elec = ( ElectromagneticWave ) objectIn.readObject();
            elec.isCorrect();
        } catch ( IOException | ClassNotFoundException | ElectromagneticException ex ) {
            elec = new ElectromagneticWave ( 11e9, 20e-3, 10e-3, (byte) 1,(byte) 0 );
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog( center, new String [] {"Ранее были введены не коректные данные для ","прямоугольного волновода. "}, "Сообщение", JOptionPane.INFORMATION_MESSAGE );
        } finally {            
            try {
                FileOutputStream streamOut = new FileOutputStream ( file, false );
                objectOut = new ObjectOutputStream ( streamOut );                
                WaveLevelGraph.setting_textures();
            } catch ( FileNotFoundException ex ) {
                Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        gui = new GUI (elec);        
        calculat_frame = new showCalculationsFrame( elec );
        settingPanel();             // базовая настройка панелей и окна
        setIconImage( new ImageIcon("files\\interface\\icon.png").getImage() );
        long time2 = System.currentTimeMillis();
        progress_bar.setString( "Интерфейс построен за: " + (time2-time1) + " мс" );
        setVisible( true );        
    }
    /** Базовая настройка всех панелей интерфейса */
    private final void settingPanel () {
        setLayout( new BorderLayout () );
        setting_main_menu();         // Настройка верхнего меню
        setting_param_input();      // Настройка панели вывод информации
        setting_short_menu();       // настройка панели вывода короткой информации
        setting_pane_type();        // настройка панили выбора типов волн
        setting_axis_projection();  // настрока панели проекций на оси
        // Панель нижнего меню. Расплагается на south.
        south_menu.setLayout( new BoxLayout( south_menu, BoxLayout.Y_AXIS ) );
        south_menu.setName("south_menu");
        south_menu.add( short_menu );
        south_menu.add( param_input );
        short_menu.setName("short_menu");
        param_input.setName("param_input");        
        center.setName("center");
        center.setLayout( new BoxLayout( center, BoxLayout.X_AXIS ) );
        center.add( pane_axis_projection );
        add( south_menu, BorderLayout.SOUTH );      // Нижняя паняль        
        add( center, BorderLayout.CENTER );    // Центральная панель
        setJMenuBar( main_menu );                    // Установка главного меню
    }
//====================Настройка нижнего меню====================================
    /**Настройа верхнего меню*/
    private final void setting_main_menu () {        
        main_menu.setName("main_menu");
        main_menu.setBackground( color_short );         
        main_menu.setPreferredSize( MENU );
        Dimension progressBarSize = new Dimension ( 470, 22 );
        
        JMenuItem but_0 = new JMenuItem ("Проекции на оси");
            but_0.setBackground( color_short );
            but_0.setForeground( Color.white );
            but_0.setBorder( BorderFactory.createLineBorder( color_back_font, 1 ) );
            but_0.addActionListener( (e)->{
                center.removeAll();
                center.add( pane_axis_projection );
                center.revalidate();
                center.repaint();
            });
        JMenuItem but_1 = new JMenuItem ("Доступные моды");
            but_1.setBackground( color_short );
            but_1.setForeground( Color.white );
            but_1.setBorder( BorderFactory.createLineBorder( color_back_font, 1 ) );
            but_1.addActionListener( (e)->{
                if ( but_animation.isSelected() ) but_animation.doClick();
                center.removeAll();
                center.add( pane_type );
                center.revalidate();
                center.repaint();
            });        
        JButton show = new JButton ();
            show.setAction( new show_calculations() );
            show.setBorderPainted( false );
            show.setFocusPainted( false );
            show.setContentAreaFilled( false );
                    
        ImageIcon image = new ImageIcon("files\\interface\\texture.png");            
        JButton show_texture = new JButton( image );
            show_texture.setBorderPainted( false );
            show_texture.setFocusPainted( false );
            show_texture.setContentAreaFilled( false );
            show_texture.addActionListener( new ActionListener () {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if ( pane_model_E.getModel() == null || pane_model_H.getModel() == null ) {
                        JOptionPane.showMessageDialog( show_texture, "Текстуры ещё не были расчитаны !", "Ошибка", JOptionPane.WARNING_MESSAGE );
                        return;
                    }                        
                    new showTextures ( pane_model_E.getModel().getWave() );
                    new showTextures ( pane_model_H.getModel().getWave() );
                }                
            });
        // Настройка прогресс бара
        progress_bar.setStringPainted( true );
        progress_bar.setString( "Процессов нет" );
        progress_bar.setModel( progress_model );
        progress_bar.setPreferredSize( progressBarSize );
        progress_bar.setMaximumSize( progressBarSize );
        
        main_menu.setLayout( new BoxLayout( main_menu, BoxLayout.X_AXIS ) );

        main_menu.add( but_0 );
        main_menu.add( but_1 );
        main_menu.add( Box.createHorizontalGlue() );
        main_menu.add( progress_bar );
        main_menu.add( Box.createHorizontalGlue() );
        main_menu.add( show_texture );
        main_menu.add( Box.createHorizontalStrut( 5 ) );
        main_menu.add( show );
        main_menu.add( Box.createHorizontalStrut( 5 ) );
    }
    public static JPanel pane_info;
    public static JButton but_calculation;
    
    /** Настраивает панель ввода и вывода основных данных */
    private final void setting_param_input () {
        int lengthF = 5;    // длина Начальной частоты
        int length = 3; // длина текстового поля
        // Размер элементов вводимых данный
        Dimension size_input = new Dimension( 42, 15 );
        Dimension size_unit = new Dimension( 25, 20 );
        Dimension size_show = new Dimension( 80, 20 );
        Dimension size_pane_info = new Dimension ( 580, 235 );
        Dimension size_waveguide_label = new Dimension ( 350, 230 );        
        Dimension size_error = new Dimension ( 40, 40 );
        
        pane_info = new JPanel ();
            pane_info.setOpaque( false );
            pane_info.setLayout( new GridBagLayout ());
            pane_info.setPreferredSize( size_pane_info );
            pane_info.setMaximumSize( size_pane_info );
        param_input.setLayout( new BoxLayout( param_input, BoxLayout.X_AXIS ) ); 
        param_input.setBackground( background );
        //param_input.setOpaque( true );
//===============Настройка графических компонентов==============================
        // Единицы измерения
        JLabel lab_F_unit = new JLabel ("ГГц");
            lab_F_unit.setFont(font_input);
            lab_F_unit.setBackground(color_inputFont);
            lab_F_unit.setName( "lab_F_unit" );
            lab_F_unit.setPreferredSize( size_unit );            
        JLabel lab_a_unit = new JLabel ("мм");
            lab_a_unit.setFont(font_input);
            lab_a_unit.setBackground(color_inputFont);
            lab_a_unit.setName( "lab_a_unit" );
            lab_a_unit.setPreferredSize( size_unit );            
        JLabel lab_b_unit = new JLabel ("мм");
            lab_b_unit.setFont(font_input);
            lab_b_unit.setBackground(color_inputFont);
            lab_b_unit.setName( "lab_b_unit" );
            lab_b_unit.setPreferredSize( size_unit );     
        JLabel lab_l_unit = new JLabel ("мм");
            lab_l_unit.setFont(font_input);
            lab_l_unit.setBackground(color_inputFont);
            lab_l_unit.setName( "lab_l_unit" );
            lab_l_unit.setPreferredSize( size_unit );
        // Вводимые параметры
        JLabel lab_F = new JLabel ("Частота:");
            lab_F.setFont(mainFont);
            lab_F.setBackground(color_mainFont);
            lab_F.setName( "lab_F" );
            lab_F.setPreferredSize( size_show );            
            //lab_F.setMinimumSize( size_show );
        JTextArea text_F = new JTextArea( 0, lengthF ){
            @Override
            public void processKeyEvent ( KeyEvent e ) {
                Pattern pat = Pattern.compile ( "([0-9]{1,3}[.]{1}[0-9]{0,2})|([0-9]{0,2})" );
                Matcher mat = pat.matcher( this.getText() );   
                if ( e.getKeyChar() >='0' && e.getKeyChar()<='9'
                     && mat.matches()
                     || e.getKeyChar() == '.' || e.getKeyChar() == ','
                     || e.getKeyCode() == KeyEvent.VK_DELETE
                     || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                     || e.getKeyCode() == KeyEvent.VK_LEFT
                     || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    if ( e.getKeyChar() == ',' )
                        e.setKeyChar('.');
                    super.processKeyEvent(e);
                }
            }        
        };
            text_F.setFont( font_input );
            text_F.setText( String.format( "%.3f", elec.getF()*1e-9 ).replaceAll(",",".")  );
            text_F.setName( "text_F" );
            text_F.setPreferredSize( size_input );
            text_F.setMinimumSize( size_input );                        
            text_F.addFocusListener( new FocusListener () {
                @Override
                public void focusGained( FocusEvent fe ) {}
                @Override
                public void focusLost( FocusEvent fe ) {
                    if ( text_F.getText().equals("") ) {
                        JOptionPane.showMessageDialog( text_F, "Поле частоты нужно заполнить !", "Вазможная ошибка", JOptionPane.INFORMATION_MESSAGE );
                        text_F.setText( String.format( "%.3f", elec.getF()*1e-9 ).replaceAll(",",".") );
                    }
                }                
            });
            
        JLabel lab_a = new JLabel ("Ширина a:");
            lab_a.setFont(mainFont);
            lab_a.setBackground(color_mainFont);
            lab_a.setPreferredSize( size_show );
        JTextArea text_a = new JTextArea (0,length){
            @Override
            public void processKeyEvent ( KeyEvent e ) {
                Pattern pat = Pattern.compile ( "([0-9]{1,2}[.]{1}[0-9]{0,2})|([0-9]{0,1})" );
                Matcher mat = pat.matcher( this.getText() );   
                if ( e.getKeyChar() >='0' && e.getKeyChar()<='9'
                     && mat.matches()
                     || e.getKeyChar() == '.' || e.getKeyChar() == ','
                     || e.getKeyCode() == KeyEvent.VK_DELETE
                     || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                     || e.getKeyCode() == KeyEvent.VK_LEFT
                     || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    if ( e.getKeyChar() == ',' )
                        e.setKeyChar('.');
                    //System.out.println( e.getKeyChar() );
                    super.processKeyEvent(e);
                }
            }        
        };
            text_a.setFont( font_input );
            text_a.setText( Double.toString( elec.get_a()*1e3 ) );
            text_a.setName( "text_a" );
            text_a.setPreferredSize( size_input );
            text_a.setMinimumSize( size_input );
            text_a.addFocusListener( new FocusListener () {
                @Override
                public void focusGained( FocusEvent fe ) {}
                @Override
                public void focusLost( FocusEvent fe ) {
                    if ( text_a.getText().equals("") ) {
                        JOptionPane.showMessageDialog( text_a, "Не указана ширина (a) !", "Вазможная ошибка", JOptionPane.INFORMATION_MESSAGE );
                        text_a.setText( Double.toString( elec.get_a()*1000 ) );
                    }                   
                }                
            });
            
        JLabel lab_b = new JLabel ("Высота b:");
            lab_b.setFont(mainFont);
            lab_b.setBackground(color_mainFont);
            lab_b.setPreferredSize( size_show );
        JTextArea text_b = new JTextArea (0,length){
            @Override
            public void processKeyEvent ( KeyEvent e ) {
                Pattern pat = Pattern.compile ( "([0-9]{1,2}[.]{1}[0-9]{0,2})|([0-9]{0,1})" );
                Matcher mat = pat.matcher( this.getText() );   
                if ( e.getKeyChar() >='0' && e.getKeyChar()<='9'
                     && mat.matches()
                     || e.getKeyChar() == '.' || e.getKeyChar() == ','
                     || e.getKeyCode() == KeyEvent.VK_DELETE
                     || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                     || e.getKeyCode() == KeyEvent.VK_LEFT
                     || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    if ( e.getKeyChar() == ',' )
                        e.setKeyChar('.');
                    //System.out.println( e.getKeyChar() );
                    super.processKeyEvent(e);
                }
            }        
        };
            text_b.setFont( font_input );
            text_b.setText( Double.toString( elec.get_b()*1e3 ) );
            text_b.setName( "text_b" );
            text_b.setPreferredSize( size_input );
            text_b.setMinimumSize( size_input );
            text_b.addFocusListener( new FocusListener () {
                @Override
                public void focusGained( FocusEvent fe ) {}
                @Override
                public void focusLost( FocusEvent fe ) {
                    if ( text_b.getText().equals("") ) {
                        JOptionPane.showMessageDialog( text_b, "Не указана высота (b) !", "Вазможная ошибка", JOptionPane.INFORMATION_MESSAGE );
                        text_b.setText( Double.toString( elec.get_b()*1000 ) );
                    }
                }                
            });
        JLabel lab_l = new JLabel ("Длина l:");
            lab_l.setFont(mainFont);
            lab_l.setBackground(color_mainFont);
            lab_l.setPreferredSize( size_show );
        JLabel lab_l_value = new JLabel ( String.format( "%.3f", elec.get_l()*1e3 ) );
            lab_l_value.setName( "lab_l_value" );
            lab_l_value.setForeground( Color.white );
            lab_l_value.setPreferredSize( new Dimension ( size_show.width-20, size_show.height ) );
            lab_l_value.setMaximumSize( new Dimension ( size_show.width-20, size_show.height ) );
            lab_l_value.setMinimumSize( new Dimension ( size_show.width-20, size_show.height ) );
        JComboBox combobox_l = new JComboBox ( new String []{"1*Lv","2*Lv","3*Lv","4*Lv","5*Lv","7*Lv","8*Lv"} );
            combobox_l.setFont( font_input );
            combobox_l.setName( "combobox_l" );
        Dimension size_combobox = new Dimension ( size_input.width+10, size_input.height+5 );
            combobox_l.setPreferredSize( size_combobox );
            combobox_l.setMinimumSize( size_combobox );
            combobox_l.setEditable( false );
            combobox_l.addItemListener( (ie)->l = Double.valueOf(ie.getItem().toString().replaceAll("\\*Lv","")) );
            combobox_l.setSelectedItem( "2*Lv" );
            combobox_l.addItemListener( (e)->{ 
                int a = Integer.valueOf( e.getItem().toString().replaceAll( "\\*Lv", "" ) );
                lab_l_value.setText( String.format( "%.3f", (elec.getLv()*a)*1e3 ) );                 
            });
            
        JLabel lab_m = new JLabel ("Мода m:");
            lab_m.setFont( mainFont );
            lab_m.setBackground( color_mainFont );
            lab_m.setPreferredSize( size_show );           
        JTextArea text_m = new JTextArea (0,length){
            @Override
            public void processKeyEvent ( KeyEvent e ) {
                Pattern pat = Pattern.compile ( "[0-9]{0,1}" );
                Matcher mat = pat.matcher( this.getText() );   
                if ( e.getKeyChar() >='0' && e.getKeyChar()<='9'
                     && mat.matches()
                     || e.getKeyCode() == KeyEvent.VK_DELETE
                     || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                     || e.getKeyCode() == KeyEvent.VK_LEFT
                     || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    //System.out.println( e.getKeyChar() );
                    super.processKeyEvent(e);
                }
            }        
        };
            text_m.setFont( font_input );
            text_m.setText( Double.toString( elec.get_m() ).replaceAll( ".0", "" ) );
            text_m.setName( "text_m" );
            text_m.setPreferredSize( size_input );
            text_m.setMinimumSize( size_input );
            text_m.addFocusListener( new FocusListener () {
                @Override
                public void focusGained( FocusEvent fe ) {}
                @Override
                public void focusLost( FocusEvent fe ) {
                    if ( text_m.getText().equals("") ) {
                        JOptionPane.showMessageDialog( text_m, "Не указана мода m !", "Вазможная ошибка", JOptionPane.INFORMATION_MESSAGE );
                        text_m.setText( Double.toString( elec.get_m() ).replaceAll( ".0", "" ) );
                    }                      
                }                
            });
            
        JLabel lab_n = new JLabel ("Мода n:");
            lab_n.setFont( mainFont );
            lab_n.setBackground( color_mainFont );
            lab_n.setPreferredSize( size_show );
        JTextArea text_n = new JTextArea (0,length){
            @Override
            public void processKeyEvent ( KeyEvent e ) {
                Pattern pat = Pattern.compile ( "[0-9]{0,1}" );
                Matcher mat = pat.matcher( this.getText() );   
                if ( e.getKeyChar() >='0' && e.getKeyChar()<='9'
                     && mat.matches()
                     || e.getKeyCode() == KeyEvent.VK_DELETE
                     || e.getKeyCode() == KeyEvent.VK_BACK_SPACE
                     || e.getKeyCode() == KeyEvent.VK_LEFT
                     || e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                    //System.out.println( e.getKeyChar() );
                    super.processKeyEvent(e);
                }
            }        
        };       
            text_n.setFont( font_input );
            text_n.setText( Double.toString( elec.get_n() ).replaceAll( ".0", "" ) );
            text_n.setName( "text_n" );
            text_n.setPreferredSize( size_input );
            text_n.setMinimumSize( size_input );
            text_n.addFocusListener( new FocusListener () {
                @Override
                public void focusGained( FocusEvent fe ) {}
                @Override
                public void focusLost( FocusEvent fe ) {
                    if ( text_n.getText().equals("") ) {
                        JOptionPane.showMessageDialog( text_n, "Не указана мода n !", "Вазможная ошибка", JOptionPane.INFORMATION_MESSAGE );
                        text_n.setText( Double.toString( elec.get_n() ).replaceAll( ".0", "" ) );
                    }
                }                
            });            
        // Сообщение об ошибке
        JLabel lab_error = new JLabel ();
            lab_error.setFont(mainFont);
            lab_error.setForeground( Color.red );
            lab_error.setBackground( color_mainFont );
            lab_error.setName( "lab_error" );            
            lab_error.setPreferredSize( size_error );
            lab_error.setMinimumSize( size_error );
            
        JLabel rectangular_waveguide = new JLabel ( scalingImage( new ImageIcon("files\\interface\\rectangular_waveguide.png"), 320));                
            rectangular_waveguide.setAlignmentX( JComponent.CENTER_ALIGNMENT );
            rectangular_waveguide.setAlignmentY( JComponent.CENTER_ALIGNMENT );
            rectangular_waveguide.setPreferredSize( size_waveguide_label );
            rectangular_waveguide.setMaximumSize( size_waveguide_label );
            rectangular_waveguide.setMinimumSize( size_waveguide_label );
        but_calculation = new JButton ("Расчитать");
            but_calculation.setName( "Расчитать" );
            but_calculation.setBackground( color_short );
            but_calculation.setForeground( Color.white );
            but_calculation.addActionListener( (e)-> calculation_wawe() );
            
        GridBagConstraints grid = new GridBagConstraints();
        // расположение компонентов на панели     
        // Панель вывода расчётных данные
            grid.gridx=0; grid.gridy=0;    
            grid.fill = GridBagConstraints.BOTH;
            grid.gridheight = 1;
            grid.weightx = 1;
            grid.weighty = 1;
            grid.gridwidth = GridBagConstraints.REMAINDER;
        pane_info.add( get_pane_calculate_data(), grid );
        // Вводимые данные        
            grid.weightx = 0;
            grid.weighty = 0;
            grid.gridheight = 1;
            grid.gridwidth = 1;
            grid.insets = new Insets ( 5, 10, 5, 5 );            
            grid.anchor = GridBagConstraints.WEST;  
            grid.fill = GridBagConstraints.NONE;
            grid.gridx=0;   grid.gridy=1;
        pane_info.add( lab_F, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=1;   grid.gridy=1;
        pane_info.add( text_F, grid );
            grid.gridx=2;   grid.gridy=1;
        pane_info.add( lab_F_unit, grid );
        // Размеры волновода
            grid.insets = new Insets ( 5, 10, 5, 5 );
            grid.gridx=0;   grid.gridy=2;
        pane_info.add( lab_a, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=1;   grid.gridy=2;
        pane_info.add( text_a, grid );
            grid.gridx=2;   grid.gridy=2;
        pane_info.add( lab_a_unit, grid );
            grid.insets = new Insets ( 5, 10, 5, 5 );
            grid.gridx=3;   grid.gridy=2;
        pane_info.add( lab_b, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=4;   grid.gridy=2;
        pane_info.add( text_b, grid );
            grid.gridx=5;   grid.gridy=2;
        pane_info.add( lab_b_unit    , grid );        
            grid.insets = new Insets ( 5, 10, 5, 5 );
            grid.gridx=6;   grid.gridy=2;
        pane_info.add( lab_l, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=7;   grid.gridy=2;
        pane_info.add( combobox_l, grid );
            grid.gridx=8;   grid.gridy=2;
        pane_info.add( lab_l_value, grid );    
            grid.gridx=9;   grid.gridy=2;
        pane_info.add( lab_l_unit, grid );
        // Моды
            grid.insets = new Insets ( 5, 10, 5, 5 );
            grid.gridx=0;   grid.gridy=3;
        pane_info.add( lab_m, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=1;   grid.gridy=3;
        pane_info.add( text_m, grid );
            grid.insets = new Insets ( 5, 10, 5, 5 );
            grid.gridx=3;   grid.gridy=3;                
        pane_info.add( lab_n, grid );
            grid.insets = new Insets ( 5, 5, 5, 5 );
            grid.gridx=4;   grid.gridy=3;
        pane_info.add( text_n, grid );            
        // Кнопки
            grid.gridx=7;   grid.gridy=4;
            grid.gridwidth = 2;
        pane_info.add( but_calculation, grid );
        // Сообщение об ошибке
            grid.gridwidth = 1;
            grid.gridx=5;   grid.gridy=4;
            grid.insets = new Insets ( 5, 5, 5, 5 );            
        pane_info.add( lab_error, grid );
        
        //rectangular_waveguide.setBorder( BorderFactory.createLineBorder( Color.red, 2 ) );
        //lab_error.setBorder( BorderFactory.createLineBorder( Color.pink, 2) );
        //pane_info.setBorder( BorderFactory.createLineBorder( Color.yellow, 2 ) );
        // Изображения        
        param_input.add( pane_info );
        param_input.add( Box.createHorizontalGlue() );
        param_input.add( rectangular_waveguide );
        param_input.add( Box.createHorizontalStrut( 15 ) );
    }
    public final JPanel get_pane_calculate_data () {
        //-----------------------Вычисляемые данные-----------------------------            
        String text = getText();
        
        JPanel pane_calculate_data = new JPanel ();
            pane_calculate_data.setBackground( myColor.color_short );
            pane_calculate_data.setLayout( new GridBagLayout () );
            pane_calculate_data.setBackground( color_short );
            pane_calculate_data.setName( "pane_calculate_data" );
            pane_calculate_data.setBorder( BorderFactory.createLineBorder( myColor.color_back_font, 2 ) );
            
        JLabel lab_Fkr = new JLabel ("Критическая частота "+text );
            lab_Fkr.setFont(mainFont);
            lab_Fkr.setBackground(color_mainFont);    
            lab_Fkr.setForeground( Color.white );
            lab_Fkr.setName( "lab_Fkr" );
        JLabel lab_Fkr_info = new JLabel ();
            lab_Fkr_info.setFont( mainFont );
            lab_Fkr_info.setForeground( Color.white );
            lab_Fkr_info.setText( String.format( "%.3f", elec.getFkr()*1e-9 ) );
            lab_Fkr_info.setName( "lab_Fkr_info" );
        JLabel lab_Fkr_unit = new JLabel ("ГГц");
            lab_Fkr_unit.setFont(mainFont);
            lab_Fkr_unit.setBackground(color_mainFont);
            lab_Fkr_unit.setForeground( Color.white );
        JLabel lab_Lkr = new JLabel ("Критическая длина волны "+text );
            lab_Lkr.setFont(mainFont);
            lab_Lkr.setBackground(color_mainFont);
            lab_Lkr.setForeground( Color.white );
            lab_Lkr.setName( "lab_Lkr" );
        JLabel lab_Lkr_info = new JLabel ();
            lab_Lkr_info.setFont( mainFont );            
            lab_Lkr_info.setForeground( Color.white );
            lab_Lkr_info.setText( String.format( "%.3f", elec.getLkr()*1e2 ) );
            lab_Lkr_info.setName( "lab_Lkr_info" );
        JLabel lab_Lkr_unit = new JLabel ("см");
            lab_Lkr_unit.setFont(mainFont);
            lab_Lkr_unit.setBackground(color_mainFont);
            lab_Lkr_unit.setForeground( Color.white );
            
        JLabel lab_Fkr_H10 = new JLabel ( "Критическая частота H10:" );
            lab_Fkr_H10.setFont(mainFont);
            lab_Fkr_H10.setBackground(color_mainFont);    
            lab_Fkr_H10.setForeground( Color.white );
        JLabel lab_Fkr_info_H10 = new JLabel ();
            lab_Fkr_info_H10.setFont( mainFont );
            lab_Fkr_info_H10.setForeground( Color.white );
            lab_Fkr_info_H10.setText( String.format( "%.3f", elec.getFkrH10()*1e-9 ) );
            lab_Fkr_info_H10.setName( "lab_Fkr_info_H10" );
        JLabel lab_Fkr_unit_H10 = new JLabel ("ГГц");
            lab_Fkr_unit_H10.setFont(mainFont);
            lab_Fkr_unit_H10.setBackground(color_mainFont);
            lab_Fkr_unit_H10.setForeground( Color.white );
        JLabel lab_Lkr_H10 = new JLabel ( "Критическая длина волны H10:" );
            lab_Lkr_H10.setFont(mainFont);
            lab_Lkr_H10.setBackground(color_mainFont);
            lab_Lkr_H10.setForeground( Color.white );
        JLabel lab_Lkr_info_H10 = new JLabel ();
            lab_Lkr_info_H10.setFont( mainFont );            
            lab_Lkr_info_H10.setForeground( Color.white );
            lab_Lkr_info_H10.setText( String.format( "%.3f", elec.getLkrH10()*1e2 ) );
            lab_Lkr_info_H10.setName( "lab_Lkr_info_H10" );
        JLabel lab_Lkr_unit_H10 = new JLabel ("см");
            lab_Lkr_unit_H10.setFont(mainFont);
            lab_Lkr_unit_H10.setBackground(color_mainFont);
            lab_Lkr_unit_H10.setForeground( Color.white );
        
        JLabel lab_Lv = new JLabel ( "Длина вовлны в волноводе (Lv) "+text );
            lab_Lv.setName( "lab_Lv" );
            lab_Lv.setFont(mainFont);
            lab_Lv.setBackground(color_mainFont);
            lab_Lv.setForeground( Color.white );
        JLabel lab_Lv_info = new JLabel ();
            lab_Lv_info.setFont( mainFont );            
            lab_Lv_info.setForeground( Color.white );
            lab_Lv_info.setText( String.format( "%.3f", elec.getLv()*1e2 ) );
            lab_Lv_info.setName( "lab_Lv_info" );
        JLabel lab_Lv_unit = new JLabel ("см");
            lab_Lv_unit.setFont(mainFont);
            lab_Lv_unit.setBackground(color_mainFont);
            lab_Lv_unit.setForeground( Color.white );                    
            
            
        JPanel pane_both = new JPanel ();
            pane_both.setOpaque( false );
            //pane_both.setBorder( BorderFactory.createLineBorder( Color.red, 2 ) );
        GridBagConstraints grid = new GridBagConstraints();
            grid.anchor = GridBagConstraints.WEST;
            grid.insets = new Insets ( 1, 6, 1, 1 );
            
            grid.gridx = 0; grid.gridy = 0;
        pane_calculate_data.add( lab_Fkr, grid );
            grid.gridx = 1;
        pane_calculate_data.add( lab_Fkr_info, grid );
            grid.gridx = 2;
        pane_calculate_data.add( lab_Fkr_unit, grid );
            grid.gridx = 0; grid.gridy = 1;
        pane_calculate_data.add( lab_Lkr, grid );
            grid.gridx = 1; 
        pane_calculate_data.add( lab_Lkr_info, grid );
            grid.gridx = 2;
        pane_calculate_data.add( lab_Lkr_unit, grid );
            grid.gridx = 0; grid.gridy = 2;
        pane_calculate_data.add( lab_Fkr_H10, grid );
            grid.gridx = 1; 
        pane_calculate_data.add( lab_Fkr_info_H10, grid );
            grid.gridx = 2; 
        pane_calculate_data.add( lab_Fkr_unit_H10, grid );
            grid.gridx = 0; grid.gridy = 3;
        pane_calculate_data.add( lab_Lkr_H10, grid );
            grid.gridx = 1; 
        pane_calculate_data.add( lab_Lkr_info_H10, grid );
            grid.gridx = 2; 
        pane_calculate_data.add( lab_Lkr_unit_H10, grid );
            grid.gridx = 0; grid.gridy = 4;
        pane_calculate_data.add( lab_Lv, grid );
            grid.gridx = 1; 
        pane_calculate_data.add( lab_Lv_info, grid );
            grid.gridx = 2; 
        pane_calculate_data.add( lab_Lv_unit, grid );
        
            grid.fill = GridBagConstraints.BOTH;
            grid.gridx = 3; grid.gridy = 20;
            grid.weightx = 1; grid.weighty = 1;
        pane_calculate_data.add( pane_both, grid );
        
        return pane_calculate_data;
    }
    
    private String getText () {
        return "H(E)"+(int)elec.get_m()+(int)elec.get_n()+": ";
    }
    
    /**
     * Вычисляет параметры выолны
     */
    public final void calculation_wawe () {
        JPanel pane_calculate_data = (JPanel) getElement ( pane_info, "pane_calculate_data" );        
        
        JTextArea text_F = (JTextArea) getElement ( pane_info, "text_F" );
        JTextArea text_a = (JTextArea) getElement ( pane_info, "text_a" );
        JTextArea text_b = (JTextArea) getElement ( pane_info, "text_b" );
        JTextArea text_m = (JTextArea) getElement ( pane_info, "text_m" );
        JTextArea text_n = (JTextArea) getElement ( pane_info, "text_n" );
        JLabel lab_l = (JLabel) getElement ( pane_info, "lab_l_value" );
        JLabel lab_Fkr_info = (JLabel) getElement ( pane_calculate_data, "lab_Fkr_info" );
        JLabel lab_Lkr_info = (JLabel) getElement ( pane_calculate_data, "lab_Lkr_info" );
        JLabel lab_Lv_info = (JLabel) getElement ( pane_calculate_data, "lab_Lv_info" );
        JLabel lab_Fkr = (JLabel) getElement ( pane_calculate_data, "lab_Fkr" );
        JLabel lab_Lkr = (JLabel) getElement ( pane_calculate_data, "lab_Lkr" );
        JLabel lab_lv = (JLabel) getElement ( pane_calculate_data, "lab_Lv" );
        
        JLabel lab_Fkr_info_H10 = (JLabel) getElement ( pane_calculate_data, "lab_Fkr_info_H10" );
        JLabel lab_Lkr_info_H10 = (JLabel) getElement ( pane_calculate_data, "lab_Lkr_info_H10" );
        JLabel lab_error = (JLabel) getElement ( pane_info, "lab_error" );
        // Сбрасывает рамку
        text_F.setBorder( null );
        text_a.setBorder( null );
        text_b.setBorder( null );
        text_m.setBorder( null );
        text_n.setBorder( null );
        
        try {                        
            elec = new ElectromagneticWave ( Double.valueOf( text_F.getText())*1e9,
                                             Double.valueOf( text_a.getText())*1e-3,
                                             Double.valueOf( text_b.getText())*1e-3,
                                             Byte.valueOf( text_m.getText() ),
                                             Byte.valueOf( text_n.getText() ));
            // Оповещение пользователя
            String str = getText();
            lab_Fkr_info.setText( String.format( "%.3f", elec.getFkr()*1e-9 ) );
            lab_Lkr_info.setText( String.format( "%.3f", elec.getLkr()*1e2 ) );
            lab_Lv_info.setText( String.format( "%.3f", elec.getLv()*1e2 ) );
            lab_l.setText( String.format( "%.3f", elec.get_l()*1e3 ) );
            lab_Fkr.setText( "Критическая частота "+str );
            lab_Lkr.setText( "Критическая длина волны "+str );
            lab_lv.setText( "Длина вовлны в волноводе (Lv) "+str );
            
            lab_Fkr_info_H10.setText( String.format( "%.3f", elec.getFkrH10()*1e-9 ) );
            lab_Lkr_info_H10.setText( String.format( "%.3f", elec.getLkrH10()*1e2 ) );
            
            lab_error.setForeground( Color.green );
            lab_error.setIcon( new ImageIcon ("files\\interface\\correct.png") );
            lab_error.setText( "Готово !" );
                    
            calculat_frame.update();                        // обновляет информацию в окне расчётов            
            update_pane_type();                             // Обновляет информацию на панели типа волны
            // Проверка на ошибки
            elec.isCorrect();
        } catch ( ElectromagneticException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            // Оповещение пользователя
            lab_Fkr_info.setText( String.format( "%.3f", elec.getFkr()*1e-9 ) );                    
            lab_Lkr_info.setText( String.format( "%.3f", elec.getLkr()*1e2 ) );
            lab_Fkr_info_H10.setText( String.format( "%.3f", elec.getFkrH10()*1e-9 ) );
            lab_Lkr_info_H10.setText( String.format( "%.3f", elec.getLkrH10()*1e2 ) );        
            
            lab_error.setForeground( Color.red );
            lab_error.setIcon( new ImageIcon ("files\\interface\\error.png") );
            lab_error.setText( ex.getMessage() );
            JOptionPane.showMessageDialog( lab_error, ex.getMessage(), "Ошибка", JOptionPane.WARNING_MESSAGE );
            text_F.setText( String.format( "%.3f", (elec.getFkr()*1e-9)+0.001 ).replaceAll( ",", ".") );
            but_calculation.doClick();
        } finally {
            south_menu.repaint();
            gui = new GUI ( elec );
            WaveLevelGraph.setting_textures();
        }
    }
    
    /**
     * Метод ищет элемент по имени.
     * @param pane Панель на которой производится поиск.
     * @param name Имя элемента.
     * @return Возвращаемый элемент
     */
    public final Component getElement ( JPanel pane, String name ) {
        for ( Component component: pane.getComponents() ) {
            if ( component instanceof JPanel && component.getName() != null ) {
                if ( component.getName().equals(name) )
                    return component;
            }
            if ( component instanceof JTextArea && component.getName() != null ) {
                if ( component.getName().equals(name) )
                    return component;
            }
            if ( component instanceof JComboBox && component.getName() != null ) {
                if ( component.getName().equals(name) )
                    return component;                 
            }       
            if ( component instanceof JLabel && component.getName() != null ) {                
                if ( component.getName().equals(name) )
                    return component;                                    
            }            
        }
        return null;
    }    
    /**
     * Заполняет список доступных мод для заданной частоты
     * @param F
     * @return 
     */
    public final DefaultComboBoxModel identifyingAvailableMods_n ( double F ) {
        double max_n = elec.DefinitionWaveTypes( 0, F );
        ArrayList<Integer> n = new ArrayList<>(5);
        for ( int i = 0; Double.compare( i, max_n ) <= 0; i++ ) {
            n.add ( i );
        }
        DefaultComboBoxModel list_model_n = new DefaultComboBoxModel ( n.toArray() );
        return list_model_n;
    }
    /**
     * Заполняет список доступных мод для заданной частоты
     * @param F
     * @return 
     */
    public final DefaultComboBoxModel identifyingAvailableMods_m ( double F ) {
        double max_m = elec.DefinitionWaveTypesY0( F );
        ArrayList<Integer> m = new ArrayList<>(5);
        for ( int j = 0; Double.compare( j, max_m ) <= 0; j++ ) {
            m.add ( j );
            //System.out.println ( j );
        }
        DefaultComboBoxModel list_model_m = new DefaultComboBoxModel ( m.toArray() );
        return list_model_m;
    }
    
    /**Настраивает панель вывода короткой информации*/
    private void setting_short_menu () {        
        short_menu.setLayout( new BoxLayout( short_menu, BoxLayout.X_AXIS ) );        
        short_menu.setBackground( color_short );   
        short_menu.setPreferredSize( MENU );        
        String str = "0.00";       
        int left = 5;                  // отступ слева
        int width = 8;                 // отступ между компонентами
        
        JLabel lab_F_info = new JLabel ("F = " + str + " ГГц");
            lab_F_info.setFont(mainFont);
            lab_F_info.setForeground(Color.white);
            lab_F_info.setName("F");
        JLabel lab_Fkr_info = new JLabel ("Fkr = " + str + " ГГц");
            lab_Fkr_info.setForeground(Color.white);
            lab_Fkr_info.setFont(mainFont);
            lab_Fkr_info.setName("Fkr");
        JLabel lab_Fv_info = new JLabel ("Fv = " + str + " ГГц");
            lab_Fv_info.setForeground(Color.white);
            lab_Fv_info.setFont(mainFont);
            lab_Fv_info.setName("Fv");
        
        JLabel lab_L_info = new JLabel ("L = " + str + " см");
            lab_L_info.setFont(mainFont);
            lab_L_info.setForeground(Color.white);    
            lab_L_info.setName("L");
        JLabel lab_Lkr_info = new JLabel ("Lkr = " + str + " см");
            lab_Lkr_info.setForeground(Color.white);
            lab_Lkr_info.setFont(mainFont);    
            lab_Lkr_info.setName("Lkr");
        JLabel lab_Lv_info = new JLabel ("Lv = " + str + " см");
            lab_Lv_info.setForeground(Color.white);
            lab_Lv_info.setFont(mainFont);
            lab_Lv_info.setName("Lv");
        
        JLabel lab_a_info = new JLabel ("a = " + str + " мм ");
            lab_a_info.setForeground(Color.white);
            lab_a_info.setFont(mainFont);    
            lab_a_info.setName("a");
        JLabel lab_b_info = new JLabel ("b = " + str + " мм ");
            lab_b_info.setForeground(Color.white);
            lab_b_info.setFont(mainFont); 
            lab_b_info.setName("b");
           
        JButton but = new JButton ( scalingImage( new ImageIcon("files\\interface\\down.png"), 25) );
            but.setBorderPainted(false);
            but.setFocusPainted(false);
            but.setContentAreaFilled(false);
            but.setName("true");
        but.addActionListener((e)-> {
            if ( ((JButton)e.getSource()).getName().equals("true") ) {
                but.setName("false");
                but.setIcon( scalingImage( new ImageIcon("files\\interface\\up.png"), 25) );
                south_menu.remove(1);
                south_menu.revalidate();
            } else {
                if ( but_animation.isSelected() ) but_animation.doClick();
                but.setName("true");
                but.setIcon( scalingImage( new ImageIcon("files\\interface\\down.png"), 25) );
                south_menu.add( param_input );
                south_menu.revalidate();
            }
        });
        JButton show = new JButton ();
            show.setAction( new show_calculations() );
            show.setBorderPainted(false);
            show.setFocusPainted(false);
            show.setContentAreaFilled(false);    
            
        short_menu.add( Box.createHorizontalStrut(left) );
        short_menu.add( lab_F_info );   
        short_menu.add( Box.createHorizontalStrut(width) );
        short_menu.add( lab_Fkr_info );
        short_menu.add( Box.createHorizontalStrut(width) );
        short_menu.add( lab_Fv_info );
        short_menu.add( Box.createHorizontalStrut(width) );        
        short_menu.add( lab_L_info );
        short_menu.add( Box.createHorizontalStrut(width) );
        short_menu.add( lab_Lv_info );
        short_menu.add( Box.createHorizontalStrut(width) );
        short_menu.add( lab_Lkr_info );
        short_menu.add( Box.createHorizontalStrut(width) );        
        short_menu.add( lab_a_info );
        short_menu.add( Box.createHorizontalStrut(width) );
        short_menu.add( lab_b_info );        
        short_menu.add( Box.createHorizontalStrut(width) );
        
        short_menu.add( Box.createHorizontalGlue() );                    
        short_menu.add( show );
        short_menu.add( but );
    }
    
    /**Обновляет полученную информацию*/
    public void update_short_info () {
        String wave;
        if ( this.field == FIELD.TE )        
            wave = "H "+(int)elec.get_m()+""+(int)elec.get_n();
        else
            wave = "E "+(int)elec.get_m()+""+(int)elec.get_n();
        for ( Component e: short_menu.getComponents() )
            if ( e instanceof JLabel )
                switch ( e.getName() ){
                    case "F": ((JLabel) e).setText(String.format("F = %.2f ГГц", elec.getF()*1e-9)); break;
                    case "Fkr": ((JLabel) e).setText(String.format("Fkr "+wave+"= %.2f ГГц", elec.getFkr()*1e-9)); break;
                    case "Fv": ((JLabel) e).setText(String.format("Fv = %.2f ГГц", elec.getFv()*1e-9)); break;
                    case "L": ((JLabel) e).setText(String.format("L = %.2f см", elec.getL()*1e2)); break;
                    case "Lkr": ((JLabel) e).setText(String.format("Lkr "+wave+"= %.2f см", elec.getLkr()*1e2)); break;
                    case "Lv": ((JLabel) e).setText(String.format("Lv = %.2f см", elec.getLv()*1e2)); break;
                    case "a": ((JLabel) e).setText(String.format("a = %.2f мм", elec.get_a()*1e3)); break;
                    case "b": ((JLabel) e).setText(String.format("b = %.2f мм", elec.get_b()*1e3)); break;
                }                
    }   
//====================Рабочие панели приложения=================================    
    /**Определение доступных волн*/
    private void setting_pane_type () {        
        Dimension graphics_size = new Dimension ( 450, 330 );   // рзамер графика        
        Dimension text_size = new Dimension ( 200, 330 );   // рзамер графика        
        int scrolling = 10;     // Число на которое будет изменятся скролл бар при прокрутки мыши        
        pane_type.setName("pane_type");
        pane_type.setHeading( "Определение доступных мод" );
        JPanel pane_content = pane_type.getPaneContent();
            
        JLabel lab_present_F = new JLabel ( String.format("F = %.1f ГГц", elec.getF()/1e9 )  );
            lab_present_F.setName("lab_present_F");
            lab_present_F.setFont( font_output );
            lab_present_F.setForeground(Color.white);
            
        //pane_type.add( pane_heading );  // Добалвения заголовка    
        //pane_type.add( pane_content );  // ДОбавление панели содержимого
//===================Размещение содержимого панели content======================
        pane_content.setLayout( new GridBagLayout () );
        GridBagConstraints grid = new GridBagConstraints();            
            grid.insets = new Insets ( 5, 5, 5, 5 );              
            grid.gridwidth = 1;
            grid.gridheight = 1;            
        ChartPanel graphics_pane = gui.DefinitionWaveTypes(elec.getF());   // График. Элипс для определения доступных мод
            graphics_pane.setName( "graphics_pane" );
            graphics_pane.setPreferredSize( graphics_size ); 
            graphics_pane.setMinimumSize( graphics_size );
            
            grid.fill = GridBagConstraints.BOTH;
            grid.gridwidth = GridBagConstraints.REMAINDER;
            grid.weightx = 1;
            grid.weighty = 1;
            grid.gridx=0;   grid.gridy=0;
        pane_content.add( graphics_pane, grid );
            //grid.weightx = 0;
            grid.weighty = 0;
        
        JPanel pane_text = new JPanel();
            pane_text.setLayout ( new GridLayout() );
            pane_text.setName("pane_text");
            pane_text.setBackground( background );
            //pane_text.setBorder( BorderFactory.createDashedBorder( color_back_font ) );
            //pane_text.setBorder( BorderFactory.createLineBorder( myColor.color_back_font, 2 ) );
            
        
        JTextArea text = new JTextArea();
            text.setName("text/html");
            text.setPreferredSize(text_size);
            text.setMaximumSize(text_size);
            text.setMinimumSize(text_size);
            text.setSize(text_size);
            text.setForeground( myColor.color_back_font );
            //text.setWrapStyleWord( true );
            text.setLineWrap( true );
            text.setFont( myFont.font_heading );
            text.setOpaque( false );
            //text.setBackground( color_back_font );
        try {
            String str = new String ( Files.readAllBytes( Paths.get( "files\\text\\pane_type\\text.html" ) ), Charset.forName("UTF-8") );
            
            text.setText( str );
        } catch ( MalformedURLException ex ) {
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
            pane_text.add( text );
            
            //grid.gridx=1;   grid.gridy=0;
            //grid.weightx = 1f;
            //grid.gridwidth = GridBagConstraints.REMAINDER;
            //grid.fill = GridBagConstraints.BOTH;
        //pane_content.add( pane_text, grid );
        double max_F = elec.getF()*1e-8+1_000;        
        double min_F = elec.getFkr()*1e-8;
        double present = elec.getF()*1e-8;
        JSlider slider = new JSlider( (int)min_F, (int)max_F, (int)present );
            slider.setName("slider");
            slider.setMajorTickSpacing( 10 );
            slider.setPaintTicks(true);
            slider.setOpaque( false );
            slider.setForeground( Color.white );
            
        Dictionary labels = new Hashtable();
            labels.put( (int) present, new JLabel ("F") );
            labels.put( (int) min_F, new JLabel ("Fkr") );            
            slider.setLabelTable(labels);
            slider.setPaintLabels(true);
            
            grid.gridx = 0;   grid.gridy =1;
            grid.gridwidth = 1;
        pane_content.add( slider, grid );
        JLabel lab_slider_info = new JLabel ( String.format("%.1f ГГц", present/10 ) );
            lab_slider_info.setName("lab_slider_info");
            lab_slider_info.setFont( font_output );
            lab_slider_info.setForeground(Color.white);
            grid.weightx = 1;
            grid.gridx=1;   grid.gridy=1;
        pane_content.add( lab_slider_info, grid );
        // Слушатель для ползунка
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent event) {
                    double value = (double) slider.getValue()/10;
                    lab_slider_info.setText(String.format("%.1f ГГц", value ));
                    graphics_pane.removeAll();
                    graphics_pane.setChart( gui.DefinitionWaveTypes( value*1e9 ).getChart() );                    
                    graphics_pane.repaint();
                }
            });
            slider.addMouseWheelListener( new MouseWheelListener () {
                @Override
                public void mouseWheelMoved(MouseWheelEvent mwe) {
                    //f ( mwe.getButton() == MouseEvent)
                    if ( mwe.getWheelRotation() < 0 )
                        slider.setValue( slider.getValue() + scrolling );
                    if ( mwe.getWheelRotation() > 0 )
                        slider.setValue( slider.getValue() - scrolling );
                }
            });
        //pane_type.setBorder( BorderFactory.createLineBorder(Color.red, 1) );
    }
    
    /**Обновляет панель доступных типов волн*/
    public void update_pane_type () {        
        for ( Component e: ((JPanel)pane_type.getComponent(0)).getComponents() ) {
            if ( e.getName() != null )
            switch (e.getName()) {
                case "lab_present_F": ((JLabel)e).setText( elec.getF()/1e9 + " ГГц" ); break;
            }            
        }            
        for ( Component e: ((JPanel)pane_type.getComponent(1)).getComponents() ) {
            if ( e.getName() != null )
            switch (e.getName()) {
                case "graphics_pane": 
                    ((ChartPanel)e).removeAll();
                    ((ChartPanel)e).setChart( gui.DefinitionWaveTypes( elec.getF() ).getChart() );
                    break;
                case "slider":                     
                    double min_F = elec.getFkr()*1e-8;
                    double present = elec.getF()*1e-8;
                    Dictionary labels = new Hashtable();    
                    labels.put( (int) present, new JLabel ("F") );
                    labels.put( (int) min_F, new JLabel ("Fkr") );
                    ((JSlider)e).setLabelTable(labels);
                    ((JSlider)e).setMaximum( (int) (elec.getF()*1e-8+1_000) );
                    ((JSlider)e).setMinimum( (int)min_F );
                    ((JSlider)e).setValue( (int)present );
                    break;
                case "lab_slider_info":
                    ((JLabel)e).setText( String.format("%.1f ГГц", elec.getF()/1e9 )); 
                    break;  
            }            
        }        
    }           
    public static JToggleButton but_animation;
    
    /** Настраивает панель отображения моделей с уровнем распределения полей в волноводе */
    private void setting_axis_projection () {
        // рзамер Панели с моделями
        Dimension slider_frame_size = new Dimension ( 500, 40 );
        
        pane_axis_projection.setName("pane_axis_projection");
        pane_axis_projection.setHeading("Проекции на оси");
        //============= Расположение элементов на панели содержимого ===========
        JPanel pane_model = new JPanel ();
            pane_model.setName( "pane_model" );
            pane_model.setBackground( background );
            //pane_model.setLayout( new BoxLayout( pane_model, BoxLayout.X_AXIS ) );
            pane_model.setLayout( new GridLayout( 1, 2 ) );
//-------------------------Меню под моделями------------------------------------

        pane_model_E = new WaveModelPane( TYPE.E );
        pane_model_H = new WaveModelPane( TYPE.H );
        
        JPanel model_menu = new JPanel ();
            model_menu.setName( "model_menu" );
            model_menu.setBorder( BorderFactory.createLineBorder( color_back_font , 1 ) );
            model_menu.setOpaque( true );
            model_menu.setLayout( new BoxLayout( model_menu, BoxLayout.X_AXIS ) );
            model_menu.setBackground( color_short );                        
            
        JToggleButton TE_H_mode = new JToggleButton ("H",true);
            TE_H_mode.setName( "Button H" );
            TE_H_mode.setBackground( color_short );
            TE_H_mode.setForeground( color_short );
        JToggleButton TM_E_mode = new JToggleButton ("E",false);
            TM_E_mode.setName( "Button E" );
            TM_E_mode.setBackground( color_short );
            TM_E_mode.setForeground( Color.white );
        ButtonGroup TE_TM_group = new ButtonGroup ();            
            TE_TM_group.add(TE_H_mode);
            TE_TM_group.add(TM_E_mode);            
            TE_H_mode.addItemListener( new ItemListener () {                                  
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if ( ie.getStateChange() == ItemEvent.SELECTED ){
                        field = FIELD.TE;
                        TE_H_mode.setForeground( color_short );
                        TM_E_mode.setForeground( Color.white );
                        if ( !(modelList[0] != null && modelList[1] != null && modelList[2] != null && modelList[3] != null) )
                            return;
                        
                        modelList[0].set_rot_x( modelList[2].get_rot_x() );
                        modelList[1].set_rot_x( modelList[3].get_rot_x() );
                        modelList[0].set_rot_y( modelList[2].get_rot_y() );
                        modelList[1].set_rot_y( modelList[3].get_rot_y() );
                        
                        System.out.println( "---toglr-H---" );
                        pane_model_E.setModel( modelList[0] );
                        pane_model_H.setModel( modelList[1] );
                                                
                        south_menu.repaint();
                    }  
                }
            });
            TM_E_mode.addItemListener( new ItemListener () {
                @Override
                public void itemStateChanged(ItemEvent ie) {
                    if ( ie.getStateChange() == ItemEvent.SELECTED ){
                        field = FIELD.TM;
                        TM_E_mode.setForeground( color_short );
                        TE_H_mode.setForeground( Color.white );                        
                        modelList[2].set_rot_x( modelList[0].get_rot_x() );
                        modelList[3].set_rot_x( modelList[1].get_rot_x() );
                        modelList[2].set_rot_y( modelList[0].get_rot_y() );
                        modelList[3].set_rot_y( modelList[1].get_rot_y() );
                        
                        System.out.println( "---toglr-E---" );
                        pane_model_E.setModel( modelList[2] );
                        pane_model_H.setModel( modelList[3] );
                        
                        south_menu.repaint();
                    }
                }
            });
            TM_E_mode.addMouseListener( new MouseListener () {
                @Override
                public void mouseClicked( MouseEvent me ) {
                    if ( me.getButton() == MouseEvent.BUTTON1 && !TM_E_mode.isEnabled() ) {
                        String [] str = new String [] { "E - волны с 0 индексом существавать не могут поскольку в этом случае",
                                                        "поперечная составляющая будет равна нулю.",
                                                        "               m: "+String.format( "%.0f", elec.get_m() )+"     n: "+String.format( "%.0f", elec.get_n() ) };                        
                        JOptionPane.showMessageDialog( TM_E_mode, str, "Оповещение", JOptionPane.INFORMATION_MESSAGE );
                    }
                }
                @Override
                public void mousePressed( MouseEvent me ) {}
                @Override
                public void mouseReleased( MouseEvent me ) {}
                @Override
                public void mouseEntered( MouseEvent me ) {}
                @Override
                public void mouseExited( MouseEvent me ) {}
            });
            
        JButton but_calculation = new JButton ( "Вычислить" );            
        but_animation = new JToggleButton ( new ImageIcon ("files\\AxisProjection\\play.png") );
            but_animation.setBorderPainted( false );
            but_animation.setContentAreaFilled( false );
            but_animation.addActionListener( new ActionListener (){
                Timer timer;
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if ( MainWorkWindow.pane_model_E.getModel() == null || MainWorkWindow.pane_model_H.getModel() == null ) {
                        //JOptionPane.showMessageDialog( but_animation, "Модели не были созданы !", "Ошибка", JOptionPane.WARNING_MESSAGE );
                        return;
                    }
                    long pause = 1_000L/speed;                    
                    if ( but_animation.isSelected() ) {
                        but_animation.setIcon( new ImageIcon ("files\\AxisProjection\\pause.png") );
                        timer = new Timer( "thread-animation", true );                        
                        timer.scheduleAtFixedRate ( getTask(), 0, pause );
                    } else {
                        but_animation.setIcon( new ImageIcon ("files\\AxisProjection\\play.png") );
                        timer.cancel();
                    }
                }
            });
        JButton but_config = new JButton ( new ImageIcon ("files\\interface\\configuration.png") );
            but_config.setBorderPainted( false );
            but_config.setContentAreaFilled( false );
            but_config.addActionListener( (ac)->{ 
                if ( but_animation.isSelected() ) but_animation.doClick();
                ConfigurationTexture.showConfigTextureDialog( center, this ); 
            });
            
            but_calculation.addActionListener( (e)-> {                    
                    if ( but_animation.isSelected() ) but_animation.doClick();
                    calculation();
                    MainWorkWindow.slider_frame.setMaximum( fps-1 );
                    boolean flag = !(elec.get_n() == 0 || elec.get_m() == 0);
                    TE_H_mode.setSelected( flag );
                    TM_E_mode.setEnabled( flag );                    
            } );
        JButton serial = new JButton ( new ImageIcon ("files\\interface\\save.png") );
            serial.setBorderPainted( false );
            serial.setContentAreaFilled( false );
            serial.addActionListener( (e)-> {
                Thread write = new Thread ( ()-> {
                    MainWorkWindow.progress_bar.setIndeterminate(true);
                    try ( FileOutputStream streamOut = new FileOutputStream ( new File ( ModelManager.createFileName( elec ) ), false ) ) {
                        ObjectOutputStream obOut = new ObjectOutputStream ( streamOut );
                        for ( GraphicsModel model: MainWorkWindow.modelList ) {
                            if ( model != null ) {
                                obOut.writeObject( model );
                                System.out.println( "Записано" );
                            }                                
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
                    }      
                    MainWorkWindow.progress_bar.setIndeterminate(false);
                });      
                write.start();
            } );
        JButton deserial = new JButton ( new ImageIcon ("files\\interface\\laad.png") );
            deserial.setBorderPainted( false );
            deserial.setContentAreaFilled( false );
            deserial.addActionListener( (e)->{     
                ModelManager.showFileList( MainWorkWindow.this );
            });
            frame_info.setFont( font_heading );
            frame_info.setForeground( Color.white );
            frame_info.setText("0");
            frame_info.setPreferredSize( new Dimension ( 20, frame_info.getHeight() ) );
        slider_frame = new JSlider( 0, fps-1, 0 );
            slider_frame.setName("slider_frame");
            slider_frame.setPaintTicks( false );
            slider_frame.setSnapToTicks( true );
            slider_frame.setMaximumSize( slider_frame_size );
            slider_frame.setPreferredSize( slider_frame_size );
            slider_frame.setMinimumSize( slider_frame_size );
            slider_frame.setOpaque( false );
            slider_frame.addChangeListener( new ChangeListener () {
                @Override
                public void stateChanged(ChangeEvent ce) {
                    //System.out.println ( "кадр - " + slider_frame.getValue() );
                    //System.out.println( "---slider-listener---" );
                    if ( pane_model_H.getModel() != null && pane_model_E.getModel() != null ) {
                        pane_model_H.getModel().set_texture_frame( slider_frame.getValue() );
                        pane_model_E.getModel().set_texture_frame( slider_frame.getValue() );
                    }
                    frame_info.setText( slider_frame.getValue()+1 + "" );
                }
            } );
        // Добавление элементов на панель моделей волновода
            pane_model.add( pane_model_E );
            pane_model.add( pane_model_H );
        // Добавление элементов на панель
            model_menu.add( Box.createHorizontalStrut( 5 ) );
            model_menu.add( but_calculation );
            model_menu.add( Box.createHorizontalStrut( 5 ) );
            model_menu.add( TE_H_mode );
            model_menu.add( TM_E_mode );
            model_menu.add( Box.createHorizontalStrut( 5 ) );
            model_menu.add( but_config );
            //model_menu.add( serial );
            //model_menu.add( deserial );
            model_menu.add( Box.createHorizontalGlue() );
            model_menu.add( but_animation );
            model_menu.add( slider_frame );
            model_menu.add( Box.createHorizontalStrut( 10 ) );
            model_menu.add( frame_info );
            model_menu.add( Box.createHorizontalStrut( 15 ) );
        pane_axis_projection.setMainPane( pane_model );
        pane_axis_projection.setMenuPane( model_menu );
    }            
    /** Событие обеспечивающее анимацию текстур на волноводе @return
     * @return  */
    public static TimerTask getTask () {           
        return new TimerTask () {
            @Override
            public void run() {                
                int frame = pane_model_H.getModel().get_frame();
                // Определяет на кокйо угол повёрнута модель
                if ( frame+1 >= slider_frame.getMaximum() ) {
                    frame = 0;
                    pane_model_E.getModel().set_texture_frame( frame );
                    pane_model_H.getModel().set_texture_frame( frame );
                    System.gc();
                    slider_frame.setValue( frame );
                    frame_info.setText( (frame+1) + "" );
                    //System.out.println ( "frame = " + (frame) + "\tfps = " + fps );
                } else {
                    pane_model_E.getModel().set_texture_frame( frame+1 );
                    pane_model_H.getModel().set_texture_frame( frame+1 );
                    System.gc();
                    slider_frame.setValue( frame+1 );
                    frame_info.setText( (frame+2) + "" );
                    //System.out.println ( "frame = " + (frame+1) + "\tfps = " + fps );
                }  
            }
        };
    }
    public static GraphicsModel [] modelList = new GraphicsModel [4];
    public static ElectromagneticWave [] elecList = new ElectromagneticWave [4];        
    public static WaveLevelGraph [] wabeLevelGraph = new WaveLevelGraph [4];
    
    /**Вычисляет текстуры в потоке*/
    @SuppressWarnings("valuegoeshere")
    private void calculation () {
        int count = ( elec.get_n() == 0 || elec.get_m() == 0 )?2:4;
        progress_model.setMinimum( 0 );
        progress_model.setMaximum( fps*count );
        progress_model.setValue( 0 );
        WaveLevelGraph.NFRAME = 0;                      // Сброс счётчика отрисованных кадров.
        WaveLevelGraph.levelMaxMap.clear();             // Сброс карты линий уровня. Освобождение ОЗУ.
        modelList = new GraphicsModel [4];
        elecList = new ElectromagneticWave [4];        
        wabeLevelGraph = new WaveLevelGraph [4];
        try {            
            elecList[0] = elec.clone();
            elecList[1] = elec.clone();
            elecList[2] = elec.clone();
            elecList[3] = elec.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog( center, new String []{"Ошибка при клонировании объекта: ElectromagneticWave",
                                           ex.getMessage() }, "Ошибка клонирования", JOptionPane.ERROR_MESSAGE );
        }
        Thread calculation = new Thread( ()-> {
            System.gc();                                   
            Thread graphicsTexture_0 = new Thread( ()->{
                try {
                    WaveLevelGraph waveGraph_E_H = new WaveLevelGraph ( elecList[0] );
                    waveGraph_E_H.set_wave_type( TYPE.E );
                    waveGraph_E_H.set_field( FIELD.TE );    
                    modelList[0] = new GraphicsModel( waveGraph_E_H );
                    wabeLevelGraph[0] = waveGraph_E_H;
                } catch ( OutOfMemoryError er ) {
                    Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, er);
                    JOptionPane.showMessageDialog( center, new String [] {"Свободная оперативная память закончилась !",
                                                                  er.getMessage(),
                                                                  "Попробуйте запусить вычисление занова."}, 
                                                                  "Предупреждение", JOptionPane.WARNING_MESSAGE );
                    System.gc();
                }
            });
            Thread graphicsTexture_1 = new Thread( ()->{
                try {
                    WaveLevelGraph waveGraph_H_H = new WaveLevelGraph ( elecList[1] );
                    waveGraph_H_H.set_wave_type( TYPE.H );
                    waveGraph_H_H.set_field( FIELD.TE );
                    modelList[1] = new GraphicsModel( waveGraph_H_H );
                    wabeLevelGraph[1] = waveGraph_H_H;
                    //System.gc();
                } catch ( OutOfMemoryError er ) {
                    Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, er);
                    JOptionPane.showMessageDialog( center, new String [] {"Свободная оперативная память закончилась !",
                                                                  er.getMessage(),
                                                                  "Попробуйте запусить вычисление занова."}, 
                                                                  "Предупреждение", JOptionPane.WARNING_MESSAGE );
                    System.gc();
                }                    
            });
            Thread graphicsTexture_2 = new Thread( ()->{
                try {
                    WaveLevelGraph waveGraph_E_E = new WaveLevelGraph ( elecList[2] );
                    waveGraph_E_E.set_wave_type( TYPE.E );
                    waveGraph_E_E.set_field( FIELD.TM );
                    modelList[2] = new GraphicsModel( waveGraph_E_E );
                    wabeLevelGraph[2] = waveGraph_E_E;
                    //System.gc();
                } catch ( OutOfMemoryError er ) {
                    Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, er);
                    JOptionPane.showMessageDialog( center, new String [] {"Свободная оперативная память закончилась !",
                                                                  er.getMessage(),
                                                                  "Попробуйте запусить вычисление занова."}, 
                                                                  "Предупреждение", JOptionPane.WARNING_MESSAGE );
                    System.gc();
                }
            });
            Thread graphicsTexture_3 = new Thread( ()->{
                try {
                    WaveLevelGraph waveGraph_H_E = new WaveLevelGraph ( elecList[3] );
                    waveGraph_H_E.set_wave_type( TYPE.H );
                    waveGraph_H_E.set_field( FIELD.TM );
                    modelList[3] = new GraphicsModel( waveGraph_H_E );
                    wabeLevelGraph[3] = waveGraph_H_E;
                    //System.gc();
                } catch ( OutOfMemoryError er ) {
                    Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, er);
                    JOptionPane.showMessageDialog( center, new String [] {"Свободная оперативная память закончилась !",
                                                                  er.getMessage(),
                                                                  "Попробуйте запусить вычисление занова."}, 
                                                                  "Предупреждение", JOptionPane.WARNING_MESSAGE );
                    System.gc();
                }                    
            });
            graphicsTexture_0.start();
            graphicsTexture_1.start();
            if ( elec.get_n() != 0 && elec.get_m() != 0 ) {
                graphicsTexture_2.start();
                graphicsTexture_3.start();
            }            
            try {                                
                long time1 = System.currentTimeMillis();
                graphicsTexture_0.join();
                graphicsTexture_1.join();
                graphicsTexture_2.join();
                graphicsTexture_3.join();
                
                if ( field == FIELD.TE ) {
                        pane_model_E.setModel( modelList[0] );
                        pane_model_H.setModel( modelList[1] );
                } else {
                        pane_model_E.setModel( modelList[2] );
                        pane_model_H.setModel( modelList[3] );
                }
                
                SwingUtilities.invokeLater ( ()->{                    
                        long time2 = System.currentTimeMillis();
                        progress_bar.setString( "Текстуры готовы. Циклов: "+WaveLevelGraph.N+" Кадров: "+elec.get_fps()*count+" затрачено: "+(time2-time1)+" ms" );
                        update_short_info();
                });
                /**
                Toolkit.getDefaultToolkit().getSystemEventQueue()
                    .postEvent( new PeerEvent(Toolkit.getDefaultToolkit(), () -> {
                        if ( field == FIELD.TE ) {
                        pane_model_E.setModel( modelList[0] );
                        pane_model_E.setWave( wabeLevelGraph[0] );
                        pane_model_H.setModel( modelList[1] );
                        pane_model_H.setWave( wabeLevelGraph[1] );
                    } else {
                        pane_model_E.setModel( modelList[2] );
                        pane_model_E.setWave( wabeLevelGraph[2] );
                        pane_model_H.setModel( modelList[3] );
                        pane_model_H.setWave( wabeLevelGraph[3] );
                    }
                        pane_model_E.getModel().set_texture_frame( slider_frame.getValue() );
                        pane_model_H.getModel().set_texture_frame( slider_frame.getValue() );
                        long time2 = System.currentTimeMillis();
                        progress_bar.setString( progress_bar.getString()+" затрачено: "+(time2-time1)+" ms" );
                        update_short_info();
                }, PeerEvent.ULTIMATE_PRIORITY_EVENT ) );                              
                **/
            } catch ( InterruptedException ex ) {
                Logger.getLogger(MainWorkWindow.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog( center, new String [] {"Ошибка при ожидании потока !", ex.getMessage() }, "Ошибка", JOptionPane.ERROR_MESSAGE );
            }
        });
        calculation.setName( "calculation" );
        calculation.setDaemon( true );
        calculation.start();
    }
    /** Настройки необходимые для создания текстур */
    public static void setting_textures ( final WaveLevelGraph wabeGraph ) {
        // Влияет на производительность
        wabeGraph.set_Height_texture( size_texteru );           // Высота текстур
        wabeGraph.set_fps( fps );                               // Общее количество текстур
        wabeGraph.set_count( count_texteru );                   // Количество точек. Чем больше тем детальнее текстура
        wabeGraph.set_l( l );
        // Расчитиывает данные для текстуры
        wabeGraph.setting_textures();
    }
//------------------------------------------------------------------------------
    /**
     * Масшабирует переданное изображение с сохранением пропорций.
     * @param icon изображение.
     * @param x новая высота.
     * @return новое изображение.
     */
    private ImageIcon scalingImage ( ImageIcon icon, int x ) {                        
        int oldHeight = icon.getIconHeight();             
        Image image = icon.getImage().getScaledInstance( x, (int) ( ( x * oldHeight) / icon.getIconWidth() ), Image.SCALE_SMOOTH );
        return new ImageIcon( image, icon.getDescription());
    }
    /**
     * Масшабирует переданное изображение с нужными размерами..
     * @param icon изображение.
     * @param x новая высота.
     * @return новое изображение.
     */
    private ImageIcon scalingImage ( ImageIcon icon, int x, int y ) {            
        Image image = icon.getImage().getScaledInstance( x, y, Image.SCALE_AREA_AVERAGING );
        return new ImageIcon( image, icon.getDescription());
    } 
// Вспомогательные классы    
    /**Действие. Открывает окно с расчётами для текущих параметров*/
    class show_calculations extends AbstractAction {
        public show_calculations () {
            putValue( AbstractAction.SHORT_DESCRIPTION, "показать расчёты");
            putValue( AbstractAction.SMALL_ICON, scalingImage( new ImageIcon("files\\interface\\calculation.png"), 25) );
        }
        @Override
        public void actionPerformed( ActionEvent ae ) {
           if ( !calculat_frame.isVisible() ) {
               calculat_frame.update();
               calculat_frame.setVisible( true );
           }
        }
    }
}