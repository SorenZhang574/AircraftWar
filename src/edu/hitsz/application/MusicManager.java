package edu.hitsz.application;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * 音乐管理工具类
 */
public class MusicManager {
    public static volatile boolean isSoundOn = true;
    private static volatile Clip bgmClip;
    private static volatile boolean isBgmPlaying = false;

    private MusicManager() {}
    /**
     * 播放背景音乐
     */
    public static synchronized void playBgm(String filePath) {
        if (!isSoundOn) return;
        if (isBgmPlaying) {
            stopBgm();
        }
        try {
            InputStream is = MusicManager.class.getResourceAsStream(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioInputStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            isBgmPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止背景音乐
     */
    public static synchronized void stopBgm() {
        if (bgmClip != null && isBgmPlaying) {
            bgmClip.stop();
            bgmClip.close();
            isBgmPlaying = false;
        }
    }

    /**
     * 播放一次性的音效
     */
    public static void playSoundEffect(String filePath) {
        if (!isSoundOn) return;
        new Thread(() -> {
            try {
                InputStream is = MusicManager.class.getResourceAsStream(filePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
