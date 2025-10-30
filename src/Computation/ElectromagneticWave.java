package Computation;

import Exceptions.ElectromagneticException;
import java.io.Serializable;
import rectangularwaveguide.MainWorkWindow;

/**
 * Класс для вычисления различных параметров электромагнитной волны
 * @author Vladislav
 */
public class ElectromagneticWave implements Serializable, Cloneable {
    // Уникальный код сериализации
    public static final long serialVersionUID = -475065295469253554L;
// параметры электромагнитной волны. Всё в СИ.
    // Константы
    public static final double C = 2.9979*1e8d;         // скосроть света в вакууме (м/с)
    public static final double E0 = 8.854*1e-12d;       // электрическая постоянная (Ф/м)
    public static final double U0 = 1.257*1e-6d;        // магнитная постоянная (Гн/м)
    // Волновое число
    private double B;                                   // Фазовая постоянная
    private double Ks;                                  // поперечная составляющая волнового числа
    private double Kx;                                  // составляющая K
    private double Ky;                                  // Состовляющая K
    private double K;                                   // волновое число
    private double Kapr;                                // аппертура волновода
    // Время
    private double t0;                                  // начальное время волны (с)
    private double t;                                   // текущее время кадра (с)
    private double dt;                                  // приращение для времени (с)
    private int fps;                                    // Количество кадров в анимации
    // Определение плоскостей среза
    private double depthZ;                              // Определяет плоскость для проекции xy
    private double depthX;                              // Определяет плоскость для проекции yz
    private double depthY;                              // Определяет плоскость для проекции xz
    
    private double L;                                   // длина волны (м)
    private double Lkr;                                 // критическая длина волны для данного волновода (м)
    private double Lv;                                  // длина волны в водноводе (м)
    private double F;                                   // частота. Как правило будет задана она (Гц)
    private double Fkr;                                 // критическая частота (Гц)
    private double FkrH10;                              // Критическая частота волны основного типа.
    private double LkrH10;                              // Критическая длина волны основного типа.
    private double Fv;                                  // частота в волноводе (Гц)
    private double Vgr;                                 // груповая скорость (м/с)
    private double Vf;                                  // фазовая скорость (м/с)
    private double W;                                   // циклическая частота (рад/с)
    private double T;                                   // периуд колебанний (пико секунды)
    // параметры волновода
    private double a;                                   // ширина стенки (м)
    private double b;                                   // высота стенки (м)
    private double l;                                   // длина стенки (м)
    // моды
    private byte m;
    private byte n;
    // Параметры среды
    private double E = 1d;                              // относительная диэлектрическая проницаеамость среды
    private double Ea = E0*E;                           // абсолютная диэлектрическая проницаемость
    private double U = 1d;                              // относительная магнитная проницаемость
    private double Ua = U0*U;                           // абсолютная магнитная проницаемость
    // H-волны это TE- волны m и n могут принимать нулевые значения но не одновременно,
    // E-волны это TM-волны m и n не могут принимать нулевые значения
    private double Z_0;                                 // Волновое сопротивление среды заполняющей, волновод (Ом)
    private double Z_TE;                                // Волновое сопротивление TE волны (Ом)
    private double Z_TM;                                // Волновое сопротивление TM волны (Ом)
// Переменные необходимые для ускорения вычислений
    // Вычисление составляющих электромагнитного поля
    private double TEHX;
    private double TEHY;
    private double TEEX;
    private double TEEY;
    
    private double TMHX;
    private double TMHY;
    private double TMEX;
    private double TMEY;

    private double TEH_XY_x;
    private double TEH_XY_y;
    private double TEH_XY_z;
    private double TEE_XY_x;
    private double TEE_XY_y;
    
    private double TEH_XZ_x;
    private double TEH_XZ_y;
    private double TEH_XZ_z;
    private double TEE_XZ_x;
    private double TEE_XZ_y;
    
    private double TEH_YZ_x;
    private double TEH_YZ_y;
    private double TEH_YZ_z;
    private double TEE_YZ_x;
    private double TEE_YZ_y;
        
    private double TME_XY_x;
    private double TME_XY_y;
    private double TME_XY_z;
    private double TMH_XY_x;
    private double TMH_XY_y;
    
    private double TME_XZ_x;
    private double TME_XZ_y;
    private double TME_XZ_z;
    private double TMH_XZ_x;
    private double TMH_XZ_y;
    
    private double TME_YZ_x;
    private double TME_YZ_y;
    private double TME_YZ_z;
    private double TMH_YZ_x;
    private double TMH_YZ_y;      
    
    public ElectromagneticWave ( double F, double a, double b, byte m, byte n ) {
        this.F = F; this.a = a; this.b = b; this.m = m; this.n = n; // инициализация полей класса        
        FkrH10 = C/(2*a);                                   // Критическая частота основной волны H10
        LkrH10 = 2*a;                                       // Критическая длина волна основной волны H10
        W = 2*Math.PI*F;                                    // циклическая частота (рад/с)
        L = C/F;                                            // длина волны (м)        
        K = (2d*Math.PI)/L;                                 // волновое число
        Kx = (m*Math.PI)/a;                                 // Состовляющая K=Kx^2+Ky^2
        Ky = (n*Math.PI)/b;                                 // Состовляющая K=Kx^2+Ky^2
        Ks = Math.sqrt(Math.pow(Kx, 2)+Math.pow(Ky, 2));    // поперечная составляющая волнового числа        
        Lkr = (2d*Math.PI)/Ks;                              // критическая длина волны (м)
        Kapr = Math.sqrt(1-Math.pow(L/Lkr, 2));             // аппертура волновода        
        Lv = L/Kapr;                                        // длина волны в волноводе (м)
        Fkr = C/Lkr;                                        // критическая частота (м)        
        B = (2d*Math.PI)/Lv;                                // фазовая постоянная
        // можно перенести в другое место. Для ускорения вычисления            
            l = Lv*MainWorkWindow.l;              // длина стенки волновода (м)
            Fv = C/Lv;              // частота волны в волноводе (Гц)
            T = (1d/F);             // периуд колебанний (пико секунды)
            t0 = T;                 // Текущее время для расчётов        
            fps = MainWorkWindow.fps;               // Число кадров в секунду для анимации
            dt = T/fps;             // Приращение для времени
            Vf = C/Kapr;            // фазовая скосроть (м/с)
            Vgr = C*Kapr;           // груповая скорсоть (м/с)                
            depthZ = 1e-20d;        // Определяет плоскость среза xy
            depthX = 1e-20d;        // Определяет плоскость среза yz
            depthY = 1e-20d;        // Определяет плоскость среза xz
        Z_0 = Math.sqrt(Ua/Ea);                             // Волновое сопротивление среды заполняющей, волновод (Ом)
        Z_TE = Z_0/Kapr;                                    // Волновое сопротивление TE волны (Ом)
        Z_TM = Z_0*Kapr;                                    // Волновое сопротивление TM волны (Ом)
        // Данные нужные для построения графиков
        // Множители которые нужны только для уменьшения вычислений на каждой итерации
//============================ TE (H) ==========================================
        double h0 = 1d;
        double e0 = 1d;
        double Ks2 = Math.pow( Ks, 2 );        
        TEHX = h0*(Kx*B)/Ks2;
        TEHY = h0*(Ky*B)/Ks2;
        TEEX = e0*(Ky*Z_TE*B)/Ks2;
        TEEY = e0*(Kx*Z_TE*B)/Ks2;
        // переменные для проекций xy
        double factor = Math.sin(W*(t0+t)-B*depthZ);
        TEH_XY_x = Math.pow( TEHX*factor, 2);
        TEH_XY_y = Math.pow( TEHY*factor, 2);
        TEH_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2d), 2);
        TEE_XY_x = Math.pow( TEEX*factor, 2);
        TEE_XY_y = Math.pow( TEEY*factor, 2);
        // переменные для проекции xz
        factor = Math.cos(Ky*depthY);
        TEH_XZ_x = Math.pow( TEHX*factor, 2);
        TEE_XZ_y = Math.pow( TEEY*factor, 2);
        TEH_XZ_z = Math.pow( factor, 2);
        factor = Math.sin(Ky*depthY);
        TEH_XZ_y = Math.pow( TEHY*factor, 2);
        TEE_XZ_x = Math.pow( TEEX*factor, 2);        
        // переменные для проекции yz
        factor = Math.sin(Kx*depthX);
        TEH_YZ_x = Math.pow( TEHX*factor, 2);
        TEE_YZ_y = Math.pow( TEEY*factor, 2);
        factor = Math.cos(Kx*depthX);
        TEH_YZ_y = Math.pow( TEHY*factor, 2);
        TEE_YZ_x = Math.pow( TEEX*factor, 2);
        TEH_YZ_z = Math.pow( factor, 2);
//============================ TM (E) ==========================================    
        TMHX = h0*(Ky*B)/(Ks2*Z_TM);
        TMHY = h0*(Kx*B)/(Ks2*Z_TM);
        TMEX = e0*(Kx*B)/Ks2;
        TMEY = e0*(Ky*B)/Ks2;
        // переменные для проекций xy
        factor = Math.sin(W*(t0+t)-B*depthZ);
        TME_XY_x = Math.pow( TMEX*factor, 2);
        TME_XY_y = Math.pow( TMEY*factor, 2);
        TME_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2d), 2);        
        TMH_XY_x = Math.pow( TMHX*factor, 2);
        TMH_XY_y = Math.pow( TMHY*factor, 2);
        // переменные для проекций xz
        factor = Math.sin(Ky*depthY);
        TME_XZ_x = Math.pow( TMEX*factor, 2);
        TMH_XZ_y = Math.pow( TMHY*factor, 2);
        TME_XZ_z = Math.pow( factor, 2);
        factor = Math.cos(Ky*depthY);
        TME_XZ_y = Math.pow( TMEY*factor, 2);           
        TMH_XZ_x = Math.pow( TMHX*factor, 2);        
        // переменные для проекций yz
        factor = Math.cos(Kx*depthX);
        TME_YZ_x = Math.pow( TMEX*factor, 2);
        TMH_YZ_y = Math.pow( TMHY*factor, 2);
        factor = Math.sin(Kx*depthX);
        TME_YZ_y = Math.pow( TMEY*factor, 2);
        TMH_YZ_x = Math.pow( TMHX*factor, 2);    
        TME_YZ_z = Math.pow( factor, 2);        
    }
    /**
     * Проверка введённый данных на ошибки
     * @throws ElectromagneticException 
     */
    public void isCorrect () throws ElectromagneticException {
        if ( Fkr > this.F )            
            throw new ElectromagneticException ("Чистота волны меньше критической !");        
        if ( Lkr < L )            
            throw new ElectromagneticException ("Длина волны больше критической !");
    }
    
    
// <editor-fold defaultstate="collapsed" desc="Функции для построения дополнительных графиков">
//==============Функции груповой и фазовой скорости для построения графика======    
    // Фазовая скосроть 
    public double PhaseSpeedL ( double L ) {return C/Math.sqrt(1-Math.pow(L/getLkr(), 2));}
    public double PhaseSpeedF ( double F ) {return C/Math.sqrt(1-Math.pow(getFkr()/F, 2));}
    // Груповая скосроть
    public double GroupSpeedL ( double L ) {return C*Math.sqrt(1-Math.pow(L/getLkr(), 2));}
    public double GroupSpeedF ( double F ) {return C*Math.sqrt(1-Math.pow(getFkr()/F, 2));}
//====================Определение всех доступных m и n==========================
    /**
     * Функция для построения эллипса, который нуже для определения мод m и n,
     * которые возможны при данной частоте F.
     * @param m мода m.
     * @return мода n.
     */
    public double DefinitionWaveTypes ( double m ) {return Math.sqrt( Math.pow((F*2*b)/C, 2)*(1-(Math.pow(m, 2)/Math.pow((F*2*a)/C, 2))));}
    /**
     * Функция для построения эллипса, который нужен для определения мод m и n.
     * @param m мода m. Аналог x
     * @param F частота генератора.
     * @return мода n.
     */
    public double DefinitionWaveTypes ( double m, double F ) {return Math.sqrt( Math.pow((F*2*b)/C, 2)*(1-(Math.pow(m, 2)/Math.pow((F*2*a)/C, 2))));}
    /**
     * Точка пересечения графика оси x.
     * @return мода m при n=0.
     */
    public double DefinitionWaveTypesY0 () {return (F*2*a)/C;}
    /**
     * Точка пересечения графика оси x.
     * @param F частота.
     * @return мода m при n=0.
     */
    public double DefinitionWaveTypesY0 ( double F ) {return (F*2*a)/C;}
    /**
     * График зависимости длины волны в волноводе от длины волны
     * @param L
     * @return 
     */
    public double Lv_L ( double L ) {return L/Math.sqrt(1-Math.pow(L/Lkr, 2));}
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Составляющие волн в прямоугольном волноводе">    
//===================Прямоугольный волновод=====================================
//======================TE-волны (H) Ez = 0=====================================
//======================Магнитное поле==========================================
    public double TE_H_X ( double x, double y, double z ) {return (double)(TEHX*Math.sin(Kx*x)*Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TE_H_Y ( double x, double y, double z ) {return (double)(TEHY*Math.cos(Kx*x)*Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TE_H_Z ( double x, double y, double z ) {return (double)(Math.cos(Kx*x)*Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z+Math.PI/2d));}
//======================Электрическое поле======================================
    public double TE_E_X ( double x, double y, double z ) {return (double)(TEEX*Math.cos(Kx*x)*Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TE_E_Y ( double x, double y, double z ) {return (double)(TEEY*Math.sin(Kx*x)*Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z));}
//======================TM-волны (E) Hz = 0=====================================
//======================Магнитное поле==========================================    
    public double TM_H_X ( double x, double y, double z ) {return (double)(TMHX*Math.sin(Kx*x)*Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TM_H_Y ( double x, double y, double z ) {return (double)(TMHY*Math.cos(Kx*x)*Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z));}        
//======================Электрическое поле======================================    
    public double TM_E_X ( double x, double y, double z ) {return (double)(TMEX*Math.cos(Kx*x)*Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TM_E_Y ( double x, double y, double z ) {return (double)(TMEY*Math.sin(Kx*x)*Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z));}
    public double TM_E_Z ( double x, double y, double z ) {return (double)(Math.sin(Kx*x)*Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z+Math.PI/2d));}
//============================== Проекции на оси ===============================
    //===================== TE (H) волна =======================================
    public double TE_H_XY ( double x, double y ) {
        return Math.sqrt( TEH_XY_x*Math.pow(Math.sin(Kx*x)*Math.cos(Ky*y), 2) 
                        + TEH_XY_y*Math.pow(Math.cos(Kx*x)*Math.sin(Ky*y), 2) 
                        + TEH_XY_z*Math.pow(Math.cos(Kx*x)*Math.cos(Ky*y), 2));
    }
    public double TE_H_XZ ( double z, double x ) {
        return Math.sqrt( TEH_XZ_x*Math.pow( Math.sin(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEH_XZ_y*Math.pow( Math.cos(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEH_XZ_z*Math.pow( Math.cos(Kx*x)*Math.cos(W*(t0+t)-B*z), 2));
    }
    public double TE_H_YZ ( double z, double y ) {
        return Math.sqrt( TEH_YZ_x*Math.pow( Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEH_YZ_y*Math.pow( Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEH_YZ_z*Math.pow( Math.cos(Ky*y)*Math.cos(W*(t0+t)-B*z), 2));
    }
    public double TE_E_XY ( double x, double y ) {
        return Math.sqrt( TEE_XY_x*Math.pow( Math.cos(Kx*x)*Math.sin(Ky*y), 2) 
                        + TEE_XY_y*Math.pow( Math.sin(Kx*x)*Math.cos(Ky*y), 2) );
    }
    public double TE_E_XZ ( double z, double x ) {
        return Math.sqrt( TEE_XZ_x*Math.pow( Math.cos(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEE_XZ_y*Math.pow( Math.sin(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) );
    }
    public double TE_E_YZ ( double z, double y ) {
        return Math.sqrt( TEE_YZ_x*Math.pow( Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TEE_YZ_y*Math.pow( Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) );
    }
    //===================== TM (E) волна =======================================
    public double TM_E_XY ( double x, double y ) {
        return Math.sqrt( TME_XY_x*Math.pow( Math.cos(Kx*x)*Math.sin(Ky*y), 2) 
                        + TME_XY_y*Math.pow( Math.sin(Kx*x)*Math.cos(Ky*y), 2) 
                        + TME_XY_z*Math.pow( Math.sin(Kx*x)*Math.sin(Ky*y), 2));
    }
    public double TM_E_XZ ( double z, double x ) {
        return Math.sqrt( TME_XZ_x*Math.pow( Math.cos(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TME_XZ_y*Math.pow( Math.sin(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TME_XZ_z*Math.pow( Math.sin(Kx*x)*Math.sin(W*(t0+t)-B*z+Math.PI/2), 2));
    }
    public double TM_E_YZ ( double z, double y ) {
        return Math.sqrt( TME_YZ_x*Math.pow( Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TME_YZ_y*Math.pow( Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TME_YZ_z*Math.pow( Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z+Math.PI/2), 2));
    }
    public double TM_H_XY ( double x, double y ) {
        return Math.sqrt( TMH_XY_x*Math.pow( Math.sin(Kx*x)*Math.cos(Ky*y), 2) 
                        + TMH_XY_y*Math.pow( Math.cos(Kx*x)*Math.sin(Ky*y), 2) );
    }
    public double TM_H_XZ ( double z, double x ) {
        return Math.sqrt( TMH_XZ_x*Math.pow( Math.sin(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TMH_XZ_y*Math.pow( Math.cos(Kx*x)*Math.sin(W*(t0+t)-B*z), 2) );
    }
    public double TM_H_YZ ( double z, double y ) {
        return Math.sqrt( TMH_YZ_x*Math.pow( Math.cos(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) 
                        + TMH_YZ_y*Math.pow( Math.sin(Ky*y)*Math.sin(W*(t0+t)-B*z), 2) );
    }    
// </editor-fold>      
// <editor-fold defaultstate="collapsed" desc="Функции изменения данных">
    public void setDepthX ( double depthX ) {
        this.depthX = depthX;
        // переменные для проекции yz
        double factor = Math.sin(Kx*depthX);
        TEH_YZ_x = Math.pow( TEHX*factor, 2);
        TEE_YZ_y = Math.pow( TEEY*factor, 2);
        factor = Math.cos(Kx*depthX);
        TEH_YZ_y = Math.pow( TEHY*factor, 2);
        TEE_YZ_x = Math.pow( TEEX*factor, 2);
        TEH_YZ_z = Math.pow( factor, 2);
        // переменные для проекций yz
        factor = Math.cos(Kx*depthX);
        TME_YZ_x = Math.pow( TMEX*factor, 2);
        TMH_YZ_y = Math.pow( TMHY*factor, 2);
        factor = Math.sin(Kx*depthX);
        TME_YZ_y = Math.pow( TMEY*factor, 2);
        TMH_YZ_x = Math.pow( TMHX*factor, 2);    
        TME_YZ_z = Math.pow( factor, 2);                    
    }
    public void setDepthZ ( double depthZ ) {
        this.depthZ = depthZ;
        // Для проекции xy
        double factor = Math.sin(W*(t0+t)-B*depthZ);
        TEH_XY_x = Math.pow( TEHX*factor, 2);
        TEH_XY_y = Math.pow( TEHY*factor, 2);
        TEH_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);
        TEE_XY_x = Math.pow( TEEX*factor, 2);
        TEE_XY_y = Math.pow( TEEY*factor, 2);
        factor = Math.sin(W*(t0+t)-B*depthZ);
        TME_XY_x = Math.pow( TMEX*factor, 2);
        TME_XY_y = Math.pow( TMEY*factor, 2);
        TME_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);        
        TMH_XY_x = Math.pow( TMHX*factor, 2);
        TMH_XY_y = Math.pow( TMHY*factor, 2);
    }
    public void setDepthY ( double depthY ) {
        this.depthY = depthY;
        // переменные для проекции xz
        double factor = Math.cos(Ky*depthY);
        TEH_XZ_x = Math.pow( TEHX*factor, 2);
        TEE_XZ_y = Math.pow( TEEY*factor, 2);
        TEH_XZ_z = Math.pow( factor, 2);
        factor = Math.sin(Ky*depthY);
        TEH_XZ_y = Math.pow( TEHY*factor, 2);
        TEE_XZ_x = Math.pow( TEEX*factor, 2);        
        factor = Math.sin(Ky*depthY);
        TME_XZ_x = Math.pow( TMEX*factor, 2);
        TMH_XZ_y = Math.pow( TMHY*factor, 2);
        TME_XZ_z = Math.pow( factor, 2);
        factor = Math.cos(Ky*depthY);
        TME_XZ_y = Math.pow( TMEY*factor, 2);           
        TMH_XZ_x = Math.pow( TMHX*factor, 2);        
    }
    public void set_fps ( int fps ) {
        this.fps = fps;
        this.dt = (double)(T/fps);         // Приращение для времени
    }
    public int get_fps() {return fps;}
    
    public void time_plus () {
        this.t += dt;  
        if ( Double.compare( this.t, this.T ) > 0 )
            this.t = t-T;
        double factor = Math.sin(W*(t0+t)-B*depthZ);
        TEH_XY_x = Math.pow( TEHX*factor, 2);
        TEH_XY_y = Math.pow( TEHY*factor, 2);
        TEH_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);
        TEE_XY_x = Math.pow( TEEX*factor, 2);
        TEE_XY_y = Math.pow( TEEY*factor, 2);
        factor = Math.sin(W*(t0+t)-B*depthZ);
        TME_XY_x = Math.pow( TMEX*factor, 2);
        TME_XY_y = Math.pow( TMEY*factor, 2);
        TME_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);        
        TMH_XY_x = Math.pow( TMHX*factor, 2);
        TMH_XY_y = Math.pow( TMHY*factor, 2);        
    }
    public void time_frame ( int frame ) {
        this.t = dt*frame;
        //System.out.println( "Время = " + t );
        double factor = Math.sin(W*(t0+t)-B*depthZ);
        TEH_XY_x = Math.pow( TEHX*factor, 2);
        TEH_XY_y = Math.pow( TEHY*factor, 2);
        TEH_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);
        TEE_XY_x = Math.pow( TEEX*factor, 2);
        TEE_XY_y = Math.pow( TEEY*factor, 2);
        factor = Math.sin(W*(t0+t)-B*depthZ);
        TME_XY_x = Math.pow( TMEX*factor, 2);
        TME_XY_y = Math.pow( TMEY*factor, 2);
        TME_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);        
        TMH_XY_x = Math.pow( TMHX*factor, 2);
        TMH_XY_y = Math.pow( TMHY*factor, 2); 
    }
    public void time_minus () {
        this.t -= dt;
        if ( Double.compare( this.t, 0 ) < 0 )
            this.t = T;
        double factor = Math.sin(W*(t0+t)-B*depthZ);
        TEH_XY_x = Math.pow( TEHX*factor, 2);
        TEH_XY_y = Math.pow( TEHY*factor, 2);
        TEH_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);
        TEE_XY_x = Math.pow( TEEX*factor, 2);
        TEE_XY_y = Math.pow( TEEY*factor, 2);
        factor = Math.sin(W*(t0+t)-B*depthZ);
        TME_XY_x = Math.pow( TMEX*factor, 2);
        TME_XY_y = Math.pow( TMEY*factor, 2);
        TME_XY_z = Math.pow( Math.sin(W*(t0+t)-B*depthZ+Math.PI/2), 2);        
        TMH_XY_x = Math.pow( TMHX*factor, 2);
        TMH_XY_y = Math.pow( TMHY*factor, 2);        
    }
    public void set_t ( double t ) {
        this.t = t;
    }
    
// </editor-fold>        
//====================Цилиндирческий волновод===================================
// <editor-fold defaultstate="collapsed" desc="Геттеры сеттеры">
//======================Гетеры сеттеры==========================================
    public double getB() {return B;}
    public double getKs() {return Ks;}
    public double getKx() {return Kx;}
    public double getKy() {return Ky;}
    public double getK() {return K;}
    public double getKapr() {return Kapr;}
    public double getL() {return L;}
    public double getLkr() {return Lkr;}
    public double getLkrH10 () {return LkrH10;};
    public double getLv() {return Lv;}
    public double getF() {return F;}
    public double getFkr() {return Fkr;}
    public double getFkrH10 () {return FkrH10;};
    public double getFv() {return Fv;}
    public double getVgr() {return Vgr;}
    public double getVf() {return Vf;}
    public double getW() {return W;}
    public double getT() {return T;}
    public double get_a() {return a;}
    public double get_b() {return b;}
    public double get_l() {return l;}
    public double get_m() {return m;}
    public double get_n() {return n;}    
    public double getZ_0() {return Z_0;}
    public double getZ_TE() {return Z_TE;}
    public double getZ_TM() {return Z_TM;}
    
    public void setL(double L) {this.L = L;}
    public void setF(double F) {this.F = F;}
    public void set_a(double a) {this.a = a;}
    public void set_b(double b) {this.b = b;}
    public void set_l(double l) {this.l = l;}
    public void set_m (byte m) {this.m = m;}
    public void set_n (byte n) {this.n = n;}
// </editor-fold>
//================Переопределение стандартных методов===========================
    @Override
    public String toString () {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Частота волны: %.3f ГГц\tЧастота волны в волноводе: %.3f ГГц\t\nКритическая частота E(H)"+m+n+": %.3f ГГц"
                                + "\nДлина волны: %.3f см\tДлина волны в волноводе: %.3f см\t\nКритическая длина волны: %.3f см"
                                + "\nЦиклическая частота: %.3fe9 Рад/с\nФазовая скорость: %.3fe8 м/с     Груповая скорость: %.3fe8 м/с"
                                + "\nПериуд коллебаний: %.3f пико с"
                                + "\n\nПараметры волновода\n\tширина a: %.2f мм\tвысота b: %.2f мм\tдлина l: %.2f мм"
                                + "\nВолновое сопротивление\n\tZ_TE: %.3f Ом\tZ_TM: %.3f Ом\tZ_0: %.3f Ом", 
                                F*1e-9,Fv*1e-9,Fkr*1e-9,L*1e2,Lv*1e2,Lkr*1e2,W*1e-9,Vf*1e-8,Vgr*1e-8,T*1e12,a*1e3,b*1e3,l*1e3,Z_TE,Z_TM,Z_0));                
        str.append( String.format( "\n\n---------------------TE-----------------------\t---------------------TM-----------------------\n" + 
                          "TEHX = %.3f\t\t\tTMHX = %.3f" +
                          "\nTEHY = %.3f\t\t\tTMHY = %.3f" + 
                          "\nTEEX = %.3f\t\t\tTMEX = %.3f" + 
                          "\nTEEY = %.3f\t\tTMEY = %.3f\n" +
                          "\nTEH_XY_x = %.3f\t\tTME_XY_x = %.3f" + 
                          "\nTEH_XY_y = %.3f\t\tTME_XY_y = %.3f" + 
                          "\nTEH_XY_z = %.3f\t\tTME_XY_z = %.3f" +
                          "\nTEE_XY_x = %.3f\t\tTMH_XY_x = %.3f" +
                          "\nTEE_XY_y = %.3f\t\tTMH_XY_y = %.3f" +
                          "\nTEH_XZ_x = %.3f\t\tTME_XZ_x = %.3f" + 
                          "\nTEE_XZ_y = %.3f\t\tTMH_XZ_y = %.3f" + 
                          "\nTEH_XZ_z = %.3f\t\tTME_XZ_z = %.3f" + 
                          "\nTEH_XZ_y = %.3f\t\tTME_XZ_y = %.3f" +
                          "\nTEE_XZ_x = %.3f\t\tTMH_XZ_x = %.3f" +
                          "\nTEH_YZ_x = %.3f\t\tTME_YZ_x = %.3f" +
                          "\nTEE_YZ_y = %.3f\t\tTMH_YZ_y = %.3f" +
                          "\nTEH_YZ_y = %.3f\t\tTME_YZ_y = %.3f" + 
                          "\nTEE_YZ_x = %.3f\t\tTMH_YZ_x = %.3f" +
                          "\nTEH_YZ_z = %.3f\t\tTME_YZ_z = %.3f" ,
                TEHX, TMHX,
                TEHY, TMHY,
                TEEX, TMEX,
                TEEY, TMEY,
                TEH_XY_x, TME_XY_x,
                TEH_XY_y, TME_XY_y,
                TEH_XY_z, TME_XY_z,
                TEE_XY_x, TMH_XY_x,
                TEE_XY_y, TMH_XY_y, 
                TEH_XZ_x, TME_XZ_x, 
                TEE_XZ_y, TMH_XZ_y, 
                TEH_XZ_z, TME_XZ_z, 
                TEH_XZ_y, TME_XZ_y,
                TEE_XZ_x, TMH_XZ_x, 
                TEH_YZ_x, TME_YZ_x,
                TEE_YZ_y, TMH_YZ_y,
                TEH_YZ_y, TME_YZ_y,
                TEE_YZ_x,TEH_YZ_z,
                TMH_YZ_x,TME_YZ_z ) );
        return str.toString ();
    }    
    @Override
    public ElectromagneticWave clone () throws CloneNotSupportedException {
        return (ElectromagneticWave) super.clone();
    }
}