package com.pikapika.view;

import com.pikapika.utils.SoundEffectPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverView extends JpanelBackground implements ActionListener {

    private JLabel messageLabel;
    private JLabel scoreLabel;
    private JButton restartButton;
    private JButton menuButton;
    private GameOverListener listener;

    public GameOverView(String imagePath) {
        super(imagePath);
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());

        // Tạo panel chứa nội dung, giống như menu chính
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false); // cho phép vẽ trong paintComponent
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Làm nền mờ hoặc hiệu ứng kính (vẽ lại)
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Label Game Over
        messageLabel = new JLabel("GAME OVER!", SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 36));
        messageLabel.setForeground(Color.RED);
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Score
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.BLACK);
        contentPanel.add(scoreLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Nút Play Again
        restartButton = new JButton("Play Again");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setPreferredSize(new Dimension(160, 40));
        restartButton.setMaximumSize(new Dimension(160, 40));
        restartButton.addActionListener(this);
        contentPanel.add(restartButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Main Menu
        menuButton = new JButton("Main Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setPreferredSize(new Dimension(160, 40));
        menuButton.setMaximumSize(new Dimension(160, 40));
        menuButton.addActionListener(this);
        contentPanel.add(menuButton);

        // Đặt contentPanel ra giữa màn hình
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(contentPanel, gbc);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        new SoundEffectPlayer().playGameOver();

        if (listener != null) {
            if (e.getSource() == restartButton) {
                listener.onRestartClicked();
            } else if (e.getSource() == menuButton) {
                listener.onMenuClicked();
            }
        }
    }

    public void setGameOverListener(GameOverListener listener) {
        this.listener = listener;
    }

    public interface GameOverListener {

        void onRestartClicked();

        void onMenuClicked();
    }
}
