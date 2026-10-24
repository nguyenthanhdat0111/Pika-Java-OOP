package com.pikapika.control;

import com.pikapika.utils.SoundEffectPlayer;
import com.pikapika.utils.Utils;
import com.pikapika.view.MenuView;
import com.pikapika.view.Pikachu;
import com.pikapika.view.PlayGameView;
import com.pikapika.view.SplashView;
import com.pikapika.view.GameOverView;
import com.pikapika.utils.MusicPlayer;
import com.pikapika.view.WinGameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

import static com.pikapika.utils.Utils.MAP_COL;
import static com.pikapika.utils.Utils.MAP_ROW;
import com.pikapika.view.PauseMenuView;
import com.pikapika.view.PauseMenuView.PauseMenuListener;

public class GameController extends JFrame implements GameOverView.GameOverListener {

    private SplashView splashView;
    private MenuView menuView;
    private PlayGameView playGameView;
    private PauseMenuView pauseMenuView;
    private Matrix matrix;
    private Timer timer;
    private int countDown;
    private int score;  
    private int scoreSum;
    private int mapNumber;
    private int coupleDone;
    private ActionListener timeAction;
    private GameOverView gameOverView;
    private int lastGameType = MenuView.TYPE_EASY;
    private SoundEffectPlayer soundEffectPlayer;
    private MusicPlayer backgroundMusic; 
    private MusicPlayer gameMusic;       
    private WinGameView winGameView;  
    private boolean hintUsed = false; 

    public GameController(String title) throws HeadlessException {
        super(title);
        Image icon = (new ImageIcon(getClass().getResource("/com/pikapika/resources/pika_icon.png"))).getImage();
        setIconImage(icon);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        soundEffectPlayer = new SoundEffectPlayer();
        backgroundMusic = new MusicPlayer();
        gameMusic = new MusicPlayer();
    }

    @Override
    protected void frameInit() {
        super.frameInit();

        this.menuView = new MenuView("/com/pikapika/resources/menu_bg.png");
        this.playGameView = new PlayGameView(MAP_ROW, MAP_COL);
        this.pauseMenuView = new PauseMenuView("/com/pikapika/resources/menu_bg.png");
        this.gameOverView = new GameOverView("/com/pikapika/resources/game_over_bg.jpg");
        this.splashView = new SplashView("/com/pikapika/resources/splash_background.png");
        this.winGameView = new WinGameView("/com/pikapika/resources/win_bg.png");

        try {
            backgroundMusic.play("/sounds/background_music.wav", true);
            backgroundMusic.setVolume(-15.0f);
        } catch (Exception e) {
            System.err.println("Lỗi phát nhạc nền: " + e.getMessage());
        }

        this.gameOverView.setGameOverListener(this); 
        this.matrix = new Matrix(MAP_ROW, MAP_COL);

        this.timeAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                --countDown;
                playGameView.updateProgress(countDown);
                playGameView.updateTimer("Time: " + countDown);
                if (countDown == 0) {
                    timer.stop();
                    showGameOver("TIME OUT, GAME OVER!", scoreSum + score);
                }
            }
        };

        this.timer = new Timer(1000, timeAction);

        this.splashView.setLoadingListener(new SplashView.OnLoadingListener() {
            @Override
            public void onStartLoading() {}
            @Override
            public void onLoading() {}
            @Override
            public void onStopLoading() {
                splashView.setVisible(false);
                menuView.setVisible(true);
                try {
                    gameMusic.stop(); 
                    backgroundMusic.play("/sounds/background_music.wav", true);
                    backgroundMusic.setVolume(-15.0f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        menuView.setOnClickMenuListener(new MenuView.OnClickMenuListener() {
            @Override
            public void onNewGameClicked(int type) {
                try {
                    backgroundMusic.stop(); 
                    gameMusic.play("/sounds/MusicPlay.wav", true);
                    gameMusic.setVolume(-10.0f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                menuView.setVisible(false);
                
                
                matrix = new Matrix(MAP_ROW, MAP_COL);
                playGameView.renderMap(matrix.getMatrix()); 
                
                int i = (new Random()).nextInt(5);
                playGameView.setBackgroundImage("/com/pikapika/resources/bg_" + i + ".png");

                score = 0;
                scoreSum = 0;
                mapNumber = 1;
                coupleDone = 0;
                lastGameType = type;
                hintUsed = false;
                playGameView.enableHintButton(); 

                switch (type) {
                    case MenuView.TYPE_EASY: countDown = 180; break;
                    case MenuView.TYPE_MEDIUM: countDown = 120; break;
                    case MenuView.TYPE_HARD: countDown = 60; break;
                }
                playGameView.updateMaxProgress(countDown);
                playGameView.updateScore("Score: " + score);
                playGameView.updateTimer("Time: " + countDown);
                playGameView.updateMapNum("Map: " + mapNumber);
                playGameView.setVisible(true);
                timer.start();
            }

            @Override
            public void onQuitClicked() {
                try {
                    backgroundMusic.stop();
                    gameMusic.stop();
                    if (timer != null) timer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }

            @Override
            public void onHelpClicked() {
                showScalableImageDialog("How to Play", "/com/pikapika/resources/howtoplay.jpg");
            }

            @Override
            public void onAboutClicked() {
                showScalableImageDialog("About Us", "/com/pikapika/resources/aboutus.jpg");
            }
        });

        this.playGameView.setPlayGameListener(new PlayGameView.PlayGameListener() {
            @Override
            public void onReplayClicked() {
                matrix = new Matrix(MAP_ROW, MAP_COL);
                playGameView.updateMap(matrix.getMatrix());
                score = scoreSum;
                coupleDone = 0;
                countDown = playGameView.getMaxCountDown();
                playGameView.updateMaxProgress(countDown);
                playGameView.updateScore("Score: " + score);
                playGameView.updateTimer("Time: " + countDown);
                playGameView.updateMapNum("Map: " + mapNumber);
                hintUsed = false;
                playGameView.enableHintButton(); 
                playGameView.resetSelection();
            }

            @Override
            public void onPauseClicked() {
                gameMusic.pause();
                timer.stop();
                playGameView.setVisible(false);
                pauseMenuView.setVisible(true);
            }
            
            private boolean isAnimating = false;
            private final com.pikapika.utils.SoundEffectPlayer soundPlayer = new com.pikapika.utils.SoundEffectPlayer();

            @Override
            public void onPikachuClicked(int clickCounter, Pikachu... pikachus) {
                if (isAnimating) return; 

                if (clickCounter == 1) {
                    pikachus[0].drawBorder(Color.red);
                } else if (clickCounter == 2) {
                    pikachus[1].drawBorder(Color.red);

                    if (matrix.algorithm(pikachus[0], pikachus[1])) {
                        isAnimating = true; 
                        
                        playGameView.setDrawPath(matrix.getCurrentPath());

                        Timer delayTimer = new Timer(150, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                playGameView.setDrawPath(null); 
                                
                                matrix.setXY(pikachus[0], 0);
                                matrix.setXY(pikachus[1], 0);
                                pikachus[0].removeBorder();
                                pikachus[1].removeBorder();
                                pikachus[0].setVisible(false);
                                pikachus[1].setVisible(false);

                                SwingUtilities.invokeLater(() -> soundPlayer.playCorrect()); 

                                coupleDone++;
                                score += 100;
                                playGameView.updateScore("Score: " + score);

                                if (!matrix.canPlay() && coupleDone < (matrix.getRow() - 2) * (matrix.getCol() - 2) / 2) {
                                    timer.stop();
                                    showGameOver("GAME OVER! NO MORE MOVES!", scoreSum + score);
                                }

                                if (coupleDone == (matrix.getRow() - 2) * (matrix.getCol() - 2) / 2) {
                                    ++mapNumber;
                                    if (mapNumber <= 3) {
                                        score = countDown * 10 + 500;
                                        scoreSum += score;
                                        score = scoreSum;
                                        countDown = playGameView.getMaxCountDown() - 10 * mapNumber;
                                        coupleDone = 0;
                                        
                                     
                                        matrix = new Matrix(MAP_ROW, MAP_COL);
                                        playGameView.renderMap(matrix.getMatrix());
                                        
                                        playGameView.updateMaxProgress(countDown);
                                        playGameView.updateTimer("Time: " + countDown);
                                        playGameView.updateMapNum("Map: " + mapNumber);
                                        playGameView.updateScore("Score: " + score);
                                        
                                        hintUsed = false;
                                        playGameView.enableHintButton();
                                        playGameView.resetSelection(); // Xóa viền chuột lưu tạm
                                    } else {
                                        timer.stop();
                                        int levelBonus = countDown * 10 + 500;
                                        score += levelBonus;
                                        scoreSum += score;
                                        showWinGame(scoreSum);
                                    }
                                }
                                isAnimating = false; 
                            }
                        });
                        delayTimer.setRepeats(false); 
                        delayTimer.start();
                    } else {
                        pikachus[0].removeBorder();
                        pikachus[1].removeBorder();
                        playGameView.setCountClicked(0);
                        SwingUtilities.invokeLater(() -> soundPlayer.playWrong()); 
                    }
                }
            }

            @Override
            public void onHintClicked() {
                onHintButtonClicked();
            }
        });

        this.pauseMenuView.setPauseMenuListener(new PauseMenuListener() {
            @Override
            public void onContinueCliked() {
                pauseMenuView.setVisible(false);
                playGameView.setVisible(true);
                timer.start();
                gameMusic.resume(); 
            }
            @Override
            public void onBackMenuClicked() {
                pauseMenuView.setVisible(false);
                menuView.setVisible(true);
                gameMusic.stop();
                backgroundMusic.play("/sounds/background_music.wav", true);
                backgroundMusic.setVolume(-15.0f);
            }
            @Override
            public void onQuitClicked() {
                try {
                    backgroundMusic.stop();
                    gameMusic.stop();
                    if (timer != null) timer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        this.winGameView.setWinGameListener(new WinGameView.WinGameListener() {
            @Override
            public void onRestartClicked() {
                gameMusic.stop();
                gameMusic.play("/sounds/GamePlay.wav", true);
                gameMusic.setVolume(-10.0f);
                winGameView.setVisible(false);
                menuView.startNewGame(lastGameType);
                startNewLevel();
            }
            @Override
            public void onMenuClicked() {
                gameMusic.stop();
                backgroundMusic.play("/sounds/background_music.wav", true);
                backgroundMusic.setVolume(-15.0f);
                winGameView.setVisible(false);
                menuView.setVisible(true);
            }
            @Override
            public void onNextLevelClicked() {
                if (lastGameType < MenuView.TYPE_HARD) {
                    lastGameType++;
                    mapNumber = 1;
                    matrix = new Matrix(MAP_ROW, MAP_COL);
                    playGameView.renderMap(matrix.getMatrix());
                    coupleDone = 0;
                    score = 0;
                    scoreSum = 0;
                    switch (lastGameType) {
                        case MenuView.TYPE_MEDIUM: countDown = 200; break;
                        case MenuView.TYPE_HARD: countDown = 100; break;
                    }
                    playGameView.updateMaxProgress(countDown);
                    playGameView.updateScore("Score: " + score);
                    playGameView.updateTimer("Time: " + countDown);
                    playGameView.updateMapNum("Map: " + mapNumber);
                    hintUsed = false;
                    playGameView.enableHintButton();
                    playGameView.resetSelection();
                    winGameView.setVisible(false);
                    playGameView.setVisible(true);
                    timer.start();
                    gameMusic.play("/sounds/MusicPlay.wav", true);
                    gameMusic.setVolume(-10.0f);
                } else {
                    JOptionPane.showMessageDialog(GameController.this,
                            "You've completed all difficulties!",
                            "Congratulations",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        this.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;

        this.add(splashView, gbc);
        this.add(menuView, gbc);
        this.add(playGameView, gbc);
        this.add(pauseMenuView, gbc);
        this.add(gameOverView, gbc);
        this.add(winGameView, gbc);

        menuView.setVisible(false);
        playGameView.setVisible(false);
        pauseMenuView.setVisible(false);
        gameOverView.setVisible(false);
        winGameView.setVisible(false);
        splashView.setVisible(true); 
    }

    private void showScalableImageDialog(String title, String imagePath) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(800, 600); 
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel() {
            private Image img = new ImageIcon(getClass().getResource(imagePath)).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
        JButton backBtn = new JButton("Back Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        backBtn.addActionListener(e -> dialog.dispose()); 
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(backBtn);

        dialog.add(imagePanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setResizable(true); 
        dialog.setVisible(true);
    }

    private void onHintButtonClicked() {
        if (hintUsed) { return; }
        int[][] currentMap = matrix.getMatrix();
        java.util.List<Point> visiblePoints = new ArrayList<>();
        java.util.List<Integer> visibleValues = new ArrayList<>();

        for (int i = 1; i < MAP_ROW - 1; i++) {
            for (int j = 1; j < MAP_COL - 1; j++) {
                if (currentMap[i][j] != 0) {
                    visiblePoints.add(new Point(i, j));
                    visibleValues.add(currentMap[i][j]);
                }
            }
        }
        Collections.shuffle(visibleValues);
        for (int idx = 0; idx < visiblePoints.size(); idx++) {
            Point p = visiblePoints.get(idx);
            currentMap[p.x][p.y] = visibleValues.get(idx);
        }
        matrix.setMatrix(currentMap);
        for (int i = 1; i < MAP_ROW - 1; i++) {
            for (int j = 1; j < MAP_COL - 1; j++) {
                if (currentMap[i][j] != 0) {
                    Pikachu btn = playGameView.getPikachuIcon(i, j);
                    if (btn != null) {
                        Icon icon = playGameView.getIcon(currentMap[i][j]);
                        btn.setIcon(icon);
                        btn.setVisible(true);
                    }
                }
            }
        }
        hintUsed = true;
        playGameView.disableHintButton();
        soundEffectPlayer.playHintSound();
    }

    private int[][] getCurrentMapState() {
        int[][] mapState = new int[MAP_ROW][MAP_COL];
        int[][] currentMatrix = matrix.getMatrix();
        for (int i = 0; i < MAP_ROW; i++) {
            for (int j = 0; j < MAP_COL; j++) {
                mapState[i][j] = currentMatrix[i][j];
            }
        }
        return mapState;
    }

    private void startNewLevel() {
        hintUsed = false;
        playGameView.enableHintButton();
        playGameView.renderMap(matrix.getMatrix());
        playGameView.updateMapNum("Map: " + mapNumber);
        playGameView.resetSelection();
        playGameView.updateProgress(countDown);
    }

    public void start() {
        splashView.start();
        setVisible(true);
    }

    @Override
    public void onRestartClicked() {
        gameMusic.stop();
        gameMusic.play("/sounds/GamePlay.wav", true);
        gameMusic.setVolume(-10.0f);
        gameOverView.setVisible(false);
        menuView.startNewGame(lastGameType);
        startNewLevel(); 
    }

    @Override
    public void onMenuClicked() {
        gameMusic.stop();
        backgroundMusic.play("/sounds/background_music.wav", true);
        backgroundMusic.setVolume(-15.0f);
        gameOverView.setVisible(false);
        menuView.setVisible(true);
    }

    private void showGameOver(String message, int score) {
        gameMusic.stop();
        if (message.contains("WIN")) {
            soundEffectPlayer.playVictory();
        } else {
            soundEffectPlayer.playGameOver();
        }
        gameMusic.stop();
        playGameView.setVisible(false);
        gameOverView.setScore(score);
        gameOverView.setMessage(message);
        gameOverView.setVisible(true);
    }

    private void showWinGame(int score) {
        gameMusic.stop();
        soundEffectPlayer.playVictory();
        playGameView.setVisible(false);
        winGameView.setScore(score);
        winGameView.setVisible(true);
    }
}