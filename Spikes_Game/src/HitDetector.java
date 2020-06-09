import java.awt.*;

public class HitDetector {
    private MainPanel p;
    private Polygon polygon;

    HitDetector(MainPanel p) {
        this.p = p;
    }

    public boolean hitSpikes_UD() {
        return (this.p.bird.y <= 30 || this.p.bird.y + 40 >= 770);
    }

    public boolean hitSpikes_R() {
        int selector = (this.p.bird.y/40) - 1; // CHECKS IN WHICH LINES BIRD CAN HIT SPIKES
        boolean hit = false;

        if (selector >= 0 && this.p.spikesR[selector].isOut) {
            this.polygon = new Polygon(this.p.spikesR[selector].x, this.p.spikesR[selector].y, 3);
            hit = this.polygon.intersects(this.p.bird.x, this.p.bird.y, 40, 40);
        }

        if (!hit && this.p.bird.y % 40 != 0 && selector + 1 <= 17 && this.p.spikesR[selector + 1].isOut) {
            this.polygon = new Polygon(this.p.spikesR[selector + 1].x, this.p.spikesR[selector + 1].y, 3);
            hit = this.polygon.intersects(this.p.bird.x, this.p.bird.y, 40, 40);
        }

        return hit;
    }

    public boolean hitSpikes_L() {
        int selector = (this.p.bird.y/40) - 1; // CHECKS IN WHICH LINES BIRD CAN HIT SPIKES
        boolean hit = false;

        if (selector >= 0 && this.p.spikesL[selector].isOut) {
            this.polygon = new Polygon(this.p.spikesL[selector].x, this.p.spikesL[selector].y, 3);
            hit = this.polygon.intersects(this.p.bird.x, this.p.bird.y, 40, 40);
        }

        if (!hit && this.p.bird.y % 40 != 0 && selector + 1 <= 17 && this.p.spikesL[selector + 1].isOut) {
            this.polygon = new Polygon(this.p.spikesL[selector + 1].x, this.p.spikesL[selector + 1].y, 3);
            hit = this.polygon.intersects(this.p.bird.x, this.p.bird.y, 40, 40);
        }

        return hit;
    }
}