package com.pikapika.view;

import com.pikapika.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinGameView extends JPanel {
    private JLabel messageLabel;
    private JLabel scoreLabel;
    private JButton restartButton;
    private JButton menuButton;
    private Image backgroundImage;
    private JButton nextLevelButton; // Thêm nút Next Level

    public interface WinGameListener {
        void onRestartClicked();
        void onMenuClicked();
        void onNextLevelClicked(); // Thêm sự kiện cho nút Next Level
    }

    private WinGameListener listener;

    public void setWinGameListener(WinGameListener listener) {
        this.listener = listener;
    }

    public WinGameView(String backgroundPath) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(backgroundPath)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT));
        setOpaque(false);

        // === Khung chứa chính (nền trắng mờ) ===
        JPanel containerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 200)); // trắng mờ
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setPreferredSize(new Dimension(400, 400));

        // === Label ===
        messageLabel = new JLabel(" YOU WIN! ");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 36));
        messageLabel.setForeground(new Color(65, 105, 225));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setForeground(Color.DARK_GRAY);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Buttons ===
        nextLevelButton = createStyledButton(" Next Level"); // Tạo nút Next Level
        restartButton = createStyledButton(" Play Again");
        menuButton = createStyledButton(" Back to Menu");

        
        // Thêm sự kiện cho nút Next Level
        nextLevelButton.addActionListener(e -> {
            if (listener != null) listener.onNextLevelClicked();
        });
        restartButton.addActionListener(e -> {
            if (listener != null) listener.onRestartClicked();
        });
        menuButton.addActionListener(e -> {
            if (listener != null) listener.onMenuClicked();
        });

        // === Add to container ===
        containerPanel.add(Box.createVerticalStrut(30));
        containerPanel.add(messageLabel);
        containerPanel.add(Box.createVerticalStrut(15));
        containerPanel.add(scoreLabel);
        containerPanel.add(Box.createVerticalStrut(30));
        containerPanel.add(nextLevelButton); // Thêm nút Next Level
        containerPanel.add(Box.createVerticalStrut(15));
        containerPanel.add(restartButton);
        containerPanel.add(Box.createVerticalStrut(15));
        containerPanel.add(menuButton);
        containerPanel.add(Box.createVerticalGlue());

        add(containerPanel); // Add khung vào giữa panel
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(230, 230, 250)); // nhạt, nhẹ
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(250, 40));
        button.setMaximumSize(new Dimension(300, 45));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    
}
