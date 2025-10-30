package MyListeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import rectangularwaveguide.MainWorkWindow;

/**
 * Класс нужен для реализации вращения модели в окне просмотра модели.
 * @author Vladislav
 */
public class MouseMovement implements MouseMotionListener, MouseListener, Serializable {
    // Начальные кординаты при зажатии мыши.
    private int x0 = 0, y0 = 0;
    // Начальное положение модели. Начальное положение прибавляется к высчитанным углам
    // Нужно для того чтобы сохранялось положение подели.
    private float model_x0 = 0, model_y0 = 0;
    // регулирует чувствительность вращения 0.25
    private float sensitivity = 0.25f;
    // флаг который разрешает вращение
    private boolean flag = false;    
    
    private float x = 0, y = 0;
    
    public MouseMovement () {}
    
    @Override
    public void mouseDragged(MouseEvent me) {
        if ( flag ) {
            //System.out.println("---mouse---");
            y = model_y0 + (float) (Math.toRadians((x0-me.getX())) )*sensitivity;
            x = model_x0 + (float) (Math.toRadians((y0-me.getY())) )*sensitivity;
            MainWorkWindow.pane_model_E.getModel().set_rot_x( x );
            MainWorkWindow.pane_model_E.getModel().set_rot_y( y );
            MainWorkWindow.pane_model_H.getModel().set_rot_x( x );
            MainWorkWindow.pane_model_H.getModel().set_rot_y( y );
            //System.out.println ( "dx = " + x + "\tdy = " + y );
            //if ( me.getX()%10 == 0 || me.getY()%10 == 0 ) {
                MainWorkWindow.pane_model_E.getModel().standardPosition();
                MainWorkWindow.pane_model_H.getModel().standardPosition();
            //}            
        }
    }
    @Override
    public void mousePressed ( MouseEvent me ) {
        if ( MainWorkWindow.pane_model_E.getModel() == null ) return;
        if ( me.getButton() == MouseEvent.BUTTON1 ) {    
            flag = true;
            x0 = me.getX();
            y0 = me.getY();
            model_x0 = MainWorkWindow.pane_model_E.getModel().get_rot_x();
            model_y0 = MainWorkWindow.pane_model_E.getModel().get_rot_y();
        } else {
            flag = false;
        }
    }
    
    
    @Override
    public void mouseMoved(MouseEvent me) {}    
    @Override
    public void mouseClicked(MouseEvent me) {}    
    @Override
    public void mouseReleased( MouseEvent me ) {
        if ( MainWorkWindow.pane_model_E.getModel() == null 
          || MainWorkWindow.pane_model_H.getModel() == null ) 
            return;
        if ( me.getButton() == MouseEvent.BUTTON1 ) {            
            MainWorkWindow.pane_model_E.getModel().set_rot_x( x );
            MainWorkWindow.pane_model_E.getModel().set_rot_y( y );   
            MainWorkWindow.pane_model_H.getModel().set_rot_x( x );
            MainWorkWindow.pane_model_H.getModel().set_rot_y( y );   
            //System.out.println( "угол поворота сохранён: rotx = " + model.get_rot_x()
            //                   +"\t roty = " + model.get_rot_y() );
        }        
    }
    @Override
    public void mouseEntered(MouseEvent me) {}
    @Override
    public void mouseExited(MouseEvent me) {}    
}