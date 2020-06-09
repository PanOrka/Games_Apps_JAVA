import java.util.Random;

public class SpikeSetter {
    private MainPanel p;
    private Random rand;

    SpikeSetter(MainPanel p) {
        // private
        this.p = p;
        this.rand = new Random();
    }

    public void setSpikes() {
        int rightNumber, leftNumber, counter, temp;

        for (int i=1; i<17; i++) {
            this.p.spikesR[i].isOut = false;

            this.p.spikesL[i].isOut = false;
        }

        rightNumber = this.rand.nextInt(3) + 5;
        leftNumber = this.rand.nextInt(3) + 5;

        counter = 0;

        while (counter != rightNumber) {
            temp = this.rand.nextInt(16) + 1;
            if (!this.p.spikesR[temp].isOut) {
                this.p.spikesR[temp].isOut = true;
                counter++;
            }
        }

        counter = 0;

        while (counter != leftNumber) {
            temp = this.rand.nextInt(16) + 1;
            if (!this.p.spikesL[temp].isOut) {
                this.p.spikesL[temp].isOut = true;
                counter++;
            }
        }
    }

    public void moveSpikesBack() {
        for (int i=1; i<17; i++) {
            if (this.p.spikesR[i].isOut) {
                this.p.spikesR[i].x[0] += 2;
                this.p.spikesR[i].x[1] += 2;
                this.p.spikesR[i].x[2] += 2;
            }

            if (this.p.spikesL[i].isOut) {
                this.p.spikesL[i].x[0] -= 2;
                this.p.spikesL[i].x[1] -= 2;
                this.p.spikesL[i].x[2] -= 2;
            }
        }
    }

    public void moveSpikesForward() {
        for (int i=1; i<17; i++) {
            if (this.p.spikesR[i].isOut) {
                this.p.spikesR[i].x[0] -= 2;
                this.p.spikesR[i].x[1] -= 2;
                this.p.spikesR[i].x[2] -= 2;
            }

            if (this.p.spikesL[i].isOut) {
                this.p.spikesL[i].x[0] += 2;
                this.p.spikesL[i].x[1] += 2;
                this.p.spikesL[i].x[2] += 2;
            }
        }
    }

}
