package uet.oop.bomberman.entities.items;

import javafx.scene.image.Image;
import static uet.oop.bomberman.BombermanGame.music;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.sound.Sound;

public abstract class Item extends Entity {
    protected boolean pickUp = false;
    protected boolean active = false;
    protected int duration = 600;
    protected int timeCounter = 0;

    public Item(int x, int y, Image img) {
        super(x, y, img);
    }

    public int getDuration() {
        return duration;
    }

    public void pick() {
        pickUp = true;
        Sound.collectItem.play();
        music.stop();
        music = Sound.powerupGet;
        music.play();
    }

    public boolean isPickUp() {
        return pickUp;
    }
}
