import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

class Fruit implements ActionListener {
    private MyPanel p;
    private Random random;
    private Timer timer;
    private boolean isBlocked;

    int xPos, yPos;
    boolean isEaten;

    Fruit(MyPanel p) {
        this.p = p;
        this.isEaten = false;
        this.xPos = 100;
        this.yPos = 100;
        this.isBlocked = false;

        this.random = new Random();
        this.timer = new Timer(3000, this);
        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.isEaten && e.getSource() == this.timer) {
            this.xPos = random.nextInt(50)*10;
            this.yPos = random.nextInt(50)*10;
            this.isBlocked = false;

            for (int i=0; i<this.p.snakeList.size(); i++) {
                if (this.xPos == this.p.snakeList.get(i).xPos && this.yPos == this.p.snakeList.get(i).yPos) {
                    this.isBlocked = true;
                }
            }

            if (!this.isBlocked) {
                this.isEaten = false;
            }
        }
    }
}
