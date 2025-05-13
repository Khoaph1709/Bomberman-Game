package uet.oop.bomberman.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    String path;
    private Clip clip;

    public Sound(String path) {
        this.path = path;
        try {
            File f = new File("res/soundz/" + path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public static Sound titleScreen = new Sound("title_screen.wav");
    public static Sound collectItem = new Sound("collect_item.wav");
    public static Sound powerupGet = new Sound("powerup_get.wav");
    public static Sound placeBomb = new Sound("place_bomb.wav");
    public static Sound died = new Sound("hurt.wav");
    public static Sound mobDied = new Sound("mob_hurt.wav");
    public static Sound move = new Sound("move.wav");
    public static Sound explosion = new Sound("explosion.wav");
    public static Sound win = new Sound("stage_clear.wav");
    public static Sound ending = new Sound("miss.wav");
    public static Sound youWin = new Sound("stage_clear.wav");
}