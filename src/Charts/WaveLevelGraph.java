package Charts;

import Color.ColorFill;
import Computation.ElectromagneticWave;
import Settings.myColor;
import java.awt.Color;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import Data.FIELD;
import Data.TYPE;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import rectangularwaveguide.MainWorkWindow;

/** Класс содержит всю информацию необходимую для построения графиков.
 *  График линий уровня элеткромагнитной волны.
 */
public class WaveLevelGraph implements myColor, Serializable {
    private static final BufferedImage null_image = new BufferedImage ( 50, 50, BufferedImage.TYPE_INT_ARGB );
    
    public static int NFRAME = 0;       // Сщётчик отрисованных кадров
    public static int N = 0;                  // Сщётчик итераций циклов
    public static int staticN = 0;            // Сщётчик для статических вычислений.    
    
    public static int countX;                  // Количество значений X
    public static int countY;                  // Количество значений Y
    public static int countZ;                  // Количество значений Z
    public static int countXY;                 // Количество значений XY
    public static int countXZ;                 // Количество значений XZ
    public static int countYZ;                 // Количество значений YZ    
    
    public static float a [];                 // расчитанные значения x
    public static float b [];                 // расчитанные значения y
    public static float l [];                 // расчитанные значения z            
    
    private static float start;               // Начальное значение для dx, dy, dz
    // Приращения для вычисления координат
    private static float dx;                  // Приращение x
    private static float dy;                  // Приращение y
    private static float dz;                  // Приращение z    
    /** Определяет тип распространяемой волны TE(H) или TM(E)*/
    public FIELD field;
    /** Определяет поле (E или H)*/
    public TYPE wave_type = TYPE.E;
    private final ElectromagneticWave elec;     // Объект электромагнитной волны
    // Хранит кадры для кажого графика
    private transient HashMap <Integer,BufferedImage> frameXY = new HashMap(0);    // Карта хранит изображения для плоскости xy
    private transient HashMap <Integer,BufferedImage> frameXZ = new HashMap<>(0);    // Карта хранит изображения для плоскости xz
    private transient HashMap <Integer,BufferedImage> frameYZ = new HashMap<>(0);    // Карта хранит изображения для плоскости yz    
//------------------ Переменные храянит размеры текстур ------------------------
    private static float heightXY;            // Определяет высоту тектсуры xy в пикселях
    private static float widthXY;             // Ширина текстуры xy
    //private double DXY;                 // Соотношение сторон текстуры xy    
    private static float heightXZ;            // Высота текстуры xz    
    private static float widthXZ;             // Ширина текстуры xz
    //private double DXZ;                 // Соотношение сторон текстуры xz    
    private static float heightYZ;            // Высота текстуры yz. Ширина равна ширине текстуры xz
    private static float widthYZ;
    
    public int getTextureSize () {
        return (int) heightXY;
    }
    public int getTextureCount () {
        return countX;
    }
    // ConcurrentHashMap
    // ConcurrentSkipListMap
    // Колекция хранит максимальное знчения для каждого кадра линии уровня.
    public static final Map<String,Float> levelMaxMap = new ConcurrentHashMap ();
    // Градиент закраски
    private transient ColorFill gradient;
    
    static {        
        Graphics2D gr = (Graphics2D) null_image.getGraphics();
        gr.setPaint( myColor.color_short );
        gr.fillRect( 0, 0, null_image.getWidth(), null_image.getHeight() );
    }        
    
    public WaveLevelGraph ( final ElectromagneticWave elec ) {        
        this.elec = elec;        
        // Определяет каким будет разрешение текстуры
        // Вычисление данных необходимых для построения графика        
        //setting_textures();
    }
    /** Производит все расчёты необходимые для построения текстур */
    public static void setting_textures () {
        heightXY = (short) MainWorkWindow.size_texteru;        // Определяет высоту тектсуры xy в пикселях\
        countX = MainWorkWindow.count_texteru;                 // Количество точек по x
        calculationSizeTexture();
        staticN = 0;                                // Сброс счётчика итераций
        calculationCount();                // Расчитывает основную инфорацию необходимую для отрисовки текстур        
        SwingUtilities.invokeLater ( ()->{ 
            MainWorkWindow.progress_bar
                .setString( "Данные для рендера текстур готовы. Итераций: "+staticN );
        });
    }
    /**Создаёт текстуры плотности электромагнитного поля*/
    public void createFrame_All () {
        // Ощищает колекцию кадров
        int count = ( elec.get_n() == 0 || elec.get_m() == 0 )?2:4;
        frameXY.clear();
        frameXZ.clear();
        frameYZ.clear();
        for ( int frame = 0; frame < ( elec.get_fps()-1 ); frame++ ) {
            //System.gc();
            SwingUtilities.invokeLater ( ()-> {
                MainWorkWindow.progress_bar.setString( "Отрисовка кадра "+(++NFRAME)+" Кадров: "+elec.get_fps()*count );
                MainWorkWindow.progress_bar.setValue( MainWorkWindow.progress_bar.getValue()+1 );
            });
            elec.time_frame( frame );            
            createTexture_all( frame );            
        }
        System.gc();
        elec.set_t( elec.getT() );           
        createTexture_all( elec.get_fps()-1 );
        SwingUtilities.invokeLater ( ()-> {
            MainWorkWindow.progress_bar.setString( "Отрисовка кадра "+(++NFRAME)+" Кадров: "+elec.get_fps()*count );
            MainWorkWindow.progress_bar.setValue( MainWorkWindow.progress_bar.getValue()+1 );
        });        
    }
// Получение изображения с разреза
    public BufferedImage createFrameSlice_XY ( final int frame, final double slice ) {
        elec.time_frame(frame);
        return create_XY_slice ( slice, frame );
    }
    public BufferedImage createFrameSlice_XZ ( final int frame, final double slice ) {
        elec.time_frame(frame);
        return create_XZ_slice ( slice, frame );
    }
    public BufferedImage createFrameSlice_YZ ( final int frame, final double slice ) {
        elec.time_frame(frame);
        return create_YZ_slice ( slice, frame );
    }
//========================Построение графика волн===============================
    /**
     * Нужно вызвать при изменении a, b, n, m, F, Lv
     * Расчитывает необходимые данные для построения графиков
     */
    private static void calculationSizeTexture () {        
        // Вычисление размеров текстур              
        //DXY = heightXY;                     // Соотношение сторон текстуры xy
        widthXY = heightXY;                 // Ширина текстуры xy
        heightXZ = heightXY;                // Высота текстуры xz
        //DXZ = heightXY;                     // Соотношение сторон текстуры xz
        widthXZ = heightXY;                 // Ширина текстуры xz
        heightYZ = heightXY;                // Высота текстуры yz. Ширина равна ширине текстуры xz
        // Вычисляет размеры текстуры. Для расчётов нужна только ширина.
        //DXY = elec.get_a()/elec.get_b();    // Соотношение сторон текстуры xy
        //widthXY = heightXY*DXY;             // Ширина текстуры xy
        heightXZ = widthXY;                 // Высота текстуры xz
        //DXZ = elec.get_l()/elec.get_a();    // Соотношение сторон текстуры xz
        //widthXZ = heightXZ*DXZ;             // Ширина текстуры xz
        heightYZ = heightXY;                // Высота текстуры yz. Ширина равна ширине текстуры xz
        widthYZ = heightXY;
    }   
    /**Расчитывает основные данные необходимые для построения любой текстуры*/
    public static void calculationCount () {
        // Вычисление общего числа точек на графике        
        countY = (int)((MainWorkWindow.elec.get_b()/MainWorkWindow.elec.get_a())*countX);                // Количесто точек н
        //this.countZ = (int)(((elec.get_l()/4)/elec.get_a())*countX);          // Количесто точек z
        countZ = (int)((countY+countX)/2);                                 // Количесто точек z
        // Вычисление общего числа точек для каждой проекции
        countXY = (countX+1)*(countY+1);    // Количесто точек на графике XY
        countXZ = (countX+1)*(countZ+1);    // Количесто точек на графике XZ
        countYZ = (countY+1)*(countZ+1);    // Количесто точек на графике YZ
        double ddx = MainWorkWindow.elec.get_a()/countX;   // Величина на которую будет изменятся приращение dx
        double ddy = MainWorkWindow.elec.get_b()/countY;   // Величина на которую будет изменятся приращение dy
        double ddz = MainWorkWindow.elec.get_l()/countZ;   // Величина на которую будет изменятся приращение dz
        start = 0;                     // Начальное значение приращений
        dx = start;                    // Приращение для x
        dy = start;                    // Приращение для y
        dz = start;                    // Приращение для z
        // Мавссив хранят значения координат. Высчитываются заранее. Потому что пройтись по элементам массива быстрее чем расчитать занова
        a = new float [countX+1];     // расчитанные значения x
        b = new float [countY+1];     // расчитанные значения y
        l = new float [countZ+1];     // расчитанные значения z
        //=======================Расчёт координат=======================================
        coordinateCalculation ( ddx, ddy, ddz );
        //System.out.println ( "countX = " + countX + "\tcountY = " + countY + "\tcountZ = " + countZ );
        //System.out.println ( "countXY = " + countXY + "\tcountXZ = " + countXZ + "\tcountYZ = " + countYZ );        
    }    
    /**
     * Расчёт кординат.
     * @param ddx Приращение по x.
     * @param ddy Приращение по y.
     * @param ddz Приращение по z.
     */
    private static void coordinateCalculation ( final double ddx, final double ddy, final double ddz ) {
        if ( MainWorkWindow.elec.get_a() == MainWorkWindow.elec.get_b() ) {
            for ( int i = 0; i < countX; i++ ) {
                a[i] = dx;
                b[i] = dx;
                dx+=ddx;
                staticN++;
            }
            for ( int k = 0; k < countZ; k++ ) {
                l[k] = dz;
                dz+=ddz;
                staticN++;
            }
        } else {
            for ( int i = 0; i < countX; i++ ) {
                a[i] = dx;
                dx+=ddx;
                staticN++;
            }
            for ( int j = 0; j < countY; j++ ) {
                b[j] = dy;
                dy+=ddy;
                staticN++;
            }
            for ( int k = 0; k < countZ; k++ ) {
                l[k] = dz;
                dz+=ddz;
                staticN++;
            }
        }
        // В конце в массив добавляются граничные значения. Реальные размеры волновода. Так график будет симметричным.
        a[countX] = (float) MainWorkWindow.elec.get_a();
        b[countY] = (float) MainWorkWindow.elec.get_b();
        l[countZ] = (float) MainWorkWindow.elec.get_l();
        // Добавление размера массива с учётом граничнымх значений равных рамзерам влновода
        countX++;   countY++;   countZ++;
    }
    /**
     * Генерирует ключ для карты максимального уровня каждого кадра.
     * @param frame
     * @return
     */
    private String createKey ( int frame ) {
        return field.toString()+"-"+wave_type.toString()+"-frame-"+frame;
    }
/*******************************************************************************
 *              Методы заполнения карты линий уровня                          *
*******************************************************************************/
    private float [][] fillingLevelMap_XY ( final int frame ) {
        float [][] level = new float [3][countXY];
        for ( int i=0; i<widthXY; i++ ) {
            for ( int j=0; j<heightXY; j++ ) {
                int x = (int) ((i/widthXY)*(countX));
                int y = (int) ((j/heightXY)*(countY));
                if ( field == FIELD.TE ) {
                    level[2][x*countY+y]=(wave_type==TYPE.E)?(float)elec.TE_E_XY(a[x],b[y]):(float)elec.TE_H_XY(a[x],b[y]);
                } else {
                    level[2][x*countY+y]=(wave_type==TYPE.E)?(float)elec.TM_E_XY(a[x],b[y]):(float)elec.TM_H_XY(a[x],b[y]);
                }
                level[0][x*countY+y]=(float)a[x];
                level[1][x*countY+y]=(float)b[y];
                if ( Double.compare( levelMaxMap.get(createKey(frame)), level[2][x*countY+y] ) < 0 ) {
                    levelMaxMap.put(createKey(frame), level[2][x*countY+y]);
                    //System.out.println( level[2][x*countY+y] );
                }                    
                N++;
            }
        }
        return level;
    }
    private float [][] fillingLevelMap_XZ ( final int frame ) {
        float [][] level = new float [3][countXZ];
        for ( int i=0; i<heightXZ; i++ ) {
            for ( int k=0; k<widthXZ; k++ ) {
                int x = (int) ((i/heightXZ)*(countX));
                int z = (int) ((k/widthXZ)*(countZ));
                if ( field == FIELD.TE ) {
                    level[2][x*countZ+z]=(wave_type==TYPE.E)?(float)elec.TE_E_XZ(l[z],a[x]):(float)elec.TE_H_XZ(l[z],a[x]);
                } else {
                    level[2][x*countZ+z]=(wave_type==TYPE.E)?(float)elec.TM_E_XZ(l[z],a[x]):(float)elec.TM_H_XZ(l[z],a[x]);
                }
                level[0][x*countZ+z]= (float) l[z];
                level[1][x*countZ+z]= (float) a[x];
                if ( Double.compare( levelMaxMap.get(createKey(frame)), level[2][x*countZ+z] ) < 0 ) {
                    levelMaxMap.put( createKey(frame), level[2][x*countZ+z] );
                    //System.out.println( level[2][x*countY+z] );
                }                    
                N++;
            }
        }
        return level;
    }
    private float [][] fillingLevelMap_YZ ( final int frame ) {
        float [][] level = new float [3][countYZ];
        for ( int j=0; j<heightYZ; j++ ) {
            for ( int k=0; k<widthYZ; k++ ) {
                int y = (int) ((j/heightYZ)*(countY));
                int z = (int) ((k/widthYZ)*(countZ));
                if ( field == FIELD.TE ) {
                    level[2][y*countZ+z]=(wave_type==TYPE.E)?(float)elec.TE_E_YZ(l[z],b[y]):(float)elec.TE_H_YZ(l[z],b[y]);
                } else {
                    level[2][y*countZ+z]=(wave_type==TYPE.E)?(float)elec.TM_E_YZ(l[z],b[y]):(float)elec.TM_H_YZ(l[z],b[y]);
                }
                level[0][y*countZ+z]= (float) l[z];
                level[1][y*countZ+z]= (float) b[y];          
                if ( Double.compare( levelMaxMap.get(createKey(frame)), level[2][y*countZ+z] ) < 0 ) {
                    levelMaxMap.put( createKey(frame), level[2][y*countZ+z] );
                    //System.out.println( level[2][y*countY+z] );
                }                    
                N++;
            }
        }
        return level;
    }
    /** Класс обёртка для массива */
    class levelList {public float [][] level;}
//================== Создание текстур ==========================================
    private void createTexture_all ( final int frame ) {
        levelMaxMap.put( createKey(frame), -10f );
        // Установление уровня среза для трёх проекций
        elec.setDepthX( 0 ); elec.setDepthY( 0 ); elec.setDepthZ( 0 );
        levelList [] level_list = new levelList[] { new levelList(), new levelList(), new levelList() };                
        Thread thread_0 = new Thread ( ()->level_list[0].level = fillingLevelMap_XY( frame ));
        Thread thread_1 = new Thread ( ()->level_list[1].level = fillingLevelMap_XZ( frame ));
        Thread thread_2 = new Thread ( ()->level_list[2].level = fillingLevelMap_YZ( frame ));
        try {
            thread_0.start();
            thread_1.start();
            thread_2.start();
            
            thread_0.join();
            thread_1.join();
            thread_2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(WaveLevelGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Настройка градиента цветов
        gradient = ColorFill.getFill( levelMaxMap.get( createKey(frame) ), MainWorkWindow.fill );
        Thread thread_3 = new Thread ( ()->frameXY.put( frame, draw_xy( level_list[0].level ) ));
        Thread thread_4 = new Thread ( ()->frameXZ.put( frame, draw_xz( level_list[1].level ) ));
        Thread thread_5 = new Thread ( ()->frameYZ.put( frame, draw_yz( level_list[2].level ) ));
        try {
            thread_3.start();
            thread_4.start();
            thread_5.start();
            
            thread_3.join();
            thread_4.join();
            thread_5.join();
        } catch ( InterruptedException ex ) {
            Logger.getLogger(WaveLevelGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createTexture_XY ( final int frame ) {
        // Установление уровня среза для трёх проекций
        elec.setDepthZ( 0 );
        float [][] levelxy = fillingLevelMap_XY( frame );
        // Настройка градиента цветов
        gradient = ColorFill.getFill( levelMaxMap.get( createKey(frame) ), MainWorkWindow.fill );
        // Заполнение карты линий уровня
        BufferedImage xy = draw_xy( levelxy );
        // Заполнение буфера текстур
        frameXY.put( frame, xy );        
    }
    private void createTexture_XZ ( final int frame ) {
        // Установление уровня среза для трёх проекций
        elec.setDepthY( 0 );
        float [][] levelxz = fillingLevelMap_XZ( frame );
        // Настройка градиента цветов
        gradient = ColorFill.getFill( levelMaxMap.get( createKey(frame) ), MainWorkWindow.fill );
        // Заполнение карты линий уровня
        // Заполнение карты линий уровня        
        BufferedImage xz = draw_xz( levelxz );
        // Заполнение буфера текстур
        frameXZ.put( frame, xz );        
    }
    private void createTexture_YZ ( final int frame ) {
        // Установление уровня среза для трёх проекций
        elec.setDepthX( 0 );
        float [][] levelyz = fillingLevelMap_YZ( frame );        
        // Настройка градиента цветов
        gradient = ColorFill.getFill( levelMaxMap.get( createKey(frame) ), MainWorkWindow.fill );
        // Заполнение карты линий уровня
        // Заполнение карты линий уровня        
        BufferedImage yz = draw_yz( levelyz );
        // Заполнение буфера текстур
        frameYZ.put( frame, yz );        
    }
    
    private BufferedImage create_XY_slice ( final double slice, final int frame ) {
        // Установление уровня среза
        elec.setDepthZ( slice );
        // Заполнение карты линий уровня        
        // создание текстур в потоке диспечерезации событий
        return draw_xy( fillingLevelMap_XY( frame ) );
    }
    private BufferedImage create_XZ_slice ( final double slice, final int frame ) {
        // Установление уровня среза
        elec.setDepthY( slice );
        // Заполнение карты линий уровня
        // создание текстур в потоке диспечерезации событий
        return draw_xz( fillingLevelMap_XZ( frame ) );
    }
    private BufferedImage create_YZ_slice ( final double slice, final int frame ) {
        // Установление уровня среза
        elec.setDepthX( slice );
        // Заполнение карты линий уровня        
        // создание текстур в потоке диспечерезации событий        
        return draw_yz( fillingLevelMap_YZ( frame ) );
    }    
//=================== Методы создания текстуры =================================
    public BufferedImage draw_xy ( final float level[][] ) {
        BufferedImage bufimage = new BufferedImage( (int) widthXY, (int) heightXY, BufferedImage.TYPE_INT_ARGB );
        for ( int i=0; i<(int) widthXY; i++ ) {
            for ( int j=0; j<(int) heightXY; j++ ) {
                int x = (int) ((i/widthXY)*(countX));
                int y = (int) ((j/heightXY)*(countY));
                Color color = gradient.get_Color( level[2][ x*this.countY+y ] );
                bufimage.setRGB( i, j, color.getRGB() );
            }
        }
        return bufimage;
    }    
    public BufferedImage draw_xz ( final float level[][] ) {
        BufferedImage bufimage = new BufferedImage( (int) widthXZ, (int) heightXZ, BufferedImage.TYPE_INT_ARGB );
        for ( int i=0; i<this.heightXZ; i++ ) {
            for ( int k=0; k<this.widthXZ; k++ ) {
                int x = (int) ((i/heightXZ)*(countX));
                int z = (int) ((k/widthXZ)*(countZ));
                Color color = gradient.get_Color( level[2][ x*this.countZ+z ] );
                bufimage.setRGB( k, i, color.getRGB() );
            }
        }
        return bufimage;
    }
    public BufferedImage draw_yz ( final float level[][] ) {
        BufferedImage bufimage = new BufferedImage( (int) widthYZ, (int) heightYZ, BufferedImage.TYPE_INT_ARGB );
        for ( int j=0; j<this.heightYZ; j++ ) {
            for ( int k=0; k<this.widthYZ; k++ ) {
                int y = (int) ((j/heightYZ)*(countY));
                int z = (int) ((k/widthYZ)*(countZ));
                Color color = gradient.get_Color( level[2][y*this.countZ+z] );
                bufimage.setRGB( k, j, color.getRGB() );
            }
        }
        return bufimage;
    }
//======================== Гетеры сетеры =======================================
    public BufferedImage getFrameXY ( final int frame ) {
        if ( this.frameXY.get(0) == null )
            return null_image;
        return frameXY.get(frame);    
    }
    public BufferedImage getFrameXZ ( final int frame ) {
        if ( this.frameXZ.get(0) == null )
            return null_image;        
        return frameXZ.get(frame);    
    }
    public BufferedImage getFrameYZ ( final int frame ) {
        if ( this.frameYZ.get(0) == null )
            return null_image;
        return frameYZ.get(frame);   
    }
    public boolean isFullMap () {
        return !( this.frameXY.get(0) == null || this.frameXZ.get(0) == null || this.frameYZ.get(0) == null );
    }
    
    public void set_l ( double l ) { this.elec.set_l(l); }
    public double get_l () { return this.elec.get_l(); }
    public void set_fps ( int fps ) {this.elec.set_fps ( fps );}
    public int get_fps () { return this.elec.get_fps();}
    public void set_Height_texture ( int height ) {
        this.heightXY = (short) height;
        calculationSizeTexture();
    }
    public void set_count ( int count ) {this.countX = count;}
    public FIELD get_field () { return this.field; }
    public TYPE get_wave_type () { return this.wave_type; }
    public void set_field ( FIELD field ) { this.field = field; }
    public void set_wave_type ( TYPE wave_type ) { this.wave_type = wave_type;}
    public ElectromagneticWave get_elec () { return this.elec; }
    
    
    
//----------------- Сериализация -----------------------------------------------    
    private void writeObject ( ObjectOutputStream out )
        throws IOException {
        // Запись дескрипторо ви полей.
        out.defaultWriteObject();
        // Запись текстуры XY
        out.writeInt( elec.get_fps() );
        for ( BufferedImage image: frameXY.values() ) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            byte[] bytes = baos.toByteArray();
            out.writeInt( bytes.length );
            out.write( bytes );
        }
        // Запись текстуры XZ
        for ( BufferedImage image: frameXZ.values() ) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos );
            byte[] bytes = baos.toByteArray();
            out.writeInt( bytes.length );
            out.write( bytes );
        }
        // Запись текстуры YZ
        for ( BufferedImage image: frameYZ.values() ) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "png", baos);
            byte[] bytes = baos.toByteArray();
            out.writeInt( bytes.length );
            out.write( bytes );
        }
    }
    private void readObject ( ObjectInputStream in ) 
            throws IOException, ClassNotFoundException {        
        in.defaultReadObject();        
        frameXY = new HashMap(0);    // Карта хранит изображения для плоскости xy
        frameXZ = new HashMap<>(0);    // Карта хранит изображения для плоскости xz
        frameYZ = new HashMap<>(0);    // Карта хранит изображения для плоскости yz            
        // Чтение XY
        int countfps = in.readInt();
        byte[][][] byteMap = new byte[3][countfps][];
        for ( int i = 0; i<countfps; i++  ) {
            int lenght = in.readInt();
            byte [] bytes = new byte[ lenght ];
            in.readFully ( bytes );        
            byteMap[0][i] = bytes;
        }
        // Чтение XZ
        for ( int i = 0; i<countfps; i++  ) {
            int lenght = in.readInt();
            byte [] bytes = new byte[ lenght ];
            in.readFully( bytes );  
            byteMap[1][i] = bytes;
        }
        // Чтение YZ
        for ( int i = 0; i<countfps; i++  ) {
            int lenght = in.readInt();
            byte [] bytes = new byte[ lenght ];
            in.readFully( bytes );
            byteMap[2][i] = bytes;
        }
        
        for ( int i = 0; i<byteMap[0].length; i++ ) {
            ByteArrayInputStream bais = new ByteArrayInputStream( byteMap[0][i] );            
            BufferedImage image = ImageIO.read( bais );
            frameXY.put( i, image );
        }
        for ( int i = 0; i<byteMap[1].length; i++ ) {
            ByteArrayInputStream bais = new ByteArrayInputStream( byteMap[1][i] );            
            BufferedImage image = ImageIO.read( bais );
            frameXZ.put( i, image );
        }
        for ( int i = 0; i<byteMap[2].length; i++ ) {
            ByteArrayInputStream bais = new ByteArrayInputStream( byteMap[2][i] );            
            BufferedImage image = ImageIO.read( bais );
            frameYZ.put( i, image );
        }
    }
    
}