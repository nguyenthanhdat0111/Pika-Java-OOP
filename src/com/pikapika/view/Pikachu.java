package com.pikapika.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class Pikachu extends JButton {

    private int xPoint;
    private int yPoint;
    private int imageIndex;
    private Image bgImage;

    public Pikachu(int x, int y) {
        super();
        this.xPoint = x;
        this.yPoint = y;
        
        // Làm trong suốt nền để bắt sự kiện chuột mượt mà
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public int getXPoint() { return xPoint; }
    public int getYPoint() { return yPoint; }
    public int getImageIndex() { return imageIndex; }

    public void drawBorder(Color color) {
        this.setBorder(new LineBorder(color, 3));
    }

    public void removeBorder() {
        this.setBorder(null);
    }
    
    public void setIcon(Icon icon, int index) {
        if (icon instanceof ImageIcon) {
            this.bgImage = ((ImageIcon) icon).getImage();
        }
        this.imageIndex = index;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Tự động scale hình ảnh theo kích thước co giãn của màn hình
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}