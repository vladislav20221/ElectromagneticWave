package Charts;

import Computation.ElectromagneticWave;
import Exceptions.ElectromagneticException;
import Settings.myColor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Ellipse2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 * Класс для работы с графиками.
 * @author Vladislav
 */
public class GUI implements myColor {                       
    private ElectromagneticWave elec;
        
    public GUI ( ElectromagneticWave elec ) {
        this.elec = elec;
    }
//==================Построение графика для определения длины волны==============
    public void WavelengthDetermination ( ElectromagneticWave elec ) {
        long time1 = System.currentTimeMillis();        
        double N = 0;   // сщётчик итераций цикла
        // Данные
        System.out.println(elec.toString());
        XYSeries series_1 = new XYSeries("L");                                     
        XYSeries series_2 = new XYSeries("Lkr");
            series_2.add(elec.getLkr(), 0);
            series_2.add(elec.getLkr(), 0.1);
        XYSeries series_3 = new XYSeries("L=Lv");        
        for ( double L = 0; Double.compare(L, elec.getLkr()*0.99) < 0; L+=elec.getLkr()/100 ) {
            series_1.add ( L, elec.Lv_L(L) );
            series_3.add (L,L);
            N++;
        }            
            series_1.add( 0.0099, elec.Lv_L(0.0099) );
            series_1.add( elec.getLkr()*0.995, elec.Lv_L(0.0099)*1.2 );
            series_1.add( elec.getLkr()*0.999, elec.Lv_L(0.0099)*1.5 );
        // наборы данных
        XYSeriesCollection xyDateset1 = new XYSeriesCollection();
            xyDateset1.addSeries(series_1);
        XYSeriesCollection xyDateset2 = new XYSeriesCollection();
            xyDateset2.addSeries(series_2);  
            xyDateset2.addSeries(series_3);        
        // Внешний вед графика. Холст с графиком
        JFreeChart chart = ChartFactory.createXYLineChart("Lv(L)","L", "Lv",null);
        XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(graphics_background);       // цвет фона графика            
            plot.setDomainGridlinePaint(graphics_grid);     // цвет деления по y
            plot.setRangeCrosshairPaint(graphics_grid);  // цвет деления по x
            // установка набора данных
            plot.setDataset(0,xyDateset1);
            plot.setDataset(1,xyDateset2);
            // установка рендера
        XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
            render.setSeriesPaint( 0, Color.black );           
        XYLineAndShapeRenderer render2 = new XYLineAndShapeRenderer();
            render2.setSeriesPaint( 0, Color.YELLOW );    
        plot.setRenderer(0,render2);
        plot.setRenderer(1,render);
        ChartFrame frame = new ChartFrame ("Закон дисперсии",chart);       
        frame.pack();                
        frame.setVisible(true);
        long time2 = System.currentTimeMillis();
        System.out.println( "Итераций: " + N + "\tзатраченно времени: " + (time2-time1) + " ms" );
    }
//===================Построение графиков для определения типов волн=============
    public ChartPanel DefinitionWaveTypes ( double F ) {
        XYSeries series_1 = new XYSeries("H");        // Набор данных для элипса   
//============================Построение элипса=================================
        double pdt = 0;
        for ( double m = 0, n; !Double.isNaN(n = elec.DefinitionWaveTypes(m, F)); m+=0.15 ) {
            series_1.add ( m, n );
            pdt = m - 0.15;         // Точка при которой данный цикл заканчивает свою работу
        }
        // Этот цикл начинается с точки где график пересекает ось OX 
        // и заканчивается в точке где остановился прошлый цикл
        for ( double m = elec.DefinitionWaveTypesY0(F); m > pdt; m -=0.005 ) {
            series_1.add( m, elec.DefinitionWaveTypes(m,F) );
        }
        // Допустимые точки m и n
        XYSeries series_2 = new XYSeries("n m");                             
        for ( int m = (int) series_1.getMinX(); m <= series_1.getMaxX(); m++  ) {
            for ( int n = (int) series_1.getMinY(); n <= series_1.getMaxY(); n++ ) {
                if ( n <= elec.DefinitionWaveTypes(m,F) ) {
                    series_2.add(m,n);
                }                    
            }
        }
        // наборы данных
        XYSeriesCollection xyDateset1 = new XYSeriesCollection();
            xyDateset1.addSeries(series_1);            
        XYSeriesCollection xyDateset2 = new XYSeriesCollection();
            xyDateset2.addSeries(series_2);
        // Внешний вед графика. Холст с графиком
        JFreeChart chart = ChartFactory.createXYLineChart("","m", "n",null);
            GradientPaint gradient = new GradientPaint ( 20,5,color_short , 400, 300, background  );
            chart.setBackgroundPaint( gradient );
            chart.getLegend().setBackgroundPaint(color_back_font);  // Фон легенды
            // Настройка подписи осей
            ((XYPlot) chart.getPlot()).getDomainAxis().setLabelPaint( Color.white );    // Настраивает цвет легенды по игрику
            ((XYPlot) chart.getPlot()).getDomainAxis().setTickLabelPaint(Color.white );    // Настраивает цвет легенды по игрику
            ((XYPlot) chart.getPlot()).getRangeAxis().setLabelPaint( Color.white );     // Настраивает легенду по иксу
            ((XYPlot) chart.getPlot()).getRangeAxis().setTickLabelPaint(Color.white );     // Настраивает легенду по иксу
            //((XYPlot) chart.getPlot()).get.setLabelPaint( Color.white );
        XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(graphics_background);           // цвет фона графика
            plot.setDomainGridlinePaint(graphics_grid);             // закрашивает сетку по y
            plot.setRangeGridlinePaint(graphics_grid);              // закрашивает сетку по x
            // установка набора данных
            plot.setDataset(0,xyDateset1);
            plot.setDataset(1,xyDateset2);
        XYLineAndShapeRenderer render_0 = new XYLineAndShapeRenderer();     // Рендер линии элипса
            render_0.setSeriesPaint( 0, Color.black );
            //render_0.setDefaultShapesFilled(false);               // Отключает закрашивание фигуры
            render_0.setDefaultShapesVisible(false);                // Отключает прорисовку фигур
            render_0.setSeriesStroke(0, new BasicStroke(2.0f));     // Толщина линии
        XYLineAndShapeRenderer render_1 = new XYLineAndShapeRenderer();     // Рендер точек доступных мод
            render_1.setSeriesPaint( 0, Color.red );
            render_1.setSeriesLinesVisible (0,false);               // Отключение прорисовки линий
            Ellipse2D elips = new Ellipse2D.Float(0f,0f,5f,5f);
            render_1.setLegendLine( elips );                        // Устанавливает фигуру для каждой точки
            render_1.setDefaultShapesFilled(false);                 // Отключает закрашивание фигуры            
            //render_1.setDrawOutlines(false);                      // Устанавливает контур фигур
        plot.setRenderer(0,render_0); 
        plot.setRenderer(1,render_1);
        return new ChartPanel (chart);
    }
//=========================График Закона дисперсии==============================
    private void DispersionLaw () {
        ElectromagneticWave elec = null;
        ElectromagneticWave elec2 = null;
        ElectromagneticWave elec3 = null;
        elec = new ElectromagneticWave ( 10e9,20e-2,40e-2,(byte)1,(byte)1 );
        elec2 = new ElectromagneticWave ( 10e9,20e-2,40e-2,(byte)2,(byte)2 );
        elec3 = new ElectromagneticWave ( 10e9,20e-2,40e-2,(byte)3,(byte)3 );
        long time1 = System.currentTimeMillis();
//=================Построение графика дисперсионного закона=====================
        double max = elec.getFkr()*5;           // максималая точка для графика
        double N = 0;                           // счётчик числа итераций в алгоритмах построения графика
        XYSeries series_1 = new XYSeries("С");
            series_1.add( 0,ElectromagneticWave.C );
            series_1.add( max,ElectromagneticWave.C );
        XYSeries series_2 = new XYSeries("Fkr");
            series_2.add( elec.getFkr(),0 );
            series_2.add( elec.getFkr(),3.0e9 );
//==============Данные для графиков фазовой и гурповой скорости=================
        XYSeries series_3 = new XYSeries("Vgr11");
        XYSeries series_4 = new XYSeries("Vf11");
        XYSeries series_5 = new XYSeries("Vgr22");
        XYSeries series_6 = new XYSeries("Vf22");
        XYSeries series_7 = new XYSeries("Vgr33");
        XYSeries series_8 = new XYSeries("Vf33");
        for ( double f = 1e5; f <= max; f+=f*0.05 ) {
            series_3.add( f, elec.PhaseSpeedF(f) );
            series_4.add( f, elec.GroupSpeedF(f) );
            series_5.add( f, elec2.GroupSpeedF(f) );
            series_6.add( f, elec2.PhaseSpeedF(f) );
            series_7.add( f, elec3.GroupSpeedF(f) );
            series_8.add( f, elec3.PhaseSpeedF(f) );
            N++;
        }
        // наборы данных
        XYSeriesCollection xyDateset1 = new XYSeriesCollection();
            xyDateset1.addSeries(series_1);
            xyDateset1.addSeries(series_2);
        XYSeriesCollection xyDateset2 = new XYSeriesCollection();
            xyDateset2.addSeries(series_3);
            xyDateset2.addSeries(series_4);
        XYSeriesCollection xyDateset3 = new XYSeriesCollection();
            xyDateset3.addSeries(series_5);
            xyDateset3.addSeries(series_6);
        XYSeriesCollection xyDateset4 = new XYSeriesCollection();
            xyDateset4.addSeries(series_7);
            xyDateset4.addSeries(series_8);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Дисперсионный закон",
                "F (Гц)",
                "V (м/c)",
                null);
        // Внешний вед графика. Холст с графиком
        XYPlot plot = chart.getXYPlot();
            plot.setBackgroundPaint(Color.black);       // цвет фона графика
            plot.setDomainGridlinePaint(Color.red);     // цвет деления по y
            plot.setRangeCrosshairPaint(Color.yellow);  // цвет деления по x
            // установка набора данных
            plot.setDataset(0,xyDateset1);
            plot.setDataset(1,xyDateset2);
            plot.setDataset(2,xyDateset3);
            plot.setDataset(3,xyDateset4);
            // установка рендера
            plot.setRenderer(0,getRenderLineShape(Color.red, 2 ));
            plot.setRenderer(1,getRenderSpline(Color.orange, 2 ));
            plot.setRenderer(2,getRenderSpline(Color.YELLOW, 2 ));
            plot.setRenderer(3,getRenderSpline(Color.BLUE, 2 ));
        ChartFrame frame = new ChartFrame ("Закон дисперсии",chart);
        frame.pack();
        frame.setVisible(true);
        long time2 = System.currentTimeMillis();
        System.out.println( "итераций: " + N + "\tзатраченно времени: " + (time2-time1) + " ms" );
    }
    /**
     * Настраивает рендер для осевых линий.
     * @param color цвет линий.
     * @param size число набора данных.
     * @return натсроенный рендер.
     */
    private XYLineAndShapeRenderer getRenderLineShape ( Color color, int size ) {
        XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
            for ( int i = 0; i < size; i++ ) {
                render.setSeriesPaint( i, color);                       // цвет линии
                //renderer.setSeriesLinesVisible (1,false);             // удаление связуещих линий между точками
                render.setSeriesShapesVisible( i,false );               // удаление меток
                // Делает линию штрихованной
                render.setSeriesStroke( i, new BasicStroke(1.0f,
                                            BasicStroke.CAP_ROUND, 
                                            BasicStroke.JOIN_ROUND,
                                        1.0f, new float[] {10.0f, 6.0f}, 0.0f));
            }
        return render;
    }
    /**
     * Настраивает рендер для основных линий графика.
     * @param color цвет линий.
     * @param size размер набора данных.
     * @param precision сглаживание.
     * @return 
     */
    private XYSplineRenderer getRenderSpline ( Color color, int size ) {
        XYSplineRenderer render = new XYSplineRenderer();
            render.setPrecision(8);                                         // сглаживание линий
            for ( int i = 0; i < size; i++ ) {
                render.setSeriesPaint( i, color );                          // цвет линии
                System.out.println ( color.getRed()+ ":" + color.getGreen() + ":" + color.getBlue() );
                //renderer.setSeriesLinesVisible (i,false);                 // удаление связуещих линий между точками
                //render.setSeriesShapesVisible( i, false );                // удаление меток
                render.setSeriesStroke( i, new BasicStroke(2.0f) );         // настройка толщины линии
            }
         return render;
    }
}