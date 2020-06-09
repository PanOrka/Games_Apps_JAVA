import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class PlayerController extends KeyAdapter {
    private MyPanel p;

    PlayerController(MyPanel p) {
        this.p = p;
    }

    public void keyPressed(KeyEvent e) {
        if (this.p.switchDir) {
            switch (e.getKeyChar()) {
                case 'w':
                    if (this.p.moveDir != 's') {
                        this.p.moveDir = 'w';
                        this.p.switchDir = false;
                    }
                    break;
                case 's':
                    if (this.p.moveDir != 'w') {
                        this.p.moveDir = 's';
                        this.p.switchDir = false;
                    }
                    break;
                case 'a':
                    if (this.p.moveDir != 'd') {
                        this.p.moveDir = 'a';
                        this.p.switchDir = false;
                    }
                    break;
                case 'd':
                    if (this.p.moveDir != 'a') {
                        this.p.moveDir = 'd';
                        this.p.switchDir = false;
                    }
                    break;
            }
        }
    }
}
