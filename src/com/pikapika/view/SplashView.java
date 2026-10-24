package com.pikapika.view;

import com.pikapika.utils.MusicPlayer;
import java.awt.Font;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class SplashView extends JpanelBackground implements PropertyChangeListener {
    private JProgressBar loadingProgress;
    private JLabel loadingText;
    private OnLoadingListener listener;
    private GroupLayout layout;
    private Task loadingTask;
    private MusicPlayer musicPlayer; // Thêm biến để phát nhạc

    public SplashView(String background) {
        super(background);
        layout = new GroupLayout(this);
        setOpaque(true);
        setLayout(layout);
        initUI();
        setVisible(false);
        musicPlayer = new MusicPlayer(); // Khởi tạo nhạc nền
    }

    private void initUI() {
        loadingProgress = new JProgressBar(0, 100);
        loadingProgress.setValue(0);
        loadingProgress.setPreferredSize(new java.awt.Dimension(400, 20));

        loadingText = new JLabel("Loading...");
        loadingText.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        loadingText.setHorizontalAlignment(SwingConstants.CENTER);
        loadingText.setForeground(java.awt.Color.BLACK);

        // ĐÃ SỬA: Đưa thanh loading ra giữa và nâng bottom lề dưới lên 280
        this.setLayout(new java.awt.BorderLayout());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new java.awt.GridBagLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 120, 0)); 

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(0, 0, 10, 0); 
        bottomPanel.add(loadingProgress, gbc);

        gbc.gridy = 1;
        bottomPanel.add(loadingText, gbc);

        this.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        loadingTask = new Task();
        loadingTask.addPropertyChangeListener(this);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int prog = (Integer) evt.getNewValue();
            loadingProgress.setValue(prog);
            if (listener != null) {
                listener.onLoading();
            }
        }
    }

    public void setLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    public void start() {
        setVisible(true);
        loadingTask.execute();
        if (listener != null) {
            listener.onStartLoading();
        }
    }

    public interface OnLoadingListener {
        void onStartLoading();
        void onLoading();
        void onStopLoading();
    }

    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            setProgress(0);
            while (progress < 100) {
                try {
                    Thread.sleep(random.nextInt(300) + 100); // loading ngẫu nhiên
                } catch (InterruptedException ignore) {}
                progress += random.nextInt(10) + 5;
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        @Override
        public void done() {
            setCursor(null);
            if (listener != null) {
                listener.onStopLoading();
            }

          
        }
    }
}
