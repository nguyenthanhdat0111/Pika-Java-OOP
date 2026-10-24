package com.pikapika.view;

import com.pikapika.utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class JpanelBackground extends JPanel {
    protected Image backgroundImage = null;

    public JpanelBackground() {
        this(null);
    }

    public JpanelBackground(String imagePath) {
        setOpaque(false);
        if (imagePath != null) {
            // Sửa: Kiểm tra null trước khi tạo ImageIcon
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                this.backgroundImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("Không tìm thấy ảnh: " + imagePath);
                // Tạo hình ảnh mặc định nếu cần
                this.backgroundImage = null;
            }
        } else {
            Utils.debug(getClass(), "Can't load null Image!");
        }
    }

  @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
        // Vẽ ảnh phủ toàn bộ panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}


    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            // Sửa: Kiểm tra null trước khi tạo ImageIcon
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                this.backgroundImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("Không tìm thấy ảnh: " + imagePath);
                this.backgroundImage = null;
            }
        } else {
            Utils.debug(getClass(), "Can't load null Image!");
        }
        this.repaint();
    }
}