public class Spike {
    public int[] x, y;
    public boolean isOut;

    Spike(int[] y, boolean isRight) {
        // public
        this.x = new int[3];
        if (isRight) {
            this.x[0] = 1000;
            this.x[1] = 980;
            this.x[2] = 1000;
        } else {
            this.x[0] = 0;
            this.x[1] = 20;
            this.x[2] = 0;
        }

        this.y = new int[3];

        for (int i=0; i<3; i++) {
            this.y[i] = y[i];
        }
    }
}
