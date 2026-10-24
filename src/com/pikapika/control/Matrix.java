package com.pikapika.control;

import com.pikapika.utils.Utils;
import com.pikapika.view.Pikachu;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.pikapika.utils.Utils.PIKACHU_NUMBER;

public class Matrix {
    private int[][] matrix;
    private static final int CONST_VALUE = 0; 
    private int row;
    private int col;
    private int value;
    
    // BIẾN LƯU TỌA ĐỘ ĐƯỜNG ĐI
    private List<Point> currentPath = new ArrayList<>();

    public List<Point> getCurrentPath() {
        return currentPath;
    }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public int[][] getMatrix() { return matrix; }
    public void setMatrix(int[][] matrix) { this.matrix = matrix; }
    public void setXY(Pikachu pikachu, int value) { this.matrix[pikachu.getXPoint()][pikachu.getYPoint()] = value; }
    public void setXY(int x, int y, int value) { this.matrix[x][y] = value; }
    public int getXY(Pikachu pikachu) { return matrix[pikachu.getXPoint()][pikachu.getYPoint()]; }
    public int getXY(int i, int j) { return matrix[i][j]; }

    public Matrix(int row, int col) {
        this.setCol(col);
        this.setRow(row);
        renderMatrix();
    }

    public int[][] renderMatrix() {
        this.matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = CONST_VALUE;
            }
        }
        Random random = new Random();
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                matrix[i][j] = random.nextInt(PIKACHU_NUMBER) + 1;
            }
        }
        for (int i = 1; i <= PIKACHU_NUMBER + 1; i++) {
            if (demPT(i) % 2 != 0) {
                change(i);
            }
        }
        return this.matrix;
    }

    private int demPT(int value) {
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == value) count++;
            }
        }
        return count;
    }

    private void change(int value) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (this.matrix[i][j] == value) this.matrix[i][j]++;
            }
        }
    }

    private boolean checkLineX(int y1, int y2, int x) {
        int minCol = Math.min(y1, y2);
        int maxCol = Math.max(y1, y2);
        for (int y = minCol + 1; y < maxCol; y++) {
            if (matrix[x][y] != CONST_VALUE) return false;
        }
        return true;
    }

    private boolean checkLineY(int x1, int x2, int y) {
        int maxRow = Math.max(x1, x2);
        int minRow = Math.min(x1, x2);
        for (int x = minRow + 1; x < maxRow; x++) {
            if (this.matrix[x][y] != CONST_VALUE) return false;
        }
        return true;
    }

    private boolean checkRecX(Pikachu p1, Pikachu p2) {
        Pikachu pMinY = p1, pMaxY = p2;
        if (p1.getYPoint() > p2.getYPoint()) { pMinY = p2; pMaxY = p1; }
        for (int y = pMinY.getYPoint(); y <= pMaxY.getYPoint(); y++) {
            if (y > pMinY.getYPoint() && matrix[pMinY.getXPoint()][y] != CONST_VALUE) return false;
            if ((matrix[pMaxY.getXPoint()][y] == CONST_VALUE || y == pMaxY.getYPoint())
                    && checkLineY(pMinY.getXPoint(), pMaxY.getXPoint(), y)
                    && checkLineX(y, pMaxY.getYPoint(), pMaxY.getXPoint())) {
                currentPath.add(new Point(pMinY.getXPoint(), pMinY.getYPoint()));
                currentPath.add(new Point(pMinY.getXPoint(), y));
                currentPath.add(new Point(pMaxY.getXPoint(), y));
                currentPath.add(new Point(pMaxY.getXPoint(), pMaxY.getYPoint()));
                return true;
            }
        }
        return false;
    }

    private boolean checkRecY(Pikachu p1, Pikachu p2) {
        Pikachu pMinX = p1, pMaxX = p2;
        if (p1.getXPoint() > p2.getXPoint()) { pMinX = p2; pMaxX = p1; }
        for (int x = pMinX.getXPoint(); x <= pMaxX.getXPoint(); x++) {
            if (x > pMinX.getXPoint() && matrix[x][pMinX.getYPoint()] != CONST_VALUE) return false;
            if ((matrix[x][pMaxX.getYPoint()] == CONST_VALUE || x == pMaxX.getXPoint())
                    && checkLineX(pMinX.getYPoint(), pMaxX.getYPoint(), x)
                    && checkLineY(x, pMaxX.getXPoint(), pMaxX.getYPoint())) {
                currentPath.add(new Point(pMinX.getXPoint(), pMinX.getYPoint()));
                currentPath.add(new Point(x, pMinX.getYPoint()));
                currentPath.add(new Point(x, pMaxX.getYPoint()));
                currentPath.add(new Point(pMaxX.getXPoint(), pMaxX.getYPoint()));
                return true;
            }
        }
        return false;
    }

    private boolean checkMoreLineX(Pikachu p1, Pikachu p2, int type) {
        Pikachu pMinY = p1, pMaxY = p2;
        if (p1.getYPoint() > p2.getYPoint()) { pMinY = p2; pMaxY = p1; }
        int y = pMaxY.getYPoint() + type;
        int _row = pMinY.getXPoint();
        int colFinish = pMaxY.getYPoint();
        if (type == -1) { colFinish = pMinY.getYPoint(); y = pMinY.getYPoint() + type; _row = pMaxY.getXPoint(); }
        if ((this.matrix[_row][colFinish] == CONST_VALUE || pMinY.getYPoint() == pMaxY.getYPoint())
                && checkLineX(pMinY.getYPoint(), pMaxY.getYPoint(), _row)) {
            while (this.matrix[pMinY.getXPoint()][y] == CONST_VALUE && this.matrix[pMaxY.getXPoint()][y] == CONST_VALUE) {
                if (checkLineY(pMinY.getXPoint(), pMaxY.getXPoint(), y)) {
                    currentPath.add(new Point(pMinY.getXPoint(), pMinY.getYPoint()));
                    currentPath.add(new Point(pMinY.getXPoint(), y));
                    currentPath.add(new Point(pMaxY.getXPoint(), y));
                    currentPath.add(new Point(pMaxY.getXPoint(), pMaxY.getYPoint()));
                    return true;
                }
                y += type;
            }
        }
        return false;
    }

    private boolean checkMoreLineY(Pikachu p1, Pikachu p2, int type) {
        Pikachu pMinX = p1, pMaxX = p2;
        if (p1.getXPoint() > p2.getXPoint()) { pMinX = p2; pMaxX = p1; }
        int x = pMaxX.getXPoint() + type;
        int _col = pMinX.getYPoint();
        int rowFinish = pMaxX.getXPoint();
        if (type == -1) { rowFinish = pMinX.getXPoint(); x = pMinX.getXPoint() + type; _col = pMaxX.getYPoint(); }
        if ((this.matrix[rowFinish][_col] == CONST_VALUE || pMinX.getXPoint() == pMaxX.getXPoint())
                && checkLineY(pMinX.getXPoint(), pMaxX.getXPoint(), _col)) {
            while (this.matrix[x][pMinX.getYPoint()] == CONST_VALUE && this.matrix[x][pMaxX.getYPoint()] == CONST_VALUE) {
                if (checkLineX(pMinX.getYPoint(), pMaxX.getYPoint(), x)) {
                    currentPath.add(new Point(pMinX.getXPoint(), pMinX.getYPoint()));
                    currentPath.add(new Point(x, pMinX.getYPoint()));
                    currentPath.add(new Point(x, pMaxX.getYPoint()));
                    currentPath.add(new Point(pMaxX.getXPoint(), pMaxX.getYPoint()));
                    return true;
                }
                x += type;
            }
        }
        return false;
    }

    public boolean algorithm(Pikachu p1, Pikachu p2) {
        currentPath.clear(); 
        if (matrix[p1.getXPoint()][p1.getYPoint()] == matrix[p2.getXPoint()][p2.getYPoint()]) {
            if (p1.getXPoint() == p2.getXPoint()) {
                if (this.checkLineX(p1.getYPoint(), p2.getYPoint(), p1.getXPoint())) {
                    currentPath.add(new Point(p1.getXPoint(), p1.getYPoint()));
                    currentPath.add(new Point(p2.getXPoint(), p2.getYPoint()));
                    return true;
                }
            }
            if (p1.getYPoint() == p2.getYPoint()) {
                if (this.checkLineY(p1.getXPoint(), p2.getXPoint(), p1.getYPoint())) {
                    currentPath.add(new Point(p1.getXPoint(), p1.getYPoint()));
                    currentPath.add(new Point(p2.getXPoint(), p2.getYPoint()));
                    return true;
                }
            }
            if (this.checkRecX(p1, p2)) return true;
            if (this.checkRecY(p1, p2)) return true;
            if (this.checkMoreLineX(p1, p2, 1)) return true;
            if (this.checkMoreLineX(p1, p2, -1)) return true;
            if (this.checkMoreLineY(p1, p2, 1)) return true;
            if (this.checkMoreLineY(p1, p2, -1)) return true;
        }
        return false; 
    }

    public boolean canPlay(){
        for (int i = 1; i < row-1; i++){
            for ( int j = 1; j < col-1;j++){
                if (matrix[i][j]!=0){
                    for (int m = 1 ; m < row -1; m++){
                        for (int n = 1; n < col-1; n++){
                            if ((m!=i || n!=j) && matrix[m][n]!=0 && matrix[m][n] == matrix[i][j]){
                                if (algorithm(new Pikachu(m,n),new Pikachu(i,j))){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}