import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerController extends KeyAdapter {
    private MyPanel _p;

    PlayerController(MyPanel p) {
        this._p = p;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && this._p.isStarted) {
            this._p.timeX = 0;
        } else {
            this._p.isStarted = true;
        }
    }
}
