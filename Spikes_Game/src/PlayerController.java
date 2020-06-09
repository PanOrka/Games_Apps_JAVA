import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerController extends KeyAdapter {
    private MainPanel p;

    PlayerController(MainPanel p) {
        this.p = p;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!this.p.isStarted && e.getKeyChar() == KeyEvent.VK_SPACE) {
            this.p.isStarted = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.p.timerX = 0;
        }
    }
}
