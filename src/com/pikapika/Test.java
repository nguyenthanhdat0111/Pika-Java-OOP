package com.pikapika;

/**
 * 
 */

import com.pikapika.view.JpanelBackground;

import javax.swing.*;

public class Test extends JFrame{

    public static void main(String[] args) {
        Test t = new Test();
        t.setSize(720,460);
        JpanelBackground background = new JpanelBackground("../resources/bg_0.png");
        t.add(background);
        t.setVisible(true);
    }

}
