package com.pikapika;
import com.pikapika.control.GameController;
import javax.swing.JFrame;

import static com.pikapika.utils.Utils.WINDOW_HEIGHT;
import static com.pikapika.utils.Utils.WINDOW_WIDTH;

public class Main {
    public static void main(String[] args) {
        GameController pikachu = new GameController("Pikachu");
        pikachu.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Lệnh này giúp game mở full màn hình ngay khi chạy
        pikachu.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        pikachu.setLocationRelativeTo(null);
        pikachu.start();
    }
}