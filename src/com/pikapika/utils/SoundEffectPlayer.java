package com.pikapika.utils;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundEffectPlayer {
    private final ExecutorService soundExecutor = Executors.newCachedThreadPool();
    
    public void playCorrect() {
        playSound("/sounds/correct.wav");
    }

    public void playWrong() {
        playSound("/sounds/wrong.wav");
    }

    public void playClick() {
        playSound("/sounds/click.wav");
    }
    
    public void playGameOver() {
        playSound("/sounds/GameOver.wav");
    }
    public void playVictory() {
        playSound("/sounds/GameWin.wav");
    }

    private void playSound(String path) {
        soundExecutor.submit(() -> {
            try (InputStream audioStream = getClass().getResourceAsStream(path)) {
                if (audioStream == null) {
                    System.err.println("Sound file not found: " + path);
                    return;
                }
                
                try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioStream)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    
                    // Tự động đóng clip khi kết thúc
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                    
                    clip.start();
                    
                    // Chờ clip kết thúc trước khi tiếp tục
                    while (clip.isRunning()) {
                        Thread.sleep(10);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error playing sound: " + path);
                e.printStackTrace();
            }
        });
    }
    
    public void shutdown() {
        soundExecutor.shutdown();
    }
    
     public void playHintSound() {
        try {
            // Tạo âm thanh hint đơn giản
            Clip clip = AudioSystem.getClip();
            AudioFormat format = new AudioFormat(44100, 8, 1, true, true);
            byte[] data = new byte[1024];
            
            // Tạo sóng sine
            for (int i = 0; i < data.length; i++) {
                double angle = i / 20.0 * 2.0 * Math.PI;
                data[i] = (byte) (Math.sin(angle) * 127.0);
            }
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, data.length);
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}