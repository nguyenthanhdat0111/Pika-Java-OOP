package com.pikapika.view;

import com.pikapika.utils.MusicPlayer;
import com.pikapika.utils.SoundEffectPlayer;
import com.pikapika.utils.Utils;
import static com.pikapika.utils.Utils.BT_HINT;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.pikapika.utils.Utils.BT_PAUSE;
import static com.pikapika.utils.Utils.BT_RESUM;

public class PlayGameView extends JpanelBackground implements ActionListener {

    private JPanel topMenuPanel;
    private JPanel pikachuPanel;
    private BorderLayout mainLayout;
    private GroupLayout topMenuLayout;
    private JButton resumGame;
    private JProgressBar timerProgress;
    private JLabel timer;
    private JLabel score;
    private JLabel totalScore; 
    private JButton pauseGame;
    private JLabel mapCount;
    private PlayGameListener playGameListener;
    private GridLayout pikachuLayout;
    private Pikachu[][] pikachuIcon;
    private int row;
    private int col;
    private int countClicked = 0;
    private Pikachu one;
    private Pikachu two;
    private MusicPlayer backgroundMusic; 
    private JButton hintButton; 
    private boolean hintUsed = false; 
    private SoundEffectPlayer soundPlayer;
    private boolean isPlaying = true;

    // Biến lưu tọa độ vẽ đường đi màu xanh
    private java.util.List<Point> drawPath;

    public PlayGameView() {
        this(10, 10);
    }

    public PlayGameView(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        setVisible(false);
        initUI();
        backgroundMusic = new MusicPlayer();
    }

    private void initUI() {
        setVisible(false);
        mainLayout = new BorderLayout();
        this.setLayout(mainLayout);

        topMenuPanel = new JPanel();
        topMenuLayout = new GroupLayout(topMenuPanel);

        topMenuPanel.setSize(720, 60);
        topMenuPanel.setOpaque(false);
        topMenuPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

        resumGame = new JButton();
        resumGame.addActionListener(this);
        Image image = new ImageIcon(getClass().getResource("/com/pikapika/resources/resum.png")).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        resumGame.setIcon(icon);
        resumGame.setMargin(new Insets(0, 0, 0, 0));
        resumGame.setBorder(null);
        resumGame.setActionCommand(BT_RESUM);

        timerProgress = new JProgressBar(0, 100);
        timerProgress.setValue(100);
        timer = new JLabel("Time: 100");
        timer.setForeground(Color.WHITE);

        score = new JLabel("Score: 0");
        score.setForeground(Color.WHITE);

        totalScore = new JLabel("Total: 0");
        totalScore.setForeground(Color.WHITE);

        mapCount = new JLabel("Map: 1");
        mapCount.setForeground(Color.WHITE);

        pauseGame = new JButton();
        pauseGame.addActionListener(this);
        Image img = new ImageIcon(getClass().getResource("/com/pikapika/resources/pause.png")).getImage();
        Icon ico = new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        pauseGame.setIcon(ico);
        pauseGame.setMargin(new Insets(0, 0, 0, 0));
        pauseGame.setBorder(null);
        pauseGame.setActionCommand(BT_PAUSE);

        hintButton = new JButton("Hint");
        hintButton.addActionListener(this);
        hintButton.setActionCommand(BT_HINT);
        hintButton.setEnabled(true); 

        topMenuPanel.setLayout(topMenuLayout);

        topMenuLayout.setHorizontalGroup(
                topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topMenuLayout.createSequentialGroup()
                                .addComponent(resumGame, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hintButton) 
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(timerProgress, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                        .addGroup(topMenuLayout.createSequentialGroup()
                                                .addComponent(timer)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(mapCount)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(score)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(totalScore)
                                        )
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pauseGame, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        topMenuLayout.setVerticalGroup(
                topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(topMenuLayout.createSequentialGroup()
                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(topMenuLayout.createSequentialGroup()
                                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(timer)
                                                        .addComponent(mapCount)
                                                        .addComponent(score)
                                                        .addComponent(totalScore)
                                                )
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(timerProgress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(topMenuLayout.createSequentialGroup()
                                                .addGroup(topMenuLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(resumGame, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(hintButton) 
                                                        .addComponent(pauseGame, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                )
                                        )
                                )
                        )
        );

        pikachuPanel = new JPanel();
        pikachuLayout = new GridLayout(row - 2, col - 2, 0, 0);
        pikachuPanel.setLayout(pikachuLayout);
        pikachuPanel.setOpaque(false);
        setAlignmentY(JPanel.CENTER_ALIGNMENT);

        add(topMenuPanel, BorderLayout.PAGE_START);

        // ĐÃ SỬA: Loại bỏ bọc GridBagLayout, thêm padding để khung co giãn mượt mà
        pikachuPanel.setBorder(new EmptyBorder(20, 80, 50, 80));
        add(pikachuPanel, BorderLayout.CENTER);
    }

    public void startBackgroundMusic() {
        backgroundMusic.play("/sounds/MusicPlay.wav", true);
        backgroundMusic.setVolume(-10.0f);
    }

    public void pauseBackgroundMusic() {
        backgroundMusic.pause();
    }

    public void resumeBackgroundMusic() {
        backgroundMusic.resume();
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof Pikachu)) {
            switch (e.getActionCommand()) {
                case BT_RESUM:
                    if (playGameListener != null) {
                        playGameListener.onReplayClicked();
                    }
                    resumeBackgroundMusic(); 
                    break;
                case BT_PAUSE:
                    if (playGameListener != null) {
                        playGameListener.onPauseClicked();
                    }
                    pauseBackgroundMusic(); 
                    break;

                case BT_HINT:
                    if (playGameListener != null) {
                        playGameListener.onHintClicked();
                    }
                    break;
                default:
                    break;
            }
        } else {
            ++countClicked;
            switch (countClicked) {
                case 1:
                    one = (Pikachu) e.getSource();
                    if (playGameListener != null) {
                        playGameListener.onPikachuClicked(countClicked, one);
                    }
                    break;
                case 2:
                    if (!one.equals(e.getSource())) {
                        two = (Pikachu) e.getSource();
                        if (playGameListener != null) {
                            playGameListener.onPikachuClicked(countClicked, one, two);
                        }
                    } else {
                        Utils.debug(getClass(), "Remove border");
                        one.removeBorder();
                    }
                    countClicked = 0;
                    break;
                default:
                    break;
            }
        }

        if (BT_HINT.equals(e.getActionCommand())) {
            if (!hintUsed && playGameListener != null) {
                playGameListener.onHintClicked();
                hintUsed = true;
                hintButton.setEnabled(false); 
            }
        }
    }

    public void renderMap(int[][] matrix) {
        pikachuIcon = new Pikachu[row][col];
        pikachuPanel.removeAll();
        for (int i = 1; i <= row - 2; i++) {
            for (int j = 1; j <= col - 2; j++) {
                pikachuIcon[i][j] = createButton(i, j);
                Icon icon = getIcon(matrix[i][j]);
                
                // ĐÃ SỬA: Loại bỏ các lệnh cài đặt Icon dư thừa
                pikachuIcon[i][j].setIcon(icon, matrix[i][j]); 
                pikachuIcon[i][j].removeBorder();
                pikachuPanel.add(pikachuIcon[i][j]);
            }
        }
        // ĐÃ SỬA: Gọi revalidate() để cấp kích thước cho nút, giúp click được
        pikachuPanel.revalidate();
        pikachuPanel.repaint();
    }

public void updateMap(int[][] matrix) {
        for (int i = 1; i <= row - 2; i++) {
            for (int j = 1; j <= col - 2; j++) {
                // ĐÃ SỬA: Bắt buộc dùng setIcon có kèm biến index (matrix[i][j]) 
                // để thuật toán nhận diện đúng con thú
                pikachuIcon[i][j].setIcon(getIcon(matrix[i][j]), matrix[i][j]);
                pikachuIcon[i][j].setVisible(true);
            }
        }
        pikachuPanel.revalidate();
        pikachuPanel.repaint();
    }

    public Icon getIcon(int index) {
        // ĐÃ SỬA: Không bóp méo hình ảnh xuống 40x40 nữa, trả về kích thước gốc
        Image image = new ImageIcon(getClass().getResource("/com/pikapika/resources/ic_" + index + ".png")).getImage();
        return new ImageIcon(image);
    }

    private Pikachu createButton(int x, int y) {
        Pikachu btn = new Pikachu(x, y);
        btn.setBorder(null);
        btn.addActionListener(this);
        return btn;
    }

    public void setPlayGameListener(PlayGameListener playGameListener) {
        this.playGameListener = playGameListener;
    }

    public void updateTimer(String timer) {
        this.timer.setText(timer);
    }

    public void updateScore(String score) {
        this.score.setText(score);
    }

    public void updateMapNum(String map) {
        this.mapCount.setText(map);
    }

    public void setCountClicked(int value) {
        this.countClicked = value;
    }

    public void updateProgress(int progress) {
        timerProgress.setValue(progress);
        invalidate();
    }

    public void updateMaxProgress(int progess) {
        timerProgress.setMaximum(progess);
        timerProgress.setValue(progess);
    }

    public int getMaxCountDown() {
        return timerProgress.getMaximum();
    }

    public int getCountDownValue() {
        return timerProgress.getValue();
    }

    public String getTextCurrentTime() {
        return timer.getText();
    }

    public interface PlayGameListener {
        void onReplayClicked();
        void onPauseClicked();
        void onPikachuClicked(int clickCounter, Pikachu... pikachus);
        void onHintClicked();
    }

    public void updateTotalScore(String totalScore) {
        this.totalScore.setText("Total: " + totalScore);
    }

    public void updateTotalScore(int totalScore) {
        this.totalScore.setText("Total: " + totalScore);
    }

    public void resetHint() {
        hintUsed = false;
        hintButton.setEnabled(true);
    }

    public int[][] getCurrentMapState() {
        int[][] mapState = new int[row][col];
        for (int i = 1; i <= row - 2; i++) {
            for (int j = 1; j <= col - 2; j++) {
                if (pikachuIcon[i][j].isVisible()) {
                    mapState[i][j] = pikachuIcon[i][j].getImageIndex();
                } else {
                    mapState[i][j] = 0; 
                }
            }
        }
        return mapState;
    }

    public void disableHintButton() {
        hintButton.setEnabled(false);
    }

    public void enableHintButton() {
        if (hintButton != null) {
            hintButton.setEnabled(true);
        }
    }

    public void resetSelection() {
        countClicked = 0;
        one = null;
        two = null;
    }

    public Pikachu getPikachuIcon(int i, int j) {
        if (i >= 1 && i < row - 1 && j >= 1 && j < col - 1) {
            return pikachuIcon[i][j];
        }
        return null;
    }
    
    // =======================================================
    // CODE VẼ ĐƯỜNG MÀU XANH NỐI 2 CON THÚ
    // =======================================================
    public void setDrawPath(java.util.List<Point> path) {
        this.drawPath = path;
        this.repaint(); 
    }

   @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        // Nếu có tọa độ đường đi, tiến hành vẽ
        if (drawPath != null && drawPath.size() >= 2) {
            Graphics2D g2 = (Graphics2D) g;
            
            // ĐÃ SỬA 1: Làm đường kẻ hẹp hơn (3px), bo tròn mượt mà
            g2.setColor(new Color(50, 205, 50)); // Màu xanh lá
            g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 
            
            // ĐÃ SỬA 2: Fix lỗi tràn viền bằng cách lấy 1 nút Pikachu chuẩn làm mốc đo
            Pikachu sampleBtn = pikachuIcon[1][1];
            if (sampleBtn == null) return; // Bảo vệ nếu chưa load map

            int cellW = sampleBtn.getWidth();
            int cellH = sampleBtn.getHeight();
            
            // Lấy tọa độ x, y thực tế của ô (1,1) trên màn hình
            Point baseOffset = SwingUtilities.convertPoint(sampleBtn, 0, 0, this);
            
            for (int i = 0; i < drawPath.size() - 1; i++) {
                Point p1 = drawPath.get(i);
                Point p2 = drawPath.get(i + 1);
                
                // Tính tọa độ trung tâm tuyệt đối của từng ô thú để nối
                int x1 = baseOffset.x + (p1.y - 1) * cellW + cellW / 2;
                int y1 = baseOffset.y + (p1.x - 1) * cellH + cellH / 2;
                int x2 = baseOffset.x + (p2.y - 1) * cellW + cellW / 2;
                int y2 = baseOffset.y + (p2.x - 1) * cellH + cellH / 2;
                
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }
}