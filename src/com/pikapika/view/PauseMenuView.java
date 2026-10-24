package com.pikapika.view;

import com.pikapika.utils.SoundEffectPlayer;
import com.pikapika.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.pikapika.utils.Utils.BT_CONTINUE;
import static com.pikapika.utils.Utils.BT_BACK_MENU;
import static com.pikapika.utils.Utils.BT_QUIT;

public class PauseMenuView extends JpanelBackground implements ActionListener {
    private JButton continueGame;
    private JButton quitGame;
    private JButton backMenu;
    private PauseMenuListener pauseMenuListener;

    // Thêm trình phát âm thanh
    private final SoundEffectPlayer soundPlayer = new SoundEffectPlayer();

    public PauseMenuView() {
        super();
    }

    public PauseMenuView(String imagePath) {
        super(imagePath);
        setVisible(false);
        initUI();
    }

    private void initUI() {
        continueGame = new JButton(BT_CONTINUE);
        continueGame.addActionListener(this);

        backMenu = new JButton(BT_BACK_MENU);
        backMenu.addActionListener(this);

        quitGame = new JButton(BT_QUIT);
        quitGame.addActionListener(this);

        // ==========================================
        // SỬ DỤNG GRIDBAGLAYOUT ĐỂ TỰ ĐỘNG CĂN GIỮA
        // ==========================================
        this.setLayout(new GridBagLayout()); 
        
        // Tạo một Panel trong suốt để chứa các nút bấm
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false); // Trong suốt để thấy ảnh nền

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = GridBagConstraints.RELATIVE; // Tự động xếp dọc các nút
        gbc.fill = GridBagConstraints.HORIZONTAL; // Các nút giãn ngang bằng nhau
        gbc.insets = new Insets(10, 0, 10, 0); // Khoảng cách giữa các nút là 10px

        Dimension btnSize = new Dimension(360, 70);

        // Đưa các nút vào mảng và add vào panel
        JButton[] buttons = {continueGame, backMenu, quitGame};
        for (JButton btn : buttons) {
            btn.setPreferredSize(btnSize);
            buttonPanel.add(btn, gbc);
        }

        // Đẩy cụm nút bấm này ra giữa màn hình Pause
        this.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Utils.debug(getClass(), e.getActionCommand());

        // Phát âm thanh mỗi khi nhấn nút
        soundPlayer.playClick();

        if (pauseMenuListener != null) {
            switch (e.getActionCommand()) {
                case BT_CONTINUE:
                    pauseMenuListener.onContinueCliked();
                    break;
                case BT_BACK_MENU:
                    pauseMenuListener.onBackMenuClicked();
                    break;
                case BT_QUIT:
                    pauseMenuListener.onQuitClicked();
                    break;
                default:
                    break;
            }
        }
    }

    public void setPauseMenuListener(PauseMenuListener pauseMenuListener) {
        this.pauseMenuListener = pauseMenuListener;
    }

    public interface PauseMenuListener {
        void onContinueCliked();
        void onBackMenuClicked();
        void onQuitClicked();
    }
}