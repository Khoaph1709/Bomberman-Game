package uet.oop.bomberman.input;

import java.util.HashSet;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Lớp quản lý sự kiện bàn phím bằng cách theo dõi trạng thái các phím thông qua JavaFX Scene.
 * <p>
 * Cơ chế hoạt động:
 * - Sử dụng Set<KeyCode> để lưu trữ các phím đang được nhấn (activeKeys)
 * - Khi phím được nhấn (KEY_PRESSED), mã phím được thêm vào Set
 * - Khi phím được thả (KEY_RELEASED), mã phím bị xóa khỏi Set
 * - Scene đóng vai trò như container chính cho nội dung UI, giúp bắt sự kiện từ toàn bộ không gian hiển thị
 *
 * <p>
 * Trong JavaFX, Scene đại diện cho một container chứa tất cả các thành phần UI của ứng dụng.
 * Mỗi Scene được gắn vào một Window (Stage) và có thể nhận sự kiện đầu vào từ người dùng.
 */
public class KeyListener implements EventHandler<KeyEvent> {
    private final Set<KeyCode> activeKeys = new HashSet<>();

    /**
     * Khởi tạo KeyListener với Scene được chỉ định.
     * <p>
     * Scene đóng vai trò là root container trong JavaFX, giúp theo dõi sự kiện bàn phím
     * trên toàn bộ khu vực hiển thị của ứng dụng. Khi được gắn vào Scene,
     * listener sẽ nhận được mọi sự kiện phát sinh từ vùng hiển thị này.
     * 
     * @param scene Đối tượng Scene từ JavaFX để lắng nghe sự kiện
     */
    public KeyListener(Scene scene) {
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
    }

    /**
     * Xử lý sự kiện bàn phím từ Scene, cập nhật trạng thái phím vào activeKeys.
     * <p>
     * Scene sẽ chuyển tiếp các sự kiện bàn phím từ hệ điều hành tới phương thức này,
     * cho phép theo dõi trạng thái phím theo thời gian thực.
     * 
     * @param event Sự kiện bàn phím từ JavaFX Scene
     */
    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            activeKeys.add(event.getCode());
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            activeKeys.remove(event.getCode());
        }
    }

    /**
     * Kiểm tra xem một phím cụ thể có đang được nhấn hay không.
     * 
     * @param keyCode Mã phím cần kiểm tra
     * @return true nếu phím đang được nhấn, ngược lại trả về false
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return activeKeys.contains(keyCode);
    }
}
