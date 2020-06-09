public class Owolf extends Thread {
private MainFrame f;

public int position, sheepsEaten, sheepFocused;
public boolean isFull;
public Owolf(MainFrame f, int position) {
        this.f = f;
        this.position = position;
        this.sheepsEaten = 0;
        this.isFull = false;
}

public void run() {
        while (!f.noSheeps) {
                this.f.WQueue();
        }
}
}
