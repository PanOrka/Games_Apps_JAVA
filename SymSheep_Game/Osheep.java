public class Osheep extends Thread {
private MainFrame f;
private int sheepNumber;

public int position;
public boolean isEaten;
public Osheep(MainFrame f, int position, int sheepNumber) {
        this.f = f;
        this.position = position;
        this.sheepNumber = sheepNumber;
        this.isEaten = false;
}

public void run() {
        while (!this.isEaten) {
                this.f.SQueue(this.sheepNumber);
        }
}
}
