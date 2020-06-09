public class BirdMove {
    private MyPanel _p;

    BirdMove(MyPanel p) {
        this._p = p;
    }

    public void doMove() {
        int deltaY;
        deltaY = this.Func(this._p.timeX) - this.Func(this._p.timeX - 1);

        this._p.birdY += deltaY;

        if (this._p.birdY >= 440){
            this._p.birdY = 440;
            this._p.GameOver = true;
        }
    }

    private int Func(int x) {
        return (x*(x - 20));
    }
}
