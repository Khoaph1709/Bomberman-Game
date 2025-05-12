package uet.oop.bomberman.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Tất cả sprite (hình ảnh game) được lưu trữ vào một ảnh duy nhất
 * Class này giúp lấy ra các sprite riêng từ 1 ảnh chung duy nhất đó
 */
public class SpriteSheet {

	// đường dẫn đến ảnh chung
	// mỗi sprite có kích thước 16x16 pixel
	// có 256 sprite
	// ảnh được lưu trữ trong file classic.png
	private String _path;
	public final int SIZE;
	public int[] _pixels;
	public BufferedImage image;

	public static SpriteSheet tiles = new SpriteSheet("/textures/classic.png", 256);

	public SpriteSheet(String path, int size) {
		_path = path;
		SIZE = size;
		_pixels = new int[SIZE * SIZE];
		load();
	}
	
	/**
	 * Lấy ra 1 sprite từ ảnh chung
	 * @param x vị trí x của sprite trong ảnh chung
	 * @param y vị trí y của sprite trong ảnh chung
	 * @return sprite thứ x, y
	 */
	private void load() {
		try {
			URL a = SpriteSheet.class.getResource(_path);
			image = ImageIO.read(a);
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0, 0, w, h, _pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
