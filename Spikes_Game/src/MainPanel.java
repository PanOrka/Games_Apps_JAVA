import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {
    private Timer timer;
    private int[] xSpike, ySpike;
    private boolean isAlive;
    private HitDetector hD;
    private SpikeSetter spSet;
    private int spikeMover;

    public Spike[] spikesR;
    public Spike[] spikesL;
    public Birdie bird;
    public int timerX;
    public boolean isStarted;

    MainPanel() {
        this.setPreferredSize(new Dimension(1000, 800));
        this.setBackground(Color.GRAY);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new PlayerController(this));

        // private
        this.timer = new Timer(15, this);
        this.bird = new Birdie(this);
        this.xSpike = new int[3];
        this.ySpike = new int[3];
        this.isAlive = true;
        this.hD = new HitDetector(this);
        this.spSet = new SpikeSetter(this);
        this.spikeMover = 11;

        // public
        this.spikesR = new Spike[18];
        this.spikesL = new Spike[18];

        for (int i=0; i<18; i++) {
            this.ySpike[0] = 40 + i*40;
            this.ySpike[1] = 60 + i*40;
            this.ySpike[2] = 80 + i*40;

            this.spikesR[i] = new Spike(this.ySpike ,true);

        }
        this.spikesR[0].isOut = true;
        this.spikesR[0].x[0] = 980;
        this.spikesR[0].x[1] = 960;
        this.spikesR[0].x[2] = 980;

        this.spikesR[17].isOut = true;
        this.spikesR[17].x[0] = 980;
        this.spikesR[17].x[1] = 960;
        this.spikesR[17].x[2] = 980;

        for (int i=0; i<18; i++) {
            this.ySpike[0] = 40 + i*40;
            this.ySpike[1] = 60 + i*40;
            this.ySpike[2] = 80 + i*40;

            this.spikesL[i] = new Spike(this.ySpike ,false);
        }
        this.spikesL[0].isOut = true;
        this.spikesL[0].x[0] = 20;
        this.spikesL[0].x[1] = 40;
        this.spikesL[0].x[2] = 20;

        this.spikesL[17].isOut = true;
        this.spikesL[17].x[0] = 20;
        this.spikesL[17].x[1] = 40;
        this.spikesL[17].x[2] = 20;

        this.isStarted = false;
        this.timerX = 0;

        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.isStarted && isAlive && e.getSource() == this.timer) {
            this.timerX++;

            this.bird.doMove();

            if (this.spikeMover == 11) {
                this.spikeMover++;
                this.spSet.setSpikes();
            }

            if (this.spikeMover <= 10) {
                this.spikeMover++;
                this.spSet.moveSpikesBack();
            } else if (this.spikeMover <= 21) {
                this.spikeMover++;
                this.spSet.moveSpikesForward();
            }

            if (this.hD.hitSpikes_UD()) {
                this.isAlive = false;
            }

            if (this.bird.x + 40 >= 960 && this.hD.hitSpikes_R()) {
                this.isAlive = false;
            } else if (this.bird.x <= 40 && this.hD.hitSpikes_L()) {
                this.isAlive = false;
            }

            if (this.bird.x + 40 >= 980 || this.bird.x <= 20) {
                this.bird.isFacingRight = !this.bird.isFacingRight;
                this.spikeMover = 1;
            }

            this.repaint();
        } else if (!isAlive && e.getSource() == this.timer) {
            this.bird.doDeath();

            if (this.bird.y >= 1000) {
                this.restart();
            }
            this.repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.doDraw(g);
    }

    private void doDraw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // WALLS
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, 1000, 20);
        g2d.fillRect(0, 780, 1000, 20);
        g2d.fillRect(0, 0, 20, 800);
        g2d.fillRect(980, 0, 20, 800);

        // UP & DOWN SPIKES
        for (int i=1; i<49; i++) {
            this.xSpike[0] = i*20;
            this.xSpike[1] = i*20 + 10;
            this.xSpike[2] = i*20 + 20;

            //UP
            this.ySpike[0] = 20;
            this.ySpike[1] = 30;
            this.ySpike[2] = 20;
            g2d.fillPolygon(this.xSpike, this.ySpike,3);

            //DOWN
            this.ySpike[0] = 780;
            this.ySpike[1] = 770;
            this.ySpike[2] = 780;
            g2d.fillPolygon(this.xSpike, this.ySpike, 3);
        }

        // LEFT & RIGHT SPIKES
        for (int i=0; i<18; i++) {
            g2d.fillPolygon(this.spikesR[i].x, this.spikesR[i].y,3);
            g2d.fillPolygon(this.spikesL[i].x, this.spikesL[i].y,3);
        }

        // BIRDIE
        if (isAlive) g2d.setColor(Color.BLACK);
        else g2d.setColor(Color.RED);
        g2d.fillRect(this.bird.x, this.bird.y, 40, 40);
    }

    private void restart() {
        this.isStarted = false;
        this.isAlive = true;
        this.timerX = 0;

        this.bird.x = 480;
        this.bird.y = 480;
        this.bird.isFacingRight = true;

        for (int i=1; i<17; i++) {
            this.spikesR[i].x[0] = 1000;
            this.spikesR[i].x[1] = 980;
            this.spikesR[i].x[2] = 1000;

            this.spikesL[i].x[0] = 0;
            this.spikesL[i].x[1] = 20;
            this.spikesL[i].x[2] = 0;
        }
        this.spikeMover = 11;
    }
}
