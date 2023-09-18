package Computation;

import Exceptions.MatrixDifferentDimensionsException;

/**
 * Класс для работы над матрицами
 * @author Vladislav
 */
public class JMatrix implements Cloneable {
    private JMatrix (){}
//==============Методы создание матриц различных типов==========================
    /**
     * Cоздаёт матрицу JMatrix. Матрица по умолчанию заполняется нулями.
     * @param m количество строк.
     * @param n количество столбцов.
     * @return объект JMatrix.
     */
    public static int[][] createMatrix ( int m, int n ) {
        int [][] create = new int [m][n];
        for ( int i = 0; i < m; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт матрицу JMatrix. Матрица заполняется аргументом z.
     * @param m количество строк.
     * @param n количество столбцов.
     * @param z значение элементов матрицы.
     * @return объект JMatrix.
     */
    public static int[][] createMatrix ( int m, int n, int z ) {
        int [][] create = new int [m][n];
        for ( int i = 0; i < m; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = z;
            }
        }
        return create;
    }
    /**
     * Cоздаёт матрицу JMatrix. Матрица заполняется аргументом z.
     * @param m количество строк.
     * @param n количество столбцов.
     * @param z значение элементов матрицы.
     * @return объект JMatrix.
     */
    public static double[][] createMatrix ( int m, int n, double z ) {
        double [][] create = new double [m][n];
        for ( int i = 0; i < m; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = z;
            }
        }
        return create;
    }
    /**
     * Cоздаёт квадратичную матрицу JMatrix. Матрица по умолчанию заполняется нулями.
     * @param n размер квадратичной матрицы.
     * @return объект JMatrix.
     */
    public static int[][] createQuadraticMatrix ( int n ) {
        int [][] create = new int [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт квадратичную матрицу JMatrix. Матрица заполняется аргументом z.
     * @param n размер квадратичной матрицы.
     * @param z значение элементов матрицы.
     * @return объект JMatrix.
     */
    public static int[][] createQuadraticMatrix ( int n, int z ) {
        int [][] create = new int [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = z;
            }
        }
        return create;
    }
    /**
     * Cоздаёт квадратичную матрицу JMatrix. Матрица заполняется аргументом z.
     * @param n размер квадратичной матрицы.
     * @param z значение элементов матрицы.
     * @return объект JMatrix.
     */
    public static double[][] createQuadraticMatrix ( int n, double z ) {
        double [][] create = new double [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {
                create[i][j] = z;
            }
        }
        return create;
    }
    /**
     * Cоздаёт единичную JMatrix.
     * @param n размер квадратичной матрицы.
     * @return объект JMatrix.
     */
    public static int[][] createIdentityMatrix ( int n ) {
        int [][] create = new int [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {               
                create[i][j] = (i==j) ? 1: 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт диагональную матрицу рамзером n с элементами в главной диагонали z.
     * @param n размер квадратичной матрицы.
     * @param z элементы диагонали.
     * @return объект JMatrix.
     */
    public static int[][] createDiagonalMatrix ( int n, int z ) {
        int [][] create = new int [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {               
                create[i][j] = (i==j) ? z: 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт диагональную матрицу рамзером n с элементами в главной диагонали z.
     * @param n размер квадратичной матрицы.
     * @param z элементы диагонали.
     * @return объект JMatrix.
     */
    public static double[][] createDiagonalMatrix ( int n, double z ) {
        double[][] create = new double [n][n];
        for ( int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++) {               
                create[i][j] = (i==j) ? z: 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт диагональную матрицу с элементами диагонали z[].
     * @param z элементы диагонали.
     * @return объект JMatrix.
     */
    public static int[][] createDiagonalMatrix ( int ... z ) {
        int[][] create = new int [z.length][z.length];
        for ( int i = 0; i < z.length; i++ ) {
            for (int j = 0; j < z.length; j++) {               
                create[i][j] = (i==j) ? z[i]: 0;
            }
        }
        return create;
    }
    /**
     * Cоздаёт диагональную матрицу с элементами диагонали z[].
     * @param z элементы диагонали.
     * @return объект JMatrix.
     */
    public static double[][] createDiagonalMatrix ( double ... z ) {
        double [][] create = new double [z.length][z.length];
        for ( int i = 0; i < z.length; i++ ) {
            for (int j = 0; j < z.length; j++) {               
                create[i][j] = (i==j) ? z[i]: 0;
            }
        }
        return create;
    }
//==============Математические действия над матрицами===========================    
//============Сложение, вычетание, умножение====================================
    /**
     * Складывает две переданные матрицы.
     * @param matrix1 1-я матрица первое слагаемое.
     * @param matrix2 2-я матрица второе слагаемое.
     * @return возвращает матрицу которая является суммой двух переданных матриц.
     */
    public static int[][] Tracking ( int[][] matrix1, int[][] matrix2 )  {
        int[][] matrix = new int[matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return matrix;                    
    }    
    /**
     * Складывает две переданные матрицы.
     * @param matrix1 1-я матрица первое слагаемое.
     * @param matrix2 2-я матрица второе слагаемое.
     * @return возвращает матрицу которая является суммой двух переданных матриц.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static double[][] Tracking ( double[][] matrix1, double[][] matrix2 )  {
        double[][] matrix = new double[matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return matrix;                    
    }    
    /**
     * Складывает две переданные матрицы.
     * @param matrix1 1-я матрица первое слагаемое.
     * @param matrix2 2-я матрица второе слагаемое.
     * @return возвращает матрицу которая является суммой двух переданных матриц.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static float[][] Tracking ( float[][] matrix1, float[][] matrix2 ) {
        float[][] matrix = new float [matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return matrix;                    
    }  
    /**
     * Вычитает две переданные матрицы.
     * @param matrix1 1-я матрица уменьшаемое.
     * @param matrix2 2-я матрица вычетаемое.
     * @return возвращает матрицу которая является разностью двух переданных матриц.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static int[][] Subtraction ( int[][] matrix1, int[][] matrix2 )  {
        int[][] matrix = new int[matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return matrix;                    
    }  
    /**
     * Вычитает две переданные матрицы.
     * @param matrix1 1-я матрица уменьшаемое.
     * @param matrix2 2-я матрица вычетаемое.
     * @return возвращает матрицу которая является разностью двух переданных матриц.
     */
    public static double[][] Subtraction ( double[][] matrix1, double[][] matrix2 ) {
        double[][] matrix = new double[matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return matrix;                    
    }    
    /**
     * Вычитает две переданные матрицы.
     * @param matrix1 1-я матрица уменьшаемое.
     * @param matrix2 2-я матрица вычетаемое.
     * @return возвращает матрицу которая является разностью двух переданных матриц.
     */
    public static float[][] Subtraction ( float[][] matrix1, float[][] matrix2 ) {
        float[][] matrix = new float [matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return matrix;                    
    }  
    /**
     * Вычитает две переданные матрицы.
     * @param matrix1 1-я матрица уменьшаемое.
     * @param matrix2 2-я матрица вычетаемое.
     * @return возвращает матрицу которая является разностью двух переданных матриц.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static float[] Subtraction ( float[] matrix1, float[] matrix2 ) {
        float[] matrix = new float [matrix1.length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            matrix[i] = matrix1[i]-matrix2[i];
        }
        return matrix;                    
    } 
    /**
     * Производит умножение матрицы на скаляр.
     * @param matrix1 матрица.
     * @param skolar скаляр.
     * @return умноженная матрица на skolar.
     */
    public static int[][] ScalarProduct ( int[][] matrix1, int skolar ) {
        int[][] matrix = new int[matrix1.length][matrix1[0].length];
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[i][j] = matrix1[i][j] * skolar; 
        return matrix;
    }
    /**
     * Производит умножение матрицы на скаляр.
     * @param matrix1 матрица.
     * @param skolar скаляр.
     * @return умноженная матрица на skolar.
     */
    public static double[][] ScalarProduct ( int[][] matrix1, double skolar ) {
        double[][] matrix = new double[matrix1.length][matrix1[0].length];
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[i][j] = matrix1[i][j] * skolar; 
        return matrix;
    }
    /**
     * Производит умножение матрицы на скаляр.
     * @param matrix1 матрица.
     * @param skolar скаляр.
     * @return умноженная матрица на skolar.
     */
    public static double[][] ScalarProduct ( double[][] matrix1, double skolar ) {
        double[][] matrix = new double[matrix1.length][matrix1[0].length];
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[i][j] = matrix1[i][j] * skolar; 
        return matrix;
    }
    /**
     * Производит умножение матрицы на скаляр.
     * @param matrix1 матрица.
     * @param skolar скаляр.
     * @return умноженная матрица на skolar.
     */
    public static float[][] ScalarProduct ( float[][] matrix1, float skolar ) {
        float[][] matrix = new float[matrix1.length][matrix1[0].length];
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[i][j] = matrix1[i][j] * skolar; 
        return matrix;
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static int[][] Multiplication ( int[][] matrix1, int[][] matrix2 ) {
        int sum;
        int[][] matrix = new int[ matrix1.length ][ matrix2[0].length ];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix2[0].length; j++ ){
                sum = 0;
                for ( int b = 0; b < matrix1[0].length; b++ )
                    sum += matrix1[i][b] * matrix2[b][j];
                matrix [i][j] = sum;                
            }                                       
        }
        return matrix;                    
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static double[][] Multiplication ( double[][] matrix1, double[][] matrix2 ) {
        double sum;
        double[][] matrix = new double[ matrix1.length ][ matrix2[0].length ];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix2[0].length; j++ ){
                sum = 0d;
                for ( int b = 0; b < matrix1[0].length; b++ )
                    sum += matrix1[i][b] * matrix2[b][j];
                matrix [i][j] = sum;                
            }                                       
        }
        return matrix;                    
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static float[][] Multiplication ( float[][] matrix1, float[][] matrix2 ) {
        float sum;
        float[][] matrix = new float[ matrix1.length ][ matrix2[0].length ];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix2[0].length; j++ ){
                sum = 0f;
                for ( int b = 0; b < matrix1[0].length; b++ )
                    sum += matrix1[i][b]*matrix2[b][j];
                matrix [i][j] = sum;                     
            }                                       
        }
        return matrix;                    
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static int[] Multiplication ( int[] matrix1, int [][] matrix2 ) {        
        int sum = 0;
        int [] matrix = new int [matrix1.length];
        for ( int j = 0; j < matrix2.length; j++ ) {
            for ( int i = 0; i < matrix1.length; i++  ) {
                sum += matrix1[i]*matrix2[i][j];
            }
            matrix[j] = sum;
            sum = 0;
        }
        return matrix;
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static double[] Multiplication ( double[] matrix1, double [][] matrix2 ) {
        double sum = 0;
        double [] matrix = new double [matrix1.length];
        for ( int j = 0; j < matrix2.length; j++ ) {
            for ( int i = 0; i < matrix1.length; i++  ) {
                sum += matrix1[i]*matrix2[i][j];
            }
            matrix[j] = sum;
            sum = 0;
        }
        return matrix;
    }
    /**
     * Выполняет умножение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static float[] Multiplication ( float[] matrix1, float [][] matrix2 ) {
        float sum = 0;
        float [] matrix = new float [matrix1.length];
        for ( int j = 0; j < matrix2.length; j++ ) {
            for ( int i = 0; i < matrix1.length; i++  ) {
                sum += matrix1[i]*matrix2[i][j];
            }
            matrix[j] = sum;
            sum = 0;
        }
        return matrix;
    }
    /**
     * Выполняет умножение Адомара (поэлементное умножение) двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static int[][] AdomarMultiplication ( int[][] matrix1, int[][] matrix2 ) {
        int [][] matrixEnd = new int [matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrixEnd [i][j] = matrix1 [i][j]*matrix2 [i][j]; 
            }
        }     
        return matrixEnd;
    }
    /**
     * Выполняет умножение Адомара (поэлементное умножение) двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static double[][] AdomarMultiplication ( double[][] matrix1, double[][] matrix2 ) {
        double [][] matrixEnd = new double [matrix1.length][matrix1[0].length];
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                matrixEnd [i][j] = matrix1 [i][j]*matrix2 [i][j]; 
            }
        }     
        return matrixEnd;
    }
    /**
     * Выполняет скалярное произведение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static int ScalarMultiplication ( int[][] matrix1, int[][] matrix2 ) {
        int sum = 0;
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                sum += matrix1[i][j]*matrix2[i][j];
            }
        }
        return sum;
    }
    /**
     * Выполняет скалярное произведение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static double ScalarMultiplication ( double[][] matrix1, double[][] matrix2 ) {
        double sum = 0d;
        for ( int i = 0; i < matrix1.length; i++ ) {
            for ( int j = 0; j < matrix1[0].length; j++ ) {
                sum += matrix1[i][j]*matrix2[i][j];
            }
        }
        return sum;
    }
    /**
     * Выполняет скалярное произведение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static int ScalarMultiplication ( int[] matrix1, int[] matrix2 ) {
        int sum = 0;
        for ( int i = 0; i < matrix1.length; i++ ) {
                sum += matrix1[i]*matrix2[i];
        }
        return sum;
    }
    /**
     * Выполняет скалярное произведение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static double ScalarMultiplication ( double[] matrix1, double[] matrix2 ) {
        double sum = 0d;
        for ( int i = 0; i < matrix1.length; i++ ) {
                sum += matrix1[i]*matrix2[i];
        }
        return sum;
    }
    /**
     * Выполняет скалярное произведение двух матриц.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица.
     * @return Матрица являющаяся результатом умножения двух переданных матриц.
     */
    public static float ScalarMultiplication ( float[] matrix1, float[] matrix2 ) {
        float sum = 0f;
        for ( int i = 0; i < matrix1.length; i++ ) {
                sum += matrix1[i]*matrix2[i];
        }
        return sum;
    }
    /**
     * Находит векторное произведение двух векторов.
     * @param matrix1
     * @param matrix2
     * @return 
     */
    public static float[] VectorProduct ( float[] matrix1, float[] matrix2 ) {
        float [] matrix = new float [matrix1.length];
        matrix[0] = matrix1[1]*matrix2[2]-matrix1[2]*matrix2[1];
        matrix[1] = matrix1[2]*matrix2[0]-matrix1[0]*matrix2[2];
        matrix[2] = matrix1[0]*matrix2[1]-matrix1[1]*matrix2[0];
        //matrix[3] = 1f;
        return matrix;
    }
    /**
     * Находит модуль ветора
     * @param matrix
     * @return 
     */
    public static float VectorABS ( float[] matrix ) {
        float abs = 0f;
        for ( float a: matrix ) {
            abs+=a*a;
        }
        return (float) Math.sqrt(abs);
    }
//=================Дейстия над матрицами========================================
    /**
     * Выполняет транспонирование переданной матрицы.
     * @param matrix1 матрица.
     * @return транспонированая матрица.
     */
    public static int[][] Transpose ( int[][] matrix1 ) {
        int[][] matrix = new int[ matrix1[0].length ][ matrix1.length ];        
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[j][i] = matrix1[i][j]; 
        return matrix;
    }
    /**
     * Выполняет транспонирование переданной матрицы.
     * @param matrix1 матрица.
     * @return транспонированая матрица.
     */
    public static double[][] Transpose ( double[][] matrix1 ) {
        double[][] matrix = new double[ matrix1[0].length ][ matrix1.length ];        
            for ( int i = 0; i < matrix1.length; i++ )
                    for ( int j = 0; j < matrix1[0].length; j++ )
                        matrix[j][i] = matrix1[i][j]; 
        return matrix;
    }
    /**
     * Вычисляет декременант матрицы det (определитель).
     * @param matrix матрица.
     * @return определитель.
     */
    public static int Determinant ( int[][] matrix ) {
            switch ( matrix.length ) {
                case 1: 
                    return matrix[0][0];
                case 2:
                    return matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];                    
                case 3: 
                    return matrix[0][0]*matrix[1][1]*matrix[2][2]
                          +matrix[0][1]*matrix[2][0]*matrix[1][2]
                          +matrix[0][2]*matrix[1][0]*matrix[2][1]
                          -matrix[0][0]*matrix[1][2]*matrix[2][1]
                          -matrix[0][1]*matrix[1][0]*matrix[2][2]
                          -matrix[0][2]*matrix[1][1]*matrix[2][0];
                default: return -Integer.MIN_VALUE;
            }
            /**Почему то код не работает!!
            int [] list = new int [ matrix1.length - 1 ];//хранит числа. Это колонка от куда берётся ячейка.
            Integer [] mno;
            int sum = 0;//слагаемые
            int mnoj = 1;//множители            
            int Z = 0; //сщётчик ряда чисел
                for ( int i = 0; i < matrix1.length; i++ ) {
                    int q = 0;
                    for ( int z = 0; z < matrix1.length; z++ ) {                                                                                
                            if ( z != i ) {
                                list[q] = z;
                                if ( q < (list.length-1) )
                                    q++;
                                else 
                                    q = 0;
                            }
                    }
                    mno = permutationArrayElements( list );//хранит все возможные столбцы
                    int[] Zlist = new int [ matrix1.length ];
                    Zlist[0] = i;         
                    for ( int n = 0; n < factorial( list.length ); n++ ) {                        
                        mnoj *= matrix1[ 0 ][ i ];
                        for ( int z = 1; z < matrix1.length; z++ ){                            
                            mnoj *= matrix1[z][ mno[Z] ];          
                            Zlist[z] = mno[Z];
                            Z++;
                        }
                        mnoj *= (int) Math.pow( -1, numberInversions( Zlist ) );
                        sum += mnoj;                        
                        mnoj = 1;                        
                    }                                                                
                    Z = 0;
                }
            return sum;
            **/
    }
    /**
     * Вычисляет декременант матрицы det (определитель).
     * @param matrix матрица.
     * @return определитель.
     */
    public static double Determinant ( double[][] matrix ) {
            switch ( matrix.length ) {
                case 1: 
                    return matrix[0][0];
                case 2: 
                    return matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];                    
                case 3: 
                    return matrix[0][0]*matrix[1][1]*matrix[2][2]
                          +matrix[0][1]*matrix[2][0]*matrix[1][2]
                          +matrix[0][2]*matrix[1][0]*matrix[2][1]
                          -matrix[0][0]*matrix[1][2]*matrix[2][1]
                          -matrix[0][1]*matrix[1][0]*matrix[2][2]
                          -matrix[0][2]*matrix[1][1]*matrix[2][0];
                default: return Double.MIN_VALUE;
            }
    }
    /**
     * Вычисляет декременант матрицы det (определитель).
     * @param matrix матрица.
     * @return определитель.
     * @throws MatrixDifferentDimensionsException Возникает когда матрица не квадратичная так как у не квадратичных матриц нет определителя.
     */
    public static float Determinant ( float[][] matrix ) {
            switch ( matrix.length ) {
                case 1: 
                    return matrix[0][0];
                case 2: 
                    return matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];                    
                case 3: 
                    return matrix[0][0]*matrix[1][1]*matrix[2][2]
                          +matrix[0][1]*matrix[2][0]*matrix[1][2]
                          +matrix[0][2]*matrix[1][0]*matrix[2][1]
                          -matrix[0][0]*matrix[1][2]*matrix[2][1]
                          -matrix[0][1]*matrix[1][0]*matrix[2][2]
                          -matrix[0][2]*matrix[1][1]*matrix[2][0];
                default: return Float.MIN_VALUE;
            }
    }
    /**
     * Вычисляет минор элемента ij матрицы. Отсчёт начинается с 0.
     * @param matrix1 матрица.
     * @param im стока.
     * @param jm столбец.
     * @return минор ij элемента.
     * @throws MatrixDifferentDimensionsException Возникает когда матрица не квадратичная так как у не квадратичных матриц нет определителя.
     */
    public static int Minor ( int[][] matrix1, int im, int jm ) {
        int z = 0; // номер строки матрицы matrixMinor
        int s = 0; // номер столбца матрицы matrixMinor
        int [][] matrixMinor = new int [matrix1.length-1][matrix1.length-1];        
        for ( int i = 0; i < matrix1.length; i++ ) {
            if ( i == im  )
                continue;
            for ( int j = 0; j < matrix1.length; j++ ) {                                
                if ( j == jm )
                    continue;
                matrixMinor [z][s] = matrix1[i][j];                
                if ( s < matrix1.length - 2 )
                    s++;
                else 
                    s = 0;                
            }
            if ( z < matrix1.length - 2 )
                    z++;
                else 
                    z = 0;
        }
        return Determinant ( matrixMinor ); 
    }
    /**
     * Вычисляет минор элемента ij матрицы. Отсчёт начинается с 0.
     * @param matrix1 матрица.
     * @param im стока.
     * @param jm столбец.
     * @return минор ij элемента.
     */
    public static double Minor ( double[][] matrix1, int im, int jm ) {
        int z = 0; // номер строки матрицы matrixMinor
        int s = 0; // номер столбца матрицы matrixMinor
        double [][] matrixMinor = new double [matrix1.length-1][matrix1.length-1];        
        for ( int i = 0; i < matrix1.length; i++ ) {
            if ( i == im  )
                continue;
            for ( int j = 0; j < matrix1.length; j++ ) {                                
                if ( j == jm )
                    continue;
                matrixMinor [z][s] = matrix1[i][j];                
                if ( s < matrix1.length - 2 )
                    s++;
                else 
                    s = 0;                
            }
            if ( z < matrix1.length - 2 )
                    z++;
                else 
                    z = 0;
        }
        return Determinant ( matrixMinor ); 
    }
    /**
     * Вычисляет алгебраическое дополнение элемента ij матрицы.
     * @param matrix матрица.
     * @param im стока.
     * @param jm столбец.
     * @return алгебраическое дополнение ij элемента.
     */
    public static int AlgebraicComplement ( int[][] matrix, int im, int jm )  {
        return (int)(Math.pow( -1, im+jm+2 )*Minor (matrix, im, jm));
    }
    /**
     * Вычисляет алгебраическое дополнение элемента ij матрицы.
     * @param matrix матрица.
     * @param im стока.
     * @param jm столбец.
     * @return алгебраическое дополнение ij элемента.
     */
    public static double AlgebraicComplement ( double[][] matrix, int im, int jm )  {
        return (int)(Math.pow( -1, im+jm+2 )*Minor (matrix, im, jm));
    }
    /**
     * Создаёт присоединённую матрицу (состоящую из алгебраических дополнений) на основе данной.
     * @param matrix матрица.
     * @return присоединённая матрица.
     */
    public static int[][] matrixAdjoint ( int[][] matrix ) {
        int [][] adjoint = new int [matrix.length][matrix.length];
        for ( int i = 0; i < matrix.length; i++ ) {
            for ( int j = 0; j < matrix.length; j++ ) {
                adjoint[i][j] = AlgebraicComplement ( matrix, i, j );
            }
        }
        return adjoint;
    }
    /**
     * Создаёт присоединённую матрицу (состоящую из алгебраических дополнений) на основе данной.
     * @param matrix матрица.
     * @return присоединённая матрица.
     */
    public static double[][] matrixAdjoint ( double[][] matrix ) {
        double [][] adjoint = new double [matrix.length][matrix.length];
        for ( int i = 0; i < matrix.length; i++ ) {
            for ( int j = 0; j < matrix.length; j++ ) {
                adjoint[i][j] = AlgebraicComplement ( matrix, i, j );
            }
        }
        return adjoint;
    }
    /**
     * Создаёт обратную матрицу на основе данной.
     * @param matrix матрица.
     * @return обратная матрица.
     */
    public static double[][] inverseMatrix ( int[][] matrix ) {
        int[][] inversion;
        inversion = Transpose( matrixAdjoint(matrix) );
        return ScalarProduct(inversion,((double) 1/Determinant(matrix)));
    } 
    /**
     * Определяет длину (норму) вектора.
     * @param matrix матрица-вектор.
     * @return норма.
     */
    public static double vectorNorm ( int [][] matrix ) {
        double sum = 0d;
        for ( int [] matrix2: matrix )
            for ( int a: matrix2 )
                sum += Math.pow(a,2);
        return Math.sqrt(sum);
    }
//=================Проверка на что-либо=========================================
    /**
     * Определяет тип матрицы.
     * @param e матрица типа Integer [][].
     * @return сроку являющуюся названием типа матрицы.
     */
    public static String definitionMatrixType ( int [][] e ){                
        String build = "не определенно!";
            if ( e.length == 1 ){
                build = "Вектор строка";  
            } else 
                if ( e[0].length == 1 ){
                    build = "Вектор стобец";
                } else 
                    if ( e.length == e[0].length ){
                        boolean flagNull = true;
                        boolean flagDiagonalOne = true;
                        boolean flagDiagonal = true;
                            for ( int i = 0; i < e.length; i++ )
                                for ( int j = 0; j < e[i].length; j++ ){
                                    if ( e[i][j] != 0 ) flagNull = false;
                                    if ( i != j && e[i][j] != 0 ) flagDiagonalOne = flagDiagonal = false;
                                    if ( i == j && e[i][j] != 1 ) flagDiagonalOne = false;
                                }                                    
                            if ( flagNull )
                                build = "Квадратная нулевая матрица";                                
                            else
                                if ( flagDiagonalOne )
                                    build = "Квадратная диагональная единичная матрица";
                                else
                                    if ( flagDiagonal )
                                        build = "Квадратная диагональная матрица";
                                    else 
                                        build = "Квадратная матрица";
                    }
        return build;        
    }
    /**
     * Определяет тип матрицы.
     * @param e матрица типа Integer [][].
     * @return сроку являющуюся названием типа матрицы.
     */
    public static String definitionMatrixType ( double [][] e ){                
        String build = "не определенно!";
            if ( e.length == 1 ){
                build = "Вектор строка";  
            } else 
                if ( e[0].length == 1 ){
                    build = "Вектор стобец";
                } else 
                    if ( e.length == e[0].length ){
                        boolean flagNull = true;
                        boolean flagDiagonalOne = true;
                        boolean flagDiagonal = true;
                            for ( int i = 0; i < e.length; i++ )
                                for ( int j = 0; j < e[i].length; j++ ){
                                    if ( e[i][j] != 0 ) flagNull = false;
                                    if ( i != j && e[i][j] != 0 ) flagDiagonalOne = flagDiagonal = false;
                                    if ( i == j && e[i][j] != 1 ) flagDiagonalOne = false;
                                }                                    
                            if ( flagNull )
                                build = "Квадратная нулевая матрица";                                
                            else
                                if ( flagDiagonalOne )
                                    build = "Квадратная диагональная единичная матрица";
                                else
                                    if ( flagDiagonal )
                                        build = "Квадратная диагональная матрица";
                                    else 
                                        build = "Квадратная матрица";
                    }
        return build;        
    }
    /**
     * Проверяет на равенство двух матриц.
     * @param matrix1 первая матрица.
     * @param matrix2 вторая матрица.
     * @return false если матрицы не равны. 
     * true если матрицы равны дргу-дргугу.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static boolean isComparisonMatrices ( int[][] matrix1, int[][] matrix2 )  {
        boolean flag = true;
        for ( int i = 0; i < matrix1.length; i++ ){
            for ( int j = 0; j < matrix1[0].length; j++ ){                        
                if ( Integer.compare(matrix1[i][j], matrix2[i][j]) == 0 ) {
                    flag = false;
                    break;
                }                        
            }
            if ( !flag ) 
                break;
        }                                                                
        return flag;                    
    }
    /**
     * Проверяет на равенство двух матриц.
     * @param matrix1 первая матрица.
     * @param matrix2 вторая матрица.
     * @return false если матрицы не равны. 
     * true если матрицы равны дргу-дргугу.
     * @throws MatrixDifferentDimensionsException Возникает в случает когда размерности матриц разные.
     */
    public static boolean isComparisonMatrices ( double[][] matrix1, double[][] matrix2 )  {
        boolean flag = true;
        for ( int i = 0; i < matrix1.length; i++ ){
            for ( int j = 0; j < matrix1[0].length; j++ ){                        
                if ( Double.compare( matrix1[i][j], matrix2[i][j]) == 0 ) {
                    flag = false;
                    break;
                } else {
                }                        
            }
            if ( !flag ) 
                break;
        }                                                                
        return flag;                    
    }    
    /**
     * Определяет выраждееная матрица или нет.
     * @param matrix1 матрица.
     * @return true если матрица выражденная. false если матрица не выражденная.
     */
    public static boolean isNonDegenerateMatrix ( int[][] matrix1 ) {
        return Determinant( matrix1 ) == 0;
    }    
    /**
     * Определяет выраждееная матрица или нет.
     * @param matrix1 матрица.
     * @return true если матрица выражденная. false если матрица не выражденная.
     * @throws MatrixDifferentDimensionsException Возникает когда матрица не квадратичная так как у не квадратичных матриц нет определителя.
     */
    public static boolean isNonDegenerateMatrix ( double[][] matrix1 ) {
        return Determinant( matrix1 ) == 0;
    }   
    /**
     * Выполняет проверку на коммутативность.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица
     * @return true если матрицы коммутативны. false если матрицы не коммутативны.
     */
    public static boolean isCommutativeMatrices ( int[][] matrix1, int[][] matrix2 )  {
        return isComparisonMatrices ( Multiplication (matrix1, matrix2)
                                  , Multiplication (matrix2, matrix1));
    }    
    /**
     * Выполняет проверку на коммутативность.
     * @param matrix1 1-я матрица.
     * @param matrix2 2-я матрица
     * @return true если матрицы коммутативны. false если матрицы не коммутативны.
     */
    public static boolean isCommutativeMatrices ( double[][] matrix1, double[][] matrix2 )  {
        return isComparisonMatrices ( Multiplication (matrix1, matrix2)
                                  , Multiplication (matrix2, matrix1));
    }    
    /**
     * Проверяет может ли сущесвтовтаь обратная матрица для данной матрицы.
     * @param matrix проверяемая матрица.
     * @return true можне существовать. 
     * false не может существовать.
     */
    public static boolean isInverseMatrix ( int [][] matrix )  {
        return ( matrix.length == matrix[0].length && Integer.compare(Determinant(matrix), 0) != 0 );
    }
    /**
     * Проверяет может ли сущесвтовтаь обратная матрица для данной матрицы.
     * @param matrix проверяемая матрица.
     * @return true можне существовать. 
     * false не может существовать.
     */
    public static boolean isInverseMatrix ( double [][] matrix ) {
        return ( matrix.length == matrix[0].length && Double.compare(Determinant(matrix), 0) != 0 );
    }
    /**
     * Проверяет симметричная матрица или нет.
     * @param matrix матрица.
     * @return true матрица симметричная.
     * false матрица не симметричная.
     * @throws MatrixDifferentDimensionsException 
     */
    public static boolean isSymmetricMatrix ( int [][] matrix )  {
        return isComparisonMatrices ( matrix, Transpose (matrix) );
    }
    /**
     * Проверяет симметричная матрица или нет.
     * @param matrix матрица.
     * @return true матрица симметричная.
     * false матрица не симметричная.
     */
    public static boolean isSymmetricMatrix ( double [][] matrix )  {
        return isComparisonMatrices ( matrix, Transpose (matrix) );
    }
//==========Всмопогательные действия============================================
    /**
     * Выполняет перестановки ряда чисел всеми возможными способами.
     * @param e масив.
     * @return Массив в котором подряд записаны все полученные ряды.
     */
    public static int[] permutationArrayElements ( int ... e ) {
        int [] list = new int [ factorial( e.length )*e.length ];      
        int element;
        int j = 0; //сщётчик
        int q = 0;//сщётчик большого списка
            for ( int i = 0; i < factorial( e.length ); i++ ) {
                if ( (j + 1) < e.length ){
                    element = e [j];
                    e [j] = e [j+1];
                    e [j+1]  = element;                    
                }                    
                else {
                    element = e [0];
                    e [0] = e[j];                    
                    e [j]  = element;                
                }           
                for ( int z = 0; z < e.length; z++ ){                    
                    list[q] = e[z];
                    q++;
                }                
                if ( j < (e.length - 1) ) j++;
                else j = 0;
            }
        return list;
    }
    /**
     * Расщитывает факториал.
     * @param e число.
     * @return факториал.
     */
    public static int factorial ( int e ) {
        int sum = 1;
            for ( int i = 1; i <= e; i++ )        
                sum *= i ;
        return sum;
    }
    /**
     * Определяет число инверсий указанного ряда чисел.
     * @param e масив чисел.
     * @return инверсия.
     */
    public static int numberInversion ( int ... e ) {
        int number = 0; //хранит чисо инверсий
        int pastNumber = e[0];//хранит число расположенное правее
        for ( int i = 0; i < e.length; i++ ){
            if ( e[i] < pastNumber ) number ++;
            pastNumber = e[i];
        }        
        return number;
    }                      
//===========Методы связанные с визуальным представлением матриц================    
    /**
     * Выводит переданную матрицу в строку. В удобном представлении.
     * @param matrix матрица.
     * @return Строка содержащая элементы матрицы.
     */
    public static String showMatrix ( int[][] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
            for ( int [] a: matrix ){
                for ( int b: a)
                    build.append( String.format( "%d\t", b ) );                        
                build.append ( "\n" );                    
            }        
        return build.toString();
    }    
    /**
     * Выводит переданную матрицу в строку. В удобном представлении.
     * @param matrix матрица.
     * @return Строка содержащая элементы матрицы.
     */    
    public static String showMatrix ( double[][] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
            for ( double [] a: matrix ){
                for ( double b: a)
                    build.append( String.format( "%.3f\t", b ) );                        
                build.append ( "\n" );                    
            }        
        return build.toString();
    }
    /**
     * Выводит переданную матрицу в строку. В удобном представлении.
     * @param matrix матрица.
     * @return Строка содержащая элементы матрицы.
     */    
    public static String showMatrix ( float[][] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
            for ( float [] a: matrix ){
                for ( float b: a)
                    build.append( String.format( "%.3f\t", b ) );                        
                build.append ( "\n" );                    
            }        
        return build.toString();
    }
    public static String shoMatrix ( int [] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
        for ( int a: matrix )
            build.append(String.format( "%d\t", a));            
        build.append("\n");
        return build.toString();
    }
    public static String shoMatrix ( double [] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
        for ( double a: matrix )
            build.append(String.format( "%.3f\t", a));            
        build.append("\n");
        return build.toString();
    }
    /**
    public static String shoMatrix ( float [] matrix ) {
        StringBuilder build = new StringBuilder();
        build.append ("\n");
        for ( float a: matrix )
            build.append(String.format( "%.3f\t", a));            
        build.append("\n");
        return build.toString();
    }
    **/
}