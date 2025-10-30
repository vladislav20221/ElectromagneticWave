package Settings;

import java.awt.Color;
/**
 * Хранит цвета используемые в интерфейсе
 * @author Vladislav
 */
public interface myColor {
    /**Цвет светлого фона*/
    Color background = new Color ( 127, 139, 168 );
    /**Главный цвет для шрифта*/
    Color color_mainFont = new Color ( 18, 22, 61 );
    /**Цвет выводимого текста*/
    Color color_inputFont = new Color ( 255, 255, 255 );
    /**Цвет тёмного фона*/
    Color color_short = new Color ( 60, 80, 130 );
    /**Светлый цвет фона текстовых сообщений. Цвет выделения элементов интерфейса*/
    Color color_back_font = new Color ( 210, 227, 236 );
    /**Цвет фона графика*/
    Color graphics_background = Color.white;
    /**Цвет меток деления графика*/
    Color graphics_grid = Color.black;
}
