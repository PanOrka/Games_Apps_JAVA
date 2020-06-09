import java.util.Random;

public class Obstacles {
    private Random _r;

    // GAP SIZE 150px
    // OBSTACLE WIDTH 40px;
    public int x, y;

    Obstacles() {
        this._r = new Random();
    }

    public void setGap() {
        this.y = 10*(this._r.nextInt(28) + 17);
    }

    public boolean isHit(int birdX, int birdY) {
        return (((birdX + 30 >= this.x && birdX + 30 <= this.x + 40) || (birdX <= this.x + 40 && birdX >= this.x)) && (birdY <= this.y - 150 || birdY + 30 >= this.y));
    }
}
