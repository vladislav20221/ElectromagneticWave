package Color;

import java.awt.Color;

/**
 *
 * @author Vladislav
 */
class Gradient implements ColorFill {
    private double max;
    
    private final double [][] oddsR = new double [4][];
    private final double [][] oddsG = new double [4][];
    private final double [][] oddsB = new double [4][];
    
    public Gradient ( double max ) {
        this.max = max;
        calculationOddsR();
        calculationOddsG();
        calculationOddsB();
    }
    
    private void calculationOddsR () {
        oddsR[0] = new double [2];
        oddsR[1] = new double [4];
        oddsR[2] = new double [4];
        oddsR[3] = new double [4];
       
        oddsR[0][0] = -(2200d)/(7*Math.pow( max, 3 ));
        oddsR[0][1] = (4755d)/(14d*max);
        
        oddsR[1][0] = (6520d)/(7*Math.pow( max, 3 ));
        oddsR[1][1] = -(6540d)/(7*Math.pow( max, 2 ));
        oddsR[1][2] = (8025d)/(14d*max);
        oddsR[1][3] = -545d/28d;
        
        oddsR[2][0] = -(12680d)/(7*Math.pow( max, 3 ));
        oddsR[2][1] = (3180d)/Math.pow( max, 2 );
        oddsR[2][2] = -(20775d)/(14d*max);
        oddsR[2][3] = 9055d/28d;
        
        oddsR[3][0] = (8360d)/(7*Math.pow( max, 3 ));
        oddsR[3][1] = -(25080d)/(7*Math.pow( max, 2 ));
        oddsR[3][2] = (50235d)/(14d*max);
        oddsR[3][3] = -(13225d)/(14d);
    }
    private void calculationOddsG () {
        oddsG[0] = new double [2];
        oddsG[1] = new double [4];
        oddsG[2] = new double [4];
        oddsG[3] = new double [4];
        
        oddsG[0][0] = -(14080d)/(7*Math.pow( max, 3 ));
        oddsG[0][1] = (6480d)/(7*max);
        
        oddsG[1][0] = (5440d)/(7*Math.pow( max, 3 ));
        oddsG[1][1] = -(14640d)/(7*Math.pow( max, 2 ));
        oddsG[1][2] = (10140d)/(7*max);
        oddsG[1][3] = -305d/7d;
        
        oddsG[2][0] = -(960d)/(7*Math.pow( max, 3 ));
        oddsG[2][1] = -(720d)/Math.pow( max, 2 );
        oddsG[2][2] = +(5340d)/(7*max);
        oddsG[2][3] = 495d/7d;
        
        oddsG[3][0] = (9600d)/(7*Math.pow( max, 3 ));
        oddsG[3][1] = -(28800d)/(7*Math.pow( max, 2 ));
        oddsG[3][2] = (23160d)/(7*max);
        oddsG[3][3] = -3960d/7d;
    }
    private void calculationOddsB () {
        oddsB[0] = new double [3];
        oddsB[1] = new double [4];
        oddsB[2] = new double [4];
        oddsB[3] = new double [4];
        
        oddsB[0][0] = -(920d)/Math.pow( max, 3 );
        oddsB[0][1] = -(45d)/(2d*max);
        oddsB[0][2] = 255;
        
        oddsB[1][0] = (440d)/Math.pow( max, 3 );
        oddsB[1][1] = -(1020d)/Math.pow( max, 2 );
        oddsB[1][2] = (465d)/(2d*max);
        oddsB[1][3] = 935d/4d;
        
        oddsB[2][0] = (1720d)/Math.pow( max, 3 );
        oddsB[2][1] = -(2940d)/Math.pow( max, 2 );
        oddsB[2][2] = (2385d)/(2d*max);
        oddsB[2][3] = 295d/4d;
      
        oddsB[3][0] = -(1240d)/Math.pow( max, 3 );
        oddsB[3][1] = (3720d)/Math.pow( max, 2 );
        oddsB[3][2] = -(7605d)/(2d*max);
        oddsB[3][3] = 2645d/2d;
    }
    
    public int get_R ( double x ) {
        if ( Double.compare( x, max/4 ) < 0 ) {
            return (int) (oddsR[0][0]*Math.pow(x,3)+oddsR[0][1]*x);
        } if ( Double.compare( x, max/2 ) < 0 ) {
            return (int) (oddsR[1][0]*Math.pow(x,3)+oddsR[1][1]*Math.pow(x,2)+oddsR[1][2]*x+oddsR[1][3]);
        } if ( Double.compare( x, (max*3)/4 ) < 0 ) {
            return (int) (oddsR[2][0]*Math.pow(x,3)+oddsR[2][1]*Math.pow(x,2)+oddsR[2][2]*x+oddsR[2][3]);
        } else {
            return (int) (oddsR[3][0]*Math.pow(x,3)+oddsR[3][1]*Math.pow(x,2)+oddsR[3][2]*x+oddsR[3][3]);
        }
    }
    public int get_G ( double x ) {
        if ( Double.compare( x, max/4 ) < 0 ) {
            return (int) (oddsG[0][0]*Math.pow(x,3)+oddsG[0][1]*x);
        } if ( Double.compare( x, max/2 ) < 0 ) {
            return (int) (oddsG[1][0]*Math.pow(x,3)+oddsG[1][1]*Math.pow(x,2)+oddsG[1][2]*x+oddsG[1][3]);
        } if ( Double.compare( x, (max*3)/4 ) < 0 ) {
            return (int) (oddsG[2][0]*Math.pow(x,3)+oddsG[2][1]*Math.pow(x,2)+oddsG[2][2]*x+oddsG[2][3]);
        } else {
            return (int) (oddsG[3][0]*Math.pow(x,3)+oddsG[3][1]*Math.pow(x,2)+oddsG[3][2]*x+oddsG[3][3]);
        }
    }    
    public int get_B ( double x ) {
        if ( Double.compare( x, max/4 ) < 0 ) {
            return (int) (oddsB[0][0]*Math.pow(x,3)+oddsB[0][1]*x+oddsB[0][2]);
        } if ( Double.compare( x, max/2 ) < 0 ) {
            return (int) (oddsB[1][0]*Math.pow(x,3)+oddsB[1][1]*Math.pow(x,2)+oddsB[1][2]*x+oddsB[1][3]);
        } if ( Double.compare( x, (max*3)/4 ) < 0 ) {
            return (int) (oddsB[2][0]*Math.pow(x,3)+oddsB[2][1]*Math.pow(x,2)+oddsB[2][2]*x+oddsB[2][3]);
        } else {
            return (int) (oddsB[3][0]*Math.pow(x,3)+oddsB[3][1]*Math.pow(x,2)+oddsB[3][2]*x+oddsB[3][3]);
        }
    }
        
    @Override
    public Color get_Color ( double x ) {
        int r = get_R (x);
        int g = get_G (x);
        int b = get_B (x);
        //if ( r >= 255 || g >= 255 || b >= 255 )
            //System.out.println( "r: "+r+"\tg: "+g+"\tb: "+b );
        return new Color ( r, g, b );
    }
}