package Settings;

import java.awt.Dimension;

/**
 * Хранит все нужные размеры для элементов интерфейса
 * @author Vladislav
 */
public interface mySize {
    // Размер окна приложения
    Dimension MAIN_WINDOW = new Dimension ( 1050, 580 );
    // Минимальный размер окна приложения
    Dimension MAIN_WINDOW_MIN = new Dimension ( 980, 560 );
    // Размер верхнего меню и панели вывода короткой информации
    Dimension MENU = new Dimension( (int) MAIN_WINDOW.getWidth(), 32 );
    
}
