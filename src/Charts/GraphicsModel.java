package Charts;

import Computation.JMatrix;
import Exceptions.MatrixDifferentDimensionsException;
import Settings.myColor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import rectangularwaveguide.MainWorkWindow;
//import sun.awt.PeerEvent;
/**
 * Модель прямоугольного волновода
 * @author Vladislav
 */
public class GraphicsModel implements myColor, Serializable {
    // Ширина и высота окна моделей
    public static int WIDTH = 350, HIGHT = 350;                             // Размер буфера
    //private BufferedImage buffer;                            // Буфер для рендера
    //private Box tris;                                        // Хранит вершины фигуры
    
    public static final Map<Integer,Float> zBuffer = new ConcurrentHashMap ();
    private Model model = new Model();
    
    class Model implements Serializable {
        public transient BufferedImage buffer;
        public Box box;
                        
        //public float[] zBuffer = new float[WIDTH*HIGHT];
        
        //public synchronized float getZBuffer ( int index ) {
        //    return zBuffer[index];
        //}
        //public synchronized void setZBuffer ( int index, float value ) {
        //    zBuffer[index] = value;
        //}
    }
    
    // Начальная матрица трансформирования
    private float [][] transform = new float [][] {{1f, 0f, 0f, 0f},        
                                                   {0f, 1f, 0f, 0f},
                                                   {0f, 0f, 1f, 0f},
                                                   {0f, 0f, 0f, 1f}};    
    // Текущий отображаемый кадр
    private int frame = 0;
    // Начальные отступы для модели
    private float dx, dy;
    // Размеры волновода
    private int width, hight, length;
    // Начальный поворот - "Изометрическая проекция"
    private final int ax0 = -25, ay0 = 35;
    // хранит углы поворота модели
    private float rotX = ax0, rotY = ay0;
    
    private WaveLevelGraph waveLevelGraph;
    // Панель которую нужно перерисовывать при смене позиции модели или текстуры.
    public JPanel renderPane;
    
    public GraphicsModel ( WaveLevelGraph waveLevelGraph ) {
        this.rotX = (float) Math.toRadians( ax0 );
        this.rotY = (float) Math.toRadians( ay0 );
        this.waveLevelGraph = waveLevelGraph;
        model.buffer = new BufferedImage( WIDTH, HIGHT, BufferedImage.TYPE_INT_ARGB );
        setRasterSize( WIDTH, HIGHT );   
        // Установка размеров волновода. В соответствии с указанными параметрами
        //this.width = (int) (waveLevelGraph.get_elec().get_a()*1e3)*2;
        //this.hight = (int) (waveLevelGraph.get_elec().get_b()*1e3)*2;
        //this.length = (int) (waveLevelGraph.get_elec().get_l()*1e3);
        // Создание кадров
        waveLevelGraph.createFrame_All();
    }
    /**
     * Высчитывает отступы а также размеры волновода.
     * Необходимо вызывать при изменении размера растра.
     * @param w
     * @param h 
     */
    public final void setRasterSize ( int w, int h ) {
        WIDTH = w; HIGHT = h;
        //model.zBuffer = new float[ w*h ];
        // Определяет размеры волновода
        this.dx = w/2;
        this.dy = h/2;
        width = (int)(w*0.45);
        hight = (int)(h*0.35);
        length = (w+(int)(h*0.5))/2;
        model.buffer = new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
        model.box = new Box ( width, hight, length );
        indentRecalculation();
    }
    
    private void readObject ( ObjectInputStream in ) 
        throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            setRasterSize( WIDTH, HIGHT );
    }
    
    public final void indentRecalculation () {        
        if ( this.waveLevelGraph.isFullMap()  )
            set_texture_frame( MainWorkWindow.slider_frame.getValue() );
        else return;
        standardPosition();
    }
    
    public void standardPosition () {
        resetTransformationMatrix();
        try {            
            CordShift ( width/2, hight/2, length/2 );
            RotationY( get_rot_y () );
            RotationX( get_rot_x () );
            CordShift ( -dx, -dy, -1f );
        } catch (MatrixDifferentDimensionsException ex) {
            Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
        } 
        // Обновление буфера в потоке диспечерезации событий        
        render_update();
    }
    
    public void movePosition ( float x, float y ) {
        resetTransformationMatrix();
        try {            
            CordShift ( width/2, hight/2, length/2 );
            RotationY( y );
            RotationX( x );
            CordShift ( -dx, -dy, -1f );
        } catch (MatrixDifferentDimensionsException ex) {
            Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
        } 
        // Обновление буфера в потоке диспечерезации событий        
        render_update();
    }
    
    public float get_rot_x () {return rotX;}
    public float get_rot_y () {return rotY;}    
    public void set_rot_x ( float x ) {this.rotX = x;}
    public void set_rot_y ( float y ) {this.rotY = y;}
        
    public synchronized void set_texture_frame ( int frame ) {
        // Установление текстур            
        this.frame = frame;
        //System.out.println( "текстура = " + waveLevelGraph.getFrameXZ(frame) );
        // Верх низ
        model.box.set_top( waveLevelGraph.getFrameXZ(frame) );
        model.box.set_top( waveLevelGraph.getFrameXZ(frame) );
        model.box.set_bottom( waveLevelGraph.getFrameXZ(frame) );
        // Перед зад
        model.box.set_front( waveLevelGraph.getFrameXY(frame) );
        model.box.set_backside( waveLevelGraph.getFrameXY(frame) );
        // Лево право
        model.box.set_leftside( waveLevelGraph.getFrameYZ(frame) );
        model.box.set_rightside( waveLevelGraph.getFrameYZ(frame) );
        standardPosition();
    }
    /**Сбрасывает матрицу трансформации*/
    public void resetTransformationMatrix () {
        this.transform = new float [][]{{ 1f, 0f, 0f, 0f },
                                        { 0f, 1f, 0f, 0f },
                                        { 0f, 0f, 1f, 0f },
                                        { 0f, 0f, 0f, 1f }};        
    }
    /** Обновляет модель */
    @SuppressWarnings("valuegoeshere")
    public void render_update () {
        SwingUtilities.invokeLater ( ()->{ 
            clearBuffer();
            rasterizationBarecentricCoord();
            if ( renderPane != null )
                    this.renderPane.repaint();
        });
        //Toolkit.getDefaultToolkit().getSystemEventQueue()
        //    .postEvent( new PeerEvent(Toolkit.getDefaultToolkit(), () -> {
        //        clearBuffer();
        //        rasterizationBarecentricCoord();                
        //}, PeerEvent.ULTIMATE_PRIORITY_EVENT ) );                
    }
    /**Очищает буфер рендера*/
    public void clearBuffer () {
        Graphics2D gr = (Graphics2D) model.buffer.getGraphics();
        gr.setPaint( myColor.background );
        gr.fillRect( 0, 0, model.buffer.getWidth(), model.buffer.getHeight() );
    }
    /**Растерезация буфера при момощи барицентрических координтат*/
    public synchronized void rasterizationBarecentricCoord () {
        // Z-буфферезация
        for ( int x = 0; x < model.buffer.getWidth()*model.buffer.getHeight(); x++ )
            zBuffer.put( x, Float.NEGATIVE_INFINITY );
        
        //================== Рендек ====================================
        //System.out.println( "model: "+model.box.model.size() );
        //ArrayList<Poligon> poligon = (ArrayList<Poligon>) model.box.get;
        
        /**
        long time1 = System.nanoTime();        
        
        Thread threadRasterizationPoligon_0 = new Thread(()->{  
            try {
                rasterizationPoligon(model.box.model.get(0));
                rasterizationPoligon(model.box.model.get(1));
                
                rasterizationPoligon(model.box.model.get(2));
                rasterizationPoligon(model.box.model.get(3));
                
                rasterizationPoligon(model.box.model.get(4));                        
                rasterizationPoligon(model.box.model.get(5));
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Thread threadRasterizationPoligon_1 = new Thread(()->{
            try {
                rasterizationPoligon(model.box.model.get(6));
                rasterizationPoligon(model.box.model.get(7));
                
                rasterizationPoligon(model.box.model.get(8));
                rasterizationPoligon(model.box.model.get(9));                                
                
                rasterizationPoligon(model.box.model.get(10));
                rasterizationPoligon(model.box.model.get(11));
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        threadRasterizationPoligon_0.start();
        threadRasterizationPoligon_1.start();
        
        try {
            threadRasterizationPoligon_0.join();
            threadRasterizationPoligon_1.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        long time2 = System.nanoTime();
        System.out.println( (time2-time1)+" ms" );
        **/
        //long time1 = System.nanoTime();        
        for ( Poligon t: model.box.model ) {
            try {
                rasterizationPoligon ( t );
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //long time2 = System.nanoTime();
        //System.out.println( (time2-time1)+" ms" );
    }
    
    class vector implements Serializable {
        public float [] v1 = new float [4];
        public float [] v2 = new float [4];
        public float [] v3 = new float [4];
        
        public short minX;
        public short maxX;
        public short minY;
        public short maxY;
        
        public short dumpY;
        public short dumpX;
        
        public float triangleArea;
    }
    
    private void rasterizationPoligon ( final Poligon t  ) throws InterruptedException {
        vector vec = new vector (); 

        for ( short j = 0; j < this.transform.length; j++ ) {
            for ( short i = 0; i < t.v[0].cor.length; i++  ) {
                vec.v1[j] += t.v[0].cor[i]*this.transform[i][j];
                vec.v2[j] += t.v[1].cor[i]*this.transform[i][j];
                vec.v3[j] += t.v[2].cor[i]*this.transform[i][j];
            }
        }        
        // Вычисляет коэффициенты для решения уравнения
        // Ииспользуется афинное преобразование координат
        // ДЛя наложения тектсуры нужно решить уравнения
        // Данный метод решает уравнения и полученные коэффиценты
        // Используются для сопаставляния текстуры и положения полигона
        
        t.calculat_Coef( new myPoint [] {new myPoint( vec.v1[0],vec.v1[1] ), new myPoint ( vec.v2[0],vec.v2[1] ), new myPoint( vec.v3[0],vec.v3[1] ) });
        vec.triangleArea = (vec.v1[1]-vec.v3[1])*(vec.v2[0]-vec.v3[0])+(vec.v2[1]-vec.v3[1])*(vec.v3[0]-vec.v1[0]);        
        vec.minX = (short) Math.max( 0, Math.ceil(Math.min(vec.v1[0], Math.min(vec.v2[0], vec.v3[0]))));
        vec.maxX = (short) Math.min( model.buffer.getWidth()-1, Math.floor(Math.max(vec.v1[0], Math.max(vec.v2[0], vec.v3[0]))));
        vec.minY = (short) Math.max( 0, Math.ceil(Math.min(vec.v1[1], Math.min(vec.v2[1], vec.v3[1]))));        
        vec.maxY = (short) Math.min( model.buffer.getHeight()-1, Math.floor(Math.max(vec.v1[1], Math.max(vec.v2[1], vec.v3[1]))));
        
        
        vec.dumpX = (short) (((vec.maxX-vec.minX)<<1)+vec.minX);
        vec.dumpY = (short)(((vec.maxY-vec.minY)<<1)+vec.minY);
        // Барецентрические координаты в сумме равны единице
        // Это позволяет высчитать b3 как 1-b1-b2 и тем самым немного уменошить вычисления на каждой итерации
        //long time1 = System.nanoTime();        
        Thread rasterization_oct_11 = new Thread (()->{
            for ( short x = vec.minX; x < vec.dumpX; x++ ) {
                for ( short y = vec.minY; y < vec.dumpY; y++ ) {
                    float b1 = ((y-vec.v3[1])*(vec.v2[0]-vec.v3[0])+(vec.v2[1]-vec.v3[1])*(vec.v3[0]-x))/vec.triangleArea;
                    float b2 = ((y-vec.v1[1])*(vec.v3[0]-vec.v1[0])+(vec.v3[1]-vec.v1[1])*(vec.v1[0]-x))/vec.triangleArea;
                    float b3 = 1-b1-b2;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        // Z-буфферезация
                        float depth = b1*vec.v1[2]+b2*vec.v2[2]+b3*vec.v3[2];
                        int zIndex = y*model.buffer.getWidth()+x;                    
                        if ( zBuffer.get( zIndex ) < depth ) {
                            model.buffer.setRGB( x, y, t.get_texture().getRGB((int)t.get_xt(x,y),(int)t.get_yt(x,y)) );
                            zBuffer.put( zIndex, depth );
                        }
                    }
                }
            }
        });
        Thread rasterization_oct_12 = new Thread (()->{
            for ( short x = vec.dumpX; x <= vec.maxX; x++ ) {
                for ( short y = vec.minY; y < vec.dumpY; y++ ) {
                    float b1 = ((y-vec.v3[1])*(vec.v2[0]-vec.v3[0])+(vec.v2[1]-vec.v3[1])*(vec.v3[0]-x))/vec.triangleArea;
                    float b2 = ((y-vec.v1[1])*(vec.v3[0]-vec.v1[0])+(vec.v3[1]-vec.v1[1])*(vec.v1[0]-x))/vec.triangleArea;
                    float b3 = 1-b1-b2;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        // Z-буфферезация
                        float depth = b1*vec.v1[2]+b2*vec.v2[2]+b3*vec.v3[2];
                        int zIndex = y*model.buffer.getWidth()+x;
                        if ( zBuffer.get( zIndex ) < depth ) {
                            model.buffer.setRGB( x, y, t.get_texture().getRGB((int)t.get_xt(x,y),(int)t.get_yt(x,y)) );
                            zBuffer.put( zIndex, depth );
                        }
                    }
                }
            }
        });
        Thread rasterization_oct_21 = new Thread (()->{
            for ( short x = vec.minX; x < vec.dumpX; x++ ) {
                for ( short y = vec.dumpY; y <= vec.maxY; y++ ) {
                    float b1 = ((y-vec.v3[1])*(vec.v2[0]-vec.v3[0])+(vec.v2[1]-vec.v3[1])*(vec.v3[0]-x))/vec.triangleArea;
                    float b2 = ((y-vec.v1[1])*(vec.v3[0]-vec.v1[0])+(vec.v3[1]-vec.v1[1])*(vec.v1[0]-x))/vec.triangleArea;
                    float b3 = 1-b1-b2;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        // Z-буфферезация
                        float depth = b1*vec.v1[2]+b2*vec.v2[2]+b3*vec.v3[2];
                        int zIndex = y*model.buffer.getWidth()+x;
                        if ( zBuffer.get( zIndex ) < depth ) {
                            model.buffer.setRGB( x, y, t.get_texture().getRGB((int)t.get_xt(x,y),(int)t.get_yt(x,y)) );
                            zBuffer.put( zIndex, depth );
                        }
                    }
                }
            }
        });
        Thread rasterization_oct_22 = new Thread (()->{
            for ( short x = vec.dumpX; x <= vec.maxX; x++ ) {
                for ( short y = vec.dumpY; y <= vec.maxY; y++ ) {
                    float b1 = ((y-vec.v3[1])*(vec.v2[0]-vec.v3[0])+(vec.v2[1]-vec.v3[1])*(vec.v3[0]-x))/vec.triangleArea;
                    float b2 = ((y-vec.v1[1])*(vec.v3[0]-vec.v1[0])+(vec.v3[1]-vec.v1[1])*(vec.v1[0]-x))/vec.triangleArea;                    
                    float b3 = 1-b1-b2;
                    if ( b1>=0 && b1<=1 && b2>=0 && b2<=1 && b3>=0 && b3<=1 ) {
                        // Z-буфферезация
                        float depth = b1*vec.v1[2]+b2*vec.v2[2]+b3*vec.v3[2];
                        int zIndex = y*model.buffer.getWidth()+x;
                    
                        if ( zBuffer.get( zIndex ) < depth ) {
                            int color = t.get_texture().getRGB((int)t.get_xt(x,y),(int)t.get_yt(x,y));
                            model.buffer.setRGB( x, y, color );
                            zBuffer.put( zIndex, depth );
                        }
                    }
                }
            }
        });
        rasterization_oct_11.setDaemon( true );        
        rasterization_oct_11.setPriority( Thread.MAX_PRIORITY );        
        rasterization_oct_11.setName("thread_rasterization_oct_11");
        
        rasterization_oct_12.setDaemon( true );        
        rasterization_oct_12.setPriority( Thread.MAX_PRIORITY );
        rasterization_oct_12.setName("thread_rasterization_oct_12");

        rasterization_oct_21.setDaemon( true );        
        rasterization_oct_21.setPriority( Thread.MAX_PRIORITY );
        rasterization_oct_21.setName("thread_rasterization_oct_21");
        
        rasterization_oct_22.setDaemon( true );        
        rasterization_oct_22.setPriority( Thread.MAX_PRIORITY );
        rasterization_oct_22.setName("thread_rasterization_oct_22");

        rasterization_oct_11.start();        
        rasterization_oct_12.start();
        rasterization_oct_21.start();        
        rasterization_oct_22.start();
        
        rasterization_oct_11.join();
        rasterization_oct_12.join();
        rasterization_oct_21.join();
        rasterization_oct_22.join();

        //long time2 = System.nanoTime();
        //System.out.println( (time2-time1)+" nanos" );
    }
    
    private float [] rasterizationMatrixCalculation ( final float [] cor, final float [][] transform ) {
        return JMatrix.Multiplication( cor, transform );
    }
    
//========== Афинные преобразования Объектов ===================================
    /**
     * Растяжение сжатие
     * @param kx
     * @param ky
     * @param kz
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void Scaling ( float kx, float ky, float kz ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ kx, 0f, 0f, 0f },
                                       { 0f, ky, 0f, 0f },
                                       { 0f, 0f, kz, 0f },
                                       { 0f, 0f, 0f, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Сдвиг
     * @param dx
     * @param dy
     * @param dz 
     * @throws Exceptions.MatrixDifferentDimensionsException 
     */
    public void Shift ( float dx, float dy, float dz ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ 1f, 0f, 0f, 0f },
                                       { 0f, 1f, 0f, 0f },
                                       { 0f, 0f, 1f, 0f },
                                       { dx, dy, dz, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
//======================== Поворот =============================================
    /**
     * Повророт на угол f вокруг оси x
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void RotationX ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ 1f, 0f,                0f,                 0f },
                                       { 0f, (float)Math.cos(f),(float)Math.sin(f), 0f },
                                       { 0f,-(float)Math.sin(f),(float)Math.cos(f), 0f },
                                       { 0f, 0f,                0f,                 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Повророт на угол f вокруг оси y
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void RotationY ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ (float)Math.cos(f), 0f, (float)Math.sin(f), 0f },
                                       { 0f,                 1f, 0f,                 0f },
                                       {-(float)Math.sin(f), 0f, (float)Math.cos(f), 0f },
                                       { 0f,                 0f, 0f,                 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Повророт на угол f вокруг оси z
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void RotationZ ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ (float)Math.cos(f),(float)Math.sin(f), 0f, 0f },
                                       {-(float)Math.sin(f),(float)Math.cos(f), 0f, 0f },
                                       { 0f,                0f,                 1f, 0f },
                                       { 0f,                0f,                 0f, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
//========== Афинные преобразования координат ==================================
    /**
     * Растяжение сжатие
     * @param kx
     * @param ky
     * @param kz
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void CordScaling ( float kx, float ky, float kz ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ (1f/kx), 0f,      0f,     0f },
                                       { 0f,      (1f/ky), 0f,     0f },
                                       { 0f,      0f,      (1/kz), 0f },
                                       { 0f,      0f,      0f,     1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Сдвиг
     * @param dx
     * @param dy
     * @param dz 
     * @throws Exceptions.MatrixDifferentDimensionsException 
     */
    public void CordShift ( float dx, float dy, float dz ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ 1f, 0f, 0f, 0f },
                                       { 0f, 1f, 0f, 0f },
                                       { 0f, 0f, 1f, 0f },
                                       {-dx,-dy,-dz, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
//======================== Поворот =============================================
    /**
     * Повророт на угол f вокруг оси x
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void CordRotationX ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ 1f, 0f,                 0f,                 0f },
                                       { 0f, (float)Math.cos(f),-(float)Math.sin(f), 0f },
                                       { 0f, (float)Math.sin(f), (float)Math.cos(f), 0f },
                                       { 0f, 0f,                 0f,                 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Повророт на угол f вокруг оси y
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void CordRotationY ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ (float)Math.cos(f), 0f,-(float)Math.sin(f), 0f },
                                       { 0f,                 1f, 0f,                 0f },
                                       { (float)Math.sin(f), 0f, (float)Math.cos(f), 0f },
                                       { 0f,                 0f, 0f,                 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Повророт на угол f вокруг оси z
     * @param f угол
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void CordRotationZ ( float f ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][] {{ (float)Math.cos(f),-(float)Math.sin(f), 0f, 0f },
                                       { (float)Math.sin(f), (float)Math.cos(f), 0f, 0f },
                                       { 0f,                 0f,                 1f, 0f },
                                       { 0f,                 0f,                 0f, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
//======================== Проецирование =======================================
    /**
     * Косиугольное проецирование
     * @param l
     * @param a
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void ObliqueProjection ( float l, float a ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][]{{ 1f,                   1f,                   0f, 0f },
                                      { 0f,                   0f,                   0f, 0f },
                                      { l*(float)Math.cos(a), l*(float)Math.sin(a), 0f, 0f },
                                      { 0f,                   0f,                   0f, 1f }};                
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Центральное проецирование
     * @param d
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void CenterProjection ( float d ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][]{{ 1f, 1f, 0f, 0f            },
                                      { 0f, 0f, 0f, 0             },
                                      { 0f, 0f, 1f, (float)(1f/d) },
                                      { 0f, 0f, 0f, 0f            }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Ортогональная проекция
     * @param z0
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void OrthogonalProjection ( float z0 ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][]{{ 1f, 0f, 0f, 0f },
                                      { 0f, 1f, 0f, 0f },
                                      { 0f, 0f, 0f, 0f },
                                      { 0f, 0f, z0, 1f }};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Аксонометрическая проекция
     * @param a
     * @param b
     * @throws Exceptions.MatrixDifferentDimensionsException
     */
    public void AxonometricProjection ( float a, float b ) throws MatrixDifferentDimensionsException {
        float [][] t = new float [][]{{ (float)Math.cos(a),(float)(Math.sin(a)*Math.cos(b)),(float)(Math.sin(a)*Math.sin(b)),0f},
                                      {-(float)Math.sin(a),(float)(Math.cos(a)*Math.cos(b)),(float)(Math.cos(a)*Math.sin(b)),0f},
                                      { 0f,               -(float)Math.sin(b),              (float)Math.cos(b),              0f},
                                      { 0f,                0f,                              0f,                              1f}};
        this.transform = JMatrix.Multiplication( this.transform, t );
    }
    /**
     * Перспективная центральная проекция
     * @param a
     * @param b
     * @param z
     */
    public void PerspectiveProjection ( float a, float b, float z ) {        
        float zk = 1_200f;          // Положение камеры
        float zp = 200f;            // Положение проецирующей плоскости
        float d = (zk-zp)/(zk-z);
        float [][] t1 = new float [][]{{d, 0f, 0f,  0f},
                                       {0f,d,  0f,  0f},
                                       {0f,0f, 1f,  0f},
                                       {0f,0f,-zp, 1f}};
        try {
            // Сначала выполняется поворот потом проецирование согластно t1
            CordRotationZ(a);
            CordRotationX(b);
            this.transform = JMatrix.Multiplication( this.transform, t1 );
        } catch (MatrixDifferentDimensionsException ex) {
            Logger.getLogger(GraphicsModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage get_buffer () {
        return model.buffer;
    }    
    public int get_frame () {
        return this.frame;
    }
    
    public WaveLevelGraph getWave () { return this.waveLevelGraph; }
    
    public void sirializabled () {
        File file = new File ( "waweModel.data" );        
        try {
            FileOutputStream streamOut = new FileOutputStream ( file, false );
            ObjectOutputStream objectOut = new ObjectOutputStream ( streamOut );     
            objectOut.writeObject( this );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public static GraphicsModel desirializabled () {
        File file = new File ( "waweModel.data" );        
        GraphicsModel model = null;
        try {
            FileInputStream streamIn = new FileInputStream ( file );
            ObjectInputStream objectIn = new ObjectInputStream ( streamIn ); 
            model = (GraphicsModel) objectIn.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {    
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }
}
/**
 * Класс вершины. Вершина хранит три координаты.
 * @author Vladislav
 */
class Vertex implements Serializable {
    public final float cor [] = new float [4];
    public final float x;
    public final float y;
    public final float z;
    
    public Vertex ( float x, float y, float z ) {
        this.cor[0] = x;
        this.cor[1] = y;
        this.cor[2] = z;
        this.cor[3] = 1;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

/**
 * Класс Полигон. Полигон состоит из вершин. У полигона есть цвет заливки.
 * @author Vladislav
 */
class Poligon implements Serializable {  
    public Color color;                         // Хранит цвет полигона
    public final Vertex [] v;                         // Хранит вершины
    private float A, B, C, D, E, F;     // Коэффициенты для решения уравнения
    public float det;
    private myPoint [] uv;
    private transient BufferedImage texture;
    
    public Poligon ( final Vertex ... v ) {
        this.v = v;
        this.color = Color.red;
        set_textur_map ( new myPoint [] { new myPoint(0,0), new myPoint(1,0), new myPoint(0,1) } );
    }
    /**Устанавливает цвет полигона @param color*/
    public void setColor ( final Color color ) {
        this.color = color;
    }
    public final void set_textur_map ( final myPoint [] uv ) {
        this.uv = uv;
    }
    public void set_texture ( final Image image ) {
        // Определение текстурных координат
        this.texture = (BufferedImage) image;
    }
    public void calculat_Coef ( final myPoint [] xy ) {
        // Задание координат объекта
        float X1, X2, X3;           // Координаты текстуры
        float Y1, Y2, Y3;           // Координаты текстуры
        // Задание координат текстуры
        X1 = texture.getWidth()*uv[0].x; 
        X2 = texture.getWidth()*uv[1].x;
        X3 = texture.getWidth()*uv[2].x;
        
        Y1 = texture.getHeight()*uv[0].y; 
        Y2 = texture.getHeight()*uv[1].y;
        Y3 = texture.getHeight()*uv[2].y;
        // Детерминант матрицы
        det = xy[0].x*(xy[1].y-xy[2].y)+xy[1].x*(xy[2].y-xy[0].y)+xy[2].x*(xy[0].y-xy[1].y);
        A = (X1*(xy[1].y-xy[2].y)+X2*(xy[2].y-xy[0].y)+X3*(xy[0].y-xy[1].y))/det;
        B = (X1*(xy[2].x-xy[1].x)+X3*(xy[1].x-xy[0].x)+X2*(xy[0].x-xy[2].x))/det;
        C = X1-A*xy[0].x-B*xy[0].y;
        D = (Y1*(xy[1].y-xy[2].y)+Y2*(xy[2].y-xy[0].y)+Y3*(xy[0].y-xy[1].y))/det; 
        E = (xy[0].x*(Y2-Y3)+xy[1].x*(Y3-Y1)+xy[2].x*(Y1-Y2))/det;
        F = Y1-D*xy[0].x-E*xy[0].y;
    }
    public BufferedImage get_texture () {
        return texture;
    }
    public float get_xt ( final float x, final float y ) {
        float xt = A*x+B*y+C;
        if ( xt < 0 )
            xt = 0;
        if ( xt >= texture.getWidth() )
            xt = texture.getWidth()-1;
        return xt;
    }
    public float get_yt ( final float x, final float y ) {
        float yt = D*x+E*y+F;
        if ( yt < 0 )
            yt = 0;
        if ( yt >= texture.getHeight() )
            yt = texture.getHeight()-1;
        return yt;
    }            
}

class myPoint implements Serializable {
    public final float x;
    public final float y;
    
    public myPoint ( float x, float y ) {
        this.x = x;
        this.y = y;
    }
}

class Edge implements Serializable {
    Point [] AB;
    Point [] AC;    
    public Edge ( Point [] AB, Point [] AC ) {
        this.AB = AB;
        this.AC = AC;
    }
}

class Model implements Serializable {
    ArrayList<Poligon> model;
    public Model ( Poligon ... p ) {
        model = new ArrayList<>(p.length);
        for ( Poligon po: p )
            model.add(po);
    }
    public Model () {}
    public Model ( ArrayList<Poligon> p ) {
        model = p;
    }    
    
    public ArrayList<Poligon> getCloneModel () {
        ArrayList<Poligon> poligon = new ArrayList<Poligon>();
        model.forEach( (a)-> poligon.add(a) );
        return poligon;
    }
}

class Box extends Model implements Serializable {    
    // Верхняя сторона
    public void set_top ( Image image ) {
        if ( this.model.get(0) != null && this.model.get(1) != null ) {
            this.model.get(0).set_texture( image );
            this.model.get(1).set_texture( image );
        }        
    }
    public void set_textur_map_top ( myPoint [] point1, myPoint [] point2 ) {
        if ( this.model.get(0) != null && this.model.get(1) != null ) {
            model.get(0).set_textur_map( point1 );
            model.get(1).set_textur_map( point2 );
        }
    }
    // Нижняя сторона
    public void set_bottom ( Image image ) {
        this.model.get(2).set_texture( image );
        this.model.get(3).set_texture( image );
    }
    public void set_textur_map_bottom ( myPoint [] point1, myPoint [] point2 ) {
        model.get(2).set_textur_map( point1 );
        model.get(3).set_textur_map( point2 );
    }
    // Передняя сторона
    public void set_front ( Image image ) {
        this.model.get(4).set_texture( image );
        this.model.get(5).set_texture( image );
    }
    public void set_textur_map_front ( myPoint [] point1, myPoint [] point2 ) {
        model.get(4).set_textur_map( point1 );
        model.get(5).set_textur_map( point2 );
    }
    // Задняя сторона
    public void set_backside ( Image image ) {
        this.model.get(6).set_texture( image );
        this.model.get(7).set_texture( image );
    }
    public void set_textur_map_backside ( myPoint [] point1, myPoint [] point2 ) {
        model.get(6).set_textur_map( point1 );
        model.get(7).set_textur_map( point2 );
    }
    // Левый бок
    public void set_leftside ( Image image ) {
        this.model.get(8).set_texture( image );
        this.model.get(9).set_texture( image );
    }
    public void set_textur_map_leftside ( myPoint [] point1, myPoint [] point2 ) {
        model.get(8).set_textur_map( point1 );
        model.get(9).set_textur_map( point2 );
    }
    // Правый бок
    public void set_rightside ( Image image ) {
        this.model.get(10).set_texture( image );
        this.model.get(11).set_texture( image );
    }
    public void set_textur_map_rightside ( myPoint [] point1, myPoint [] point2 ) {
        model.get(10).set_textur_map( point1 );
        model.get(11).set_textur_map( point2 );
    }
    
    /**
     * Создаёт параллелепипед из полигонов из треёх точек.
     * @param width
     * @param hight
     * @param length
     * @return 
     */
    public Box ( int width, int hight, int length ) {
        // Заполнение вершин
        this.model = new ArrayList<>(12);    
//======================== Верхняя и нижняя части ==============================
        model.add(new Poligon ( new Vertex( 0f,    0f, 0f     ), 
                                new Vertex( width, 0f, length ), 
                                new Vertex( 0f,    0f, length )));
        model.add(new Poligon ( new Vertex( 0f,    0f, 0f     ), 
                                new Vertex( width, 0f, 0f     ), 
                                new Vertex( width, 0f, length )));        
        
        this.set_textur_map_top ( new myPoint [] { new myPoint(1,0), 
                                                   new myPoint(0,1), 
                                                   new myPoint(0,0)},
                                  new myPoint [] { new myPoint(1,0), 
                                                   new myPoint(1,1), 
                                                   new myPoint(0,1)});        
        
        model.add(new Poligon ( new Vertex( 0f,    hight, 0f     ),
                                new Vertex( width, hight, length ),
                                new Vertex( 0f,    hight, length )));
        model.add(new Poligon ( new Vertex( 0f,    hight, 0f     ), 
                                new Vertex( width, hight, 0f     ), 
                                new Vertex( width, hight, length )));
        
        this.set_textur_map_bottom ( new myPoint [] { new myPoint(1,0), 
                                                      new myPoint(0,1), 
                                                      new myPoint(0,0)},
                                     new myPoint [] { new myPoint(1,0), 
                                                      new myPoint(1,1),                                                       new myPoint(0,1)});
//______________________________________________________________________________        
//======================== Передняя и задняя части =============================
        model.add(new Poligon ( new Vertex( 0f,    0f,    length ),
                                new Vertex( 0f,    hight, length ),
                                new Vertex( width, hight, length )));
        model.add(new Poligon ( new Vertex( 0f,    0f,    length ),
                                new Vertex( width, 0f,    length ),
                                new Vertex( width, hight, length )));     
                
        set_textur_map_front ( new myPoint [] { new myPoint(0,0), 
                                                new myPoint(0,1), 
                                                new myPoint(1,1)},
                               new myPoint [] { new myPoint(0,0), 
                                                new myPoint(1,0), 
                                                new myPoint(1,1)});        
        
        model.add(new Poligon ( new Vertex( 0f,    0f,    0f ), 
                                new Vertex( 0f,    hight, 0f ), 
                                new Vertex( width, hight, 0f )));
        model.add(new Poligon ( new Vertex( 0f,    0f,    0f ), 
                                new Vertex( width, 0f,    0f ),
                                new Vertex( width, hight, 0f )));
        
        set_textur_map_backside ( new myPoint [] { new myPoint(0,0), 
                                                   new myPoint(0,1), 
                                                   new myPoint(1,1)},
                                  new myPoint [] { new myPoint(0,0), 
                                                   new myPoint(1,0), 
                                                   new myPoint(1,1)});        
//______________________________________________________________________________        
//======================== Боковые части =======================================
        model.add(new Poligon ( new Vertex( 0f, 0f,    0f     ),
                                new Vertex( 0f, hight, 0f     ),
                                new Vertex( 0f, hight, length )));
        model.add(new Poligon ( new Vertex( 0f, 0f,    0f     ),
                                new Vertex( 0f, hight, length ),
                                new Vertex( 0f, 0f,    length )));
        set_textur_map_leftside ( new myPoint [] { new myPoint(1,0), 
                                                   new myPoint(1,1), 
                                                   new myPoint(0,1)},
                                  new myPoint [] { new myPoint(1,0), 
                                                   new myPoint(0,1), 
                                                   new myPoint(0,0)});
        
        model.add(new Poligon ( new Vertex( width, 0f,    0f     ),
                                new Vertex( width, hight, 0f     ),
                                new Vertex( width, hight, length )));
        model.add(new Poligon ( new Vertex( width, 0f,    0f     ),
                                new Vertex( width, hight, length ),
                                new Vertex( width, 0f,    length )));
        set_textur_map_rightside ( new myPoint [] { new myPoint(1,0), 
                                                    new myPoint(1,1), 
                                                    new myPoint(0,1)},
                                   new myPoint [] { new myPoint(1,0), 
                                                    new myPoint(0,1),
                                                    new myPoint(0,0)});        
        // Установка цвета вершины
        model.get(0).setColor( Color.red );
        model.get(1).setColor( Color.red );
        
        model.get(2).setColor( Color.blue );
        model.get(3).setColor( Color.blue );
        
        model.get(4).setColor( Color.white );
        model.get(5).setColor( Color.white );
        
        model.get(6).setColor( Color.cyan );
        model.get(7).setColor( Color.cyan );
        
        model.get(8).setColor( Color.yellow );
        model.get(9).setColor( Color.yellow );
        
        model.get(10).setColor( Color.pink );
        model.get(11).setColor( Color.pink );          
    }        
}