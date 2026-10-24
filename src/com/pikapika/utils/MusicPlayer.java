package com.pikapika.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class MusicPlayer {

    private Clip clip;
    private FloatControl volumeControl;
    private long pausePosition;
    private String currentFilePath;
    private boolean looping;

    public void play(String soundFilePath, boolean loop) {
        
        stop();
        // Nếu đang phát cùng file, không làm gì
        if (soundFilePath.equals(currentFilePath) && isPlaying()) {
            return;
        }

        Clip oldClip = this.clip;
        this.currentFilePath = soundFilePath;
        this.looping = loop;

        try {
            URL soundURL = getClass().getResource(soundFilePath);
            System.out.println("===> Sound URL: " + soundURL);
            if (soundURL == null) {
                System.err.println("Không tìm thấy file: " + soundFilePath);
                return;
            }

            // Đóng clip cũ nếu đang mở
            if (oldClip != null && oldClip.isOpen()) {
                oldClip.close();
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

            // Cấu hình điều khiển âm lượng
            try {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (IllegalArgumentException e) {
                System.err.println("Không hỗ trợ điều chỉnh âm lượng cho file: " + soundFilePath);
            }

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Định dạng không được hỗ trợ: " + soundFilePath);
            this.clip = oldClip; // Khôi phục clip cũ
        } catch (IOException e) {
            System.err.println("Lỗi đọc file: " + soundFilePath);
            this.clip = oldClip;
        } catch (LineUnavailableException e) {
            System.err.println("Thiết bị âm thanh bận hoặc không khả dụng");
            this.clip = oldClip;
        }
    }

    public void stop() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
            clip = null;
            currentFilePath = null;
            pausePosition = 0;
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning() && pausePosition > 0) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
            if (looping) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        }
    }

    public void setVolume(float volume) {
        if (volumeControl == null && clip != null) {
            try {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (IllegalArgumentException e) {
                System.err.println("Không hỗ trợ điều chỉnh âm lượng");
                return;
            }
        }

        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float adjustedVolume = Math.max(min, Math.min(volume, max));
            volumeControl.setValue(adjustedVolume);
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public boolean isPaused() {
        return clip != null && !clip.isRunning() && pausePosition > 0;
    }
}
