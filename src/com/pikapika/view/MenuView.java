package com.pikapika.view;

import com.pikapika.utils.MusicPlayer;
import com.pikapika.utils.SoundEffectPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static com.pikapika.utils.Utils.*;

public class MenuView extends JpanelBackground implements ActionListener {
    public static final int TYPE_EASY = 0;
    public static final int TYPE_MEDIUM = 1;
    public static final int TYPE_HARD = 2;

    private JButton btnPlay, btEasy, btMedium, btHard, btnGuide, btnAbout, btQuit, backMenu;
    private OnClickMenuListener onClickMenuListener;
    private MusicPlayer musicPlayer;
    private SoundEffectPlayer soundEffectPlayer;

    public MenuView(String backgroundPath) {
        super(backgroundPath);
        setVisible(false);
        initUI();
        musicPlayer = new MusicPlayer();
        soundEffectPlayer = new SoundEffectPlayer(); 
    }

    private void initUI() {
        btnPlay = new JButton("Play");
        btEasy = new JButton(BT_EASY);
        btMedium = new JButton(BT_MEDIUM);
        btHard = new JButton(BT_HARD);
        btnGuide = new JButton("How to play");
        btnAbout = new JButton("About Us");
        btQuit = new JButton(BT_QUIT);
        backMenu = new JButton("Back");

        JButton[] allBtns = {btnPlay, btEasy, btMedium, btHard, btnGuide, btnAbout, btQuit, backMenu};
        for (JButton btn : allBtns) {
            btn.addActionListener(this);
        }

        btEasy.setVisible(false);
        btMedium.setVisible(false);
        btHard.setVisible(false);
        backMenu.setVisible(false);

        // ĐÃ SỬA: Dùng GridBagLayout để căn giữa chính xác
        this.setLayout(new GridBagLayout()); 
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = GridBagConstraints.RELATIVE; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(10, 0, 10, 0); 

        Dimension btnSize = new Dimension(336, 52);
        JButton[] displayBtns = {btnPlay, btEasy, btMedium, btHard, backMenu, btnGuide, btnAbout, btQuit};
        for (JButton btn : displayBtns) {
            btn.setPreferredSize(btnSize);
            buttonPanel.add(btn, gbc);
        }

        this.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (soundEffectPlayer != null) soundEffectPlayer.playClick();
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Play":
                btnPlay.setVisible(false);
                btnGuide.setVisible(false);
                btnAbout.setVisible(false);
                btQuit.setVisible(false);

                btEasy.setVisible(true);
                btMedium.setVisible(true);
                btHard.setVisible(true);
                backMenu.setVisible(true);
                break;
            case BT_EASY:
                stopMusic();
                if (onClickMenuListener != null) onClickMenuListener.onNewGameClicked(TYPE_EASY);
                break;
            case BT_MEDIUM:
                stopMusic();
                if (onClickMenuListener != null) onClickMenuListener.onNewGameClicked(TYPE_MEDIUM);
                break;
            case BT_HARD:
                stopMusic();
                if (onClickMenuListener != null) onClickMenuListener.onNewGameClicked(TYPE_HARD);
                break;
            case "Back":
                btEasy.setVisible(false);
                btMedium.setVisible(false);
                btHard.setVisible(false);
                backMenu.setVisible(false);

                btnPlay.setVisible(true);
                btnGuide.setVisible(true);
                btnAbout.setVisible(true);
                btQuit.setVisible(true);
                playBackgroundMusic();
                break;
            case "How to play":
                if (onClickMenuListener != null) onClickMenuListener.onHelpClicked();
                break;
            case "About Us":
                if (onClickMenuListener != null) onClickMenuListener.onAboutClicked();
                break;
            case BT_QUIT:
                if (onClickMenuListener != null) onClickMenuListener.onQuitClicked();
                break;
        }
    }

    public void playBackgroundMusic() { if (musicPlayer != null) musicPlayer.play("/sounds/background_music.wav", true); }
    public void stopMusic() { if (musicPlayer != null) musicPlayer.stop(); }

    public interface OnClickMenuListener {
        void onNewGameClicked(int type);
        void onQuitClicked();
        void onHelpClicked();
        void onAboutClicked();
    }
    public void setOnClickMenuListener(OnClickMenuListener onClickMenuListener) { this.onClickMenuListener = onClickMenuListener; }
    public void startNewGame(int type) { if (onClickMenuListener != null) onClickMenuListener.onNewGameClicked(type); }
}