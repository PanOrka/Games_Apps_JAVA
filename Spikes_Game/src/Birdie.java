public class Birdie { // 40x40
    private MainPanel p;

    public int x, y;
    public boolean isFacingRight;

    Birdie(MainPanel p) {
        this.p = p;

        // public
        this.x = 480;
        this.y = 480;
        this.isFacingRight = true;
    }

    public void doMove() {
        double deltaY;

        if (this.isFacingRight) x += 5;
        else this.x -= 5;

        deltaY = 0.7*(this.p.timerX*(this.p.timerX - 25) - (this.p.timerX - 1)*((this.p.timerX - 1) - 25));

        this.y += (int)deltaY;
    }

    public void doDeath() {
        this.y += 10;
    }
}
